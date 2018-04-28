/*
 * Name:			Hangman
 * Description:		...
 * Authors: 		...
 * 
 */

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
		String word; // Word/sentence user has to guess
		String hint; // Hint to make it easier for user to guess
		boolean correct = false; // Play the game until user guesses the word
		char guess; // User's guess
		int spaces; // Number of ' ' in the word user has to guess. Need this in checkGameWon() function
		String[] hangedMan = new String[7]; // Advanced graphics for background
		
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
		
		// Find the node
		word = bookTitles.getNode().getWord(); // Word that user has to guess
		hint = bookTitles.getNode().getHint(); // A hint to make guessing easier
		//System.out.println(word + " " + hint);
		
		// Get spaces
		char[] guessArray = new char[word.length()]; // Array size is the length of the word user has to guess
		int underscores = word.length(); // A number of how many '_' have to be printed
		spaces = getWord(word, guessArray); // Put '_' and ' ' into char array, return the number of spaces 
		
		int mistakes = 0; // Number of mistakes made by user
		
		// Fill the array with advanced graphics. Can't explain, this is too complicated
		hangedMan[0] = "    O\n";
		hangedMan[1] = "   /";
		hangedMan[2] = "|";
		hangedMan[3] = "\\ \n";
		hangedMan[4] = "    |\n";
		hangedMan[5] = "   /";
		hangedMan[6] = " \\ \n";
		
		hangAMan(hangedMan, mistakes); // Display 'graphics'
		// Run the loop until user makes 7 mistakes or guesses the word/sentence
		while(mistakes != 7 || correct == false)
		{
			printGuess(guessArray, underscores); // Show the progress (underscores and guessed letters)
			System.out.print("Input your guess: ");
			guess = input.next().toLowerCase().charAt(0);
			mistakes = checkGuess(guess, word, guessArray, mistakes); // Puts correct letters, increments if its a mistakes
			hangAMan(hangedMan, mistakes); // Display 'graphics'
			correct = checkGameWon(guessArray, mistakes, spaces, underscores); // Check if user won or lost the game
			
		}
		//System.out.println("Game ended");
	}
	
	/**
	 * checkGameWon()
	 * @param char[] guessArray		Array of correct letter and underscores
	 * @param int mistakes			Number of mistakes
	 * @param int spaces			Number of spaces in the word/sentence 
	 * @param int underscores		Length of the word/sentence
	 * @return boolean correct		Return if the game has to end or not
	 * Description					Checks if the game has to end or not
	 */
	public static boolean checkGameWon(char[] guessArray, int mistakes, int spaces, int underscores)
	{
		boolean correct = false; // End the game
		int temp = spaces; // Start with number of spaces
		if(mistakes == 7)
		{
			System.out.println("You made 7 mistakes and lost the game.");
			correct = true;
		}
		else
		{
			// Count how many letters are guessed correctly
			for(int i = 0; i < underscores; i++)
			{
				if(guessArray[i] != '_' && guessArray[i] != ' ')
				{
					temp++;
				}
			}
		}
		//System.out.println("Temp: " + temp + " | length: " + guessArray.length);
		
		// If all the letters are guessed correctly, end the game
		if(temp == guessArray.length)
		{
			correct = true;
			System.out.println("You guessed the word right!");
		}

		return correct;
	}
	
	/**
	 * checkGuess()
	 * @param char guess			User's guess
	 * @param char[] guessArray		Array of letters and underscores
	 * @param String word			Word/sentence that has to be guessed
	 * @param int mistakes			Number of mistakes
	 * @return int mistakes			Incremented number of mistakes
	 * Description					Checks if the guess was correct or not
	 */
	public static int checkGuess(char guess, String word, char[] guessArray, int mistakes)
	{
		int isChar = -1; // If the letter user guessed exists in the word/sentence
		isChar = word.indexOf(guess); // Find user's guessed letter
		// If the guess was correct
		if(isChar != -1)
		{
			for(int i = 0; i < word.length(); i++)
			{
				if(word.charAt(i) == guess)
				{
					guessArray[i] = guess; // Put the letter(s) in its correct spot
				}
			}			
		}
		else
		{
			mistakes++;
		}
		return mistakes;
	}
	
	/**
	 * hangAMan()
	 * @param String hangedMan		String array that contains 'graphics'/background
	 * @param int mistakes			Number of mistakes
	 * Description					Print the background/'graphics' + the progress of the game
	 */
	public static void hangAMan(String[] hangedMan, int mistakes)
	{
		// Always print
		System.out.println("    _____");
		System.out.println("    |");
		System.out.println("    |");
		// Print parts of stickman according to mistakes
		for(int i = 0; i < mistakes; i++)
		{
			System.out.print(hangedMan[i]);
		}
		System.out.println();
	}
	
	/**
	 * printGuess()
	 * @param char[] guessArray		Array of correct letters and underscores
	 * @param underscores			Length of the word/sentence
	 * Description					Print underscores and correctly guessed letters
	 */
	public static void printGuess(char[] guessArray, int underscores)
	{
		for(int i = 0; i < underscores; i++)
		{
			System.out.print(guessArray[i] + " ");
		}
		System.out.println();
	}
	
	/**
	 * getWord()
	 * @param String word			Word that user has to guess
	 * @param char[] guessArray		Array of correct letters and underscores
	 * @return int spaces			Number of spaces in the word/sentence
	 * Description					Fill guessArray with _ instead of letters and count spaces
	 */
	public static int getWord(String word, char[] guessArray)
	{
		int spaces = 0;
		for(int i = 0; i < word.length(); i++)
		{
			if(word.charAt(i) != (' '))
			{
				guessArray[i] = '_';
			}
			else
			{
				guessArray[i] = ' ';
				spaces++;
			}
		}
		return spaces;
	}

}
