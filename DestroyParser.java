/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : DestroyParser.java
*/

import java.util.*;

public class DestroyParser { // UML from previous SURLY
  	private String relation;

	DestroyParser(Parser p){
		getInfo(p);
	}

	DestroyParser(String input) {
		Parser p = new Parser(input);
		getInfo(p);
	}

	private void getInfo(Parser p){
		LinkedList<String> commands = p.parseCommandSet();
		for(int i = 1; i < commands.size(); i++){
		  String currCommand = commands.get(i);
		  if(!currCommand.equals(";")){
			relation = currCommand;
		  }
		}
	}

	public String parseRelationName() {
		return this.relation;
	}
}

// ### END ###
