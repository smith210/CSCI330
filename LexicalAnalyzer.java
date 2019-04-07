import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class LexicalAnalyzer{
	private static final int ATTRIBUTE_SYNTAX = 3;
	
	public static void run(String filename){
		File contain = null;
		Scanner line = null;

		try { 
			contain = new File (filename);
			line = new Scanner (contain);
		}catch (Exception e){}  
		
		List<String> symbols = new ArrayList<String>();

		boolean evalAttr = false;
		int sizeAttr = 0;

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
										System.out.println(key);
										frontIndex = endIndex;
									}
									break;
								case '(':
								case ')':								
								case ';':
								case '*':
									if(charList[i] == '('){
										evalAttr = true;
									}
									if(charList[i] == ')'){
										if(sizeAttr == ATTRIBUTE_SYNTAX){
											sizeAttr = 0;
										}else{
											System.out.println("ERROR - num attributes given: " + sizeAttr);
											System.exit(0);
										}
										evalAttr = false;
									}
									key = Character.toString(charList[i]);
									System.out.println(key);
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
										System.out.println(key);
										frontIndex++;
									}
									break;
								case '=':
									if(isEqualOr){
										endIndex = i+1;
										key = currLine.substring(frontIndex, endIndex);
										System.out.println(key);
										frontIndex = endIndex;
									}else{
										key = Character.toString(charList[i]);
										System.out.println(key);
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
									System.out.println(key);
									frontIndex = endIndex;
								}
							}
						}
						if(!key.isEmpty() && evalAttr && charList[i] != '('){
							sizeAttr++;
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
