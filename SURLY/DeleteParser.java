/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : DeleteParser.java
*/


import java.util.*;

public class DeleteParser {
	private Parser parser;
    private LinkedList<String> commands;
	private AllConditions conditions;
	private LinkedList<String> relations;

	 DeleteParser(String input) {
		Parser p = new Parser(input);
	 	commands = p.parseCommandSet();
	 }

	DeleteParser(Parser command){
		parser = command;
		conditions = new AllConditions();
		commands = parser.parseCommandSet();
		relations = new LinkedList<String>();
		getDeleteRelations();
		createCases();
	}

	private void getDeleteRelations(){
		for(int i = 1; i < commands.size(); i = i+2){
			relations.add(commands.get(i));
		}
	}

	private void createCases(){
		if(parser.hasWhere()){
			conditions.createAllConds(commands);
		}
	}

	public AllConditions getConditions(){ return conditions; }

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
