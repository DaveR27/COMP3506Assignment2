package comp3506.assn2.application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import comp3506.assn2.adts.Trie;
import comp3506.assn2.adts.TrieNode;
import comp3506.assn2.utils.Pair;




/**
 * Hook class used by automated testing tool.
 * The testing tool will instantiate an object of this class to test the functionality of your assignment.
 * You must implement the constructor stub below and override the methods from the Search interface
 * so that they call the necessary code in your application.
 * 
 * @author 
 */
public class AutoTester implements Search {
	private Trie wordTrie;
	private StringBuffer documentData;
	private StringBuffer indexData;
	private StringBuffer stopWordsData;
	private int documentIndex;
	
	/**
	 * Create an object that performs search operations on a document.
	 * If indexFileName or stopWordsFileName are null or an empty string the document should be loaded
	 * and all searches will be across the entire document with no stop words.
	 * All files are expected to be in the files sub-directory and 
	 * file names are to include the relative path to the files (e.g. "files\\shakespeare.txt").
	 * 
	 * @param documentFileName  Name of the file containing the text of the document to be searched.
	 * @param indexFileName     Name of the file containing the index of sections in the document.
	 * @param stopWordsFileName Name of the file containing the stop words ignored by most searches.
	 * @throws FileNotFoundException if any of the files cannot be loaded. 
	 *                               The name of the file(s) that could not be loaded should be passed 
	 *                               to the FileNotFoundException's constructor.
	 * @throws IllegalArgumentException if documentFileName is null or an empty string.
	 */
	public AutoTester(String documentFileName, String indexFileName, String stopWordsFileName) 
			throws FileNotFoundException, IllegalArgumentException {
		this.documentIndex = 0;
		this.documentData = new StringBuffer();
		this.indexData = new StringBuffer();
		this.stopWordsData = new StringBuffer();
		if (documentFileName == null || documentFileName == "") {
			throw new IllegalArgumentException();
		}
		if (indexFileName != null || indexFileName != "") {
			this.loadFile(indexFileName, this.indexData);
		}
		if (stopWordsFileName != null || stopWordsFileName != "") {
			this.loadFile(stopWordsFileName, this.stopWordsData);
		}
		this.loadFile(documentFileName, this.documentData);
		this.wordTrie = new Trie();
		this.loadTrie();
	}
	
	private void loadTrie() {
		int lineIndex = 0;
		int documentIndex = 0;
		Scanner wordFinder = new Scanner(this.documentData.toString());
		while(wordFinder.hasNextLine()) {
			lineIndex++;
			String line = wordFinder.nextLine();
			int lineLength = line.length();
			documentIndex += lineLength;
			Scanner wordFind = new Scanner(line);
			while(wordFind.hasNext()) {
				int colIndex = 0;
				String word = wordFind.next();
				word = this.cleanLine(word);
				int wordIndex = line.indexOf(word, colIndex);
				colIndex += (wordIndex+word.length());
				int overalIndex = documentIndex - (lineLength-wordIndex);
				this.wordTrie.addWord(word.replaceAll("[ ]", "").toLowerCase(), lineIndex, wordIndex+1, overalIndex);
			}
			wordFind.close();
		}
		wordFinder.close();
	}

	
	/**
	 * Edits the string to remove unwanted punctuation, allowing the
	 * words in the file to be stored in the custom Trie.
	 * 
	 * @param line The line to be stripped of unwanted punctuation.
	 * @return The fixed line that doesn't included unwanted chars.
	 */
	private String cleanLine(String line) {
		line = line.replaceAll("[^'a-zA-z ]", "");
		line = line.replaceAll("[_-]", " ");
		line = line.replaceAll("\\]", "");
		line = line.replaceAll("\\[", "");
		line = line.replaceAll("[`]", "");
		line = line.replaceAll("[\\\\]", "");
		line = line.replaceAll("[/]", "");
		line = line.replaceAll("[,]", "");
		return line;
	}
	
	/**
	 * Used to add a Title to the trie, this method is used to that the
	 * title line doesn't go through the line editing like normal words.
	 * 
	 * @param line Title line to be added to structure.
	 * @param lineScanner Scanner of the title line.
	 * @param lineIndex the line number.
	 * @param colIndex the column number of the words.
	 * @return colIndex is returned so that it can be added to the colIndex
	 * 			that is used in loadTrie.
	 */
	private int titleLine(String line, Scanner lineScanner, int lineIndex, int colIndex) {
		while (lineScanner.hasNext()) {
			String word = lineScanner.next().toLowerCase();
			int wordIndex = line.indexOf(word, colIndex);
			this.documentIndex += wordIndex;
			this.wordTrie.addWord(word, lineIndex, wordIndex,this.documentIndex);
			colIndex += wordIndex;
			
		}
		lineScanner.close();
		return colIndex;
	}
	
