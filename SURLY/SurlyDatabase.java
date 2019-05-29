/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : SurlyDatabase.java
*/
import java.io.*;
import java.util.*;

public class SurlyDatabase{
	private LinkedList<Relation> relations;

	SurlyDatabase(){
		relations = new LinkedList<Relation>();
		//create the CATALOG
		Relation catalog = new Relation();
		LinkedList<Attribute> catalogSchema = new LinkedList<Attribute>();
		LinkedList<Tuple> catalogTuples = new LinkedList<Tuple>();
		Attribute catalogAttr1 = new Attribute();
		Attribute catalogAttr2 = new Attribute();
		catalog.setName("CATALOG");
		catalogAttr1.setAttributeName("RELATION");
		catalogAttr1.setDataType("CHAR");
		catalogAttr1.setLength(30);
		catalogAttr2.setAttributeName("ATTRIBUTES");
		catalogAttr2.setDataType("NUM");
		catalogSchema.add(catalogAttr1);
		catalogSchema.add(catalogAttr2);
		catalog.setSchema(catalogSchema);
		relations.add(catalog);//add CATALOG
	}

	public Relation getRelation(String name){
		Relation r = relations.getFirst();
		int index = 1;

		try{
			while(!(r.parseRelationName()).equals(name)){
				r = relations.get(index);
				index++;
			}
		}catch(Exception e){
				r = new Relation();
		}
		return r;
	}

	public void destroyRelation(String name){
		if(!name.equals("CATALOG") && !getRelation(name).parseRelationName().isEmpty()){//can't destroy CATALOG
			relations.remove(getRelation(name));
			Relation catalog = getRelation("CATALOG");
			catalog.deleteTuple(name);//delete the destroyed relation from the CATALOG
		}
		else {
			System.out.println("Cannot destroy " + name + "!");
		}
	}

	public void add(Relation relation){
		relations.add(relation);
	}

}
