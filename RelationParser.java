/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : RelationParser.java
*/

import java.util.*;

public class RelationParser{ // from SURLY1

	private String name;
	private int attrNum;
	private Relation relation;

	RelationParser(){ // constructors
		relation = new Relation();
	}

	RelationParser(String command){
		relation = new Relation();
		Parser p = new Parser(command);
		LinkedList<String> commands = p.parseCommandSet();
		createRelation(commands);
	}

	RelationParser(Parser commands){
		relation = new Relation();
		createRelation(commands.parseCommandSet());
	}

	public void createRelation(LinkedList<String> commands){ // numerically builds from scratch a relation, starting with a schema
		Attribute attribute = new Attribute();                 // assumes proper syntax (i.e. CNUM CHAR 8).
		LinkedList<Attribute> schema = new LinkedList<Attribute>();

		int attrVar = 0;
		int schemaVal = 0;
		boolean nameSet = false;
		boolean hitComma = false;
		boolean evalAttribute = false;

		for(int i = 1; i < commands.size(); i++){
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
							attrNum = -1;
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
		if(attrNum != -1){
			attrNum = attrVar;
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

// ### END ###
