class Node
{
	private String word;		//a single word
	private String hint;

	Node()
	{
		word = null;
		hint = null;
	}
		//overloaded constructor
	Node (String wordIN)
	{
		word = wordIN;
		hint = null;
	}
	Node (String wordIN, String hintIN)
	{
		word = wordIN;
		hint = hintIN;
	}

	/*
	* Getters and Setters
	*/
	public void setWord(String wordIN)
	{
		word = wordIN;
	}
	public String getWord()
	{
		return word;
	}
	public String getHint()
	{
		return hint;
	}
	public void setString(String hintIN)
	{
		hint = hintIN;
	}
}