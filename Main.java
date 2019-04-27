/*
Name: Martin Smith
CSCI 330 - Spring 2019
File Name : Main.java
*/

public class Main{	
	public static void main(String[] args){
		LexicalAnalyzer surly = new LexicalAnalyzer();
		String filename = "";
		try{
			filename = args[0];
		}catch (Exception e){
			System.out.println("ERROR - no file declared");
			System.exit(0);
		} 
		surly.run(filename);
	
	}
}
