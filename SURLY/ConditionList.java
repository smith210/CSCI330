/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : ConditionList.java
*/

import java.util.*;

public class ConditionList{
	private LinkedList<Condition> cList;

	ConditionList(){
		cList = new LinkedList<Condition>();
	}

	public void add(Condition c){ cList.add(c); }

	public int numConditions(){ return cList.size(); }

	public LinkedList<Condition> retrieveList(){ return cList; }

	private LinkedList<Tuple> removeDups(LinkedList<Tuple> t){
		LinkedList<Tuple> noDups = new LinkedList<Tuple>();
		while(t.size() != 0){
			Tuple tup = t.poll();
			if(t.contains(tup)){
				noDups.add(tup);
			}
		}
		return noDups;
	}

	public LinkedList<Tuple> evalAllConds(Relation r){//evaluate each condition
		LinkedList<Tuple> temp = new LinkedList<Tuple>();
		LinkedList<Tuple> chosenOnes = new LinkedList<Tuple>();
		LinkedList<Tuple> tupleRef = new LinkedList<Tuple>();

		for(int c = 0; c < r.parseRelationTuples().size(); c++){
			tupleRef.add(r.parseRelationTuples().get(c));
		}

		for(int i = 0; i < cList.size(); i++){
			Condition curr = cList.get(i);
			String left = curr.returnLeft();

			if(r.inSchema(left) != -1 && curr.typeValid(r.parseRelationSchema().get(r.inSchema(left)).parseAttributeType())) {//search
				temp = curr.parseTuples(r.inSchema(left), tupleRef, tupleRef);
				chosenOnes.addAll(temp);
				if(chosenOnes.size() > 1 && i > 0){
					chosenOnes = removeDups(chosenOnes);
				}
			}
			else {
				System.out.println("Attribute " + left + " is not in the schema.");
				chosenOnes = new LinkedList<Tuple>();
			}
  		}
		return chosenOnes;
	}
}
