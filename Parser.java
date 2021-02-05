/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : Parser.java
*/

import java.util.*;

public class Parser{ // does all of the file processing.
	private LinkedList<String> commandSet;

	Parser(){
		commandSet = new LinkedList<String>();
	}

	Parser(String command){ // two constructions
		commandSet = new LinkedList<String>();
		String str = "";
		int index = 0;
		if(isBreak(command.charAt(index))){
			breakCharState(command, index);
		}else{
			symbolState(command, str, index, false);
		}
	}

	private void breakCharState(String command, int index){ // makes sure operators with more than one character (like <=) are dealt with.
			char curr = command.charAt(index);
			if(curr != ' ' && curr != '\''){
				if(curr == '!' && command.charAt(index+1) == '=' || curr == '>' && command.charAt(index+1) == '=' || curr == '<' && command.charAt(index+1) == '='){

					String equivalence = Character.toString(curr);
					equivalence = equivalence.concat("=");
					commandSet.add(equivalence);
					index = index + 1;
				}else{
					commandSet.add(Character.toString(command.charAt(index)));
					if(command.charAt(index) == ';'){
						return;
					}
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

	private void symbolState(String command, String str, int index, boolean inQuote){ // tosses between breakCharState and itself, depending on the character list from below
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

	public void printContent(){ // for debugging purposes, make sure Parser is getting the right values.
		for(int i = 0; i < commandSet.size(); i++){
			System.out.println(commandSet.get(i));
		}
	}

	public boolean hasCatalog(){ // is CATALOG in the line? CATALOG has special qualities as a relation, so it is a necessary boolean.
		boolean catalogPresent = false;
		for(int i = 0; i < commandSet.size(); i++){
			String curr = commandSet.get(i);
			if(curr.equals("CATALOG")){
				catalogPresent = true;
			}
		}
		return catalogPresent;
	}

	public boolean hasWhere(){ // imperative boolean check if WHERE exists.
		boolean hasWhere = false;
		for(int i = 0; i < commandSet.size(); i++){
			if(commandSet.get(i).equals("WHERE")){
				hasWhere= true;
			}
		}
		return hasWhere;
	}

	public boolean hasEqual(){
		return commandSet.get(1).equals("=");
	}

	public String getSecondaryName(){ // basically, any command from SURLY2. A command that isn't the first word of a line.
		return commandSet.get(2);
	}

	public String getRelationName(){ // getter
		return commandSet.get(0);
	}

	private boolean isBreak(char c){ // list of special characters for our purposes, mostly from SURLY0.
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

// ### END ###
