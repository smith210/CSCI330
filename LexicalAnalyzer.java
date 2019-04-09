import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class LexicalAnalyzer{
	private static final int ATTRIBUTE_SYNTAX = 3;
	private static final int NO_COMMAND = 0;	
	private static final int RELATION_TYPE = 1;
	private static final int INSERT_TYPE = 2;
	private static final int PRINT_TYPE = 3;
	
	public static void run(String filename){
		File contain = null;
		Scanner line = null;

		try { 
			contain = new File (filename);
			line = new Scanner (contain);
		}catch (Exception e){}  
		
		List<String> symbols = new ArrayList<String>();
		
		int commandType = NO_COMMAND;


		boolean evalAttr = false;
		boolean nameSet = false;
		int sizeAttr = 0;
		int commaCounter = 0;

		while (line.hasNextLine()){
			String currLine = line.nextLine();

			if(!currLine.isEmpty()){			
				char[] charList = currLine.toCharArray();
				if(charList[0] != '#'){
					int frontIndex = 0;

					if(charList[0] == ' '){
						while(charList[frontIndex] == ' '){
							frontIndex++;
						}
					}

					int startFrontIndex = frontIndex;
					int endIndex = frontIndex;
					boolean inQuote = false;
					boolean isEqualOr = false;
					for(int i = startFrontIndex; i < charList.length; i++){
						String key = "";
						
						if(isBreak(charList[i])){
							switch(charList[i]){
								case '\'':
									if(!inQuote){
										inQuote = true;
									}else{
										inQuote = false;
										endIndex = i+1;
										key = currLine.substring(frontIndex+1, endIndex-1);
										frontIndex = endIndex;
									}
									break;
								case '(':
								case ')':								
								case ';':
								case '*':
									key = Character.toString(charList[i]);
									frontIndex++;
									break;
								case ',':
									if(evalAttr){
										if(sizeAttr == ATTRIBUTE_SYNTAX){
											sizeAttr = 0;
										}else{
											System.out.println("ERROR - num attributes given: " + sizeAttr);
											System.exit(0);
										}
									}
									commaCounter++;
									frontIndex++;
									break;
								case '>':
								case '<':
								case '!':
									if(charList[i+1] == '='){
										isEqualOr = true;
										frontIndex = i;
									}else{
										key = Character.toString(charList[i]);
										frontIndex++;
									}
									break;
								case '=':
									if(isEqualOr){
										endIndex = i+1;
										key = currLine.substring(frontIndex, endIndex);
										frontIndex = endIndex;
									}else{
										key = Character.toString(charList[i]);
										frontIndex++;
									}
									break;
								default:
									System.out.println("ERROR - invalid character");
									System.exit(0);
							}
						}else if(charList[i] == ' '){
							if(!inQuote){							
								frontIndex++;
							}
						}else{
							if(charList[i+1] == ' ' || isBreak(charList[i+1])){
								if(!inQuote){
									endIndex = i+1;
									key = currLine.substring(frontIndex, endIndex);
									frontIndex = endIndex;
								}
							}
						}

						if(!key.isEmpty()){
							switch(key){
								case "RELATION":
									commandType = RELATION_TYPE;
									break;
								case "INSERT":
									commandType = INSERT_TYPE;
									break;
								case "PRINT":
									commandType = PRINT_TYPE;
									break;
								case "(":
									evalAttr = true;
									break;
								case ")":
									if(sizeAttr == ATTRIBUTE_SYNTAX){
										sizeAttr = 0;
									}else{
										System.out.println("ERROR - num attributes given: " + sizeAttr);
										System.exit(0);
										}
									evalAttr = false;
									break;
								case ";":
									if(commandType == RELATION_TYPE){
										commaCounter++;
										System.out.print("Creating " + key + " with " + commaCounter + " attributes");
									}else if(commandType == INSERT_TYPE){
										//grab number of attributes
										System.out.print("Insert Inputted");
									}else if(commandType == PRINT_TYPE){
										commaCounter++;
										System.out.print("Printing " + commaCounter + " relations: " + key);
									}else{
										System.out.println("ERROR - didn't evaluate command");
										System.exit(0);
									}
									System.out.println(".");
									//reset for next command
									commaCounter = 0;
									nameSet = false;
									commandType = NO_COMMAND;
									break;
								default:
									if(commandType == RELATION_TYPE){								
										if(!evalAttr){
											RelationParser relation = new RelationParser(key);
											System.out.println(relation.parseRelationName());	
										}else{


										}
									}else if(commandType == INSERT_TYPE){
										if(nameSet){
											InsertParser insert = new InsertParser(key);
											System.out.println(insert.parseRelationName());	
											//count arguement counter
										}else{
											//look for relation											
											nameSet = true;
										}
									}else if(commandType == PRINT_TYPE){
										if(nameSet){
											//someting
										}else{
											nameSet = true;
										}
									}else{
										System.out.println("ERROR - no command specified");
										System.exit(0);
									}
							}				
							//System.out.println(commandType);			
							if(evalAttr && charList[i] != '('){
								sizeAttr++;
							}
						}
					}
				}
			}
		}

	}

	public static boolean isBreak(char eval){
		switch(eval){
			case ',':
			case '\'':
			case ';':
			case '(':
			case ')':
			case '<':
			case '>':
			case '=':
			case '*':
			case '!':
				return true;
			default:
				return false;
		}

	}

}
