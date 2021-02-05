/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : AllConditions.java
*/

import java.util.*;

public class AllConditions{ // stores the list of inputted conditions lists. For all of the conditions nested inside OR statements.
	private LinkedList<ConditionList> cLists;

	AllConditions(){
		cLists = new LinkedList<ConditionList>();
	}

	public void add(ConditionList cL){ cLists.add(cL); }

	public int size(){ return cLists.size(); } // return how many condition clauses there are

	public ConditionList get(int index){ return cLists.get(index); }

	public LinkedList<ConditionList> getConditions(){ return cLists; } // getter


	private LinkedList<Tuple> removeDups(LinkedList<Tuple> t){ // ANDS/ORS with same result are eliminated
		LinkedList<Tuple> noDups = new LinkedList<Tuple>();
		while(t.size() != 0){
			Tuple tup = t.poll();
			if(!t.contains(tup)){
				noDups.add(tup);
			}
		}
		return noDups;
	}

	public void createAllConds(LinkedList<String> commands){ // a mini parser to get the left and right attributes with an operator in the middle.
			int start = commands.indexOf("WHERE");
			Condition c = new Condition();
			ConditionList cL = new ConditionList();

			int cPtr = 0;
			for(int i = start; i < commands.size(); i++){
				switch(commands.get(i)){
					case "and": // the AND case appends requirements to the query
						if(c.syntaxValid()){
							cL.add(c);
							c = new Condition();
						}
						break;
					case "or": // like AND, but creates a new list of AND conditions to divide the requirements
						if(c.syntaxValid()){
							cL.add(c);
							cLists.add(cL);
							cL = new ConditionList();
							c = new Condition();
						}
						break;
					case ";": // no more new conditions added
						if(c.syntaxValid()){
							cL.add(c);
							cLists.add(cL);
						}
						break;
					default: // officially set up condition
						if(cPtr == 1){
							c.setLeft(commands.get(i));
						}else if(cPtr == 2){
							c.setEvaluator(commands.get(i));
						}else if(cPtr == 3){
							c.setRight(commands.get(i));
							cPtr = 0;
						}
						cPtr++;
				}
			}
	}

	public LinkedList<Tuple> evaluateConditions(Relation r){ // home function for evaluating (testing against the database) the conditions.
		LinkedList<Tuple> temp = new LinkedList<Tuple>();
		LinkedList<Tuple> chosenOnes = new LinkedList<Tuple>(); // tuples that meet the requirements
		LinkedList<Tuple> tupleRef = r.parseRelationTuples();

		for(int i = 0; i < cLists.size(); i++){
			ConditionList c = cLists.get(i);
			temp = c.evalAllConds(r); // the evaluations are done here
			chosenOnes.addAll(temp);
			if(chosenOnes.size() > 1 && i > 0){
				chosenOnes = removeDups(chosenOnes); // helper function from above
			}
		}
		return chosenOnes;
	}
}

// ### END ###
