/*
Name: Martin Smith, Eric Anderson
CSCI 330 - Spring 2019
File Name : Main.java
*/
import java.io.*;
public class Main{

	public static void main(String[] args) throws IOException {
		LexicalAnalyzer surly = new LexicalAnalyzer();
		surly.run(args[0]);
	}
}
