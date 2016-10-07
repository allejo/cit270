import functs_jimenez as User

value = 0
user_db = User.UserManager()

my_functions = {
    'a': user_db.add_user,
    'm': user_db.mod_user,
    'r': user_db.del_user,
    'g': user_db.show_list,
    'q': user_db.save_file
}

user_db.read_db()

while value == 0:
    user_db.help_msg()

    choice = input('Enter choice: ')

    try:
        value = my_functions[choice]()
    except KeyError:
        print('Unknown option: {0}'.format(choice))
