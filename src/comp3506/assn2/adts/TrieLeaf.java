package comp3506.assn2.adts;

import comp3506.assn2.utils.Pair;

public class TrieLeaf {
	private Pair<Integer, Integer>[] occurences; //where the word starts
	private int occurenceSize;
	private int pointer;
	
	@SuppressWarnings("unchecked")
	public TrieLeaf() {
		this.occurenceSize = 10;
		this.pointer = 0;
		this.occurences = (Pair<Integer, Integer>[]) new Pair[this.occurenceSize];
	}
	
	public void insertOccurence(Integer line, Integer col) {
		if (this.occurences.length == this.occurenceSize/2) {
			this.resizeOccurences();
		}
		this.occurences[this.pointer] = new Pair<Integer, Integer>(line, col);
	}
	
	public int appearences() {
		return this.occurences.length;
	}
	
	@SuppressWarnings("unchecked")
	private void resizeOccurences() {
		this.occurenceSize = this.occurenceSize*2;
		Pair<Integer, Integer>[] temp = (Pair<Integer, Integer>[]) new Pair[this.occurenceSize];
		
		for (int i = 0; i < this.occurenceSize/2; i++) {
			temp[i] = this.occurences[i];
		}
		this.occurences = temp;
	}
	
}
