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
			System.out.println("YA A BINCH " + e);
			System.exit(0);
		}
		return r;
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
