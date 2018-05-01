import java.io.BufferedWriter;
import java.io.IOException;

class BinaryTree
{
	private Node node;         //contains the data of each leaf
	private BinaryTree left;   //left side of the binary tree
	private BinaryTree right;  //right side of the binary tree
	private static int size = 0;  		   //number of nodes in the tree
	private static int randNum;
	private static int count = 0;
	BinaryTree()
	{
		node = null;
		left = null;
		right = null;
	}
		//overloaded constructor
	BinaryTree(Node root)
	{
		node = root;
		left = null;
		right = null;
		size++;
	}
	/*
	* Getters and Setters
	*/
	public int getSize()
	{
		return size;
	}
	public void setCount(int num)
	{
		count = num;
	}
	public void setNode(Node dataIN)
	{
		node = dataIN;
	}
	public Node getNode()
	{
		return node;
	}
	public BinaryTree getLeft()
	{
		return left;
	}



	/***********************Methods*****************************/
	/*
	* Sets the random number
	*/
	public void changeRandNum()
	{
		randNum = (int)(Math. random() * size + 1);
		System.out.println("Size: " + size);
	}
	/*
	* Description: will add the inputed string into a binary seach tree.
	*  			   The strings will be sorted alphabetically
	* Parameters: a string of a single word (could be multiple words if desired)
	* Returns: none
	*/
	public void addNode(String word, String hint)
	{
			//need to check if node is null, if so add word
		if (node == null)
		{
			Node nodeAdd = new Node(word, hint);  //create new Node
			node = nodeAdd;
			size++;
		}
		else
		{
				//compare the inputted string to the string already in the tree
			int resultCompareStrings = word.compareTo(node.getWord());
			if (resultCompareStrings < 0)
			{
					//less than goes left
				if (left == null)
				{
					left = new BinaryTree();  //create a new tree to branch off current node to left
				}
				left.addNode(word, hint);    //add the string recursively to the tree
			}
			else if (resultCompareStrings > 0)
			{
					//greater than goes to right
				if (right == null)
				{
					right = new BinaryTree();  //create a new tree to branch off current node to right
				}
				right.addNode(word, hint);   //add the string recursively to the tree
			}
			else if (word.equalsIgnoreCase(node.getWord())) //string is already in the tree
			{
					//increase counter of word
				//node.setWordInstances(node.getWordInstances()+1);
				//do nothing
			}
		}
	}

	/*
	* Description: deletes a given node from the tree
	* Parameters: value of node to be deleted
	* Return: deleted node
	*/
	public void deleteNode(String deleteValue)
	{
		//search the tree until you find the node you want to delete
		if (node != null && !(node.getWord().equalsIgnoreCase(deleteValue)))
		{
			if (left != null)
			{
				left.deleteNode(deleteValue);
			}
			if (right != null)
			{
				right.deleteNode(deleteValue);
			}
		}
		else if (node == null)
		{
			System.out.println("Word is not in tree");
			return;
		}
		else if (node.getWord().equalsIgnoreCase(deleteValue))
		{
			if (right == null && left != null)  //if one child is left
			{
				size--;
				node = left.node;
				if (left.right != null)
				{
					right = left.right;
				}
				else
				{
					right = null;
				}
				if (left.left != null)
				{
					left = left.left;
				}
				else
				{
					left = null;
				}
			} 
			else if (left == null && right != null)  //if one child is right
			{
				size--;
				node = right.node;
				
				if (right.left != null)
				{
					left = right.left;
				}
				else
				{
					left = null;
				}
				if (right.right != null)
				{
					right = right.right;
				}
				else
				{
					right = null;
				}
			}
			else if (right != null && left != null && right.node != null)   //if two children
			{
				node = right.findMin();
				right.deleteNode(node.getWord());
			}
			else if (right == null && left == null)
			{
				node = null;
				size--;
			}
		}
		else
		{
			System.out.println("Word is not in tree");
		}
		if (right != null && right.node == null)
		{
			right = null;
		}
		if (left != null && left.node == null)
		{
			left = null;
		}
	}
	/*
	* Description:
	* Parameters: none
	* Returns: the node with the smallest value
	*/
	public Node findMin()
	{
		Node tempNode = node;
		if (node != null)
		{
			if (left==null && right==null)
			{
				tempNode = node;
			}
			else if (left != null && left.node != null)
			{
				tempNode = left.findMin();
			}
		}
		return tempNode;
	}
	// public Node randomNode(Node root)
	// {
	// 	int randNum;
	// 	//generate a random number between 0 and the size of the tree
	// 	randNum = (int)(Math. random() * size + 1);
	// 	return randNodeUtil(root, count);	
	// }
	public Node randNodeUtil()
	{

		Node tempNode = null;
		int leftOrRight = (int)(Math.round((Math. random())));
		while (count < randNum)
		{
			if (node != null)
			{
				if (leftOrRight == 0)
				{
					if (left != null && left.node != null)
					{
						tempNode = left.randNodeUtil();
						count++;
					}
					else
					{
						tempNode = node;
						count = randNum;
					}
				}
				else if (leftOrRight == 1)
				{
					if (right != null && right.node != null)
					{
						tempNode = right.randNodeUtil();
						count++;
					}
					else
					{
						tempNode = node;
						count = randNum;
					}
				}
			}
			else
			{
				count = randNum;
			}
		}
		
		return tempNode;
	}
	/*
	* Description: prints the strings within the binary tree in alphabetical order
	* Parameters: none
	* Returns: none
	*/
	public void printInOrder()
	{
		if (node != null)
		{
			if (left != null)
			{
				left.printInOrder();
			}
			System.out.println(node.getWord() + "        " + node.getHint());
			if (right != null)
			{
				right.printInOrder();
			}
		}
	}
	/*
	* Description: prints the strings within the binary tree in preorder.
	* Parameters: none
	* Returns: none
	*/
	public void printPreOrder()
	{
		if (node != null)
		{
			System.out.println(node.getWord() + "           " + node.getHint());
			if (left != null)
			{
				left.printPreOrder();
			}
			if (right != null)
			{
				right.printPreOrder();
			}
		}
	}
	/*
	* Description: prints the strings within the binary tree in postorder.
	* Parameters: none
	* Returns: none
	*/
	public void printPostOrder()
	{
		if (node != null)
		{
			if (left != null)
			{
				left.printPostOrder();
			}
			if (right != null)
			{
				right.printPostOrder();
			}
			System.out.println(node.getWord() + "             " + node.getHint());
		}
	}
}
