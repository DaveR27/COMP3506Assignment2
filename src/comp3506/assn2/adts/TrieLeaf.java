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
	
	/**
	 * 
	 * @param line
	 * @param col
	 */
	public void insertOccurence(Integer line, Integer col) {
		if (this.occurences.length == this.occurenceSize/2) { //2
			this.resizeOccurences(); //1
		}
		this.occurences[this.pointer] = new Pair<Integer, Integer>(line, col);//3
	}
	
	/**
	 * 
	 * @return
	 */
	public int appearences() {
		return this.occurences.length; //1
	}
	
	
	@SuppressWarnings("unchecked")
	private void resizeOccurences() {
		this.occurenceSize = this.occurenceSize*2; //2
		Pair<Integer, Integer>[] temp = (Pair<Integer, Integer>[]) new Pair[this.occurenceSize]; //3
		
		for (int i = 0; i < this.occurenceSize/2; i++) { // i
			temp[i] = this.occurences[i]; //3
		}
		this.occurences = temp; // 1
	}
	
}
