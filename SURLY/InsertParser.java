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

	InsertParser(Parser p){
		addInfo(p);

	}

	InsertParser(String command){

		Parser p = new Parser(command);
		addInfo(p);

	}

	private void addInfo(Parser p){
		Tuple t = new Tuple();
		int attrVar = 0;
		boolean nameSet = false;

		LinkedList<String> commands = p.parseCommandSet();
		for(int i = 1; i < commands.size(); i++){
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
		System.out.println(attrNum);
	}

	public boolean hasValidAttNum(int attLimit){
		if(attrNum == attLimit){
			return true;
		}else{
			return false;
		}
	}

	public void implementSize(int index, int attSize){
		System.out.println(tuple.parseTupleValues().get(index).parseAttName());
		if(attSize < tuple.parseTupleValues().get(index).parseAttName().length()){

			tuple.parseTupleValues().get(index).setName(tuple.parseTupleValues().get(index).parseAttName().substring(0, attSize));
		}

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
