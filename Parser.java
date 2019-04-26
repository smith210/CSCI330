import java.util.*;

public class Parser{
	private LinkedList<String> commandSet;

	Parser(String command){
		commandSet = new LinkedList<String>();
		String str = "";		
		int index = 0;
		if(isBreak(command.charAt(index))){
			breakCharState(command, index);
		}else{
			symbolState(command, str, index, false);
		}	
	}

	private void breakCharState(String command, int index){
			if(command.charAt(index) != ' ' && command.charAt(index) != '\''){
				commandSet.add(Character.toString(command.charAt(index)));
				if(command.charAt(index) == ';'){
					return;
				}
			}

		if(isBreak(command.charAt(index+1))){
			breakCharState(command, index+1);
		}else{
			if(command.charAt(index) == '\''){
				symbolState(command, "", index+1, true);
			}else{
				symbolState(command, "", index+1, false);
			}
		}
	}

	private void symbolState(String command, String str, int index, boolean inQuote){
		if(str.isEmpty()){
			str = Character.toString(command.charAt(index));
		}else{
			str = str.concat(Character.toString(command.charAt(index)));
		}
		if(isBreak(command.charAt(index+1))){
			if(!inQuote){
				commandSet.add(str);
				breakCharState(command, index + 1);
			}else{
				if(command.charAt(index+1) == '\''){
					commandSet.add(str);
					breakCharState(command, index + 1);
				}else{
					symbolState(command, str, index + 1, inQuote);
				}
			}
		}else{
			symbolState(command, str, index + 1, inQuote);
		}
		return;
	}

	private boolean isBreak(char c){
		switch(c){
			case '\'':
			case ' ':
			case '(':
			case ')':
			case '=':
			case '!':
			case ';':
			case '*':
			case ',':
			case '<':
			case '>':
				return true;
			default:
				return false;
		}	
	}
		/*int pointer = 0;
		int frontIndex = 0;
		int endIndex = 0;
		int attrVar = 0;
		int currCommand = 0;
		boolean nameSet = false;
		boolean inQuote = false;
		boolean isValidParentheses = false;
		boolean isEqualAttach = false;
		boolean hitBreak = false;
		boolean hitComma = false;
		while(pointer != command.length()){

			String symbol = "";
			char c = command.charAt(pointer);

			if(!inQuote){
				if(c == ' '){
					hitBreak = true;
				}else if(c == ','){
					hitComma = true;
					hitBreak = true;
				}else if(c  == '\''){
					if(command.indexOf('\'',pointer) == -1){//check for matching quote
						attrNum = -1;
						break;					
					}		
					frontIndex = pointer;
					endIndex = pointer;
					inQuote = true;
				}else if(c == '(' || ')' || ';'){
					frontIndex = pointer;
					endIndex = pointer++;
					hitBreak = true;

				}else if(c == '<' || c == '>' || c == '!'){
					frontIndex = pointer;
					endIndex = pointer + 1;					
					if(command.charAt(pointer+1) == '='){
						endIndex = pointer;
						isEqualAttach = true;
					}
					
					hitBreak = true;

				}else if(c == '=' || c == '*'){
					if(isEqualAttach && c == '='){
						endIndex = pointer;
					}
					hitBreak = true;
				}else{
					if(hitBreak){//set frontIndex to front of symbol
						frontIndex = pointer;					
						hitBreak = false;
						
						if(isValidParentheses){//count attribute definitions
							attrVar++;
						}
					}
					endIndex = pointer+1;
				}
				
				if(hitBreak && frontIndex != endIndex){//create symbol
					symbol = command.substring(frontIndex, endIndex);
					frontIndex = endIndex;
				}
			}else{
				if(c  == '\''){//grab String without the quotes
					endIndex = pointer+1;
					symbol = command.substring(frontIndex + 1, endIndex - 1);
					inQuote = false;
					frontIndex = endIndex;
				}
			}
			if(!symbol.isEmpty()){
				this.parseCommandSet[currCommand] = symbol;
				currCommand++;

			}
			pointer++;
		}*/


	public LinkedList<String> parseCommandSet(){
		return this.commandSet;
	}

	

}
