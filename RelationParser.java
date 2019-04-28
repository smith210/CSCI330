/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : RelationParser.java
*/

import java.util.*;

public class RelationParser{

	private String name;
	private int attrNum;
	private Relation relation;

	RelationParser(String command){
		int attrVar = 0;
		int schemaVal = 0;
		boolean nameSet = false;
		boolean hitComma = false;
		boolean evalAttribute = false;
		Relation relation = new Relation();
		Attribute attribute = new Attribute();
		LinkedList<Attribute> schema = new LinkedList<Attribute>();

		Parser p = new Parser(command);
		LinkedList<String> commands = p.parseCommandSet();
		for(int i = 0; i < commands.size(); i++){
			String currCommand = commands.get(i);
			if(!nameSet){
				name = currCommand;
				relation.setName(name);
				nameSet = true;
			}else{
				if(evalAttribute){

					switch(schemaVal){
						case 0:
							attribute.setAttributeName(currCommand);
							schemaVal++;
							break;
						case 1:
							attribute.setDataType(currCommand);
							schemaVal++;
							break;
						case 2:
						try {
							attribute.setLength(Integer.parseInt(currCommand));
						}
						catch(Exception e) {
							attribute.setLength(-1);
						}
							schemaVal++;
							break;
						case 3:
							if(currCommand.equals(",")){
								schema.add(attribute);
								attribute = new Attribute();
								attrVar++;
								schemaVal = 0;
							}else if(currCommand.equals(")")){
								schema.add(attribute);
								attribute = new Attribute();
								attrVar++;
								evalAttribute = false;
							}else{
								this.attrNum = -1;
							}
							break;
						default:
					}
				}
				if(currCommand.equals("(")){
					evalAttribute = true;
				}
			}
		}
		attrNum = attrVar;
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
