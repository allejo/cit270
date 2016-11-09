class Student
    ID = 0
    LNAME = 1
    FNAME = 2
    MAJOR = 3
    YEAR = 4

    def initialize (data)
        @array = data.split(',')
    end

    def edit (field, value)
        @array[field] = value
    end

    def get (field)
        @array[field]
    end

    def to_s
        @array.join(',')
    end
end

class StudentDB
    CSV_HEADER = "id,lastname,firstname,major,year"

    def initialize (_filePath)
        @storage = {}
        @filePath = _filePath

        if File.exists?(@filePath)
            lines = File.read(@filePath).split("\n")
            lines.shift
            lines.each do |l|
                student = Student.new(l)
                self.push(student)
            end
        end
    end

    def push (student)
        id = student.get(Student::ID)
        @storage[id] = student
    end

    def pop (student)
        id = student.get(Student::ID)
        @storage.delete(id)
    end

    def to_s
        printable = { -1 => CSV_HEADER }
        printable.merge!(@storage)
        printable.map{|k,v| "#{v}"}.join("\n")
    end
end

print "Enter database to open/create: "
filePath = gets.chomp
sDB = StudentDB.new(filePath)

while true
    puts ""
    puts "Student Database"
    puts "----------------"
    puts "i = Insert new record into database"
    puts "u = Update existing record in database"
    puts "d = Delete existing record from database"
    puts "p = Print record(s) in database"
    puts "q = Quit"
    puts ""

    print "Enter choice: "
    choice = gets.chomp
end

puts sDB