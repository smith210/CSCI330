/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : SelectParser.java
*/

import java.util.*;

public class SelectParser{
	private String relationname;
	private Relation tempR;
	private AllConditions cLists;
	private LinkedList<String> commands;
	private Parser p;

	SelectParser(Parser p){
		relationname = new String();
		tempR = new Relation();
		cLists = new AllConditions();
		this.p = p;
		commands = p.parseCommandSet();
		addInfo();
	}

	private void addInfo(){

		relationname = commands.get(0);

		if(p.hasWhere()){
			cLists.createAllConds(commands);
		}

	}
	
	public void addRelation(SurlyDatabase s){
		Relation r = s.getRelation(commands.get(3));
		if(r.parseRelationName().length() != 0){
			tempR.setName(relationname);
			tempR.setSchema(r.parseRelationSchema());

			LinkedList<Tuple> tups = new LinkedList<Tuple>();
			if(cLists.size() != 0){
				tups = cLists.evaluateConditions(r, "SELECT");
			}else{
				tups = r.parseRelationTuples();
			}
			for(int i = 0; i < tups.size(); i++){
				tempR.insertTuple(tups.get(i));
			}
		}

	}

	public Relation getTempRelation(){ return tempR; }

}
