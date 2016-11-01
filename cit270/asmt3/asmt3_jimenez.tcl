proc new_partition {ptable} {
	upvar $ptable table

	puts -nonewline "Enter device label: "
	flush stdout
	set label [gets stdin]

	if {[info exists table($label)]} {
		puts "Label already exists"
		return [array get table]
	}

	puts -nonewline "Enter starting cylinder: "
	flush stdout
	set start_cylinder [gets stdin]

	puts -nonewline "Enter ending cylinder: "
	flush stdout
	set end_cylinder [gets stdin]

	puts -nonewline "Enter number of blocks: "
	flush stdout
	set block_no [gets stdin]

	puts -nonewline "Enter id: "
	flush stdout
	set id [gets stdin]

	puts -nonewline "Enter file system (ex: NTFS, Linux): "
	flush stdout
	set fs_type [gets stdin]

	set table($label) [list $label $start_cylinder $end_cylinder $block_no $id $fs_type]

	return [array get table]
}

proc delete_partition {ptable} {
	upvar $ptable table

	puts -nonewline "Enter partition to delete: "
	flush stdout
	set label [gets stdin]

	if {![info exists table($label)]} {
		puts "Partition doesn't exists"
		return [array get table]
	}

	unset table($label)

	return [array get table]
}

proc print_table {ptable} {
	upvar $ptable table

	puts ""
	puts "Partition	Start	End	Blocks	Id	System"
	puts "------------------------------------------------------"

	foreach key [array names table] {
		foreach field $table($key) {
			puts -nonewline "$field\t"
		}

		puts ""
	}
}

proc write_table {ptable filePath} {
	upvar $ptable table
	set fileHandle [open $filePath w]

	foreach key [array names table] {
		puts $fileHandle "$table($key)"
	}

	close $fileHandle
}

proc print_help {} {
	puts ""
	puts "FDISK Simulator"
	puts "---------------"
	puts "n = Create a new partition"
	puts "d = Delete a partition"
	puts "p = Print the partition table"
	puts "w = Write the new partition table and exit"
	puts "q = Quit without saving changes"
	puts ""
}

proc parse_table {filePath} {
	array set partitions {}

	if {[catch {open $filePath r} fid]} {
		return [array get partitions]
	} else {
		set fp [open $filePath r]
		set file_data [read $fp]
		close $fp

		set data [split $file_data "\n"]
		foreach line $data {
			if {$line eq ""} {
				continue
			}
			
			set partition [split $line " "]
			set partitions([lindex $partition 0]) $partition
		}

		return [array get partitions]
	}
}

puts -nonewline "Enter file to open: "
flush stdout
set file [gets stdin]

array set ptable [parse_table $file]

while true {
	print_help

	puts -nonewline "Enter choice: "
	flush stdout
	set choice [gets stdin]

	if {$choice eq "n"} {
		array set ptable [new_partition ptable]
	} elseif {$choice eq "d"} {
		array set ptable [delete_partition ptable]
	} elseif {$choice eq "p"} {
		print_table ptable
	} elseif {$choice eq "w"} {
		write_table ptable $file
		break
	} elseif {$choice eq "q"} {
		break
	}
}
