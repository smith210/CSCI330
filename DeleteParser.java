/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : DeleteParser.java
*/

import java.util.*;

public class DeleteParser { // modified from SURLY1
	private Parser parser;
  private LinkedList<String> commands;
	private AllConditions conditions;
	private LinkedList<String> relations;

	 DeleteParser(String input) {
		Parser p = new Parser(input);
	 	commands = p.parseCommandSet();
	 }

	DeleteParser(Parser command){ // constructor, automatically executes case creation if a WHERE exists.
		parser = command;
		conditions = new AllConditions();
		commands = parser.parseCommandSet();
		relations = new LinkedList<String>();
		getDeleteRelations();
		createCases();
	}

	private void getDeleteRelations(){ // for multiple deletes on one text file line (not for WHERE)
		for(int i = 1; i < commands.size(); i = i+2){
			relations.add(commands.get(i));
		}
	}

	private void createCases(){ // automatic execution, but only if WHERE is inputted
		if(parser.hasWhere()){
			conditions.createAllConds(commands);
		}
	}

	public AllConditions getConditions(){ return conditions; } // getter

	public String parseRelationName(){
		return commands.get(1);
	}

	public LinkedList<String> parseRelationNames() {
		return relations;
	}

  public LinkedList<String> parseCommands() {
	  return commands;
  }
}

// ### END ###
