package comp3506.assn2.adts;

import java.util.HashMap;

public class TrieNode {
	private String storedElement;
	private HashMap<Character, TrieNode> children;
	private TrieLeaf infoNode;
	private boolean endOfWord;
	
	public TrieNode(String element) {
		this.storedElement = element;
		this.children = new HashMap<Character, TrieNode>();
		this.endOfWord = false;
	}
	
	public void endOfWord(int index) {
		this.endOfWord = true;
		if (this.infoNode == null) {
			this.infoNode = new TrieLeaf();
		}
		this.infoNode.insertOccurence(index);
	}
	
	public boolean isEndOfWord() {
		return this.endOfWord;
	}
	
	public String getElement() {
		return this.storedElement;
	}
	
	public HashMap<Character, TrieNode> getChildren() {
		return this.children;
	}
	
	public TrieLeaf returnInfoLeaf() {
		if (this.infoNode == null) {
			return null;
		} else {
			return this.infoNode;
		}
	}
	
	
}
