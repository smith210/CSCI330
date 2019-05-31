import java.util.*;

public class ConditionList{
	private LinkedList<Condition> cList;

	ConditionList(){
		cList = new LinkedList<Condition>();
	}

	public void add(Condition c){ cList.add(c); }

	public int numConditions(){ return cList.size(); }

	public LinkedList<Condition> retrieveList(){ return cList; }

	public LinkedList<Tuple> evalAllConds(Relation r/*, String command*/){//evaluate each condition
		LinkedList<Tuple> chosenOnes = new LinkedList<Tuple>();
		for(int i = 0; i < cList.size(); i++){
			Condition curr = cList.get(i);
			String left = curr.returnLeft();
			if(r.inSchema(left) != -1 && curr.typeValid(r.parseRelationSchema().get(r.inSchema(left)).parseAttributeType())){//search
				chosenOnes.add(curr.parseTuples(r.inSchema(left), r.parseRelationTuples()));
				/*
				if(command.equals("DELETE")){//add to delete list
					--two linked lists, one to add one to not have
				}else{ //add to keep list
					--at end of for loop, determine command, if delete return not have, else reutn have
				}	
				*/
			}
			
		}
		return chosenOnes;
	}

}
