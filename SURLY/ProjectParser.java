/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : ProjectParser.java
*/

import java.util.*;

public class ProjectParser{
	private LinkedList<Attribute> attributeNames;
	private String relationNameString;
	private Relation tempR;
	private Parser p;
	private LinkedList<String> commands;
	private boolean validStatement;

	ProjectParser(Parser p){
		attributeNames = new LinkedList<Attribute>();
		this.p = p;
		tempR = new Relation();
		validStatement = true;
		commands = p.parseCommandSet();

	}

	private int hasFrom(LinkedList<String> info){
		int i = 0;
		while(i != info.size() && !info.get(i).equals("FROM")){
			i++;
		}
		if(i == info.size()){
			i = -1;
			validStatement = false;
		}
		return i;
	}

	public void addInfo(SurlyDatabase s){
		LinkedList<String> content = p.parseCommandSet();
		int fromPtr = hasFrom(content);
		int checkValidAtts1 = 0;
		int checkValidAtts2 = 0;
		if(fromPtr != -1){
			relationNameString = content.get(fromPtr + 1);
			Relation relation = s.getRelation(relationNameString);

			for(int i = 3; i < fromPtr; i = i + 2){
				Attribute att = new Attribute();
				att.setAttributeName(content.get(i));
				for(int j = 0; j < relation.parseRelationSchema().size(); j++) {
					if(att.parseAttributeName().equals(relation.parseRelationSchema().get(j).parseAttributeName())) {
						checkValidAtts1++;
						att.setDataType(relation.parseRelationSchema().get(j).parseAttributeType());
						att.setLength(relation.parseRelationSchema().get(j).parseAttributeLength());
				  }
				}
				attributeNames.add(att);
				checkValidAtts2++;
			}
			if (checkValidAtts1 == checkValidAtts2) { // determines if user inputted appropriate number of attributes
				tempR.setName(content.get(0));
				tempR.setSchema(attributeNames);
			}
			else {
				if(relation.parseRelationSchema().size() == 0) { // size will be 0 if a schema was never established, meaning not a relation.
					System.out.println("Relation " + relationNameString + " does not exist; check input.");
				}
				else {
					System.out.println("Relation " + relationNameString + " does not contain given schema; check input.");
		   	}
			}
		}
	}

	public void addRelation(SurlyDatabase s){
		if(tempR.parseRelationName().length() != 0){

		  LinkedList<Tuple> tempTups = new LinkedList<Tuple>();
			LinkedList<Tuple> baseTups = new LinkedList<Tuple>();
			LinkedList<Attribute> baseAtts = new LinkedList<Attribute>();
			LinkedList<Attribute> tempAtts = new LinkedList<Attribute>();
			baseTups = s.getRelation(relationNameString).parseRelationTuples();
			baseAtts = s.getRelation(relationNameString).parseRelationSchema();
			tempAtts = tempR.parseRelationSchema();

	  	boolean check = false;
			for(int i = 0; i < tempAtts.size(); i++) {
				for(int j = 0; j < baseAtts.size(); j++) {
					if(baseAtts.get(j).parseAttributeName().equals(tempAtts.get(i).parseAttributeName())) {
						for(int k = 0; k < baseTups.size(); k++){
							if(tempTups.size() > 0 && check) {
								tempTups.get(k).add(baseTups.get(k).getAttVal(j));
							}
							else {
								tempTups.add(baseTups.get(k).getTuple(j));
							}
						}
						check = true;
					}
				}
			}

			int count = 0; // checking if a duplicate tuple exists by examining each tuple attribute
			for(int i = 0; i < tempTups.size(); i++) {
				for(int j = i+1; j < tempTups.size(); j++) {
					for(int k = 0; k < tempTups.get(i).parseTupleValues().size(); k++) {
						if(tempTups.get(i).parseTupleValues().get(k).parseAttName().equals(tempTups.get(j).parseTupleValues().get(k).parseAttName())) {
							count++;
						}
					}
					if (count == tempTups.get(i).parseTupleValues().size()) {
					  tempTups.remove(i);
						count = 0;
					}
				}
			}

			for(int i = 0; i < tempTups.size(); i++) {
		   	tempR.insertTuple(tempTups.get(i));
			}
		}
	}

	public Relation getTempRelation(){ return tempR; }

	public LinkedList<Attribute> getAttributeNames(){ return attributeNames; }

	public String getRelationName(){ return relationNameString; }

	public boolean isValid(){ return validStatement; }

}
