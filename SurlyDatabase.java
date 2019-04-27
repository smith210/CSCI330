/*
Name: Martin Smith
CSCI 330 - Spring 2019
File Name : SurlyDatabase.java
*/
import java.io.*;
import java.util.*;

public class SurlyDatabase{
	private LinkedList<Relation> relations;

	SurlyDatabase(){
		relations = new LinkedList<Relation>();
		Relation catalog = new Relation();
		LinkedList<Attribute> catalogSchema = new LinkedList<Attribute>();
		LinkedList<Tuple> catalogTuples = new LinkedList<Tuple>();
		Attribute catalogAttr1 = new Attribute();
		Attribute catalogAttr2 = new Attribute();
		catalog.setName("CATALOG");
		catalogAttr1.setAttributeName("RELATION");
		catalogAttr1.setDataType("CHAR");
		catalogAttr2.setAttributeName("ATTRIBUTES");
		catalogAttr2.setDataType("NUM");
		catalogSchema.add(catalogAttr1);
		catalogSchema.add(catalogAttr2);
		catalog.setSchema(catalogSchema);
		relations.add(catalog);
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
			System.out.println(e);
			System.exit(0);
		}
		return r;
	}

	public void destroyRelation(String name){
		Relation r = getRelation("CATALOG");
		r.deleteTuple(name);

	}

	public int getRelationIndex(String name){
		int index = 0;
		Relation r = relations.get(index);
		try{
			while(!(r.parseRelationName()).equals(name)){
				r = relations.get(index);
				index++;
			}
		}catch(Exception e){
			index = -1;
		}
		return index;
	}

	public void replace(Relation relation, int index){
		relations.add(index, relation);
	}

	public void add(Relation relation){
		
		relations.add(relation);

	}
	
}
