/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : LexicalAnalyzer.java
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class LexicalAnalyzer{
	private Parser parser;
	private SurlyDatabase surly;

	LexicalAnalyzer(){ // comes directly from the Main file. Automatically kick starts the parsing into the database.
		surly = new SurlyDatabase();
		parser = new Parser();
	}

	public void run(String filename){ // scan the file
		File contain = null;
		Scanner line = null;
		try {
			contain = new File (filename);
			line = new Scanner (contain);
		}catch (Exception e){
			System.out.println("ERROR - no such file: " + filename);
			System.exit(0);
		}
		sendLine("", line);
	}

	private void updateCatalog(Relation catalog, String name, int attr){ // special insert into the Catalog to show summary
		Tuple t = new Tuple();
		AttributeValue catalogRelation = new AttributeValue();
		AttributeValue catalogAttributes = new AttributeValue();
		catalogRelation.setValue("CATALOG");
		catalogAttributes.setValue("CATALOG");
		catalogRelation.setName(name);
		catalogAttributes.setName(Integer.toString(attr));
		t.add(catalogRelation);
		t.add(catalogAttributes);
		catalog.insertTuple(t);
	}

	private void deleteErrorMssg(Relation r, String relationDLT){ // helper function for delete errors.
		if(r.parseRelationName().isEmpty()){
			System.out.println("Relation " + relationDLT + " doesn't exist.");
		}else{
			System.out.println("Cannot DELETE from " + relationDLT + ": temporary relation.");
		}
	}

	private void parseInstructions(String command){ // THE POWERHOUSE FUNCTION, goes through all command cases.

		switch(command){
			case "RELATION": //Parse Relation commands
				RelationParser rp = new RelationParser(parser);
				if(rp.parseAttributeCount() != -1){
					Relation r = rp.parseRelations();
					Relation search = surly.getRelation(r.parseRelationName());
					if(search.getTemp()){
						surly.destroyRelation(search.parseRelationName());
						search = surly.getRelation(r.parseRelationName());
					}
					if(search.parseRelationName().isEmpty()){
						surly.add(r);
						updateCatalog(surly.getRelation("CATALOG"), rp.parseRelationName(), rp.parseAttributeCount());
					}else{
						System.out.println("Relation " + r.parseRelationName() + " already exists within CATALOG."); // special error messages scattered throughout, you'll see
					}
				}else{
					System.out.println("ERROR - invalid syntax inputted for Relation " + rp.parseRelationName() + "."); //invalid syntax
				}
				break;

			case "INSERT": //parse Insert commands
				InsertParser ip = new InsertParser(parser);
				if(ip.parseAttributeCount() != -1){
					Relation r = surly.getRelation(ip.parseRelationName());
					if(!r.parseRelationName().isEmpty()){ //relation exists?
						if(ip.hasValidAttNum(r.parseRelationSchema().size())){ //valid number of attributes inserting
							if(ip.isValid(r)){
								r.insertTuple(ip.parseTuple());
							}else{
								if(r.getTemp()){
									System.out.println("Cannot INSERT into " + r.parseRelationName() + ": temporary relation.");
								}else{
									System.out.println("Invalid INSERT syntax for inserting into " + r.parseRelationName() + ".");
								}
							}
						}
					}
				}
				break;

			case "PRINT": //evaluate PRINT command
				PrintParser pp = new PrintParser(parser);
				String[] relationsToPrint = pp.parseRelationNames();
				for(int i = 0; i < relationsToPrint.length; i++){ // for each Relation to print
					Relation r = surly.getRelation(relationsToPrint[i]);
					if(r.parseRelationName().isEmpty()){ //d on't print non-existant relations
						  System.out.println("Relation " + pp.parseRelationNames()[i] + " doesn't exist within CATALOG.");
					}else{
						System.out.println("\nPrinting " + pp.parseRelationNames()[i] + "..."); // extra line for clarity, to show what table/projection/etc is displaying on the terminal
						r.display();
						System.out.println(" ");
					}
				}
				break;

			case "DELETE": // Deletes relations
				DeleteParser dlt = new DeleteParser(parser);
				LinkedList<String> relations = dlt.parseCommands();
				String relationDLT = dlt.parseRelationName();
				Relation r = new Relation();

				if(!parser.hasWhere()){
					for(int i = 0; i < dlt.parseRelationNames().size(); i++){
						r = surly.getRelation(dlt.parseRelationNames().get(i));
						if(!r.parseRelationName().isEmpty() && !r.getTemp()){
							r.deleteAllTuples();
						}else{
							deleteErrorMssg(r, dlt.parseRelationNames().get(i));
						}
					}
				}
				else { //specialized delete statement, if WHERE exists
					r = surly.getRelation(relationDLT);
					if(!r.parseRelationName().isEmpty() && !r.getTemp()){
						AllConditions conditions = dlt.getConditions();
						r.deleteTuples(conditions.evaluateConditions(r));
					}else{
						deleteErrorMssg(r, relationDLT);
					}
				}
				break;

			case "DESTROY": // Destroys single relation one at a time
				DestroyParser dst = new DestroyParser(parser);
				String relationDST = dst.parseRelationName();
				Relation rel = surly.getRelation(dst.parseRelationName());
				if(!rel.getTemp()){
					surly.destroyRelation(relationDST);
				}else{
					System.out.println("Cannot DESTROY " + relationDST + ": temporary relation.");
				}
				break;

			default: // **** Deal with temporary relations ****
				if(parser.hasEqual()){ //valid temporary relation?
					boolean tempDeleted = false;
					Relation currTemp = surly.getRelation(command);
					if(currTemp.parseRelationName().length() != 0){
						if(currTemp.getTemp()){
							tempDeleted = true;
							surly.destroyRelation(currTemp.parseRelationName());
						}else{ //relation is not temp, DO NOT OVERWRITE!
							System.out.println("Cannot overwrite base relation " + currTemp.parseRelationName() + "!");
							break;
						}
					}
					switch(parser.getSecondaryName()){ // sub-switch case for the commands that deal with temp relations, too
						case "SELECT":
							SelectParser sPsr = new SelectParser(parser);
							Relation rSel = surly.getRelation(sPsr.getRelationName());
							if(tempDeleted){ // extra if case to overwrite a temp relation even if something goes wrong (as per the instructions).
								rSel = currTemp;
							}
							sPsr.addRelation(rSel);
							Relation temp1 = sPsr.getTempRelation();
							temp1.tempBuff(); // set this relation as temporary

							if(temp1.tupleSize() == 0){
								temp1.setSchema(new LinkedList<Attribute>()); // empty schema if the projection is projected something invalid/non-existant
							}
							if(temp1.parseRelationName().length() != 0){ // puts in database, after all is said and done
								surly.add(temp1);
								updateCatalog(surly.getRelation("CATALOG"), temp1.parseRelationName(), temp1.parseRelationSchema().size());
							}
							break;

						case "PROJECT":
							ProjectParser prj = new ProjectParser(parser);
							if(prj.isValid()){
								Relation rPrj = surly.getRelation(prj.getRelationName());
								if(tempDeleted){ // extra if case to overwrite a temp relation even if something goes wrong (as per the instructions).
									rPrj = currTemp;
								}
								prj.addInfo(rPrj);
								prj.addRelation(rPrj);
								Relation temp2 = prj.getTempRelation();
								temp2.tempBuff(); // set this relation as temporary

								if(temp2.tupleSize() == 0){
									temp2.setSchema(new LinkedList<Attribute>()); // empty schema if the projection is projected something invalid/non-existant
								}
								if(temp2.parseRelationName().length() != 0){ // puts in database, after all is said and done
									surly.add(temp2);
									updateCatalog(surly.getRelation("CATALOG"), temp2.parseRelationName(), temp2.parseRelationSchema().size());
								}
							}
							break;
						case "JOIN":
							JoinParser jPsr = new JoinParser(parser);
							if(jPsr.isValid()){
								Relation A = surly.getRelation(jPsr.getRelationAName()); // future joiners
								Relation B = surly.getRelation(jPsr.getRelationBName()); //
								if(tempDeleted){ // extra if case to overwrite a temp relation even if something goes wrong (as per the instructions).
									if(A.parseRelationName().isEmpty() && B.parseRelationName().isEmpty()){
										A = currTemp;
										B = currTemp;
									}else if(A.parseRelationName().isEmpty()){//A relation is empty
										A = currTemp;
									}else{//B relation is empty
										B = currTemp;
									}
								}
								if(!A.parseRelationName().isEmpty() && !B.parseRelationName().isEmpty()){
									jPsr.setRelationA(A);
									jPsr.setRelationB(B);
									if(jPsr.validRelations()){
										jPsr.getComparison();
										Relation temp3 = jPsr.getTempRelation();

										if(temp3.tupleSize() == 0){
											temp3.setSchema(new LinkedList<Attribute>());
										}
										if(temp3.parseRelationName().length() != 0){ // puts in database, after all is said and done
											surly.add(temp3);
											updateCatalog(surly.getRelation("CATALOG"), temp3.parseRelationName(), temp3.parseRelationSchema().size());
										}
									}
								}
							}
							break;
						default: // not valid temporary syntax
						  System.out.println("Invalid SELECT, PROJECT, or JOIN syntax.");
					}
				}
		 }
	}

	private void sendLine(String command, Scanner s){ // handles the entering of new lines
		String currLine = "";
		if(s.hasNextLine()){
			currLine = s.nextLine();
		}else{
			return;
		}

		if(currLine.isEmpty()){ // don't parse the empty lines
			sendLine(command, s);
		}else{
			int pointer = 0;
			while(pointer != currLine.length() && Character.isWhitespace(currLine.charAt(pointer))){
				pointer++;
			}
			if(pointer != 0){
				if(pointer == currLine.length()){
					sendLine(command,s);
				}else{
					currLine = currLine.substring(pointer);
				}
			}

			if(!currLine.contains("#")){ // do not parse comments
				command = command.concat(currLine);
				if(command.contains(";")){ // end of command has been reached
					parser = new Parser(command);
					String nextCommand = command.substring(command.indexOf(';') + 1);
					if(!parser.hasCatalog()){
						parseInstructions(parser.getRelationName());

					}else{
						if(parser.getRelationName().equals("PRINT")){
							parseInstructions(parser.getRelationName());
						}
					}
					int linePointer = 0;
					while(linePointer != currLine.length() && Character.isWhitespace(currLine.charAt(linePointer))){ 	//removes excess space from nextCommand
						linePointer++;
					}
					nextCommand = nextCommand.substring(linePointer);
					sendLine(nextCommand, s); // start with new command
				}else{
					sendLine(command,s); // keep concatenating command until ';' seen
				}
			}else{
				sendLine("", s); // not parsing comments
			}
		}
	}
}

// ### END ###
