package comp3506.assn2.application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
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
		wordLine.useDelimiter(",| | \r\n");
		while (wordLine.hasNextLine()) {
			lineIndex++;
			Integer colIndex = 0;
			line = wordLine.nextLine();
			Scanner wordFinder = new Scanner(line);
			if (this.indexData != null) {
				if (this.indexData.toString().contains(line)) {
					colIndex = this.titleLine(line, wordFinder, lineIndex, colIndex);
					continue;
				}
			}
			wordFinder.useDelimiter(",| | \r\n");
			while (wordFinder.hasNext()) {
			String word = wordFinder.next().toLowerCase().replaceAll("^\\p{Punct}+|\\p{Punct}+$", "");
			word = word.replaceAll("[0-9]", "");
			word = word.replaceAll("-", " ");
			int wordIndex = line.indexOf(word);
			this.wordTrie.addWord(word, lineIndex, wordIndex);
			colIndex += wordIndex;
			}
			wordFinder.close();
		}
		wordLine.close();
	}
	
	private int titleLine(String line, Scanner lineScanner, int lineIndex, int colIndex) {
		while (lineScanner.hasNext()) {
			String word = lineScanner.next().toLowerCase();
			int wordIndex = line.indexOf(word, colIndex);
			this.wordTrie.addWord(word, lineIndex, wordIndex);
			colIndex += wordIndex;
		}
		lineScanner.close();
		return colIndex;
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
	
	
//	public List<Pair<Integer,Integer>> phraseOccurrence(String phrase) throws IllegalArgumentException {
//		this.arguementCheck(phrase);
//		
//	}
//	
//	
//	public List<Pair<Integer,Integer>> prefixOccurrence(String prefix) throws IllegalArgumentException {
//		this.arguementCheck(prefix);
//		List<Pair<Integer,Integer>> listOccurences = null;
//		HashMap<Character, TrieNode> occurences = this.wordTrie.getPrefixOccurence(prefix);
//		this.findPrefixTraversal(occurences, listOccurences);
//		
//		//TODO: Check to see if it is end of word for every node from the children and if it is get the data of the occurrences from that node.
//	}
//	
//	private void findPrefixTraversal(HashMap<Character, TrieNode> occurences, List<Pair<Integer,Integer>> listOccurences) {
//		for (int i = 0; i<occurences.size(); i++) {
//			
//		}
//	}
//	private boolean arguementCheck(String word) throws IllegalArgumentException {
//		
//	}
	
	

}
