/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : Attribute.java
*/

public class Attribute{ // UML from previous SURLY
	private String name;
	private String dataType;
	private int length;

	public void setAttributeName(String name){
		this.name = name;
	}

	public void setDataType(String dataType){
		this.dataType = dataType;
	}

	public void setLength(int length){
		this.length = length;
	}

	public String parseAttributeName(){
		return this.name;
	}

	public String parseAttributeType(){
		return this.dataType;
	}

	public int parseAttributeLength(){
		return this.length;
	}
}

// ### END ###
