package comp3506.assn2.adts;

import comp3506.assn2.utils.Pair;

public class TrieLeaf {
	private Pair<Integer, Integer>[] occurences; //where the word starts
	private int occurenceSize;
	private int pointer;
	private int[] occurenceIndex;
	private int indexPointer;
	private int indexSize;
	
	@SuppressWarnings("unchecked")
	public TrieLeaf() {
		this.occurenceSize = 20;
		this.pointer = 0;
		this.occurences = (Pair<Integer, Integer>[]) new Pair[this.occurenceSize];
		this.indexSize = 10;
		this.indexPointer = 0;
		this.occurenceIndex = new int[this.indexSize];
	}
	
	/**
	 * 
	 * @param line
	 * @param col
	 */
	public void insertOccurence(Integer line, Integer col, int index) {
		if (this.pointer == this.occurenceSize/2) { //2
			this.resizeOccurences(); //1
		}
		if (this.indexPointer == this.indexSize/2) {
			this.resizeIndex();
		}
		this.occurenceIndex[this.indexPointer] = index;
		this.indexPointer++;
		this.occurences[this.pointer] = new Pair<Integer, Integer>(line, col);//3
		this.pointer++;
	}
	
	/**
	 * 
	 * @return
	 */
	public int appearences() {
		return this.pointer; //1
	}
	
	public int[] indexes() {
		return this.occurenceIndex;
	}
	public Pair<Integer, Integer>[] pairs() {
		return this.occurences;
	}
	
	
	private void resizeIndex() {
		this.indexSize = this.indexSize*2;
		int[] temp = new int[this.indexSize];
		for (int i = 0; i < this.occurenceIndex.length; i++) {
			temp[i] = this.occurenceIndex[i];
		}
		this.occurenceIndex = temp;
	}
	
	private void resizeOccurences() {
		this.occurenceSize = this.occurenceSize*2; //2
		@SuppressWarnings("unchecked")
		Pair<Integer, Integer>[] temp = (Pair<Integer, Integer>[]) new Pair[this.occurenceSize]; //3
		
		for (int i = 0; i < this.occurenceSize/2; i++) { // i
			temp[i] = this.occurences[i]; //3
		}
		this.occurences = temp; // 1
		
	}

	
}
