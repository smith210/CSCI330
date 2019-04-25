/*
Name: Martin Smith
CSCI 330 - Spring 2019
File Name : InsertParser.java
*/

public class InsertParser{
	private String name;
	private int attrNum;
	private Relation relation;
	private Tuple tuple;

	InsertParser(String command){
		//Relation relation = new Relation();
		AttributeValue value = new AttributeValue();
		Tuple t = new Tuple();
		int pointer = 0;
		int frontIndex = 0;
		int endIndex = 0;
		int attrVar = 0;
		boolean nameSet = false;
		boolean inQuote = false;
		boolean isValidParentheses = false;
		boolean isEqualAttach = false;
		boolean hitBreak = false;

		while(pointer != command.length()){

			String symbol = "";
			char c = command.charAt(pointer);

			if(!inQuote){
				if(c == ' '){
					hitBreak = true;
				}else if(c == ','){
					hitBreak = true;
				}else if(c  == '\''){
					if(command.indexOf('\'',pointer) == -1){//check for matching quote
						attrNum = -1;
						break;					
					}		
					frontIndex = pointer;
					endIndex = pointer;
					inQuote = true;
					hitBreak = true;
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
				if(!nameSet){//set name of relation if not set
					name = symbol;
					value.setValue(name);
					nameSet = true;
				}else{
					AttributeValue attr = new AttributeValue();
					value.setName(symbol);
					t.add(value);
					attrVar = attrVar + 1;
				}

			}
			pointer++;
		}
	tuple = t;
	if(attrNum != -1){//if not invalid, count number of attributes
		attrNum = attrVar;
	}
	}

	public Tuple parseTuple(){
		return tuple;
	}

	public String parseRelationName(){
		return this.name;
	}

	public int parseAttributeCount(){
		return this.attrNum;
	}
}
