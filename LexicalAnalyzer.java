/*
Name: Martin Smith
CSCI 330 - Spring 2019
File Name : LexicalAnalyzer.java
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class LexicalAnalyzer{

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
		SurlyDatabase surly = new SurlyDatabase();
		sendLine("", line, surly);
	}
	public void updateCatalog(SurlyDatabase surly, Relation catalog, String name, int attr){
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
			surly.replace(catalog, 0);
	}

	public void sendLine(String command, Scanner s, SurlyDatabase database){
		String currLine = "";
		if(s.hasNextLine()){
			currLine = s.nextLine();
		}else{
			return;
		}


		if(currLine.isEmpty()){//don't parse empty lines
			sendLine(command, s, database);
		}else{
			int pointer = 0;
			while(pointer != currLine.length() && Character.isWhitespace(currLine.charAt(pointer))){
				pointer++;
			}
			if(pointer != 0){
				if(pointer == currLine.length()){
					sendLine(command,s, database);
				}else{
					currLine = currLine.substring(pointer);
				}
			}

			if(currLine.charAt(0) != '#'){//do not parse comments
				command = command.concat(currLine);
				if(command.contains(";")){//end of command has been reached
					String typeCommand = command.substring(0, command.indexOf(' '));
					String inputCommand = command.substring(command.indexOf(' ') + 1,
															command.indexOf(';') + 1);
					String nextCommand = command.substring(command.indexOf(';') + 1);
					switch(typeCommand){//send command to specified parser
						case "RELATION":
							RelationParser rp = new RelationParser(inputCommand);
							if(rp.parseAttributeCount() != -1){
								Relation r = rp.parseRelations();
								database.add(r);
								updateCatalog(database, database.getRelation("CATALOG"), rp.parseRelationName(), rp.parseAttributeCount());
							}else{
								System.out.println("ERROR - invalid syntax inputted");
								System.exit(0);
							}
							break;
						case "INSERT":
							InsertParser ip = new InsertParser(inputCommand);
							if(ip.parseAttributeCount() != -1){

								Relation r = database.getRelation(ip.parseRelationName());
								int index = database.getRelationIndex(ip.parseRelationName());
								Tuple tp = ip.parseTuple();
								r.insertTuple(tp);
								database.replace(r, index);

							}else{
								System.out.println("ERROR - invalid syntax inputted");
								System.exit(0);
							}
							break;
						case "PRINT"://evaluate PRINT command
							PrintParser pp = new PrintParser(inputCommand);

							String[] relationsToPrint = pp.parseRelationNames();
							for(int i = 0; i < relationsToPrint.length; i++){

								Relation r = database.getRelation(relationsToPrint[i]);
								LinkedList<Tuple> tu = r.parseRelationTuples();
								LinkedList<Attribute> sch = r.parseRelationSchema();
								System.out.print(" ******************************\n");
								System.out.println(" | " + r.parseRelationName() + "                     |");
								System.out.print(" ------------------------------\n");
								System.out.print(" |");
								for(int j = 0; j < sch.size(); j++){
									int length1 = 0;
									System.out.print(" " + sch.get(j).parseAttributeName() + " ");
									while (length1 + sch.get(j).parseAttributeName().length() < 11) {
										if (length1 + sch.get(j).parseAttributeName().length() == 10) {
											System.out.print(" |");
										}
											System.out.print(" ");
											length1++;
									}
								}
								System.out.println("|");
								for(int k = 0; k < tu.size(); k++){
									System.out.print(" |");
									LinkedList<AttributeValue> temp = tu.get(k).parseTupleValues();
									for(int l = 0; l < temp.size(); l++){
										int length2 = 0;
										System.out.print(" " + temp.get(l).parseAttName());
										while (length2 + temp.get(l).parseAttName().length() < 13) {
											  if (length2 + temp.get(l).parseAttName().length() == 12) {
													System.out.print("|");
												}
												System.out.print(" ");
												length2++;
										}
									}
									System.out.println(" ");
								}
								System.out.println(" ******************************\n");
							}

							break;
						case "DELETE":
							DeleteParser dlt = new DeleteParser(inputCommand);
							String relationDLT = dlt.parseRelationName();
							Relation r = database.getRelation(relationDLT);
							r.deleteTuples();

							break;
						case "DESTROY":
							DestroyParser dst = new DestroyParser(inputCommand);
							String relationDST = dst.parseRelationName();
							int index = database.getRelationIndex(relationDST);
							database.replace(new Relation(), index);
							database.destroyRelation(relationDST);
							break;
						default://command is not recognized

					}
					//remove excess space from nextCommand
					int linePointer = 0;
					while(linePointer != currLine.length() &&
						  Character.isWhitespace(currLine.charAt(linePointer))){
						linePointer++;
					}
					nextCommand = nextCommand.substring(linePointer);
					sendLine(nextCommand, s, database);//start with new command
				}else{
					sendLine(command,s, database);//keep concatenating command until ';' seen
				}
			}else{
				sendLine(command, s, database);//not parsing comments
			}
		}

	}

}
