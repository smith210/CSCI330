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

	 DeleteParser(String input) {
		Parser p = new Parser(input);
	 	commands = p.parseCommandSet();

	 }

	DeleteParser(Parser command){
		parser = command;
		conditions = new AllConditions();
		commands = parser.parseCommandSet();
		createCases();
	}

	/*private Conditions createCond(int index){
		switch(index){
			case 0://left

				break;
			case 1://evaluator

				break;
			case 2://right

				break;
			default:

		}

	}*/

	private void createCases(){
		parser.printContent();
		if(parser.hasWhere()){
			conditions.createAllConds(commands);
		}

	}

	public AllConditions getConditions(){ return conditions; }

	public String parseRelationName() {
		return commands.get(1);
	}
 

  public LinkedList<String> parseCommands() {
    return commands;
  }
}
