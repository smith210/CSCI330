import java.util.*;

public class Relation{
	private String name;
	private LinkedList<Attribute> schema;
	private LinkedList<Tuple> tuples;

	Relation(){
		this.name = "";
		this.schema = new LinkedList<Attribute>();
		this.tuples = new LinkedList<Tuple>();
	}

	public void setName(String name){
		this.name = name;
	}

	public void setSchema(LinkedList<Attribute> schema){
		this.schema = schema;
	}

	public void insertTuple(Tuple tuple){
		tuples.add(tuple);
	}
	
	public void deleteTuples(){
		tuples = new LinkedList<Tuple>();
	}

	public void deleteTuple(String name){
		int tupleIndex = 0;
		int attributeIndex = 0;		
		boolean isFound = false;
		try{
			while(tupleIndex != tuples.size()){
				LinkedList<AttributeValue> t = tuples.get(tupleIndex).parseTupleValues();
				for(int j = 0; j < t.size(); j++){
					if(t.get(j).parseAttName().equals(name)){
						isFound = true;
						attributeIndex = j;
						break;
					}
				}
				if(isFound){
					break;
				}
				tupleIndex++;
			}
			System.out.println("ALS");
			tuples.remove(tuples.get(tupleIndex));

		}catch(Exception e){
			System.out.println(e);
			System.exit(0);
		}
	}
	
	public String parseRelationName(){
		return this.name;
	}

	public LinkedList<Attribute> parseRelationSchema(){
		return this.schema;
	}

	public LinkedList<Tuple> parseRelationTuples(){
		return this.tuples;
	}
}

