/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : Condition.java
*/

import java.util.*;

public class Condition{ // class file for individual conditional statements (like CNUM != CSCI241)
	private String left;
	private String conditionEval;
	private String right;

	Condition(){ // constructor
		left = "";
		conditionEval = "";
		right = "";
	}
  // setters and getters
	public void setLeft(String left){ this.left = left; }
	public void setRight(String right){ this.right = right; }
	public void setEvaluator(String conditionEval){ this.conditionEval = conditionEval; }

	public String returnLeft(){return left;}
	public String returnRight(){return right;}
	public String returnEvaluator(){return conditionEval; }
  //

	public boolean syntaxValid(){ // basically, is there data passed into the condition
		if(left.length() != 0 && conditionEval.length() != 0 && right.length() != 0){
			return true;
		}else{
			return false;
		}
	}

	public boolean typeValid(String type){ // tests if a CHAR is a CHAR, or a NUM is a NUM. Else, we could get data type crashes.
		if(!conditionEval.equals("=") && !conditionEval.equals("!=") && type.equals("CHAR")) {
			return false;
		}else{
			return true;
		}
	}

	public LinkedList<Tuple> parseTuples(int index, LinkedList<Tuple> allTuples, LinkedList<Tuple> tupleRef){ // the primary evaluator
	  LinkedList<Tuple> chosenOnes = new LinkedList<Tuple>(); //

		for(int i = 0; i < allTuples.size(); i++){
			Tuple curr = allTuples.get(i);
			AttributeValue evaluating = curr.parseTupleValues().get(index);

			switch(conditionEval){ // basically, do what the operator tells you to do.
				case "=":
					if(right.equals(evaluating.parseAttName())) { // does the right equal left?
						chosenOnes.add(curr);
					}
					break;
				case "!=":
					if(!right.equals(evaluating.parseAttName())){ // does the right NOT equal left?
						chosenOnes.add(curr);
					}
					break;
				case "<": // these next four are only for numerical cases.
					if(Integer.parseInt(evaluating.parseAttName()) < Integer.parseInt(right)){ // is the left less than the right? ...
						chosenOnes.add(curr);
					}
					break;
				case ">":
					if(Integer.parseInt(evaluating.parseAttName()) > Integer.parseInt(right)){ // ... and etc.
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
				default: // error: there was an invalid operation.
				   System.out.println("Wrong operator!");
			}
		}
		return chosenOnes;
	}
}

// ### END ###
