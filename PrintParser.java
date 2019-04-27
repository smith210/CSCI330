/*
Name: Martin Smith
CSCI 330 - Spring 2019
File Name : PrintParser.java
*/
import java.util.*;

public class PrintParser{
	private String[] information;

	PrintParser(String command){
		information = new String[1];		
		information[0] = "";
		int pointer = 0;
		int frontIndex = 0;
		int endIndex = 0;
		int attrVar = 0;
		boolean nameSet = false;
		boolean inQuote = false;
		boolean isValidParentheses = false;
		boolean isEqualAttach = false;
		boolean hitBreak = false;

		Parser p = new Parser(command);
		LinkedList<String> commands = p.parseCommandSet();
		for(int i = 0; i < commands.size(); i++){
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
