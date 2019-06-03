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
		relationNameString = new String();
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


	public void addInfo(Relation relation){
		LinkedList<String> content = p.parseCommandSet();
		int fromPtr = hasFrom(content);
		int checkValidAtts1 = 0;
		int checkValidAtts2 = 0;
		if(fromPtr != -1){
			relationNameString = content.get(fromPtr + 1);
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

	public void addRelation(Relation r){
		if(tempR.parseRelationName().length() != 0){

		  	LinkedList<Tuple> tempTups = new LinkedList<Tuple>();
			LinkedList<Tuple> baseTups = new LinkedList<Tuple>();
			LinkedList<Attribute> baseAtts = new LinkedList<Attribute>();
			LinkedList<Attribute> tempAtts = new LinkedList<Attribute>();
			baseTups = r.parseRelationTuples();
			baseAtts = r.parseRelationSchema();
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
			removeDuplicates(tempTups);
		}

	}

	private boolean isDup(Tuple one, Tuple two, int colNums){
		int curr = 0;
		int similars = 0;
		while(curr != colNums){
			if(one.parseTupleValues().get(curr).parseAttName().equals(two.parseTupleValues().get(curr).parseAttName())){
				similars++;
			}
			curr++;
		}
		if(similars == colNums){
			return true;
		}else{
			return false;
		}
	}

	private void removeDuplicates(LinkedList<Tuple> tups){
		int curr = 0;
		int totalCols = tups.get(curr).parseTupleValues().size();
			
		while(curr != tups.size()){
			Tuple t = tups.get(curr);
			boolean canAdd = true;
			for(int i = curr + 1; i < tups.size(); i++){
				boolean alreadyExists = isDup(t, tups.get(i), totalCols);
				if(alreadyExists){
					canAdd = false;
					break;
				}
			}
			if(canAdd){
				tempR.insertTuple(t);
			}
			curr++;
		}	
	}

	public Relation getTempRelation(){ return tempR; }

	public LinkedList<Attribute> getAttributeNames(){ return attributeNames; }

	public String getRelationName(){ return commands.get(commands.size() - 2); }

	public boolean isValid(){ return validStatement; }

}
