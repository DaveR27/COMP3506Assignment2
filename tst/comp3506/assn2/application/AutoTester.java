package comp3506.assn2.application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import comp3506.assn2.adts.Trie;


/**
 * Hook class used by automated testing tool.
 * The testing tool will instantiate an object of this class to test the functionality of your assignment.
 * You must implement the constructor stub below and override the methods from the Search interface
 * so that they call the necessary code in your application.
 * 
 * @author 
 */
public class AutoTester implements Search {
	private Trie wordMatchTrie;
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
		if (documentFileName == null || documentFileName == "") {
			throw new IllegalArgumentException();
		}
		this.wordMatchTrie = new Trie();
		this.loadFile(documentFileName);
		// TODO Implement constructor to load the data from these files and
		// TODO setup your data structures for the application.
	}
	
	
	private void loadFile(String documentFileName) {
		BufferedReader bw = null;
		Scanner scanner;
		int line = 0;
		String readLine;
		try {
			bw = new BufferedReader(new FileReader(documentFileName));
			while (bw.readLine() != null) {
				readLine = bw.readLine();
				line++;
				int scannedIndex = 1;
				scanner = new Scanner(readLine);
				while (scanner.hasNext()) {
					String scannedWord = scanner.next();
					this.wordMatchTrie.addWord(scannedWord, line, scannedIndex);
					scannedIndex = scannedWord.length() + 1;
				}
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Determines the number of times the word appears in the document.
	 * 
	 * @param word The word to be counted in the document.
	 * @return The number of occurrences of the word in the document.
	 * @throws IllegalArgumentException if word is null or an empty String.
	 */
	public int wordCount(String word) throws IllegalArgumentException {
		if (word == "" || word == null) {
			throw new IllegalArgumentException();
		}
		return this.wordMatchTrie.getWordAmount(word);
	}

}
