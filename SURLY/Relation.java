/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : Relation.java
*/

import java.util.*;

public class Relation{ // from SURLY1, modified primarily to fix the ugly PRINT code we had in SURLY1.
	private String name;
	private LinkedList<Attribute> schema;
	private LinkedList<Tuple> tuples;
	private boolean isTemp;

	Relation(){ // constructor
		this.name = "";
		this.schema = new LinkedList<Attribute>();
		this.tuples = new LinkedList<Tuple>();
		this.isTemp = false;
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

	public int tupleSize(){ return tuples.size(); }

	public void deleteAllTuples(){ // get rid of them all, for single cases
		tuples = new LinkedList<Tuple>();
	}

	public void deleteTuples(LinkedList<Tuple> deletes){ // get rid of few, for WHERE cases
		for(int i = 0; i < deletes.size(); i++){
			Tuple deleteTup = deletes.get(i);
			if(tuples.contains(deleteTup)){
				tuples.remove(deleteTup);
			}
		}
	}

	public void deleteTuple(String name){ // for the DESTROY relation case, and for overwriting single relations.
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
  // The functions below are soley for aesthetics with printing in the terminal.

	private int getWidth(Attribute att){ // is the attribute name longer than any values? Increase the distance between pipes in the tables.
		if(att.parseAttributeLength() < att.parseAttributeName().length()){
			return att.parseAttributeName().length();
		}else{
			return att.parseAttributeLength();
		}
	}

	private int tableWidth(){ // determine how big to make the tables.
		int width = 0;
		for(int i = 0; i < schema.size(); i++){
			width = width + getWidth(schema.get(i));
		}
		return width + schema.size() + 1;
	}

	private void printSpaces(int spaces){ // simply prints some spaces, made a function to clean up arguments.
		for(int i = 0; i < spaces; i ++){
			System.out.print(" ");
		}
	}

	private void displaySchema(Attribute curr){ // build a column line and immediately print the corresponding attribute name (SCHEMA ONLY)
		System.out.print("|");
		System.out.print(curr.parseAttributeName());
		printSpaces(curr.parseAttributeLength() - curr.parseAttributeName().length());
	}

	private void displayTuple(Tuple curr){ // build a column line and immediately print the corresponding attribute name (TUPLES ONLY)
		LinkedList<AttributeValue> info = curr.parseTupleValues();
		System.out.print("|");
		for(int i = 0; i < info.size(); i++){
			int max = getWidth(schema.get(i));
			System.out.print(info.get(i).parseAttName());
			printSpaces(max - info.get(i).parseAttName().length());
			System.out.print("|");
		}
	}

	private void createBorder(){ // creates border around table
		for(int dash = 0; dash < tableWidth(); dash++){
			System.out.print("-");
		}
		if(tableWidth() == 1){ // for empty relations
			System.out.print("-------------------");
		}
		System.out.println(" ");
	}

	public void display(){ // the primary called function from around the Java folder to print everything nicely
		createBorder();
		for(int currSch = 0; currSch < schema.size(); currSch++){
			displaySchema(schema.get(currSch));
		}
		System.out.print("|");
		if(tableWidth() == 1){
			printSpaces(18);
			System.out.print("|");
		}
		System.out.println(" ");

		createBorder();

		for(int currTup = 0; currTup < tuples.size(); currTup++){
			displayTuple(tuples.get(currTup));
			System.out.println(" ");
		}
		createBorder();
	}

	public int inSchema(String attName){ // a check function for how many attribute titles are in a desired schema
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

	public boolean getTemp(){ return isTemp; } // gets from the constructor the status of temp (is it a temporary relation?)

	public void tempBuff(){ isTemp = true; }

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

// ### END ###
