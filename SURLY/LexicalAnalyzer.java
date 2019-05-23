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

	LexicalAnalyzer(){
		surly = new SurlyDatabase();
		parser = new Parser();
	}

	public void run(String filename){
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

	private void updateCatalog(Relation catalog, String name, int attr){
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

	private void parseInstructions(String command){
		System.out.println(command);
		switch(command){
			case "RELATION"://Parse Relation commands
				RelationParser rp = new RelationParser(parser);
				if(rp.parseAttributeCount() != -1){
					Relation r = rp.parseRelations();
					Relation search = surly.getRelation(r.parseRelationName());
					if(search.parseRelationName().isEmpty()){
						surly.add(r);
						updateCatalog(surly.getRelation("CATALOG"), rp.parseRelationName(), rp.parseAttributeCount());
					}else{
						System.out.println("Relation " + r.parseRelationName() + " already exists within CATALOG.\n");
					}
				}else{
					System.out.println("ERROR - invalid syntax inputted");//invalid syntax
				}
				break;
			case "INSERT"://parse Insert commands
				break;
			case "PRINT"://evaluate PRINT command
				break;
			case "DELETE": // Deletes relations
				break;
			case "DESTROY": // Destroys single relation one at a time
				break;
			default: // When setting value to not a value

		}

	}

	private void sendLine(String command, Scanner s){
		String currLine = "";
		if(s.hasNextLine()){
			currLine = s.nextLine();
		}else{
			return;
		}


		if(currLine.isEmpty()){//don't parse empty lines
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

			if(!currLine.contains("#")){//do not parse comments
				command = command.concat(currLine);
				if(command.contains(";")){//end of command has been reached
					parser = new Parser(command);
					//parser.printContent();
					String nextCommand = command.substring(command.indexOf(';') + 1);
					if(!parser.hasCatalog()){
						parseInstructions(parser.getRelationName());

					}
					
					/*String typeCommand = command.substring(0, command.indexOf(' '));
					String inputCommand = command.substring(command.indexOf(' ') + 1,
															command.indexOf(';') + 1);
					String nextCommand = command.substring(command.indexOf(';') + 1);
					switch(typeCommand){//send command to specified parser


						case "RELATION"://Parse Relation commands
							RelationParser rp = new RelationParser(inputCommand);
							if(rp.parseAttributeCount() != -1){
								Relation r = rp.parseRelations();
								Relation search = database.getRelation(r.parseRelationName());
								if(search.parseRelationName().isEmpty()){
									database.add(r);
									updateCatalog(database, database.getRelation("CATALOG"), rp.parseRelationName(), rp.parseAttributeCount());
								}else{
									System.out.println("Relation " + r.parseRelationName() + " already exists within CATALOG.\n");
								}
							}else{
								System.out.println("ERROR - invalid syntax inputted");//invalid syntax
							}
							break;


						case "INSERT"://parse Insert commands
							InsertParser ip = new InsertParser(inputCommand);
							if(ip.parseAttributeCount() != -1){
								Relation r = database.getRelation(ip.parseRelationName());
								boolean canAddTuple = false;
								  if (!r.parseRelationName().isEmpty()) {//if relation exists
										if(ip.parseAttributeCount() <= r.parseRelationSchema().size()){//if right amount of attributes
											Tuple tp = ip.parseTuple();
											for (int x = 0; x < tp.parseTupleValues().size(); x++) {
												String type = r.parseRelationSchema().get(x).parseAttributeType();
												int constrict = r.parseRelationSchema().get(x).parseAttributeLength();
												if(constrict >= tp.parseTupleValues().get(x).parseAttName().length()){//if size is valid
													canAddTuple = true;
													if (type.equals("NUM")) {//evaluate type
														for(int y = 0; y < tp.parseTupleValues().get(x).parseAttName().length(); y++) {
															if (!Character.isDigit(tp.parseTupleValues().get(x).parseAttName().charAt(y))) {
																canAddTuple = false;
															}
														}
													}
												}
												else {
													canAddTuple = false;
												}

											}
											if(canAddTuple){
												r.insertTuple(tp);
												canAddTuple = false;
											}
											else{//error message for invalid type
												System.out.println("Invalid length or invalid type being inserted.\n");
											}
										}
										else{//error message for trying to input invalid amount of attributes
												System.out.print("Cannot insert " + ip.parseAttributeCount() + " attributes into " + ip.parseRelationName());
												System.out.println(" (max = " + r.parseRelationSchema().size() + ").\n");
										}
									}
									else {//error message for non-existant relation
										System.out.println("Relation " + ip.parseRelationName() + " that you are trying to insert into does not exist within CATALOG.\n");
									}

							}else{//invalid syntax
								System.out.println("ERROR - invalid syntax inputted");
							}
							break;


						case "PRINT"://evaluate PRINT command
							PrintParser pp = new PrintParser(inputCommand);
							String[] relationsToPrint = pp.parseRelationNames();
							for(int i = 0; i < relationsToPrint.length; i++){//for each Relation to print
								Relation r = database.getRelation(relationsToPrint[i]);
								LinkedList<Tuple> tu = r.parseRelationTuples();
								LinkedList<Attribute> sch = r.parseRelationSchema();
								if(r.parseRelationName().isEmpty()){//don't print non-existant relations
									  System.out.println("Relation " + pp.parseRelationNames()[i] + " doesn't exist within CATALOG.\n");
										break;
								}
								int longestTuple = longestTuple(tu, sch);
								int count = 0;
								System.out.print(" ");
								for (int x = 0; x < ((longestTuple+6)*sch.size()); x++) {//start of display, formatting ***********
									System.out.print("*");
									count++;
								}
								System.out.println(" ");
								System.out.print(" | " + r.parseRelationName());//display Relation name
								for (int x = 0; x < count - r.parseRelationName().length() - 3; x++) {
									System.out.print(" ");
								}
								System.out.print("|\n ");//end of displaying relation name
								for (int x = 0; x < count; x++) {//formatting --------------
									System.out.print("-");
								}
								System.out.print(" \n |");
								for(int j = 0; j < sch.size(); j++){//display each attribute name
									int length1 = 0;
									System.out.print(" " + sch.get(j).parseAttributeName() + " ");
									while (length1 + sch.get(j).parseAttributeName().length() < (longestTuple + 2)) {
										if (length1 + sch.get(j).parseAttributeName().length() == longestTuple+1) {
											System.out.print(" |");//end of displaying current attribute name
										}
											System.out.print(" ");
											length1++;
									}
								}
								System.out.print("\n ");
								for (int x = 0; x < count; x++) {//formatting ------------
									System.out.print("-");
								}
								System.out.println(" ");
								for(int k = 0; k < tu.size(); k++){//go through tuple list
									System.out.print(" |");
									LinkedList<AttributeValue> temp = tu.get(k).parseTupleValues();
									for(int l = 0; l < temp.size(); l++){//display each tuple set
										int length2 = 0;
										System.out.print(" " + temp.get(l).parseAttName());
										while (length2 + temp.get(l).parseAttName().length() < (longestTuple + 1)) {//if there are more tuples to parse
											  if (length2 + temp.get(l).parseAttName().length() == longestTuple) {
													System.out.print("   |");//seperate each tuple value with a pipe
												}
												System.out.print(" ");
												length2++;
										}
									}
									System.out.println(" ");
								}
								System.out.print(" ");
								for (int x = 0; x < count; x++) {//formatting *************
									System.out.print("*");
								}
								System.out.println(" \n");//end of display
							}

							break;


						case "DELETE": // Deletes single relation one at a time
							DeleteParser dlt = new DeleteParser(inputCommand);
							LinkedList<String> relations = dlt.parseCommands();
							String relationDLT = dlt.parseRelationName();
							Relation r = database.getRelation(relationDLT);
							if(!relationDLT.equals("CATALOG") && !r.parseRelationName().isEmpty()){//make sure can't delete from CATALOG
								if(!dlt.hasWhere()){								
									r.deleteTuples();
								}
								else {

								}
						 }
						 else {
						 	System.out.println("Cannot delete tuples from " + relationDLT + ".");
						 }
							break;


						case "DESTROY": // Destroys single relation one at a time
							DestroyParser dst = new DestroyParser(inputCommand);
							String relationDST = dst.parseRelationName();
							database.destroyRelation(relationDST);
							break;


						default: //command is not recognized

					}*/
					//remove excess space from nextCommand
					int linePointer = 0;
					while(linePointer != currLine.length() &&
						  Character.isWhitespace(currLine.charAt(linePointer))){
						linePointer++;
					}
					nextCommand = nextCommand.substring(linePointer);
					sendLine(nextCommand, s);//start with new command
				}else{
					sendLine(command,s);//keep concatenating command until ';' seen
				}
			}else{
				sendLine("", s);//not parsing comments
			}
		}

	}
	public int longestTuple(LinkedList<Tuple> group1, LinkedList<Attribute> group2) {//used to get formatting of PRINT
    int length = 1;
		for(int k = 0; k < group1.size(); k++) {
		  LinkedList<AttributeValue> neww1 = group1.get(k).parseTupleValues();
			for(int i = 0; i < neww1.size(); i++) {
				if (neww1.get(i).parseAttName().length() > length) {
					length = neww1.get(i).parseAttName().length();
				}
		  }
		}
		for(int k = 0; k < group2.size(); k++) {
		  if (group2.get(k).parseAttributeName().length() > length) {
				length = group2.get(k).parseAttributeName().length();
			}
		}
		return length;
	}

}
