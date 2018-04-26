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
	public static Scanner input;
	
	public static void main(String[] args) throws IOException
	{
		input = new Scanner(System.in);
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
		
		//bookTitles.deleteNode("zorba the greek");
		//bookTitles.printInOrder();
		//System.out.println("\nThere are " + size + " book titles");
		
		// Find the node
		String word = bookTitles.getNode().getWord();
		String hint = bookTitles.getNode().getHint();
		System.out.println(word + " " + hint);
		
		// Get spaces
		char[] guessArray = new char[word.length()];
		int underscores = word.length();
		
		getWord(word, guessArray, underscores); // Put '_' and ' ' into char array 
		//printGuess(guessArray, underscores); // Print guessArray[]
		
		int mistakes = 0;
		
		String[] hangedMan = new String[7];
		hangedMan[0] = "    O\n";
		hangedMan[1] = "   /";
		hangedMan[2] = "|";
		hangedMan[3] = "\\ \n";
		hangedMan[4] = "    |\n";
		hangedMan[5] = "   /";
		hangedMan[6] = " \\ \n";
		
		boolean correct = false;
		char guess;
		
		while(correct != true && mistakes != 7)
		{
			hangAMan(hangedMan, mistakes);
			printGuess(guessArray, underscores);
			System.out.print("Input your guess: ");
			guess = input.next().toLowerCase().charAt(0);
			mistakes = checkGuess(guess, underscores, word, guessArray, mistakes);
			correct = checkGameWon(correct, underscores, guessArray, mistakes, word);
		}
	}
	
	public static boolean checkGameWon(boolean correct, int underscores, char[] guessArray, int mistakes, String word)
	{
		int temp = 0;
		if(mistakes == 7)
		{
			System.out.println("You made 7 mistakes and lost the game.");
		}
		else
		{
			for(int i = 0; i < underscores; i++)
			{
				if(guessArray[i] != '_' && guessArray[i] != ' ')
				{
					temp++;
				}
			}
		}
		if(temp == guessArray.length)
		{
			correct = false;
			System.out.println("You guessed the word right!");

		}

		return correct;
	}
	
	public static int checkGuess(char guess, int underscores, String word, char[] guessArray, int mistakes)
	{
		int isChar = -1;
		isChar = word.indexOf(guess);
		if(isChar != -1)
		{
			for(int i = 0; i < underscores; i++)
			{
				if(word.charAt(i) == guess)
				{
					guessArray[i] = guess;
				}
			}			
		}
		else
		{
			mistakes++;
		}
		return mistakes;
	}
	
	public static void hangAMan(String[] hangedMan, int mistakes)
	{
		// Always print
		System.out.println("    _____");
		System.out.println("    |");
		System.out.println("    |");
		// Print according to mistakes
		for(int i = 0; i < mistakes; i++)
		{
			System.out.print(hangedMan[i]);
		}
		System.out.println();
	}
	
	public static void printGuess(char[] guessArray, int underscores)
	{
		for(int i = 0; i < underscores; i++)
		{
			System.out.print(guessArray[i] + " ");
		}
		System.out.println();
	}
	
	public static void getWord(String word, char[] guessArray, int underscores)
	{
		for(int i = 0; i < underscores; i++)
		{
			if(word.charAt(i) != (' '))
			{
				guessArray[i] = '_';
			}
			else
			{
				guessArray[i] = ' ';
			}
		}
	}

}
