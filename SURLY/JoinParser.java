/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : JoinParser.java
*/

import java.util.*;

public class JoinParser{
	private String relationAName;
	private String relationBName;
	private String relationTempName;

	private boolean validStatement;
	private Relation r;
	private Parser p;
	private JoinCondition join;

	JoinParser(Parser p){
		this.p = p;
		relationAName = new String();
		relationBName = new String();
		relationTempName = new String();
		r = new Relation();
		join = new JoinCondition();
		validStatement = true;
		addInfo();
	}

	public void setRelationA(Relation A){ join.setRelA(A); }

	public void setRelationB(Relation B){ join.setRelB(B); }
	
	public void getComparison(){
		join.compare();
		r = join.getJoinedRel();
		r.setName(relationTempName);
	}

	private int hasOn(LinkedList<String> info){
		int i = 0;
		while(i != info.size() && !info.get(i).equals("ON")){
			i++;
		}
		if(i == info.size()){
			i = -1;
			validStatement = false;
		}
		return i;
	}

	private void addInfo(){
		LinkedList<String> content = p.parseCommandSet();
		relationTempName = content.get(0);
		int onPtr = hasOn(content);
		if(onPtr != -1){
			relationAName = content.get(3);
			relationBName = content.get(5);
			join.setLeft(content.get(onPtr + 1));
			join.setEquivalence(content.get(onPtr + 2));
			join.setRight(content.get(onPtr + 3));
			
		}

	}

	public boolean isValid(){ 
		if(validStatement){
			validStatement = join.validEquivalence();
		}
		return validStatement;
	}

	public boolean validRelations(){ return join.evaluate(); }
	public String getRelationAName(){ return relationAName; }
	public String getRelationBName(){ return relationBName; }
	public Relation getTempRelation(){ return r; }

}
