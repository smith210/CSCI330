/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : Condition.java
*/

import java.util.*;

public class Condition{
	private String left;
	private String conditionEval;
	private String right;

	Condition(){
		left = "";
		conditionEval = "";
		right = "";
	}

	public void setLeft(String left){ this.left = left; }
	public void setRight(String right){ this.right = right; }
	public void setEvaluator(String conditionEval){ this.conditionEval = conditionEval; }

	public String returnLeft(){return left;}
	public String returnRight(){return right;}
	public String returnEvaluator(){return conditionEval; }

	public boolean syntaxValid(){
		if(left.length() != 0 && conditionEval.length() != 0 && right.length() != 0){
			return true;
		}else{
			return false;
		}
	}

	public boolean typeValid(String type){
		if(!conditionEval.equals("=") && !conditionEval.equals("!=") && type.equals("CHAR")) {
			return false;
		}else{
			return true;
		}
	}

	public LinkedList<Tuple> parseTuples(int index, LinkedList<Tuple> allTuples, LinkedList<Tuple> tupleRef){

	    LinkedList<Tuple> chosenOnes = new LinkedList<Tuple>();

		for(int i = 0; i < allTuples.size(); i++){
			Tuple curr = allTuples.get(i);
			AttributeValue evaluating = curr.parseTupleValues().get(index);

			switch(conditionEval){
				case "=":
					if(right.equals(evaluating.parseAttName())) {
						chosenOnes.add(curr);
					}
					break;
				case "!=":
					if(!right.equals(evaluating.parseAttName())){
						chosenOnes.add(curr);
					}
					break;
				case "<":
					if(Integer.parseInt(evaluating.parseAttName()) < Integer.parseInt(right)){
						chosenOnes.add(curr);
					}
					break;
				case ">":
					if(Integer.parseInt(evaluating.parseAttName()) > Integer.parseInt(right)){
						chosenOnes.add(curr);
					}
					break;
				case "<=":
					if(Integer.parseInt(evaluating.parseAttName()) <= Integer.parseInt(right)){
						chosenOnes.add(curr);
					}
					break;
				case ">=":
					if(Integer.parseInt(evaluating.parseAttName()) >= Integer.parseInt(right)){
						chosenOnes.add(curr);

					}
					break;
				default://error

			}

		}

		return chosenOnes;

	}

}
