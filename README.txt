Installation: Download entire directory.

Run Instructions: execute the Main.java program with one argument - a testfile of your choice formatted like SQL, similar to below:


-----------------------------------------------
# SURLY COMMAND FILE

RELATION tablename (attname type length, ... );
...
...
INSERT tablename tupleval1, ..., tupleval(length specified in RELATION);
...
...
PRINT tablename, ...;
PRINT CATALOG; #automatically produced
...
...
DELETE tablename; # optional command
DESTROY tablename; # optional command

-----------------------------------------------