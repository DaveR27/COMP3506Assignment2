package comp3506.assn2.adts;

import comp3506.assn2.utils.Pair;

/**
 * 
 * @author David Riddell
 *
 */
public class TrieLeaf {
	private Pair<Integer, Integer>[] occurrences; //where the word starts
	private int occurrenceSize;
	private int pointer;
	private int[] occurrenceIndex;
	private int indexPointer;
	private int indexSize;
	private Pair<Integer, Integer>[] lineIndex;
	private int lineIndexPointer;
	private int lineIndexSize;
	
	@SuppressWarnings("unchecked")
	public TrieLeaf() {
		this.occurrenceSize = 20;
		this.pointer = 0;
		this.occurrences = (Pair<Integer, Integer>[]) new Pair[this.occurrenceSize];
		this.indexSize = 10;
		this.indexPointer = 0;
		this.occurrenceIndex = new int[this.indexSize];
		this.lineIndexSize = 10;
		this.indexPointer = 0;
		this.lineIndex = (Pair<Integer, Integer>[]) new Pair[lineIndexSize];
	}
	
	/**
	 * 
	 * @param line
	 * @param col
	 */
	public void insertOccurrence(Integer line, Integer col, int index,
								 Pair<Integer, Integer> lineIndex) {
		if (this.pointer == this.occurrenceSize /2) { //2
			this.resizeOccurrences(); //O(n)
		}
		if (this.indexPointer == this.indexSize/2) {
			this.resizeIndex(); //O(n)
		}
		if (this.lineIndexPointer == this.lineIndexSize/2) {
			this.resizeLineIndex(); //O(n)
		}
		this.lineIndex[this.lineIndexPointer] = lineIndex; //2
		this.lineIndexPointer++; //2
		this.occurrenceIndex[this.indexPointer] = index; //2
		this.indexPointer++; //2
		this.occurrences[this.pointer] = new Pair<Integer, Integer>(line, col);//3
		this.pointer++;//2
	}
	
	/**
	 * 
	 * @return
	 */
	public int appearances() {
		return this.pointer; //1
	}
	
	public int[] indexes() {
		return this.occurrenceIndex;
	}
	public Pair<Integer, Integer>[] pairs() {
		return this.occurrences;
	}

	public Pair<Integer, Integer>[] lineIndex(){
		return this.lineIndex;
	}

	/**
	 * 
	 */
	private void resizeLineIndex() {
		this.lineIndexSize = this.lineIndexSize*2;
		@SuppressWarnings("unchecked")
		Pair<Integer, Integer>[] temp = (Pair<Integer, Integer>[]) new Pair[this.lineIndexSize];
		for (int i = 0; i < this.lineIndex.length; i++) {
			temp[i] = this.lineIndex[i];
		}
		this.lineIndex = temp;
	}
	
	/**
	 * 
	 */
	private void resizeIndex() {
		this.indexSize = this.indexSize*2;
		int[] temp = new int[this.indexSize];
		for (int i = 0; i < this.occurrenceIndex.length; i++) {
			temp[i] = this.occurrenceIndex[i];
		}
		this.occurrenceIndex = temp;
	}
	
	/**
	 * 
	 */
	private void resizeOccurrences() {
		this.occurrenceSize = this.occurrenceSize *2; //2
		@SuppressWarnings("unchecked")
		Pair<Integer, Integer>[] temp = (Pair<Integer, Integer>[]) new Pair[this.occurrenceSize]; //3
		
		for (int i = 0; i < this.occurrenceSize /2; i++) { // i
			temp[i] = this.occurrences[i]; //3
		}
		this.occurrences = temp; // 1
		
	}

	
}
