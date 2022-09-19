package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

// A model for the game of 20 questions. This type can be used to
// build a console based game of 20 questions or a GUI based game.
//
// @author Rick Mercer and Ethan Zorbas
//
public class GameTree {

	// BinaryTreeNode inner class used to create new nodes in the GameTree.
	private class TreeNode {

		// Instance variables
		private String data;
		private TreeNode left;
		private TreeNode right;

		TreeNode(String theData) {
			data = theData;
			left = null;
			right = null;
		}

		// This 2nd constructor is needed in a few methods, like privste build()
		TreeNode(String theData, TreeNode leftLink, TreeNode rightLink) {
			data = theData;
			left = leftLink;
			right = rightLink;
		}
	}

	// Instance variables
	private TreeNode root;
	private TreeNode currentNode;
	private Scanner scanner;
	private String fileName;

	// Constructor needed to create the game. It should open the input
	// file and call the recursive method build(). The String parameter
	// name is the name of the file from which we need to read the game
	// questions and answers from.
	//
	public GameTree(String name) {
		// TODO: Complete this constructor. Remember, this needs a try/catch to
		// open the file and a call to the build method code demoped in class.
		try {
			scanner = new Scanner(new File(name));
		} catch (FileNotFoundException e) {
		}
		fileName = name;
		root = build();
		currentNode = root;
		scanner.close();
	}

	private TreeNode build() {
		if (!scanner.hasNextLine())
			return null;

		// There must be at least one more char in the scanner
		String token = scanner.nextLine().trim();

		if (token.charAt(token.length() - 1) != '?')
			return new TreeNode(token);
		else {
			TreeNode leftSubtree = build();
			TreeNode rightSubtree = build();
			return new TreeNode(token, leftSubtree, rightSubtree);
		}
	}

// Add a new question and answer to the currentNode. If the current 
	// node is referencing the answer "parrot",
	// theGame.add("Does it swim?", "duck");
	// should change the GameTree like this:
	//
	// ......Feathers?......................Feathers?
	// ....../......\......................./......\
	// ..parrot....horse.........Does it swim?.....horse
	// ............................./......\
	// ..........................duck.....parrot
	//
	// @param newQuestion: The question to add where the old answer was.
	// @param newAnswer: The new yes answer to the new question.
	public void add(String newQuestion, String newAnswer) {
		String temp = this.currentNode.data;
		this.currentNode.data = newQuestion;
		this.currentNode.left = new TreeNode(newAnswer);
		this.currentNode.right = new TreeNode(temp);
	}

	// Return true if getCurrent() is an answer rather than a question. Return false
	// if the current node is an internal node rather than a leaf that is an answer.
	public boolean foundAnswer() {
		String curData = this.currentNode.data;
		if (curData.charAt(curData.length() - 1) != '?')
			return true;
		return false;
	}

	// Return the data for the current node, which could be a
	// question or an answer.
	public String getCurrent() {
		return currentNode.data;
	}

	// Ask the game to update the current node in the tree by
	// going left for Choice.yes or right for Choice.no
	// Example code:
	// theGame.playerSelected(Choice.Yes);
	//
	public void playerSelected(Choice yesOrNo) {
		if (yesOrNo.equals(Choice.YES)) {
			this.currentNode = this.currentNode.left;
		} else if (yesOrNo.equals(Choice.NO)) {
			this.currentNode = this.currentNode.right;
		}
	}

	// Begin a game at the root of the tree. getCurrent should return the question
	// at the root of this GameTree.
	public void reStart() {
		this.currentNode = this.root;
	}

	// Overwrite the old file for this gameTree with the current state that may have
	// new questions added since the game started. Get all other method working
	// first
	// Build this method last.
	public void saveGame() {
		// TODO: Complete this method
		// Hint: Call a private helper method with a root argument to do
		// a preorder traversal over the current state of this GameTree
		FileWriter charToBytesWriter = null;
		try {
			charToBytesWriter = new FileWriter(fileName);
		} catch (IOException e) {
		}
		PrintWriter diskFile = new PrintWriter(charToBytesWriter);
		String[] toWrite = preOrder(root).split(" ");
		for (String str : toWrite) {
			System.out.println(str);
			diskFile.println(str);
		}
		diskFile.close();
	}

	private String preOrder(TreeNode node) {
		if (node == null)
			return "";
		return node.data + " " + preOrder(node.left) + preOrder(node.right);
	}

	// Method used to print out a text version of the game file.
	@Override
	public String toString() {
		accumulate = "";
		toString(root, 0);
		return accumulate;
	}

	// Used in both toString methods to add strings like "- - - "
	private String accumulate;

	private void toString(TreeNode node, int lvl) {
		if (node != null) {
			toString(node.right, lvl + 1);
			for (int i = 1; i <= lvl; i++) {
				accumulate += "-  ";
			}
			accumulate = accumulate + node.data + " \n";
			toString(node.left, lvl + 1);
		}

	}
}
