package comp3506.assn2.adts;

import comp3506.assn2.utils.Pair;

/**
 * This class is usage as a utility class for WordMatch Trie. Every time
 * there is an end of a word from the text given WordMatch Trie this class
 * will store all the information needed to make fast searches in the
 * Trie possible.
 * 
 * Memory Usage:
 * This class is called for every unique string in the text given
 * to WordMatch Trie, this means it will have n memory usage where
 * n is the amount of strings within the text. Internally though
 * the most memory costly aspect is the occurrence and index arrays,
 * so for memory to be minimized within this class, having a text 
 * that doesn't have many recurring words will increase the memory
 * efficiency of the class. Therefore the memory usage for this Data
 * Structure is:
 * 
 * O(n)
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
	
	/**
	 * Creates a new TrieLead Object
	 * 
	 * RunTime:
	 * 14 primitive operations occur therefore this method has a constant
	 * runtime giving it a big-O value of:
	 * 
	 * O(1)
	 */
	@SuppressWarnings("unchecked")
	public TrieLeaf() {
		this.occurrenceSize = 20; //1
		this.pointer = 0; //1
		this.occurrences = (Pair<Integer, Integer>[]) new Pair[this.occurrenceSize]; //3
		this.indexSize = 20; //1
		this.indexPointer = 0;//7
		this.occurrenceIndex = new int[this.indexSize]; //2
		this.lineIndexSize = 20; //1
		this.indexPointer = 0; //1
		this.lineIndex = (Pair<Integer, Integer>[]) new Pair[lineIndexSize]; //3
	}
	

	/**
	 * Inserts an occurrence of the word given so it can be accessed later
	 * 
	 * RunTime:
	 * In the worst case of this method running it will have linear runtime
	 * since it will be gated by a resize method. But every other time time
	 * only 15 primitive operations occur giving it a constant runtime of
	 * O(1). This means this method will work at its peak if the text being
	 * analyzed doesn't use a lot of recurring words. Therefore the runtimes are
	 * 
	 * O(n) Worst Case && O(1) Best Case.
	 * 
	 * 	 * @param line The line number the word occurs on.
	 * @param col The column number the word starts on.
	 * @param index The index of the word in relation to the entire text.
	 * @param lineIndex The start and end points of the line the word occurs on.
	 */
	public void insertOccurrence(Integer line, Integer col, int index,
								 Pair<Integer, Integer> lineIndex) {
		if (this.pointer == this.occurrenceSize /2) { //2
			this.resizeOccurrences(); //O(n)
		}
		if (this.indexPointer == this.indexSize/2) { //2
			this.resizeIndex(); //O(n)
		}
		if (this.lineIndexPointer == this.lineIndexSize/2) { //2
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
	 * Returns where the pointer in the array is, this will be in indication
	 * of how many elements are currently being stored.
	 * 
	 * RunTime:
	 * Since only one primitive operation occurs for this method the big-O value
	 * will be:
	 * 
	 * O(1) 
	 * 
	 * @return The amount of occurrences of the word within the text currently.
	 */
	public int appearances() {
		return this.pointer; //1
	}
	
	/**
	 * Returns an array of all the indexes of the word in relation
	 * to the whole text, these can be used to jump to particular
	 * words in the document rather then scanning the whole document to
	 * find words.
	 * 
	 * RunTime:
	 * Since only one primitive operation occurs for this method the big-O value
	 * will be:
	 * 
	 * O(1)
	 * 
	 * @return An array of all the indexes of the word in relation
	 * 		to the whole text
	 */
	public int[] indexes() {
		return this.occurrenceIndex;
	}
	
	/**
	 * Returns an array of all the line and column numbers that
	 * this word occurs on.
	 * 
	 * RunTime:
	 * Since only one primitive operation occurs for this method the big-O value
	 * will be:
	 * 
	 * O(1)
	 * 
	 * @return An array of all the line and column numbers that
	 * 		this word occurs on.
	 */
	public Pair<Integer, Integer>[] pairs() {
		return this.occurrences; //1
	}
	
	/**
	 * Returns an array of indexes for the start and end points of each line
	 * this word occurs on.
	 * 
	 * RunTime:
	 * Since only one primitive operation occurs for this method the big-O value
	 * will be:
	 * 
	 * O(1)
	 * 
	 * @return An array of indexes for the start and end points of each line
	 * 		this word occurs on.
	 */
	public Pair<Integer, Integer>[] lineIndex(){
		return this.lineIndex; // 1
	}

	/**
	 * Resizes the internal array for lineIndexes by copying all the old
	 * values into a new bigger array
	 * 
	 * RunTime:
	 * Linear runtime since every element in the array needs to be scanned.
	 * 
	 * O(n)
	 */
	private void resizeLineIndex() {
		this.lineIndexSize = this.lineIndexSize*2; //2
		@SuppressWarnings("unchecked")
		Pair<Integer, Integer>[] temp = (Pair<Integer, Integer>[]) new Pair[this.lineIndexSize]; //3
		for (int i = 0; i < this.lineIndex.length; i++) { //i
			temp[i] = this.lineIndex[i]; //3
		}
		this.lineIndex = temp; //1
	}
	
	/**
	 * Resizes the internal array for indexes by copying all the old
	 * values into a new bigger array
	 * 
	 * RunTime:
	 * Linear runtime since every element in the array needs to be scanned.
	 * 
	 * O(n)
	 */
	private void resizeIndex() {
		this.indexSize = this.indexSize*2; //2
		int[] temp = new int[this.indexSize]; //3
		for (int i = 0; i < this.occurrenceIndex.length; i++) { //i
			temp[i] = this.occurrenceIndex[i]; //3
		}
		this.occurrenceIndex = temp; //1
	}
	
	/**
	 * Resizes the internal array for occurrences by copying all the old
	 * values into a new bigger array
	 * 
	 * RunTime:
	 * Linear runtime since every element in the array needs to be scanned.
	 * 
	 * O(n)
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
