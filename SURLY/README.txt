---begin README.txt------------------------------------------

Installation Guide:
	When all the files are extracted from the zip folder, you can compile all the files within terminal:
	
	USER INPUT IN TERMINAL -> javac Main.java

	Once done, create your Database file, which will contain the commands to create and insert into the
	Database. To run the program, do the following within terminal:
	
	USER INPUT IN TERMINAL -> java Main <your-file-goes-here>

	And it should create your Database just like that! Look below to see what to expect.

Demo Guide:
	The example attached to our input and output file is displayed in "Input1+Output1.txt"

	To run the code use the "Input2.txt" within the <your-file-goes-here> .

	INSTRUCTION OF COMMANDS:
		When ending your command, make sure you have a ';' to end your line.

		The '#' is used to add comments, the database will ignore reading any line containing a '#'.

		You can access what is in the database with the CATALOG by printing it.

		RELATION:
			Create the table. Example of syntax is:
				
				RELATION <name> <attributes>;

			You can have as many attributes as you like within your relation, but they must be
			seperated with a comma, as shown:
			
				(<name> <type> <length>, ... )

			Examples of how this is done:
				->   RELATION FRUIT (FNUM CHAR 3,TITLE CHAR 30, PRICE NUM 4);
				->   RELATION STORE (FNUM CHAR 3,
								NAME CHAR 20);
			
		INSERT:
			Insert values(also known as tuples) into the table. Example of syntax:
			
				INSERT <relationname> <values>;

			When inserting values, you must make sure you have the APPROPRIATE AMOUNT of values,
			stated in the relation, otherwise you can't insert the value.
			
			Examples of how this is done:
				-> 	INSERT FRUIT A1 APPLE 3;
				->	INSERT FRUIT B1 BANANA 1;
				....
				->	INSERT STORE B3 'WHOLE FOODS';
				->	INSERT STORE C1 'FARMERS MARKET';

			Notice how there are single quotes around "WHOLE FOODS" and "FARMERS MARKET". In order
			to input a value with spaces, single quotes are required.

		PRINT:
			Prints the specified table. Example of syntax is:
				
				PRINT <relationnames>;

			You can print as many relations from the database as you like, remember to seperate your
			commands with a ',' to ensure it prints.
			
			Examples of how this is done:
				->  PRINT CATALOG;
				->	PRINT STORE;
				->  PRINT FRUIT, SEASON;
	
			If you want to print the CATALOG, you must make sure that is the only thing you print, as it
			is its own value.

		DELETE:
			This will delete values from the tables.
			There are two different kinds of deletes you can do.

				1) Basic Delete
					All values/tuples are deleted from the table. The syntax is as follows:
						DELETE <relations>;
					Examples of how this is done:
						->	DELETE STORE;
						->	DELETE SEASON, FRUIT;

				2) Specified Delete
					Only the specifed values/tuples from ONE relation are deleted. The syntax is as follows:
						DELETE <relation> WHERE <conditions>;
					Examples of how this is done:
						-> DELETE FRUIT WHERE FNUM = 'A1';
						-> DELETE STORE WHERE NAME != 'FRED MEYER' or NAME != 'WHOLE FOODS';
						-> DELETE FRUIT WHERE PRICE >= 3 and FNUM = G2;
			
		DESTROY:
			This will remove the table completely from the database.
			Syntax is as follows: 
				DESTROY <relation>;
			Since destroying a relation is too powerful, for each relation to destroy must be invoked one at a time.
				DESTROY <relation1>;
				DESTROY <relation2>; 
				...
				DESTROY <relationN>;

		Temporary Relations:
			To get more personalized results from the tables, you can SELECT, PROJECT, and JOIN relations. These are however
			temporary in the database. This means that you can't do the following to these relations:
				- DELETE
				- INSERT
				- DESTROY
			However, if wanting to make a new relation (a temporary or base), you can easily override the temporary relation
			by creating it.
			
			HOW TO GET TEMPORARY RELATIONS:
				SELECT:
					Creates a table containing specified values. Syntax is:
						<tempRelationName> = SELECT <relation> WHERE <conditions>;
					Examples include:
						-> T1 = SELECT FRUIT WHERE PRICE >= 2;
						-> T2 = SELECT STORE WHERE NAME = 'FRED MEYER' or NAME = 'TRADER JOES';
				PROJECT:
					Creates a table displaying values only in certain columns. Syntax is:
						<tempRelationName> = PROJECT <attributes> FROM <relation>;
					You can select multiple attributes, seperating each with a ',' character. Examples include:
						-> T1 = PROJECT NAME FROM STORE;
						-> T2 = PROJECT TITLE, PRICE FROM FRUIT;
				JOIN:
					Joins two relations onto one table based on a shared attribute. Syntax is:
						<tempRelationName> = JOIN <relationA>, <relationB> ON <joincondition>;
					An example of this:
						-> T1 = JOIN FRUIT, STORE ON FRUIT.FNUM = STORE.FNUM;
						-> T2 = JOIN FRUIT, INVENTORY ON FNUM = FID;

---end of README.TXT------------------------------------------
