import java.util.*;

public class ConditionList{
	private LinkedList<Condition> cList;

	ConditionList(){
		cList = new LinkedList<Condition>();
	}

	public void add(Condition c){ cList.add(c); }

	public int numConditions(){ return cList.size(); }

	public LinkedList<Condition> retrieveList(){ return cList; }

	public LinkedList<Tuple> evalAllConds(Relation r, String command){//evaluate each condition
		LinkedList<Tuple> trueConds = new LinkedList<Tuple>();
		LinkedList<Tuple> chosenOnes = new LinkedList<Tuple>();
		LinkedList<Tuple> tupleRef = r.parseRelationTuples();
		for(int i = 0; i < cList.size(); i++){
			Condition curr = cList.get(i);
			String left = curr.returnLeft();
			if(r.inSchema(left) != -1 && curr.typeValid(r.parseRelationSchema().get(r.inSchema(left)).parseAttributeType())) {//search
				trueConds = curr.parseTuples(r.inSchema(left), r.parseRelationTuples(), tupleRef, command);
				chosenOnes.addAll(trueConds);
			}

		  // if(command.equals("DELETE")){ //add to delete list; --two linked lists, one to add one to not have
			//   return tupleRef;
			// }
			// else if (command.equals("SELECT")){ //add to keep list; --at end of for loop, determine command, if delete return not have, else return have
			//   return chosenOnes;
			// }
		}
		return chosenOnes;
	}
}
