require_relative 'functs_jimenez'

# Catch a CTRL+C at the command line
Signal.trap("INT") { puts ""; exit }

# Read (if it exists) the database and save it
print "Enter database to open/create: "
filePath = gets.chomp
$sDB = StudentDB.new(filePath)

while true
    puts ""
    puts "Student Database"
    puts "------------------------------"
    puts "i = Insert new record into database"
    puts "u = Update existing record in database"
    puts "d = Delete existing record from database"
    puts "p = Print record(s) in database"
    puts "q = Quit"
    puts ""

    print "Enter choice: "
    choice = gets.chomp

    case choice
    when 'p'
        puts $sDB

    when 'i'
        id = get_value("Enter Student ID: ")

        if $sDB.exists?(id)
            puts "Student ID already exists!"
            next
        end

        last_name = get_value("Enter last name: ")
        first_name = get_value("Enter first name: ")
        major = get_value("Enter major: ")
        catalog = get_value("Enter catalog year: ")

        student = Student.new([id, last_name, first_name, major, catalog])
        $sDB.insert(student)

        puts "New record has been inserted"

    when 'u'
        id = get_value("Enter Student ID: ")

        if !$sDB.exists?(id)
            puts "Student ID doesn't exist!"
            next
        end

        for i in 1..4
            puts "Modify " + StudentDB::CSV_HEADERS[i].downcase
            puts "    Current value: " + $sDB.get(id, i)

            new_value = get_value("    New value: ")
            $sDB.set(id, i, new_value)
        end

        puts "Record has been updated"

    when 'd'
        id = get_value("Enter Student ID: ")

        if !$sDB.exists?(id)
            puts "Student ID doesn't exist!"
            next
        end

        $sDB.delete(id)
        puts "Record has been deleted"

    when 'q'
        choice = get_value("Save records (y/n): ")

        if choice == 'y'
            $sDB.write()
        end

        break
    else
        puts "Unknown command"
    end
end