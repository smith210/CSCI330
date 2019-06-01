/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : ProjectParser.java
*/

import java.util.*;

public class ProjectParser{
	private LinkedList<String> attributeNames;
	private String relationName;
	private Parser p;
	private boolean validStatement;


	ProjectParser(Parser p){
		this.p = p;
		attributeNames = new LinkedList<String>();
		validStatement = true;
		addInfo();

	}

	private int hasFrom(LinkedList<String> info){
		int i = 0;
		while(i != info.size() && !info.get(i).equals("FROM")){
			i++;
		}
		if(i == info.size()){
			i = -1;
			validStatement = false;
		}
		return i;
	}

	private void addInfo(){
		LinkedList<String> content = p.parseCommandSet();
		int fromPtr = hasFrom(content);
		if(fromPtr != -1){
			for(int i = 4; i < fromPtr; i++){
				attributeNames.add(content.get(i));
			}
			relationName = content.get(fromPtr + 1);
		}
	}

	public LinkedList<String> getAttributeNames(){ return attributeNames; }

	public String getRelationName(){ return relationName; }

	public boolean isValid(){ return validStatement; }

}
