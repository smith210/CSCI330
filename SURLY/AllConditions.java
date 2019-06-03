/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : AllConditions.java
*/

import java.util.*;

public class AllConditions{
	private LinkedList<ConditionList> cLists;

	AllConditions(){
		cLists = new LinkedList<ConditionList>();
	}

	public void add(ConditionList cL){ cLists.add(cL); }

	public int size(){ return cLists.size(); }

	public ConditionList get(int index){ return cLists.get(index); }

	public LinkedList<ConditionList> getConditions(){ return cLists; }

	private LinkedList<Tuple> removeDups(LinkedList<Tuple> t){
		LinkedList<Tuple> noDups = new LinkedList<Tuple>();
		while(t.size() != 0){
			Tuple tup = t.poll();
			if(!t.contains(tup)){
				noDups.add(tup);
			}
		}
		return noDups;
	}


	public void createAllConds(LinkedList<String> commands){
			int start = commands.indexOf("WHERE");
			Condition c = new Condition();
			ConditionList cL = new ConditionList();

			int cPtr = 0;
			for(int i = start; i < commands.size(); i++){
				switch(commands.get(i)){
					case "and":
						if(c.syntaxValid()){
							cL.add(c);
							c = new Condition();
						}
						break;
					case "or":
						if(c.syntaxValid()){
							cL.add(c);
							cLists.add(cL);
							cL = new ConditionList();
							c = new Condition();
						}
						break;
					case ";":
						if(c.syntaxValid()){
							cL.add(c);
							cLists.add(cL);
						}
						break;
					default://set up condition
						if(cPtr == 1){
							c.setLeft(commands.get(i));
						}else if(cPtr == 2){
							c.setEvaluator(commands.get(i));
						}else if(cPtr == 3){
							c.setRight(commands.get(i));
							cPtr = 0;
						}else{//error

						}
						cPtr++;
				}

			}

	}

	public LinkedList<Tuple> evaluateConditions(Relation r){
		LinkedList<Tuple> temp = new LinkedList<Tuple>();
		LinkedList<Tuple> chosenOnes = new LinkedList<Tuple>();
		LinkedList<Tuple> tupleRef = r.parseRelationTuples();

		for(int i = 0; i < cLists.size(); i++){
			ConditionList c = cLists.get(i);
			temp = c.evalAllConds(r);
			chosenOnes.addAll(temp);
			if(chosenOnes.size() > 1 && i > 0){
				chosenOnes = removeDups(chosenOnes);
			}
		}
		return chosenOnes;
	}
}
