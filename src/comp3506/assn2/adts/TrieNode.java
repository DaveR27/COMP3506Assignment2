package comp3506.assn2.adts;


import comp3506.assn2.utils.Pair;

/**
 * This class is to be used in conjunction with a WordMatchTrie to store chars of a specified alphabet
 * that is outlined within the WordMatchTrie class.
 * 
 * Memory Usage:
 * The memory usage of this class is O(d) where d is the alphabet size of text that is being
 * Processed. This is due to the fact that the amount of children nodes that this class can
 * hold will depend on the size of the children array, which depends on the the size of the alphabet.
 * 
 * 
 * @author David Riddell
 *
 */
public class TrieNode {
	private String storedElement;
	private TrieNode[] children;
	private final int ALPHABET_SIZE = 27; //a-z && '
	private TrieLeaf infoNode;
	private boolean endOfWord;

    /**
     * Creates a new TrieNode and stores a letter
     *
     * RunTime:
     * The runtime of this method is linear since there is a for loop that
     * has to set all the children to null for other methods within the WordMatchTrie to
     * work, therefor this method has a big-O value of:
     *
     * O(n)
     *
     * @param letter The string to be stored by this node.
     */
	public TrieNode(String letter) {
		this.storedElement = String.valueOf(letter); //2
		this.children = new TrieNode[this.ALPHABET_SIZE]; //2
		this.endOfWord = false; //1
		
		for (int i = 0; i < this.ALPHABET_SIZE; i++) { //i
			this.children[i] = null;
		}
	}
	
	/**
	 * Makes this Node the end of a word and store the occurrence of the word,
     * this includes information about the line index and on what line the
     * word occurs on.
     *
     *
     * RunTime:
     * In the worst run of this method 4 primitive methods occur and the linear
     * runtime of insertOccurrence also happens. Since the linear runtime of
     * insertOccurrence will effect the runtime of this method, endOfWord will
     * also have a linear runtime in the worst case. But it should be noted that
     * insertOccurrence isn't always linear, if the internal arrays in the TrieLeaf
     * are big enough to fit a new element, endOfWord will run in constant time
     * just like insertOccurrence. But since the worst case is what need to be
     * taken into consideration for the big-O value this method has a value of:
     *
     * O(n);
     *
	 * @param line The Line the occurrence of the word is on.
	 * @param col The column that the word starts at.
	 * @param docIndex The overall index of that the word starts at.
	 * @param lineIndex Pair that indicates the starting index of the line
     *                     that this occurrence of the occurs and the end index
     *                      of the line.
	 */
	public void endOfWord(Integer line, Integer col, int docIndex,
						  Pair<Integer, Integer> lineIndex) {
		this.endOfWord = true; //1
		if (this.infoNode == null) { //1
			this.infoNode = new TrieLeaf(); //2
		}
		this.infoNode.insertOccurrence(line, col, docIndex, lineIndex); //O(n) + 1
	}
	
	/**
	 * Checks to see if this node has any children nodes.
     *
     * RunTime:
     * The primitive operations of this method in the worst case are i*3, since
     * the for loop is linear this will give the overall function a linear runtime
     * within the worst case. Therefore it has a big-O value of:
     *
     * O(n)
     *
	 * @return True if this node has children nodes, otherwise false.
	 */
	public boolean hasChildren() {
		for (int i = 0; i < this.children.length; i++) { //i times
			if (this.children[i] != null) { //2
				return true; //1
			}
		}
		return false; //1
	}
	
	/**
	 * Checks to see if this node is the end of a word.
     *
     * RunTime:
     * This method has constant runtime since only one primitive operation
     * occurs.
     *
     * O(1)
     *
	 * @return True if this node is the end of a word, otherwise false.
	 */
	public boolean isEndOfWord() {
		return this.endOfWord; //1
	}
	
	/**
	 * Adds an element to the children array being stored at this node.
     *
     * RunTime:
     * only 3 primitive operations occur, index, assignment and object creation
     * therefore this method runs in constant time, giving it a big-O value of:
     *
     * O(1);
     *
	 * @param index Where to store the element in children
	 * @param letter The character to be store.
	 */
	public void addElement(int index, String letter) {
		this.children[index] = new TrieNode(letter); //3
	}
	
	/**
     * This method will return either a TrieNode or null depending on what is
     * stored at the specified index given to this method.
     *
	 * RunTime:
     * There is only 2 primitive operations that occur, 1 for indexing into the
     * array(children) and then 1 for returning what is at that index. This will
     * give this method a constant runtime, thus giving this method a big-o value
     * of:
     *
     * O(1)
     *
	 * @param index The position at which the Object being stored there should
     *              be returned.
	 * @return TrieNode that is being stored at that element of children, if
     *      there is nothing at that position then null is returned.
	 */
	public TrieNode getElement(int index) {
		return this.children[index];//2
	}

	
	/**
     * Returns an array of all the children that of this node. Children are
     * other TrieNodes that are stored within the children array.
     *
	 * RunTime;
     * There is only 1 primitive operation that occurs in the worst case of this
     * method therefore it has a big-O value of
     *
     * O(n)
     *
	 * @return Children of this node, where the children are the chars that come
     *      after the one stored in this Node in a particular word.
	 */
	public TrieNode[] getChildren() {
		return this.children;//1
	}
	
	/**
	 * RunTime:
	 * Since only 2 primitive operations occur in the worse case of this method
     * there it has an O value of:
     *
     * O(1)
	 * 
	 * @return TrieLeaf if this node is the end of a word and has
	 * 		a info lead stored, otherwise null is returned.
	 */
	public TrieLeaf returnInfoLeaf() {
		if (this.infoNode == null) { //1
			return null;//1
		} else {
			return this.infoNode; //1
		}
	}
	
	
}
