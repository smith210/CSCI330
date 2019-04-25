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
							}else{
								System.out.println("ERROR - invalid syntax inputted");
								System.exit(0);
							}
							break;
						case "INSERT":
							InsertParser ip = new InsertParser(inputCommand);
							
							if(ip.parseAttributeCount() != -1){
								System.out.println(ip.parseRelationName());
								Relation re = database.getRelation(ip.parseRelationName());
								int index = database.getRelationIndex(ip.parseRelationName());
								Tuple tp = ip.parseTuple();
								re.insertTuple(tp);
								database.replace(re, index);
								//System.out.print("Inserting " + ip.parseAttributeCount());
								//System.out.println(" attributes to " + ip.parseRelationName() + ".");
							}else{
								System.out.println("ERROR - invalid syntax inputted");
								System.exit(0);
							}
							break;
						case "PRINT":
							PrintParser pp = new PrintParser(inputCommand);
							System.out.print("Printing " + pp.parseRelationNames().length);
							System.out.print(" relations: ");
							
							String[] relationsToPrint = pp.parseRelationNames();
							for(int i = 0; i < relationsToPrint.length; i++){
								System.out.print(relationsToPrint[i]);
								if(i+1 != relationsToPrint.length){
									System.out.print(", ");
								}
							}
							System.out.println(".");
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
