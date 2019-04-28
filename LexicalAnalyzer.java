/*
Name: Martin Smith, Eric Anderson
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
								Relation search = database.getRelation(r.parseRelationName());
								if(search.parseRelationName().isEmpty()){
									database.add(r);
									updateCatalog(database, database.getRelation("CATALOG"), rp.parseRelationName(), rp.parseAttributeCount());
								}else{
									System.out.println("Relation " + r.parseRelationName() + " already exists within CATALOG.\n");
								}
							}else{
								System.out.println("ERROR - invalid syntax inputted");
								System.exit(0);
							}
							break;


						case "INSERT":
							InsertParser ip = new InsertParser(inputCommand);
							if(ip.parseAttributeCount() != -1){
								Relation r = database.getRelation(ip.parseRelationName());
								boolean canAddTuple = false;
								  if (!r.parseRelationName().isEmpty()) {
										if(ip.parseAttributeCount() <= r.parseRelationSchema().size()){
											Tuple tp = ip.parseTuple();
											for (int x = 0; x < tp.parseTupleValues().size(); x++) {
												String type = r.parseRelationSchema().get(x).parseAttributeType();
												int constrict = r.parseRelationSchema().get(x).parseAttributeLength();
												if(constrict >= tp.parseTupleValues().get(x).parseAttName().length()){
													canAddTuple = true;
													if (type.equals("NUM")) {
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
											else{
												System.out.println("Invalid length or invalid type being inserted.\n");
											}
										}
										else{
												System.out.print("Cannot insert " + ip.parseAttributeCount() + " attributes into " + ip.parseRelationName());
												System.out.println(" (max = " + r.parseRelationSchema().size() + ").\n");
										}
									}
									else {
										System.out.println("Relation " + ip.parseRelationName() + " that you are trying to insert into does not exist within CATALOG.\n");
									}

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
								if(r.parseRelationName().isEmpty()){
									  System.out.println("Relation " + pp.parseRelationNames()[i] + " doesn't exist within CATALOG.\n");
										break;
								}
								int longestTuple = longestTuple(tu, sch);
								int count = 0;
								System.out.print(" ");
								for (int x = 0; x < ((longestTuple+6)*sch.size()); x++) {
									System.out.print("*");
									count++;
								}
								System.out.println(" ");
								System.out.print(" | " + r.parseRelationName());
								for (int x = 0; x < count - r.parseRelationName().length() - 3; x++) {
									System.out.print(" ");
								}
								System.out.print("|\n ");
								for (int x = 0; x < count; x++) {
									System.out.print("-");
								}
								System.out.print(" \n |");
								for(int j = 0; j < sch.size(); j++){
									int length1 = 0;
									System.out.print(" " + sch.get(j).parseAttributeName() + " ");
									while (length1 + sch.get(j).parseAttributeName().length() < (longestTuple + 2)) {
										if (length1 + sch.get(j).parseAttributeName().length() == longestTuple+1) {
											System.out.print(" |");
										}
											System.out.print(" ");
											length1++;
									}
								}
								System.out.print("\n ");
								for (int x = 0; x < count; x++) {
									System.out.print("-");
								}
								System.out.println(" ");
								for(int k = 0; k < tu.size(); k++){
									System.out.print(" |");
									LinkedList<AttributeValue> temp = tu.get(k).parseTupleValues();
									for(int l = 0; l < temp.size(); l++){
										int length2 = 0;
										System.out.print(" " + temp.get(l).parseAttName());
										while (length2 + temp.get(l).parseAttName().length() < (longestTuple + 1)) {
											  if (length2 + temp.get(l).parseAttName().length() == longestTuple) {
													System.out.print("   |");
												}
												System.out.print(" ");
												length2++;
										}
									}
									System.out.println(" ");
								}
								System.out.print(" ");
								for (int x = 0; x < count; x++) {
									System.out.print("*");
								}
								System.out.println(" \n");
							}

							break;


						case "DELETE": // Deletes single relation one at a time
							DeleteParser dlt = new DeleteParser(inputCommand);
							String relationDLT = dlt.parseRelationName();
							Relation r = database.getRelation(relationDLT);
							if(!relationDLT.equals("CATALOG") && !r.parseRelationName().isEmpty()){
								r.deleteTuples();
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
	public int longestTuple(LinkedList<Tuple> group1, LinkedList<Attribute> group2) {
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
