/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : DeleteParser.java
*/


import java.util.*;

public class DeleteParser {
	private Parser parser;
  	private LinkedList<String> commands;

	 DeleteParser(String input) {
		Parser p = new Parser(input);
	 	commands = p.parseCommandSet();

	 }

	DeleteParser(Parser command){
		parser = command;
		commands = parser.parseCommandSet();
	}

	public boolean hasWhere(){
		boolean hasWhere = false;
		for(int i = 0; i < commands.size(); i++){
			if(commands.get(i).equals("WHERE")){
				hasWhere= true;
			}
		}
		return hasWhere;
	}

	public LinkedList<String> cases(){
		LinkedList<String> cases = new LinkedList<String>();		
		if(hasWhere()){
			int start = commands.indexOf("WHERE");
			for(int i = start; i < commands.size(); i++){
				/*switch(commands.get(start)){
					case "and":
						
						break;
					case "or":
						break;
					default:

				}*/

			} 
		}
		return cases;
	}

	public String parseRelationName() {
		return commands.get(0);
	}

  public LinkedList<String> parseCommands() {
    return commands;
  }
}
