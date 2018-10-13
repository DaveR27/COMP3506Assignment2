package comp3506.assn2.adts;

import comp3506.assn2.utils.Pair;
import comp3506.assn2.utils.Triple;

import java.util.Scanner;

/**
 * Array based Word-Match WordMatchTrie Object, to be used in conjunction with a String variable that stores the
 * text that is being processed. This Data Structure will store chars from the following alphabet:
 * {a-z '}
 * This class is to be used as a means to store words along, including where they occur within
 * a given text. This class will not work if trying to store anything that is not in the alphabet
 * above. If given, this class will also store an array of titleIndexes so that searching of 
 * particular parts of the text can be done.
 * 
 * Memory Usage:
 * The memory usage of this structure can be seen as containing 4 major components, one being
 * n amount of memory where n is the amount of unique Strings within that text, two is  n which
 * is the amount of n is the amount of leaf nodes for each unique string and third is d where
 * d is the size of the array in each leaf node that stores the occurrences of each word within the
 * text.  Since this WordMatchTrie has to be used within conjunction of a String variable or a "text" the
 * four variable for space usage is m, where m is the size of the text to be processed. 
 * This can be simplified to n+d+m space complexity giving it this a big-O of:
 * 
 * O(n+d+m)
 * 
 * @author David Riddell
 *
 */
public class WordMatchTrie {
	private Triple<Integer,Integer,String>[] titleIndex;
	private TrieNode root;

    /**
     * Creates a new WordMatchTrie
     *
     * RunTime:
     * The runtime of this method is gated by the amount of words within the text
     * it is working with. Since every word in documentData is scanned by load Trie
     * this method will share loadTrie's run time. Therefore the run time for this
     * method is:
     *
     * O(n)
     *
     * @param documentData Text to be used in conjunction with this trie
     * @param indexData Index data that is supported by documentData
     */
	public WordMatchTrie(String documentData, String indexData) {
		this.titleIndex = null; // 1
		this.root = new TrieNode(null); // 2
        this.loadTrie(documentData, indexData); //O(n)
	}

    /**
     * Loads the trie with the text, it will add all the data needed for fast indexing
     * back into the text that this trie is working in conjunction with.
     *
     * RunTime:
     * This method has linear runtime since it will scan every word within the text
     * that it is given.
     *
     * O(n)
     *
     * @param documentData text to be used in conjunction with this WordMatchTrie
     * @param indexData the index data of the documentData if it is supported.
     */
    private void loadTrie(String documentData, String indexData) {
        boolean titlesAvailable = false; //1
        if (indexData != null) { //1
            titlesAvailable = true; //1
        }
        int lineIndex = 0; // line number //1
        int documentIndex = 0; // index of a word in the whole doc //1
        Scanner wordFinder = new Scanner(documentData); //2
        //Gets all the data from the lines in the file.
        while(wordFinder.hasNextLine()) { // i
            lineIndex++; //2
            String line = wordFinder.nextLine(); //2
            int lineLength = line.length(); //2
            documentIndex += lineLength + 1; //3
            int lineStartIndex = documentIndex - lineLength; //2
            int endOfLineIndex = documentIndex; //2
            Scanner wordFind = new Scanner(line); //2
            //Gets all the data from each word in the line
            while(wordFind.hasNext()) { //i
                int colIndex = 0; //1
                String word = wordFind.next(); //2
                word = this.cleanLine(word); //2
                int wordIndex = line.indexOf(word, colIndex); //2
                int overallIndex = documentIndex - (lineLength-wordIndex); //3
                this.addWord(word.replaceAll("[ ]", "").toLowerCase(), lineIndex,
                        wordIndex+1, overallIndex,
                        new Pair<Integer,Integer>(lineStartIndex, endOfLineIndex)); //8
            }
            wordFind.close(); //1
        }

        wordFinder.close(); //1
        /*
         * If there are indexes then they are also added to the WordMatchTrie so that searches can be
         * done on particular sections of the document.
         */
        if (titlesAvailable) { //1
            CustomArrayList<String> indexNames = new CustomArrayList<>(); //2
            CustomArrayList<Integer> endPoints = new CustomArrayList<>(); //2
            Scanner indexScanner = new Scanner(indexData); //2
            indexScanner.useDelimiter(","); //1
            /*
             * Gets information about each line in the index variable, note this only works for
             * index variables of the form INDEX, LINENUMBER.
             */
            while (indexScanner.hasNextLine()) { //i
                String line = indexScanner.nextLine(); //2
                String name = line.split(",")[0]; //2
                String lastIndex = line.split(",")[1]; //2
                indexNames.add(name); //1
                endPoints.add(Integer.parseInt(lastIndex));//2
            }
            indexScanner.close(); //1
            endPoints.add(lineIndex); //1
            Object[] indexName = indexNames.toArray(); //2
            Object[] endPoint = endPoints.toArray(); //2
            @SuppressWarnings("unchecked")
            Triple<Integer, Integer, String>[] titleIndexes =
                    (Triple<Integer, Integer, String>[]) new Triple[indexName.length]; //2
            /*
             * The next 3 for loops sort the indexes into Triples so that each Triple has the name
             * of the start line of the index, the end line and then the name of the index.
             */
            for (int i = 0; i < indexName.length; i++) { //i
                if (indexName[i] != null) { //2
                    titleIndexes[i] = new Triple<Integer, Integer, String>(
                            null, null, (String) indexName[i]); //5
                }
            }
            for (int i = 0; i < titleIndexes.length; i++) { //i
                if ((endPoint[i] != null) && (titleIndexes[i] != null)) { //4
                    titleIndexes[i].setLeftValue((Integer) endPoint[i]); //3
                } else {
                    break; //1
                }
            }
            for (int i = 1; i < endPoint.length; i++) { //i
                if ((endPoint[i] != null)) { //2
                    titleIndexes[i-1].setCentreValue((Integer) endPoint[i]); //6
                } else {
                    break; //1
                }
            }
            this.setTitleIndex(titleIndexes); //puts the array just made into the WordMatchTrie. //1
        }
    }

