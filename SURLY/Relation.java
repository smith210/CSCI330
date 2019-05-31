/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : Relation.java
*/


import java.util.*;

public class Relation{
	private String name;
	private LinkedList<Attribute> schema;
	private LinkedList<Tuple> tuples;

	Relation(){
		this.name = "";
		this.schema = new LinkedList<Attribute>();
		this.tuples = new LinkedList<Tuple>();
	}

	public void setName(String name){
		this.name = name;
	}

	public void setSchema(LinkedList<Attribute> schema){
		this.schema = schema;
	}

	public void insertTuple(Tuple tuple){
		tuples.add(tuple);
	}

	public void deleteTuples(){
		tuples = new LinkedList<Tuple>();
	}

	public void deleteTuple(String name){
		int tupleIndex = 0;
		int attributeIndex = 0;
		boolean isFound = false;
		try{
			while(tupleIndex != tuples.size()){
				LinkedList<AttributeValue> t = tuples.get(tupleIndex).parseTupleValues();
				for(int j = 0; j < t.size(); j++){
					if(t.get(j).parseAttName().equals(name)){
						isFound = true;
						attributeIndex = j;
						break;
					}
				}
				if(isFound){
					break;
				}
				tupleIndex++;
			}
			tuples.remove(tuples.get(tupleIndex));

		}catch(Exception e){
			System.out.println(e);
			System.exit(0);
		}
	}

	private int getWidth(Attribute att){
		if(att.parseAttributeLength() < att.parseAttributeName().length()){
			return att.parseAttributeName().length();
		}else{
			return att.parseAttributeLength();
		}
	}

	private int tableWidth(){
		int width = 0;		
		for(int i = 0; i < schema.size(); i++){
			width = width + getWidth(schema.get(i));
		}	
		return width + schema.size() + 1;
	}

	private void printSpaces(int spaces){
		for(int i = 0; i < spaces; i ++){
			System.out.print(" ");
		}
	}

	private void displaySchema(Attribute curr){
		System.out.print("|");
		System.out.print(curr.parseAttributeName());
		printSpaces(curr.parseAttributeLength() - curr.parseAttributeName().length());

	}

	private void displayTuple(Tuple curr){
		LinkedList<AttributeValue> info = curr.parseTupleValues();	
		System.out.print("|");
		for(int i = 0; i < info.size(); i++){
			int max = getWidth(schema.get(i));
			System.out.print(info.get(i).parseAttName());			
			printSpaces(max - info.get(i).parseAttName().length());
			System.out.print("|");
		}
		
	}

	private void createBorder(){
		for(int dash = 0; dash < tableWidth(); dash++){
			System.out.print("-");
		}
		System.out.println(" ");
	}


	public void display(){
		createBorder();

		for(int currSch = 0; currSch < schema.size(); currSch++){
			displaySchema(schema.get(currSch));
		}
		System.out.println("|");

		createBorder();

		for(int currTup = 0; currTup < tuples.size(); currTup++){
			displayTuple(tuples.get(currTup));
			System.out.println(" ");
		}
		createBorder();


	}

	public int inSchema(String attName){
		int i = 0;		
		while(i != schema.size() && !schema.get(i).parseAttributeName().equals(attName)){
			i++;
		}
		if(i == schema.size()){ 
			return -1;
		}else{
			return i;
		} 
	}

	public String parseRelationName(){
		return this.name;
	}

	public LinkedList<Attribute> parseRelationSchema(){
		return this.schema;
	}

	public LinkedList<Tuple> parseRelationTuples(){
		return this.tuples;
	}
}
