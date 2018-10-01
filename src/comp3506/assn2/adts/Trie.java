package comp3506.assn2.adts;

import java.util.HashMap;

public class Trie {
	
	private TrieNode root;
	
	public Trie() {
		root = new TrieNode(null);
	}
	
	/**
	 * This method will add a word to a Trie, where each letter will be a node and then the following
	 * letters will be children.
	 * 
	 * Since the method runs 2+(i*11) primitive operations  in the worst case, it can be said that the method has a linear
	 * component. Since the size of the alphabet will also be a depending factor on how many nodes are used within the tree
	 * that linear component will have to be multiplied by another constant d (the size of the alphabet). Therefore:
	 * O(dn)
	 * 
	 * 
	 * @param word Term to add to the Trie.
	 * @param line The line that the word is found on within the document.
	 * @param col the column on which the word starts.
	 */
	public void addWord(String word, Integer line, Integer col) {
		TrieNode movingNode = root; //1
		
		//Climbs down the tree adding the letters to previous nodes or making new ones.
		for(int i = 0; i < word.length(); i++) { // i times where i is the length of the string
			HashMap<Character, TrieNode> child = movingNode.getChildren(); //2
			char stringChar = word.charAt(i); //2
			
			if(child.containsKey(stringChar)) { //2
				movingNode = child.get(stringChar); //2
			} else {
				TrieNode temp = new TrieNode(String.valueOf(stringChar));//3
				child.put(stringChar, temp);//1
				movingNode = temp;//1
			}
		}
		movingNode.endOfWord(line, col);//1 //makes the last letter the end of the word and makes and info node.
	}
	
	/**
	 * Searches down the Trie, following the characters of the input string, when there is no more
	 * characters in the input it will return a hashmap of all the words endings that prefix, if there is
	 * no prefix stored within the Trie then null is returned.
	 * 
	 * Since in the worst case 5 + (i*8) primitive operations occur, it can be said that there is a linear
	 * component to the search, but since the search will also depend on the alphabet that the structure is using
	 * there will also be an another factor that the previous linear factor will be multiplied by. So if d is the 
	 * size of the alphabet using the structure in the worst cse this method runs at:
	 * O(dn)
	 * 
	 * @param word the prefix to be searched within the Trie
	 * @return HashMap of of all the word endings that come after the Prefix. If it isn't a prefix null
	 * 		is returned.
	 */
	public HashMap<Character,TrieNode> getPrefixOccurence(String word) {
		int stringLength = word.length(); //2
		TrieNode movingNode = root; //2
		
		for(int i = 0; i < stringLength; i++) { // i times where i is the length of the string
			char character = word.charAt(i); // 2
			
			HashMap<Character,TrieNode> child = movingNode.getChildren(); //2
			if (child.containsKey(character)) { //2
				movingNode = child.get(character); //2
			} else {
				return child; //1
			}
		}
		return null; //1
	}
	
	/**
	 * Searches a Trie for a matching word if it finds it it will then check the leaf node for for the
	 * amount of times that word appears within the document.
	 * 
	 * This method works in 4+(i*8) primitive operations in the worst case. But since searching through a 
	 * trie also depends on the size of the alphabet that it is holding that constant will have to be multipled
	 * by d (the size of the alphabet that is being used). Therefore this methods worst case run time:
	 * O(dn)
	 * 
	 * @param word the word to be searched for
	 * @return returns the amount of times the word is found within the document, but
	 * 		if the word does not exist in the document then -1 is returned.
	 */
	public int getWordAmount(String word) {
		TrieNode movingNode = root; // 1
		
		for(int i = 0; i < word.length(); i++) { // i times where i is the length of the word
			HashMap<Character, TrieNode> child = movingNode.getChildren(); //2
			char stringChar = word.charAt(i); // 2
			if (child.containsKey(stringChar)) { //2
				movingNode = child.get(stringChar); //2
			} else {
				return -1; //1
			}
		}
		return movingNode.returnInfoLeaf().appearences(); //3
	}
}
