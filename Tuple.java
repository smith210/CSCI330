/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : Tuple.java
*/

import java.util.*;

public class Tuple{ // kept from SURLY1
	private LinkedList<AttributeValue> values;

	Tuple(){ // constructor
		values = new LinkedList<AttributeValue>();
	}

	public void setTupleValues(LinkedList<AttributeValue> values){
		this.values = values;
	}

	public LinkedList<AttributeValue> parseTupleValues(){
		return this.values;
	}

	public void add(AttributeValue value){
		values.add(value);
	}

	public Tuple getTuple(int index) { // created solely for ProjectParser to hand-build tuples by the attribute!
		Tuple neww = new Tuple();
		neww.add(values.get(index));
		return neww;
	}

	public AttributeValue getAttVal(int index) { // created solely for ProjectParser to hand-build tuples by the attribute!
		return values.get(index);
	}

	public String getValue(String attributeName){
		int pointer = 0;
		String retVal = "";
		while(pointer != values.size() || retVal.isEmpty()){
			AttributeValue currVal = values.get(pointer);
			String currName = currVal.parseAttName();

			if(currName.equals(attributeName)){
				retVal = attributeName;
			}
			pointer++;
		}
		return retVal;
	}

	public void display(){ // a debugging function to show, per line, each entry in the tuples.
		for(int i = 0; i < values.size(); i++){
			AttributeValue a = values.get(i);
			System.out.println(a.parseAttName());
		}
	}
}

// ### END ###
