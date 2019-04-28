/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : DestroyParser.java
*/


import java.util.*;

public class DestroyParser {
  private String relation;

  DestroyParser(String input) {
    Parser p = new Parser(input);
		LinkedList<String> commands = p.parseCommandSet();
    for(int i = 0; i < commands.size(); i++){
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