    /**
     * Edits the string to remove unwanted punctuation, allowing the
     * words in the file to be stored in the custom WordMatchTrie.
	 *
	 * RunTime:
     * This method has linear runtime since it much check the whole line
     * to see if it can remove anything, therefor:
     *
     * O(n)
     *
     * @param line The line to be stripped of unwanted punctuation.
     * @return The fixed line that doesn't included unwanted chars.
     */
    public String cleanLine(String line) {
        line = line.replaceAll("[^'a-zA-z ]", ""); //2
        line = line.replaceAll("[_-]", " "); //2
        line = line.replaceAll("\\]", ""); //2
        line = line.replaceAll("\\[", "");//2
        line = line.replaceAll("[`]", "");//2
        line = line.replaceAll("[\\\\]", "");//2
        line = line.replaceAll("[/]", "");//2
        line = line.replaceAll("[,]", "");//2
        return line;
    }
	
	/**
	 * If the WordMatchTrie text being processed has indexes to be taken advantage of
     * this method is used so that an array of all the indexes can be stored.
     *
     * RunTime:
     * The runtime of this method is constant seeing as there is only one primitive
     * operation that occurs, therefore:
     *
     * O(1)
     *
	 * @param titleIndex Triple containing the start line and the end line of
     *                   an index and the title of that index.
     */
	public void setTitleIndex(Triple<Integer,Integer,String>[] titleIndex) {
		this.titleIndex = titleIndex; // 1
	}
	
	/**
	 * Searches to find if the word-match trie is storing a particular index.
     *
     * RunTime:
     * Since every element of the array need to be checked there will be a linear
     * runtime for this method therefore:
     *
     * O(n)
     *
	 * @param title Index Tile to be searched for.
	 * @return If the WordMatchTrie does contain the index, the Triple is returned, otherwise
     *      null is returned.
	 */
	public Triple<Integer, Integer, String> containsTitle(String title) {
		for (int i = 0; i < this.titleIndex.length; i++) { //i
			if (this.titleIndex[i] != null) { //2
				if (this.titleIndex[i].getRightValue().equals(title)) { //4
					return this.titleIndex[i];//2
				}
			}else {
				break; //1
			}
		}
		return null; //1
	}


	/**
	 * Adds a word to the word-match trie.
     *
     * RunTime:
     * The runTime of this method will be capped by endOfWord since it has a linear
     * runtime. To traverse the trie adding nodes it will be O(dn) where d is the
     * alphabet that is being used and n is the size of the word being added. But
     * in the worst case of endOfWord the array that is being used within it needs
     * to be resized so when that occurs the worst case of this method will also
     * be linear, but when that doesn't occur endOfWord will run in constant time
     * therefore making the best case of this method O(dn). Therefore the run times
     * for this method will be:
     *
     * O(n) - Worst Case || O(dn) Best Case.
     *
	 * @param word The word to be added to the Word-Match WordMatchTrie.
	 * @param line The line number that the word is on.
	 * @param col The column that the word starts on.
	 * @param docIndex The overall index of the word within the text.
	 * @param lineIndex The index of the line that the word is contained on.
	 */
	public void addWord(String word, Integer line, Integer col, int docIndex,
						Pair<Integer, Integer> lineIndex) {
		TrieNode movingNode = this.root; //1
		
		for(int i = 0; i < word.length(); i++) { // i
			int index = word.charAt(i) - 'a'; //2
			if (index == -58) { //1
				index = 26; //1
			}
			if(movingNode.getChildren()[index] == null) { //3
				movingNode.addElement(index, String.valueOf(word.charAt(i))); //3
			}
			movingNode = movingNode.getElement(index); //O(1)
		}
		movingNode.endOfWord(line, col, docIndex, lineIndex);  //O(n)(worst case) O(1) (best case)
	}
	
