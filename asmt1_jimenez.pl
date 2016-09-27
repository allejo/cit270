use strict;
use warnings;

use functs_jimenez;

print "\nEnter file to open: ";
my $db_loc = <>;
chomp($db_loc);

my %users = parse_db($db_loc);
my %functions = ('a' => \&add_user, 'm' => \&modify_user, 'r' => \&remove_user, 'g' => \&generate_list);

help_msg();

while (1) {
    print "\nEnter choice: ";
    my $choice = <>;
    chomp($choice);

    if (exists $functions{$choice}) {
        my $selection = $functions{$choice};
        $selection->(\%users);

        help_msg();
    }
    elsif ($choice eq "q") {
        save_file(\%users, $db_loc);

        last;
    }
    else {
        print "Unknown option: ", $choice;
    }
}
