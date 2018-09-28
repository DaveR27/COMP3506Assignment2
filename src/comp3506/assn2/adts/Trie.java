package comp3506.assn2.adts;

import java.util.HashMap;

public class Trie {
	
	private TrieNode root;
	
	public Trie() {
		root = new TrieNode(null);
	}
	
	public void addWord(String word, Integer line, Integer col) {
		TrieNode movingNode = root;
		
		for(int i = 0; i < word.length(); i++) {
			HashMap<Character, TrieNode> child = movingNode.getChildren();
			char stringChar = word.charAt(i);
			
			if(child.containsKey(stringChar)) {
				movingNode = child.get(stringChar);
			} else {
				TrieNode temp = new TrieNode(String.valueOf(stringChar));
				child.put(stringChar, temp);
				movingNode = temp;
			}
		}
		movingNode.endOfWord(line, col);
	}
	
	public HashMap<Character,TrieNode> getPrefixOccurence(String word) {
		int stringLength = word.length();
		TrieNode movingNode = root;
		
		for(int i = 0; i < stringLength; i++) {
			char character = word.charAt(i);
			
			HashMap<Character,TrieNode> child = movingNode.getChildren();
			if (child.containsKey(character)) {
				movingNode = child.get(character);
			} else {
				return child;
			}
		}
		return null;
	}
	
	public int getWordAmount(String word) {
		TrieNode movingNode = root;
		
		for(int i = 0; i < word.length(); i++) {
			HashMap<Character, TrieNode> child = movingNode.getChildren();
			char stringChar = word.charAt(i);
			if (child.containsKey(stringChar)) {
				movingNode = child.get(stringChar);
			} else {
				return -1;
			}
		}
		return movingNode.returnInfoLeaf().appearences();
	}
}
