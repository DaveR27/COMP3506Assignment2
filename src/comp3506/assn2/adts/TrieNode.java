package comp3506.assn2.adts;


import comp3506.assn2.utils.Pair;

public class TrieNode {
	private String storedElement;
	private TrieNode[] children;
	private final int ALPHABET_SIZE = 27; //a-z && '
	private TrieLeaf infoNode;
	private boolean endOfWord;
	
	public TrieNode(String letter) {
		this.storedElement = String.valueOf(letter);
		this.children = new TrieNode[this.ALPHABET_SIZE];
		this.endOfWord = false;
		
		for (int i = 0; i < this.ALPHABET_SIZE; i++) {
			this.children[i] = null;
		}
	}
	
	public void endOfWord(Integer line, Integer col, int docIndex,
						  Pair<Integer, Integer> lineIndex) {
		this.endOfWord = true;
		if (this.infoNode == null) {
			this.infoNode = new TrieLeaf();
		}
		this.infoNode.insertOccurrence(line, col, docIndex, lineIndex);
	}
	
	public boolean hasChildren() {
		for (int i = 0; i < this.children.length; i++) {
			if (this.children[i] != null) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isEndOfWord() {
		return this.endOfWord;
	}
	
	public void addElement(int index, String letter) {
		this.children[index] = new TrieNode(letter);
	}
	
	public TrieNode getElement(int index) {
		return this.children[index];
	}
	
	public String getStoredLetter() {
		return this.storedElement;
	}
	
	public TrieNode[] getChildren() {
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
