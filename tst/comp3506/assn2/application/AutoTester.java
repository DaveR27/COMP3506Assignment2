package comp3506.assn2.application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import comp3506.assn2.adts.Trie;
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
		// TODO Implement constructor to load the data from these files and
		// TODO setup your data structures for the application.
	}
	
	private void loadTrie() {
		Integer lineIndex = 0;
		String line;
		Scanner wordLine = new Scanner(this.documentData.toString());
		while (wordLine.hasNextLine()) {
			lineIndex++;
			Integer colIndex = 0;
			line = wordLine.nextLine();
			Scanner wordFinder = new Scanner(line);
			while (wordFinder.hasNext()) {
			String word = wordFinder.next();
			int wordIndex = line.indexOf(word, colIndex);
			this.wordTrie.addWord(word, lineIndex, wordIndex);
			colIndex += wordIndex;
			}
			wordFinder.close();
		}
		wordLine.close();
	}
	
	private void loadFile(String fileName, StringBuffer savingTo) {
		BufferedReader bw = null;
		String line;
		try {
			bw = new BufferedReader(new FileReader(fileName));
			while ((line = bw.readLine()) != null) {
				savingTo.append(line + '\n');
			} 
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
	}
//	
//	private boolean arguementCheck(String word) throws IllegalArgumentException {
//		
//	}
	
	

}
