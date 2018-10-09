package comp3506.assn2.adts;

import comp3506.assn2.utils.Pair;

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
	public void addWord(String word, Integer line, Integer col, int docIndex) {
		TrieNode movingNode = this.root; //1
		
		//Climbs down the tree adding the letters to previous nodes or making new ones.
		for(int i = 0; i < word.length(); i++) { // i times where i is the length of the string
			int index = word.charAt(i) - 'a';
			if (index == -58) {
				index = 26;
			}
			if(movingNode.getChildren()[index] == null) {
				movingNode.addElement(index, String.valueOf(word.charAt(i)));
			}
			movingNode = movingNode.getElement(index);
		}
		movingNode.endOfWord(line, col, docIndex);//1 //makes the last letter the end of the word and makes and info node.
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
		TrieNode movingNode = this.root; // 1
		
		for(int i = 0; i < word.length(); i++) { // i times where i is the length of the word
			int index = word.charAt(i) - 'a';
			if(index == -58) {
				index = 26;
			}
			if(movingNode.getElement(index) != null) {
				movingNode = movingNode.getElement(index);
			} else {
				return -1;
			}
		}
		return movingNode.returnInfoLeaf().appearences();
	}
	
	public int[] findWordIndex(String phrase) {
		TrieNode movingNode = root; // 1
		
		for(int i = 0; i < phrase.length(); i++) {
			int index = phrase.charAt(i) - 'a';
			if(movingNode.getElement(index) == null) {
				return null;
			} else {
				movingNode = movingNode.getElement(index);
			}
		}
		if(movingNode.isEndOfWord()) {
			return movingNode.returnInfoLeaf().indexes();
		}
		return null;
	}
	
	public Pair<Integer, Integer>[] findWordPair(String phrase) {
		TrieNode movingNode = root; // 1
		
		for(int i = 0; i < phrase.length(); i++) {
			int index = phrase.charAt(i) - 'a';
			if(movingNode.getElement(index) == null) {
				return null;
			} else {
				movingNode = movingNode.getElement(index);
			}
		}
		if(movingNode.isEndOfWord()) {
			return movingNode.returnInfoLeaf().pairs();
		}
		return null;
	}
}
	
