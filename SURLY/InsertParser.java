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
	}

	public boolean hasValidAttNum(int attLimit){
		if(attrNum == attLimit){
			return true;
		}else{
			return false;
		}
	}

	private boolean validType(AttributeValue a, String curr){

		if(curr.equals("NUM")){
			try{
				Integer.parseInt(a.parseAttName());
			}catch(Exception e){
				return false;
			}
		}
		return true;

		
	}

	private boolean validSize(AttributeValue a, int curr){

		if(curr < a.parseAttName().length()){
			return false;
		}else{
			return true;
		}


	}

	public boolean isValid(Relation r){
		LinkedList<Attribute> schema = r.parseRelationSchema();
		if(!r.getTemp()){
			int count = 0;
			for(int i = 0; i < tuple.parseTupleValues().size(); i++){
				AttributeValue a = tuple.parseTupleValues().get(i);
				Attribute curr = schema.get(i);
				if(validType(a, curr.parseAttributeType()) && validSize(a, curr.parseAttributeLength())){//don't do anything
				}else{
					break;
				}
				count++;
			}
		
			if(count != tuple.parseTupleValues().size()){
				return false;
			}else{
				return true;
			}
		}else{
			return false;
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