	/**
	 * Returns the amount of times the word appears within the text.
     *
     * RunTime:
     * Since traverseTrie is the most expensive operation within this method this
     * method will share the same big-O value as traverseTrie, therefore:
     *
     * O(dn)
     *
	 * @param word The word that is being searched for within the text
	 * @return the amount of time the word appears within the text or -1 if the
     *      word isn't in the text.
	 */
	public int getWordAmount(String word) {
		TrieNode movingNode = this.traverseTrie(word);

		if (movingNode != null){
            return movingNode.returnInfoLeaf().appearances();
        } else {
		    return -1;
        }

	}
	
	/**
	 * Traverses the WordMatchTrie structure to find a particular string, if the string
     * if not stored within the WordMatchTrie null is returned.
     *
     * RunTime:
     * The runtime of this method will depend on two things, the size of the
     * alphabet being stored by each child array in TrieNode and how big the word
     * being searched is. In the case of this application
     * the alphabet is 27 chars long, so to traverse the WordMatchTrie given a word n,
     * the runtime will be 27*n. In a more general form for big O notation, the
     * runtime in the worse case will have a value of:
     *
     * O(dn)
     * where d is the size of the alphabet
     *
	 * @param word String to search the WordMatchTrie for
	 * @return null if the word isn't found, otherwise the node that is storing
     *      the last letter of that word is returned.
	 */
	public TrieNode traverseTrie(String word) {
		TrieNode movingNode = root; // 1

		for(int i = 0; i < word.length(); i++) { // i
			int index = word.charAt(i) - 'a';
			if(index == -58) { // 1
				index = 26; //1
			}
			if(movingNode.getElement(index) == null) { //1
				return null; //1
			} else {
				movingNode = movingNode.getElement(index); //1 + 2 (2 is from getElement)
			}
		}
		return movingNode; //1
	}
	
	/**
	 * Returns an array of all the indexes of the word within the text this WordMatchTrie is being
     * used in conjunction with. The indexes are used with the whole text so that the text
     * can be indexed into without having to linear scan the whole text to find the desired word.
     *
     * RunTime:
     * Since traverseTrie is the most expensive operation within this method this
     * method will share the same big-O value as traverseTrie, therefore:
     *
     * O(dn)
     *
	 * @param phrase The word to find the indexes of.
	 * @return An array of all the indexes of the word within the text that the
     *      WordMatchTrie is in conjunction with, otherwise null if the word isn't within
     *      the text.
	 */
	public int[] findWordIndex(String phrase) {
		TrieNode movingNode = this.traverseTrie(phrase);
		if(movingNode.isEndOfWord() && movingNode != null) {
			return movingNode.returnInfoLeaf().indexes();
		}
		return null;
	}
	
	/**
	 * Traverses the WordMatchTrie to find the end of the word, if it is found will
     * return an array with all the occurrences of that word within the text.
     *
     * RunTime:
     * Since traverseTrie is the most expensive operation within this method this
     * method will share the same big-O value as traverseTrie, therefore:
     *
     * O(dn)
     *
	 * @param phrase the phrase to find the occurrences of.
	 * @return Array of all the occurrences of the phrase within the text, if
     *      the word is not found null is returned.
	 */
	public Pair<Integer, Integer>[] findWordPair(String phrase) {
		TrieNode movingNode = this.traverseTrie(phrase); //O(dn) + 1
		if(movingNode != null) { //1
		    if (movingNode.isEndOfWord()) { //1 + 1
                return movingNode.returnInfoLeaf().pairs();// 1 + 2 + 1
            }
		}
		return null; // 1
	}
	
	/**
	 * Traverses the WordMatchTrie to find a word, if it is found it will then return the
     * TrieLeaf attached to it.
     *
     * RunTime:
     * Since all the operations in this method are constant apart from calling
     * traverseTrie which is O(dn), the big-o value for this method must share
     * the same runtime as traverseTrie so:
     *
     * O(dn)
     *
	 * @param word Word to search WordMatchTrie for
	 * @return null if no infoLeaf, otherwise a LeafNode is returned with all
     *      the information about the word within the text.
	 */
	public TrieLeaf wordInfo(String word) {
		TrieNode movingNode = this.traverseTrie(word); // O(dn) + 1
		if(movingNode != null) { // 1
		    if (movingNode.isEndOfWord()) { //1
                return movingNode.returnInfoLeaf(); // 3 (1 + 2 from returnInfoLeaf);
            }
		}
		return null; //1
	}

}
	
