public class Conditions{
	private String left; 
	private String conditionEval;
	private String right;

	public void setLeft(String left){ this.left = left; }
	public void setRight(String right){ this.right = right; }
	public void setEvaluator(String conditionEval){ this.conditionEval = conditionEval; }
	
	public String returnLeft(){return left;}
	public String returnRight(){return right;}
	public String returnEvaluator(){return conditionEval; }

}
