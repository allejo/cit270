class Student
    ID = 0
    LNAME = 1
    FNAME = 2
    MAJOR = 3
    YEAR = 4

    def initialize (data)
        if data.kind_of?(Array)
            @array = data
        else
            @array = data.split(',')
        end
    end

    def edit (field, value)
        @array[field] = value
    end

    def get (field)
        @array[field]
    end

    def set (field, value)
        @array[field] = value
    end

    def raw ()
        @array
    end

    def to_s
        @array.join(',')
    end
end

class StudentDB
    CSV_HEADERS = ['ID', 'Last name', 'First name', 'Major', 'Year']

    def initialize (_filePath)
        @storage = {}
        @filePath = _filePath

        if File.exists?(@filePath)
            lines = File.read(@filePath).split("\n")
            lines.shift # Skip the header
            lines.each do |l|
                student = Student.new(l)
                self.insert(student)
            end
        end
    end

    def insert (student)
        id = student.get(Student::ID)
        @storage[id] = student
    end

    def delete (id)
        @storage.delete(id)
    end

    def exists? (id)
        @storage.key?(id)
    end

    def get (sID, field)
        @storage[sID].get(field)
    end

    def set (sID, field, value)
        @storage[sID].set(field, value)
    end

    def write ()
        File.write(@filePath, self.to_csv())
    end

    def to_csv ()
        printable = { -1 => CSV_HEADERS.map!(&:downcase).join(',').gsub(' ', '') }
        printable.merge!(@storage)
        printable.map{|k,v| "#{v}"}.join("\n")
    end

    def to_s ()
        col_size = [13, 12, 13, 5, 4]
        output = []

        output.push('-' * 60)
        output.push(CSV_HEADERS.map.with_index { |e, i| pretty_print(e, col_size[i]) }.join(' | '))
        output.push('-' * 60)

        @storage.each do |k, e|
            output.push(e.raw.map.with_index { |e, i| pretty_print(e, col_size[i]) }.join(' | '))
        end

        output.push('-' * 60)
        output.join("\n")
    end

    def pretty_print(string, length)
        (string.length > length) ? string.slice(1..length) : string.ljust(length, ' ')
    end

    private :pretty_print
end

def get_value(message)
    print message
    gets.chomp
end
