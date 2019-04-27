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
		/*while(pointer != command.length()){

			String symbol = "";
			char c = command.charAt(pointer);

			if(!inQuote){
				if(c == ' '){
					hitBreak = true;
				}else if(c == ','){
					hitBreak = true;
				}else if(c  == '\''){
					if(command.indexOf('\'',pointer) == -1){//check for matching quote
						break;					
					}		
					frontIndex = pointer;
					endIndex = pointer;
					inQuote = true;
					hitBreak = true;
				}else if(c == '('){
					if(command.indexOf(')',pointer) == -1){//check for matching parentheses
						break;					
					}					
					isValidParentheses = true;
					hitBreak = true;
				}else if(c == ')'){
					if(!isValidParentheses){//check for in order parentheses
						break;
					}
					hitBreak = true;

				}else if(c == '<' || c == '>' || c == '!'){
					if(command.charAt(pointer+1) == '='){
						frontIndex = pointer;
						endIndex = pointer;
						isEqualAttach = true;
					}
					
					hitBreak = true;

				}else if(c == '=' || c == '*'){
					if(isEqualAttach && c == '='){
						endIndex = pointer;
					}
					hitBreak = true;
				}else if(c == ';'){
					hitBreak = true;
				}else{
					if(hitBreak){//set frontIndex to front of symbol
						frontIndex = pointer;					
						hitBreak = false;
						
					}
					endIndex = pointer+1;
				}
				
				if(hitBreak && frontIndex != endIndex){//create symbol
					symbol = command.substring(frontIndex, endIndex);
					frontIndex = endIndex;
				}
			}else{
				if(c  == '\''){//grab symbol without the quotes
					endIndex = pointer+1;
					symbol = command.substring(frontIndex + 1, endIndex - 1);
					inQuote = false;
					frontIndex = endIndex;
				}
			}

			if(!symbol.isEmpty()){
				String[] newInformation = new String[attrVar + 1];
				if(!information[0].isEmpty()){
					for(int i = 0; i < attrVar; i++){
						newInformation[i] = information[i];
					}
				}
				newInformation[attrVar] = symbol;
				information = newInformation;
				attrVar++;

			}
			pointer++;
		}*/

	}
	
	public String[] parseRelationNames(){
		return this.information;
	}
}
