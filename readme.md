# Annotation based command API
#### @Command
The command that a class handles(spaces not supported)
#### @DefaultHandler
The method that should handle a command by default
#### @Subcommand
A method that contains a sub command(spaces are supported)
#### @Description
The description of a (sub)command
#### @Usage
The usage of a (sub)command)
#### @Exhaustive
If this is put on a string parameter, it will pass the rest of the input into this variable
#### @DecimalClamp
Clamps a variable within a decimal range
#### @IntClamp
Clamps a variable within an int range
#### @PreciseClamp
Clamps a variable within a decimal range, given as a string
#### @Length
Validates the length of a string
#### @Match
Validates a string matches a regex
#### @Permission
Require a permission
#### @ConsoleOnly
Only console can execute command
#### @PlayerOnly
Only a player can execute this command
