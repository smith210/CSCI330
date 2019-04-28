/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : Parser.java
*/

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

	public LinkedList<String> parseCommandSet(){
		return this.commandSet;
	}



}
