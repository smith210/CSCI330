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

