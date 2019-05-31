/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : DeleteParser.java
*/


import java.util.*;

public class DeleteParser {
	private Parser parser;
  private LinkedList<String> commands;
	private LinkedList<ConditionList> conditions;

	 DeleteParser(String input) {
		Parser p = new Parser(input);
	 	commands = p.parseCommandSet();

	 }

	DeleteParser(Parser command){
		parser = command;
		conditions = new LinkedList<ConditionList>();
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
		if(hasWhere()){
			int start = commands.indexOf("WHERE");
			Condition c = new Condition();
			ConditionList curr = new ConditionList();
			int cPtr = 0;
			for(int i = start; i < commands.size(); i++){
				switch(commands.get(start)){
					case "and":
						if(c.syntaxValid()){
							curr.add(c);
						}
						break;
					case "or":
						if(c.syntaxValid()){
							curr.add(c);
							conditions.add(curr);
							curr = new ConditionList();
						}
						break;
					default://set up condition
						if(cPtr == 0){
							c.setLeft(commands.get(start));
						}else if(cPtr == 1){
							c.setEvaluator(commands.get(start));
						}else if(cPtr == 2){
							c.setRight(commands.get(start));
							cPtr = -1;
						}else{//error

						}
						cPtr++;
				}

			}
		}

	}

	public LinkedList<ConditionList> getConditions(){ return conditions; }

	public String parseRelationName() {
		return commands.get(1);
	}

  public LinkedList<String> parseCommands() {
    return commands;
  }
}
