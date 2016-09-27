package functs_jimenez;

use strict;
use warnings;

use Exporter;
use vars qw (@ISA @EXPORT);
@ISA = qw(Exporter);
@EXPORT = ("parse_db", "save_file", "help_msg", "add_user", "modify_user", "remove_user", "generate_list");

sub parse_db {
    my $db_loc = $_[0];
    my %user_db;

    unless (-e $db_loc) {
        open (my $fh, '>', $db_loc);
        close($fh);
    }

    open (my $fh, '<', $db_loc);

    while (my $line = <$fh>) {
        my @values = split(':', $line, 2);

        chomp($values[1]);

        $user_db{$values[0]} = $values[1];
    }

    close($fh);

    return %user_db;
}

sub save_file {
    my $db = shift;
    my $db_loc = shift;

    print "Save contents? (y/n): ";
    my $save = <>;
    chomp($save);

    if ($save eq 'y') {
        open (my $fh, '>', $db_loc);

        foreach my $key (keys $db) {
            print $fh $key, ":", $db->{$key}, "\n";
        }

        close ($fh);
    }
}

sub help_msg {
    print "\nUser accounts\n";
    print "-------------\n";
    print "a = Add user account\n";
    print "m = Modify existing user account\n";
    print "r = Remove existing user account\n";
    print "g = Generate list of user accounts\n";
    print "q = Quit\n";
}

sub add_user {
    my $db = shift;

    print "Enter username: ";
    my $username = <>;
    chomp($username);

    $username = sanitize_username($username);

    if (exists $db->{$username}) {
        print "Username already exists!\n";
        return;
    }

    print "Enter password: ";
    my $password = <>;
    chomp($password);

    $password = sanitize_password($password);

    $db->{$username} = $password;
}

sub modify_user {
    my $db = shift;

    print "Enter username to modify: ";
    my $username = <>;
    chomp($username);

    unless (exists $db->{$username}) {
        print "Username does not exist!\n";
        return;
    }

    print "Enter current password: ";
    my $c_password = <>;
    chomp($c_password);

    unless ($c_password eq $db->{$username}) {
        print "Incorrect password!\n";
        return;
    }

    print "Enter new password: ";
    my $n_password = <>;
    chomp($n_password);

    $n_password = sanitize_password($n_password);

    $db->{$username} = $n_password;
}

sub remove_user {
    my $db = shift;

    print "Enter username to delete: ";
    my $username = <>;
    chomp($username);

    unless (exists $db->{$username}) {
        print "User does not exist\n";
        return;
    }

    delete $db->{$username};
    print "User removed\n"
}

sub generate_list {
    my $db = shift;

    foreach my $key (keys $db) {
        print $key, ":", $db->{$key}, "\n";
    }
}

sub sanitize_username {
    my $username = shift;

    $username =~ s/[^a-zA-Z0-9]//g;
    $username = lc($username);

    return $username;
}

sub sanitize_password {
    my $password = shift;

    $password =~ s/\'//g;

    return $password;
}

1;