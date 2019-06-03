/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : SelectParser.java
*/

import java.util.*;

public class SelectParser{ // new function from SURLY2, similar logic as PROJECT, but simpler
	private String relationname;
	private Relation tempR;
	private AllConditions cLists;
	private LinkedList<String> commands;
	private Parser p;

	SelectParser(Parser p){ // constructor
		relationname = new String();
		tempR = new Relation();
		cLists = new AllConditions();
		this.p = p;
		commands = p.parseCommandSet();
		addInfo();
	}

	private void addInfo(){ // after making sure a WHERE clause exists, conditions are applied. Otherwise just return verbatim the info of the queried relation.
		relationname = commands.get(0);
		if(p.hasWhere()){
			cLists.createAllConds(commands);
		}
	}

	public void addRelation(Relation r){ // sets up new relation and info
		tempR.setName(relationname);
		tempR.setSchema(new LinkedList<Attribute>());
		if(r.parseRelationName().length() != 0){
			tempR.setSchema(r.parseRelationSchema());
			LinkedList<Tuple> tups = new LinkedList<Tuple>();
			if(cLists.size() != 0){
				tups = cLists.evaluateConditions(r); // other important location where conditions are evaluated
			}else{
				tups = r.parseRelationTuples();
			}
			for(int i = 0; i < tups.size(); i++){ // insert one by one
				tempR.insertTuple(tups.get(i));
			}
		}
	}

	public String getRelationName(){ return commands.get(3); } // the fourth token should always be the name (i.e EXAMPLE = SELECT DEPT ....)

	public Relation getTempRelation(){ return tempR; }

}

// ### END ###
