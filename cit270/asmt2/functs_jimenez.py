import re

class UserManager(object):
    def __init__(self):
        self.user_db = {}
        self.file_path = ''

    def read_db(self):
        self.file_path = input('Enter file to open: ')

        try:
            lines = [line.rstrip('\n') for line in open(self.file_path, 'r')]
            self.user_db = dict(user.split(':', 1) for user in lines)
        except FileNotFoundError:
            self.user_db = dict()

    def help_msg(self):
        print('')
        print('User accounts')
        print('-------------')
        print('a = Add user account')
        print('m = Modify existing user account')
        print('r = Remove existing user account')
        print('g = Generate list of user accounts')
        print('q = Quit')
        print('')

    def save_file(self):
        save = input('Save contents? (y/n): ')

        if not save.lower() == 'y':
            return 1

        with open(self.file_path, 'w+') as f:
            for user, passwd in self.user_db.items():
                print('{0}:{1}'.format(user, passwd), file=f)

        return 1

    def show_list(self):
        for user, passwd in self.user_db.items():
            print('{0}:{1}'.format(user, passwd))

        return 0

    def add_user(self):
        username = self._get_sanitized_username('Enter username: ')

        if username in self.user_db:
            print('Username already exists!')
            return 0

        password = self._get_sanitized_password('Enter password: ')

        self.user_db[username] = password

        return 0

    def mod_user(self):
        username = self._get_sanitized_username('Enter username to modify: ')

        if not username in self.user_db:
            print('Username does not exist!')
            return 0

        c_pass = self._get_sanitized_password('Enter current password: ')

        if not c_pass == self.user_db[username]:
            print('Incorrect password!')
            return 0

        n_pass = self._get_sanitized_password('Enter new password: ')

        self.user_db[username] = n_pass

        return 0

    def del_user(self):
        username = self._get_sanitized_username('Enter username to delete: ')

        if not username in self.user_db:
            print('User does not exist')
            return 0

        del self.user_db[username]
        print('User removed')

        return 0

    def _get_sanitized_username(self, message):
        username = input(message)

        return re.sub(r'\W+', '', username)

    def _get_sanitized_password(self, message):
        password = input(message)

        return password.replace('\'', '')
