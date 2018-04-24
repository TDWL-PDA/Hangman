import java.util.Scanner;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.regex.*;

public class DriverCSDB {
	
	public static java.io.File inFile;  //input file
	public static Scanner inputFile;    //used to read lines of input file
	public static BufferedWriter beforeFile;	// out file for tree before deleting words.
	
	public static void main(String[] args) throws IOException
	{
		//Open File
		inFile = new java.io.File("TitlesandAuthors.csv");  //input file
		//inFile = new java.io.File("bookTitles.txt");
		if (!inFile.exists())
		{
			System.out.println("File not found");
			System.exit(-1);
		}
		BinaryTree bookTitles = new BinaryTree();
		//reads the entire text file and places it into one string
		String entireFileText = new Scanner(inFile).useDelimiter("\\Z").next();
	 	//changes all letters to lower case and replaces all characters that are not
		//a-z, A-Z, or the apostrophe with a space.
		//The string is then split by spaces and placed into an array of words
		//String[] words = entireFileText.replaceAll("[^a-zA-Z' ]", " ").toLowerCase().split("\\s+");
		String[] words = entireFileText.toLowerCase().split("\\n+");
		for (int i = 0; i<words.length;i++)
		{
			words[i] = words[i].replaceAll("\n","").replaceAll("\r", "");
		}
		Pattern pattern = Pattern.compile("[a-zA-Z0-9'’,. -]*");
		Matcher matcher;
		int size = 0;
		String[] splits = new String[2];
		for (int i = 0; i < words.length; i++)
		{
			splits = words[i].split(",");
			//System.out.println(splits[0] + " " + splits[1]);
			matcher = pattern.matcher(splits[0]);
			if (matcher.matches())
			{
				//put in tree
				bookTitles.addNode(splits[0], splits[1]);
				size++;
		    }
			else
			{
				//System.out.println(words[i]);
			}
		}
		
		bookTitles.deleteNode("zorba the greek");
		bookTitles.printInOrder();
		System.out.println("\nThere are " + size + " book titles");
	  
	}

}
