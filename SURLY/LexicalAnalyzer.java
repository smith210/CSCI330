/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : LexicalAnalyzer.java
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class LexicalAnalyzer{

	private Parser parser;
	private SurlyDatabase surly;

	LexicalAnalyzer(){
		surly = new SurlyDatabase();
		parser = new Parser();
	}

	public void run(String filename){
		File contain = null;
		Scanner line = null;

		try {
			contain = new File (filename);
			line = new Scanner (contain);
		}catch (Exception e){
			System.out.println("ERROR - no such file: " + filename);
			System.exit(0);
		}
		sendLine("", line);
	}

	private void updateCatalog(Relation catalog, String name, int attr){
		Tuple t = new Tuple();
		AttributeValue catalogRelation = new AttributeValue();
		AttributeValue catalogAttributes = new AttributeValue();
		catalogRelation.setValue("CATALOG");
		catalogAttributes.setValue("CATALOG");
		catalogRelation.setName(name);
		catalogAttributes.setName(Integer.toString(attr));
		t.add(catalogRelation);
		t.add(catalogAttributes);
		catalog.insertTuple(t);
	}

	private void parseInstructions(String command){

		switch(command){
			case "RELATION"://Parse Relation commands
				RelationParser rp = new RelationParser(parser);
				if(rp.parseAttributeCount() != -1){
					Relation r = rp.parseRelations();
					Relation search = surly.getRelation(r.parseRelationName());
					if(search.getTemp()){
						System.out.println(search.parseRelationName() + "is temp");
						surly.destroyRelation(search.parseRelationName());
						search = surly.getRelation(r.parseRelationName());
					}
					if(search.parseRelationName().isEmpty()){
						surly.add(r);
						updateCatalog(surly.getRelation("CATALOG"), rp.parseRelationName(), rp.parseAttributeCount());
					}else{
						System.out.println("Relation " + r.parseRelationName() + " already exists within CATALOG.\n");
					}

				}else{
					System.out.println("ERROR - invalid syntax inputted");//invalid syntax
				}
				break;
			case "INSERT"://parse Insert commands
				InsertParser ip = new InsertParser(parser);
				if(ip.parseAttributeCount() != -1){
					Relation r = surly.getRelation(ip.parseRelationName());
					if(!r.parseRelationName().isEmpty()){//relation exists
						if(ip.hasValidAttNum(r.parseRelationSchema().size())){//valid number of attributes inserting
							/*for(int x = 0; x < r.parseRelationSchema().size(); x++){
								ip.implementSize(x, r.parseRelationSchema().get(x).parseAttributeLength());
							}*/
							if(ip.isValid(r)){
								r.insertTuple(ip.parseTuple());
							}else{
								System.out.println("Cannot INSERT into " + r.parseRelationName());
							}
						}
					}
				}
				break;
			case "PRINT"://evaluate PRINT command
				PrintParser pp = new PrintParser(parser);
				String[] relationsToPrint = pp.parseRelationNames();
				for(int i = 0; i < relationsToPrint.length; i++){//for each Relation to print
					Relation r = surly.getRelation(relationsToPrint[i]);
					if(r.parseRelationName().isEmpty()){//don't print non-existant relations
						  System.out.println("Relation " + pp.parseRelationNames()[i] + " doesn't exist within CATALOG.\n");
					}else{
						r.display();
					}
				}
				break;
			case "DELETE": // Deletes relations
				DeleteParser dlt = new DeleteParser(parser);
				LinkedList<String> relations = dlt.parseCommands();
				String relationDLT = dlt.parseRelationName();
				Relation r = surly.getRelation(relationDLT);

				LinkedList<Tuple> empty = new LinkedList<Tuple>();
				//LinkedList<ConditionList
				//ConditionList conditionList = new ConditionList();

				if(!r.parseRelationName().isEmpty() && !r.getTemp()){//make sure not empty
					if(!parser.hasWhere()){
						r.deleteTuples(empty);
					}
					else {//specialized delete statement
						//LinkedList<Condition> setConditions = conditionList.retrieveList();
						//r.deleteTuples(conditionList.evalAllConds(r, command));
						AllConditions conditions = dlt.getConditions();
						System.out.println(conditions.size());
						//for(int i = 0; i < conditions.size(); i++){


							r.deleteTuples(conditions.evaluateConditions(r, command));
          
						//}
					}
			 	}else{
					System.out.println("Cannot DELETE from " + relationDLT);
				}
				break;
			case "DESTROY": // Destroys single relation one at a time
				DestroyParser dst = new DestroyParser(parser);
				String relationDST = dst.parseRelationName();
				Relation rel = surly.getRelation(dst.parseRelationName());
				if(!rel.getTemp()){
					surly.destroyRelation(relationDST);
				}else{
					System.out.println("Cannot DESTROY " + relationDST);
				}
				break;
			default: // Deal with temporary relations
				if(parser.hasEqual()){ //valid temporary relation
					Relation currTemp = surly.getRelation(command);				
					if(currTemp.parseRelationName().length() != 0){
						if(currTemp.getTemp()){
							surly.destroyRelation(currTemp.parseRelationName());
						}else{ //relation is not temp, DO NOT OVERWRITE!
							System.out.println("Cannot overwrite base relation!");
							break;
						}
					}
					switch(parser.getSecondaryName()){
						case "SELECT":
							SelectParser sPsr = new SelectParser(parser);
							sPsr.addRelation(surly);
							Relation temp = sPsr.getTempRelation();
							temp.tempBuff();
							if(temp.parseRelationName().length() != 0){
								surly.add(temp);
								updateCatalog(surly.getRelation("CATALOG"), temp.parseRelationName(), temp.parseRelationSchema().size());
							}
							break;
						case "PROJECT":
							ProjectParser prj = new ProjectParser(parser);
							if(prj.isValid()){


							}
							break;
						case "JOIN":
							JoinParser jPsr = new JoinParser(parser);
							if(jPsr.isValid()){


							}
							break;
						default://not valid temporary syntax

					}

				}

		}

	}

	private void sendLine(String command, Scanner s){
		String currLine = "";
		if(s.hasNextLine()){
			currLine = s.nextLine();
		}else{
			return;
		}


		if(currLine.isEmpty()){//don't parse empty lines
			sendLine(command, s);
		}else{
			int pointer = 0;
			while(pointer != currLine.length() && Character.isWhitespace(currLine.charAt(pointer))){
				pointer++;
			}
			if(pointer != 0){
				if(pointer == currLine.length()){
					sendLine(command,s);
				}else{
					currLine = currLine.substring(pointer);
				}
			}

			if(!currLine.contains("#")){//do not parse comments
				command = command.concat(currLine);
				if(command.contains(";")){//end of command has been reached
					parser = new Parser(command);
					//parser.printContent();
					String nextCommand = command.substring(command.indexOf(';') + 1);
					System.out.println(command);
					if(!parser.hasCatalog()){
						parseInstructions(parser.getRelationName());

					}else{
						if(parser.getRelationName().equals("PRINT")){
							parseInstructions(parser.getRelationName());
						}
					}
					//remove excess space from nextCommand
					int linePointer = 0;
					while(linePointer != currLine.length() &&
						  Character.isWhitespace(currLine.charAt(linePointer))){
						linePointer++;
					}
					nextCommand = nextCommand.substring(linePointer);
					sendLine(nextCommand, s);//start with new command
				}else{
					sendLine(command,s);//keep concatenating command until ';' seen
				}
			}else{
				sendLine("", s);//not parsing comments
			}
		}

	}

}
