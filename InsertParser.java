/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : InsertParser.java
*/

import java.util.*;

public class InsertParser{
	private String name;
	private int attrNum;
	private Relation relation;
	private Tuple tuple;

	InsertParser(String command){
		Tuple t = new Tuple();
		int attrVar = 0;
		boolean nameSet = false;

		Parser p = new Parser(command);
		LinkedList<String> commands = p.parseCommandSet();
		for(int i = 0; i < commands.size(); i++){
			AttributeValue value = new AttributeValue();
			String currCommand = commands.get(i);

			if(!nameSet){//set name of relation if not set
				name = currCommand;
				nameSet = true;
			}else{
				if(!currCommand.equals(";")){
					value.setValue(name);
					value.setName(currCommand);
					t.add(value);
					attrVar = attrVar + 1;
				}
			}
		}
		tuple = t;
		attrNum = attrVar;
	}

	public Tuple parseTuple(){
		return tuple;
	}

	public String parseRelationName(){
		return this.name;
	}

	public int parseAttributeCount(){
		return this.attrNum;
	}
}