	/**
	 * Loads a file into memory using a BufferedReader.
	 * 
	 * @param fileName The file to be saved to memory.
	 * @param savingTo The variable to which the document data is saved
	 * 			to.
	 */
	private void loadFile(String fileName, StringBuffer savingTo) {
		BufferedReader bw = null;
		String line;
		try {
			bw = new BufferedReader(new FileReader(fileName)); // reads the txt file
			//checks to see if there is any more txt if there is assigns it to line.
			while ((line = bw.readLine()) != null) { 
				savingTo.append(line + '\n'); //adds it to the variable.
			} 
			//io clean up
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Checks arguments for null or an empty string.
	 * 
	 * @param word the word to be checked for invalid arguments.
	 * @return true if the word is valid otherwise false.
	 * @throws IllegalArgumentException thrown if the word is an empty string
	 * 			or the word is null.
	 */
	private boolean arguementCheck(String word) throws IllegalArgumentException {
		if (word == "" || word == null) {
			throw new IllegalArgumentException();
		}
		return true;
	}	
	
	/**
	 * Determines the number of times the word appears in the document.
	 * 
	 * @param word The word to be counted in the document.
	 * @return The number of occurrences of the word in the document.
	 * @throws IllegalArgumentException if word is null or an empty String.
	 */
	public int wordCount(String word) throws IllegalArgumentException {
		this.arguementCheck(word);
		return this.wordTrie.getWordAmount(word);
	}
	
	/**
	 * Finds all occurrences of the phrase in the document.
	 * A phrase may be a single word or a sequence of words.
	 * 
	 * @param phrase The phrase to be found in the document.
	 * @return List of pairs, where each pair indicates the line and column number of each occurrence of the phrase.
	 *         Returns an empty list if the phrase is not found in the document.
	 * @throws IllegalArgumentException if phrase is null or an empty String.
	 */
	public List<Pair<Integer,Integer>> phraseOccurrence(String phrase) throws IllegalArgumentException {
		this.arguementCheck(phrase);
		String line = this.cleanLine(phrase);
		int searchStringSize = 10;
		String[] searchStrings = new String[searchStringSize];
		int searchStringPointer = 0;
		Scanner lineScanner = new Scanner(line);
		lineScanner.useDelimiter(",| | \r\n");
	
		while(lineScanner.hasNext()) {
			String scannedWord;
			scannedWord = lineScanner.next();
			if (searchStringPointer == searchStringSize/2) {
				searchStringSize = searchStringSize*2;
				String[] temp = new String[searchStringSize];
				for (int i = 0; i<searchStrings.length; i++) {
					temp[i] = searchStrings[i];
				}
				searchStrings = temp;
			}
			searchStrings[searchStringPointer] = scannedWord;
			searchStringPointer++;
		}
		lineScanner.close();
		return this.trieOccurenceSearch(searchStrings, searchStringPointer, phrase);
	}
	
	@SuppressWarnings("unchecked")
	private List<Pair<Integer,Integer>> trieOccurenceSearch(String[] searchWords, int indexPointer, String phrase) {
		Pair<Integer, Integer>[] occurence;
		int charLength = 0;
		if (indexPointer == 1) {
			occurence = this.wordTrie.findWordPair(searchWords[0]);
		} else {
			for(int i = 0; i<searchWords.length;i++) {
				charLength += searchWords[i].length();
			} 
			charLength = charLength*2;
			occurence = this.wordTrie.findWordPair(searchWords[0]);
			Pair<Integer, Integer>[] occurenceArray = (Pair<Integer, Integer>[]) new Pair[searchWords.length];
			int occurencePointer = 0;
			int[] occurenceIndex = this.wordTrie.findWordIndex(searchWords[0]);
			for (int i = 0; i<=occurenceIndex.length; i++) {
				StringBuilder wordFound = new StringBuilder();
				for(int j = 0; j <= charLength; j++) {
					wordFound.append(this.documentData.toString().charAt(occurenceIndex[i + j]));
				}
				if (wordFound.toString().contains(phrase)) {
					occurenceArray[occurencePointer] = occurence[i];
				}
			}
			occurence = occurenceArray;
		}
		if (occurence == null) {
			return new ArrayList<Pair<Integer,Integer>>(null);
		} else {
			List<Pair<Integer,Integer>> listOccurence = new ArrayList<Pair<Integer,Integer>>();
			for (int i = 0; i<occurence.length; i++) {
				if (occurence[i] != null) {
					listOccurence.add(occurence[i]);
				}
			}
			return listOccurence;
		}
	}
//		
//		ArrayList<Pair<Integer,Integer>> occurenceList = new ArrayList<Pair<Integer,Integer>>();
//		this.occurenceTraversal(occurenceList);
//		
//	}
//	
//	private void occurenceTraversal(ArrayList<Pair<Integer, Integer>> occurenceList) {
//		this.visitNode()
//	}
//	
//	private void visitNode(TrieNode node) {
//		
//	}
	
	

	
	

}
