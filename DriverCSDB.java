/*fjida;
 * Name:			Hangman
 * Description:		...
 * Authors: 		...
 * 
 */

import java.util.Random;
import java.util.Scanner;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.regex.*;
import java.util.InputMismatchException;

public class DriverCSDB {
	
	public static java.io.File inFile;  //input file
	public static Scanner inputFile;    //used to read lines of input file
	public static Scanner input;
	
	public static void main(String[] args) throws IOException
	{
		String word; // Word/sentence user has to guess
		String hint; // Hint to make it easier for user to guess
		
		boolean correct = false; // Play the game until user guesses the word
		String stringGuess; // User's guess
		char guess = ' '; // First char of the guess
		char[] guesses = new char[26]; // Letters already guessed
		int guessesInt = 0;
		
		int spaces; // Number of ' ' in the word user has to guess. Need this in checkGameWon() function
		
		String[] hangedMan = new String[7]; // Advanced graphics for background
		
		input = new Scanner(System.in);
		//Ask user for level
		String level = "";
		boolean invalidLevel = false;
		do
		{
			System.out.println("What level do you want? (easy or hard)");
			level = input.next();
			if (!level.equalsIgnoreCase("easy") && !level.equalsIgnoreCase("hard"))
			{
				System.out.println("Invalid Level. Try again.");
				invalidLevel = true;
			}
		}while (invalidLevel);

		BinaryTree bookTitles = new BinaryTree();
		generateTree(level, bookTitles);
		int mistakes = 0; // Number of mistakes made by user
		
		// Fill the array with advanced graphics. Can't explain, this is too complicated
		hangedMan[0] = "    O\n";
		hangedMan[1] = "   /";
		hangedMan[2] = "|";
		hangedMan[3] = "\\ \n";
		hangedMan[4] = "    |\n";
		hangedMan[5] = "   /";
		hangedMan[6] = " \\ \n";
		
		
		boolean playAgain = false;
		boolean invalidUserInput = false;
		char playAgainChar = 'n';
		Node randNode;
		do
		{
			mistakes = 0;
			correct = false;
			// Find the node
			randNode = findRandomNode(bookTitles);
			try
			{
				word = randNode.getWord();
				hint = randNode.getHint(); // A hint to make guessing easier
				// Get spaces
				char[] guessArray = new char[word.length()]; // Array size is the length of the word user has to guess
				int underscores = word.length(); // A number of how many '_' have to be printed
				spaces = getWord(word, guessArray); // Put '_' and ' ' into char array, return the number of spaces 

				hangAMan(hangedMan, mistakes); // Display 'graphics'
				// Run the loop until user makes 7 mistakes or guesses the word/sentence
				while(mistakes != 7 && correct == false)
				{
					//System.out.println(word);
					System.out.println("Hint: " + hint);
					printGuess(guessArray, underscores); // Show the progress (underscores and guessed letters)
					System.out.println("If you want to quit the game input 0.");
					System.out.println();
					System.out.print("Already guessed: ");
					printGuesses(guesses, guessesInt);
					System.out.print("Input your guess: ");
					do
					{
						stringGuess = input.nextLine().toLowerCase(); // Get user's guess
					} while(stringGuess.length() <= 0);
					guess = stringGuess.charAt(0); // Take the first letter
					// If 0 - quit the game
					if (guess == '0')
					{
						System.out.println("Game Over.");
						System.out.println("The word was: " + word);
						return;
					}
					// If user guessed the word right, quit the game
					if(stringGuess.equalsIgnoreCase(word))
					{
						correct = true;
						System.out.println("You guessed the word right!");
					}
					else
					{
						guesses[guessesInt] = guess;
						guessesInt++;
						mistakes = checkGuess(guess, word, guessArray, mistakes); // Puts correct letters, increments if its a mistakes
						hangAMan(hangedMan, mistakes); // Display 'graphics'
						correct = checkGameWon(guessArray, mistakes, spaces, underscores, word); // Check if user won or lost the game		
					}
				}
				System.out.println("Do you want to kill again?");
				playAgainChar = input.nextLine().toLowerCase().charAt(0);
				if (playAgainChar == 'y')
				{
					playAgain = true;
				}
				else
				{
					playAgain = false;
				}
			}
			catch (NullPointerException e)
			{
				System.out.println("There are no more words in the bank. Start game over.");
				playAgain = false;
			}
		}while (playAgain);
	}
	
	public static void printGuesses(char[] guesses, int guessesInt)
	{
		if(guessesInt >= 0)
		{
			for(int i = 0; i < guessesInt; i++)
			{
				System.out.print(guesses[i] + " ");
			}
		}
		System.out.println();
	}
	
	public static Node findRandomNode(BinaryTree storyTree)
	{
		Node tempNode = null;
		if (storyTree.getSize() > 0)
		{
			storyTree.setCount(0);
			storyTree.changeRandNum();
			tempNode = storyTree.randNodeUtil();
			storyTree.deleteNode(tempNode.getWord());
		}
		return tempNode;
	}
	public static void generateTree(String level, BinaryTree bookTitles) throws IOException
	{
		//Open File
		if (level.equalsIgnoreCase("easy"))
		{
			inFile = new java.io.File("TitlesandAuthors.csv");  //input file
			if (!inFile.exists())
			{
				System.out.println("File not found");
				System.exit(-1);
			}
		}
		else
		{
			inFile = new java.io.File("spellingBee.csv");  //input file
			if (!inFile.exists())
			{
				System.out.println("File not found");
				System.exit(-1);
			}
		}
		try
		{
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
			Pattern pattern = Pattern.compile("[a-zA-Z0-9', -]*");
			Matcher matcher;
			int size = 0;
			String[] splits = new String[2];
			System.out.println(words.length);
			for (int i = 0; i < words.length; i++)
			{
				splits = words[i].split(",");
				matcher = pattern.matcher(splits[0]);
				if (matcher.matches())
				{
					//put in tree
					bookTitles.addNode(splits[0], splits[1]);
			    }

			}
		} 
		catch (IOException e)
		{
			System.out.println("Invalid File.");
		}

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
	public static boolean checkGameWon(char[] guessArray, int mistakes, int spaces, int underscores, String word)
	{
		boolean correct = false; // End the game
		int temp = spaces; // Start with number of spaces
		if(mistakes == 7)
		{
			System.out.println("You killed a man.");
			System.out.println("The word was: " + word);
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
				if(guessArray[i] == guess)
				{
					mistakes++;
					i = word.length();
				}
				else if(word.charAt(i) == guess)
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
			if(word.charAt(i) == ',')
			{
				guessArray[i] = ',';
			}
			else if(word.charAt(i) == '\'')
			{
				guessArray[i] = '\'';
			}
			else if(word.charAt(i) == '-')
			{
				guessArray[i] = '-';
			}
			else if(Character.isLetterOrDigit(word.charAt(i)))
			{
				guessArray[i] = '_';
			}
			else if(word.charAt(i) == (' '))
			{
				guessArray[i] = ' ';
				spaces++;
			}
			else
			{
				guessArray[i] = '\'';
			}
		}
		return spaces;
	}

}
