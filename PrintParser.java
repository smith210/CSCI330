/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : PrintParser.java
*/
import java.util.*;

public class PrintParser{ // mostly unmodified from SURLY1
	private String[] information;

	PrintParser(Parser p){
		information = new String[1];
		information[0] = "";
		addInfo(p);
	}

	PrintParser(String command){
		information = new String[1];
		information[0] = "";

		Parser p = new Parser(command);
		addInfo(p);

	}

	private void addInfo(Parser p){
		int pointer = 0;
		int attrVar = 0;
		boolean nameSet = false;


		LinkedList<String> commands = p.parseCommandSet();
		for(int i = 1; i < commands.size(); i++){
			String currCommand = commands.get(i);

			if(!currCommand.equals(",") && !currCommand.equals(";")){
				String[] newInformation = new String[attrVar + 1];
				if(!information[0].isEmpty()){
					for(int j = 0; j < attrVar; j++){
						newInformation[j] = information[j];
					}
				}
				newInformation[attrVar] = currCommand;
				information = newInformation;
				attrVar++;
			}
		}
	}

	public String[] parseRelationNames(){
		return this.information;
	}
}

// ### END ###
