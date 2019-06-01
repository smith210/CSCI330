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

	public boolean hasWhere(){
		boolean hasWhere = false;
		for(int i = 0; i < commands.size(); i++){
			if(commands.get(i).equals("WHERE")){
				hasWhere= true;
			}
		}
		return hasWhere;
	}

	private void createCases(){
		parser.printContent();
		if(hasWhere()){
			int start = commands.indexOf("WHERE");
			Condition c = new Condition();
			ConditionList cList = new ConditionList();

			int cPtr = 0;
			for(int i = start; i < commands.size(); i++){
				switch(commands.get(i)){
					case "and":
						if(c.syntaxValid()){
							cList.add(c);
							c = new Condition();
						}
						break;
					case "or":
						if(c.syntaxValid()){
							cList.add(c);
							conditions.add(cList);
							cList = new ConditionList();
							c = new Condition();
						}
						break;
					case ";":
						if(c.syntaxValid()){
							cList.add(c);
							conditions.add(cList);
						}
						break;
					default://set up condition
						if(cPtr == 1){
							System.out.println("LEFT: " + commands.get(i));
							c.setLeft(commands.get(i));
						}else if(cPtr == 2){
							System.out.println("MIDDLE: " + commands.get(i));
							c.setEvaluator(commands.get(i));
						}else if(cPtr == 3){
							System.out.println("RIGHT: " + commands.get(i));
							c.setRight(commands.get(i));
							cPtr = 0;
						}else{//error

						}
						cPtr++;
				}

			}
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
