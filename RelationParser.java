/*
Name: Martin Smith
CSCI 330 - Spring 2019
File Name : RelationParser.java
*/
import java.util.*;

public class RelationParser{

	private String name;
	private int attrNum;
	private Relation relation;

	RelationParser(String command){	
		int pointer = 0;
		int frontIndex = 0;
		int endIndex = 0;
		int attrVar = 0;
		int schemaVal = 0;
		boolean nameSet = false;
		boolean inQuote = false;
		boolean isValidParentheses = false;
		boolean isEqualAttach = false;
		boolean hitBreak = false;
		boolean hitComma = false;
		Relation relation = new Relation();
		Attribute attribute = new Attribute();
		LinkedList<Attribute> schema = new LinkedList<Attribute>();

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
				}else if(c == '('){
					if(command.indexOf(')',pointer) == -1){//check for matching parentheses
						attrNum = -1;
						break;					
					}		
					isValidParentheses = true;
					hitBreak = true;
				}else if(c == ')'){
					if(!isValidParentheses){//check for in order parentheses
						attrNum = -1;
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
						
						if(isValidParentheses){//count attribute definitions
							attrVar++;
						}
					}
					endIndex = pointer+1;
				}
				
				if(hitBreak && frontIndex != endIndex){//create symbol
					symbol = command.substring(frontIndex, endIndex);
					frontIndex = endIndex;
					if(attrNum != -1 && hitComma){//evaluating attribute definition
						if(attrVar % 3 != 0){//check for valid number of attributes
							attrNum = -1;
						}
						hitComma = false;
					}
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

				if(!nameSet){//set name of relation if not set
					name = symbol;
					relation.setName(name);			
					nameSet = true;
				}else{
					switch(schemaVal){
						case 0:
							attribute.setAttributeName(symbol);
							schemaVal++;
							break;
						case 1:
							attribute.setDataType(symbol);
							schemaVal++;
							break;
						case 2:
							attribute.setLength(Integer.parseInt(symbol));
							schema.add(attribute);
							schemaVal = 0;
							break;
						default:
					}
				}

			}
			pointer++;
		}
	if(attrNum != -1){//if not invalid, count number of attributes
		attrNum = attrVar / 3;
	}
	relation.setSchema(schema);
	this.relation = relation;
	}

	public String parseRelationName(){
		return this.name;
	}

	public int parseAttributeCount(){
		return this.attrNum;
	}

	public Relation parseRelations(){
		return this.relation;
	}

}
