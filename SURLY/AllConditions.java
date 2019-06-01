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


	public LinkedList<Tuple> evaluateConditions(Relation r, String command){
		LinkedList<Tuple> temp = new LinkedList<Tuple>();
		LinkedList<Tuple> chosenOnes = new LinkedList<Tuple>();
		LinkedList<Tuple> tupleRef = r.parseRelationTuples();

		for(int i = 0; i < cLists.size(); i++){
			ConditionList c = cLists.get(i);
			temp = c.evalAllConds(r, command);
			chosenOnes.addAll(temp);
			if(chosenOnes.size() > 1 && i > 0){
				chosenOnes = removeDups(chosenOnes);
			}
			System.out.println("AC Current iteration: " + i);
			for(int j = 0; j < chosenOnes.size(); j++){
				chosenOnes.get(j).display();
	
			}

		}
		//chosenOnes.addAll(temp);


		return chosenOnes;
	}




}
