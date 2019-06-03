/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : JoinCondition.java
*/

import java.util.*;

public class JoinCondition{
	private String left;
	private String right;
	private String equivalence;
	private Relation A;
	private Relation B;
	private Relation joinedRel;

	JoinCondition(){
		left = new String();
		right = new String();
		equivalence = new String();
		A = new Relation();
		B = new Relation();
		joinedRel = new Relation();
		joinedRel.tempBuff();
	}

	public void setLeft(String left){ this.left = left; }
	public void setRight(String right){ this.right = right; }
	public void setEquivalence(String equivalence){ this.equivalence = equivalence; }
	public void setRelA(Relation A){ this.A = A; }
	public void setRelB(Relation B){ this.B = B; }
	public Relation getJoinedRel(){ return joinedRel; }

	public void compare(){
		int AAtt = isAttinRel(left, A);
		int BAtt = isAttinRel(right, B);

		LinkedList<Tuple> ATups = A.parseRelationTuples();
		LinkedList<Tuple> BTups = B.parseRelationTuples();

		joinedRel.setSchema(createSchema());

		for(int i = 0; i < ATups.size(); i++){
			Tuple aTup = ATups.get(i);	
			AttributeValue aAttribute = aTup.parseTupleValues().get(AAtt);
			String aName = aAttribute.parseAttName();
	
			for(int j = 0; j < BTups.size(); j++){
				Tuple bTup = BTups.get(j);
				AttributeValue bAttribute = bTup.parseTupleValues().get(BAtt);
				String bName = bAttribute.parseAttName();
				if(aName.equals(bName)){
					Tuple ABTup = createTuple(aTup, bTup, BAtt);
					joinedRel.insertTuple(ABTup);
				}
				
			}

		}


		
	}


	private LinkedList<Attribute> createSchema(){
		LinkedList<Attribute> ASch = A.parseRelationSchema();
		LinkedList<Attribute> BSch = B.parseRelationSchema();
		LinkedList<Attribute> ABSch = new LinkedList<Attribute>();

		
		for(int j = 0; j < ASch.size(); j++){
				ABSch.add(ASch.get(j));
		}

		for(int i = 0; i < BSch.size(); i++){
			if(!BSch.get(i).parseAttributeName().equals(grabQualifyAtt(right))){
				ABSch.add(BSch.get(i));
			}
		}
		
		return ABSch;
	}

	private Tuple createTuple(Tuple a, Tuple b, int bInd){
		Tuple temp = new Tuple();		
		
		for(int j = 0; j < a.parseTupleValues().size(); j++){
			temp.add(a.parseTupleValues().get(j));
		}

		for(int i = 0; i < b.parseTupleValues().size(); i++){
			if(i != bInd){
				temp.add(b.parseTupleValues().get(i));
			}
		}
		
		return temp;		

	} 


	public boolean validEquivalence(){
		if(equivalence.equals("=")){
			return true;
		}else{
			return false;
		}
	}

	public boolean evaluate(){
		if(isAttinRel(left, A) != -1 && isAttinRel(right, B) != -1){
			return true;
		}else{
			return false;
		}
	}

	private int isAttinRel(String s, Relation r){
		int grabSchema = -1;
		if(validQualifyAtt(s)){
			int index = s.indexOf('.');
			grabSchema = r.inSchema(s.substring(index + 1));
		}else{
			grabSchema = r.inSchema(s);
		}
		return grabSchema;
	}

	private int countDots(String s){
		int dots = 0;		
		for(int i = 0; i < s.length(); i++){
			if(s.charAt(i) == '.'){
				dots = dots + 1;
			}
		}
		return dots;
	}

	private String grabQualifyAtt(String s){
		if(validQualifyAtt(s)){
			int dotLoc = s.indexOf('.');
			s = s.substring(dotLoc + 1);
		}
		return s;
	}

	private boolean validQualifyAtt(String s){
		if(countDots(s) == 1){
			return true;
		}else{
			return false;
		}
	}
	

}
