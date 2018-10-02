package comp3506.assn2.adts;


public class TrieLeaf {
	private int[] occurences; //where the word starts
	private int occurenceSize;
	private int pointer;
	
	public TrieLeaf() {
		this.occurenceSize = 10;
		this.pointer = 0;
		this.occurences = new int[this.occurenceSize];
	}
	
	/**
	 * 
	 * @param line
	 * @param col
	 */
	public void insertOccurence(int index) {
		if (this.occurences.length == this.occurenceSize/2) { //2
			this.resizeOccurences(); //1
		}
		this.occurences[this.pointer] = index;//3
	}
	
	/**
	 * 
	 * @return
	 */
	public int appearences() {
		return this.occurences.length; //1
	}
	
	public int[] indexes() {
		return this.occurences;
	}
	
	
	private void resizeOccurences() {
		this.occurenceSize = this.occurenceSize*2; //2
		int[] temp =  new int[this.occurenceSize]; //3
		
		for (int i = 0; i < this.occurenceSize/2; i++) { // i
			temp[i] = this.occurences[i]; //3
		}
		this.occurences = temp; // 1
	}
	
}
