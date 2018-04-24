import java.io.BufferedWriter;
import java.io.IOException;

class BinaryTree
{
	private Node node;         //contains the data of each leaf
	private BinaryTree left;   //left side of the binary tree
	private BinaryTree right;  //right side of the binary tree

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
	}
	/*
	* Getters and Setters
	*/
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
		//search the tree until you find the node you want to delete
		//see if node has any child
		//if no children then just set the parent link to null
		if (node.getWord().equalsIgnoreCase(deleteValue))
		{
			if (left == null && right == null)
			{
				node=null;
			}
			else if (right == null && left != null)  //if one child is left
			{
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
			else if (right != null && left != null)   //if two children
			{
				node = right.findMin();
				right.deleteNode(node.getWord());
			}
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
		if (left==null && right==null)
		{
			tempNode = node;
		}
		else if (left != null)
		{
			tempNode = left.findMin();
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
