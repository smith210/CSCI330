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

	/*private boolean hasDup(LinkedList<Tuple> t, Tuple tup, int index){
		boolean isDup = false;		
		int curr = 0;		
		while(curr != t.size()){
			if(t.get(curr).parseTupleValues().parseAttName().equals(tup.parseTupleValues().parseAttName())){
				isDup = true;
				break;
			}
			curr++;
		}
		return isDup;

	}*/

	public LinkedList<Tuple> evalAllConds(Relation r, String command){//evaluate each condition
		LinkedList<Tuple> temp = new LinkedList<Tuple>();
		LinkedList<Tuple> chosenOnes = new LinkedList<Tuple>();
		LinkedList<Tuple> tupleRef = new LinkedList<Tuple>();

		for(int c = 0; c < r.parseRelationTuples().size(); c++){
			tupleRef.add(r.parseRelationTuples().get(c));
		}
		System.out.println("TUPLESIZE " +  tupleRef.size());

		for(int i = 0; i < cList.size(); i++){

			Condition curr = cList.get(i);
			String left = curr.returnLeft();
			System.out.println("RIGHT: " + curr.returnRight());

			if(r.inSchema(left) != -1 && curr.typeValid(r.parseRelationSchema().get(r.inSchema(left)).parseAttributeType())) {//search

				temp = curr.parseTuples(r.inSchema(left), tupleRef, tupleRef, command);
				//if(temp.size() == 0){
					//System.out.println("EMPTY");
					//chosenOnes = temp;
				//}else{				
					chosenOnes.addAll(temp);
					if(chosenOnes.size() > 1 && i > 0){
						chosenOnes = removeDups(chosenOnes);
					}
				//}
				//chosenOnes.addAll(temp);
			}


			System.out.println("CL Current iteration: " + i);
			for(int j = 0; j < chosenOnes.size(); j++){
				chosenOnes.get(j).display();
	
			}

		}


		/*for(int j = 0; j < temp.size(); j++){
			Tuple t = temp.get(j);
			if(t

		}*/

		//chosenOnes.addAll(temp);

		return chosenOnes;
	}
}
