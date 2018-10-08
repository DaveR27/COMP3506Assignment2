package comp3506.assn2.adts;



public class TrieNode {
	private TrieNode[] children;
	private final int ALPHABET_SIZE = 27; //a-z, 0-9 && '
	private TrieLeaf infoNode;
	private boolean endOfWord;
	
	public TrieNode() {
		this.children = new TrieNode[this.ALPHABET_SIZE];
		this.endOfWord = false;
		
		for (int i = 0; i < this.ALPHABET_SIZE; i++) {
			this.children[i] = null;
		}
	}
	
	public void endOfWord(Integer line, Integer col) {
		this.endOfWord = true;
		if (this.infoNode == null) {
			this.infoNode = new TrieLeaf();
		}
		this.infoNode.insertOccurence(line, col);
	}
	
	public boolean isEndOfWord() {
		return this.endOfWord;
	}
	
	public void addElement(int index) {
		this.children[index] = new TrieNode();
	}
	
	public TrieNode getElement(int index) {
		return this.children[index];
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
