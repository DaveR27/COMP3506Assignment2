package comp3506.assn2.application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import comp3506.assn2.adts.Trie;
import comp3506.assn2.adts.TrieLeaf;
import comp3506.assn2.adts.TrieNode;
import comp3506.assn2.utils.Pair;
import comp3506.assn2.utils.Triple;




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
	}
	
	private void loadTrie() {
		boolean titlesAvailable = false;
		if (this.indexData != null) {
			titlesAvailable = true;
		}
		int lineIndex = 0;
		int documentIndex = 0;
		Scanner wordFinder = new Scanner(this.documentData.toString());
		while(wordFinder.hasNextLine()) {
			lineIndex++;
			String line = wordFinder.nextLine();
			int lineLength = line.length();
			documentIndex += lineLength + 1;
			int lineStartIndex = documentIndex - lineLength;
			int endOfLineIndex = documentIndex;
			if (titlesAvailable) {
				if (this.indexData.toString().contains(String.valueOf(lineIndex))
				&& this.indexData.toString().contains(line.split(",")[0])
				&& (!(line.equals("")))) {
					this.wordTrie.insertTitleStart(line.split(",")[0], lineStartIndex, lineIndex);
				}
			}
			Scanner wordFind = new Scanner(line);
			while(wordFind.hasNext()) {
				int colIndex = 0;
				String word = wordFind.next();
				word = this.cleanLine(word);
				int wordIndex = line.indexOf(word, colIndex);
				colIndex += (wordIndex+word.length());
				int overallIndex = documentIndex - (lineLength-wordIndex);
				this.wordTrie.addWord(word.replaceAll("[ ]", "").toLowerCase(), lineIndex,
						wordIndex+1, overallIndex, new Pair<Integer,
								Integer>(lineStartIndex, endOfLineIndex));
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
	private boolean argumentCheck(String word) throws IllegalArgumentException {
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
		this.argumentCheck(word);
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
		this.argumentCheck(phrase);
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
		return this.trieOccurrenceSearch(searchStrings, searchStringPointer, phrase);
	}
	
	@SuppressWarnings("unchecked")
	private List<Pair<Integer,Integer>> trieOccurrenceSearch(String[] searchWords, int indexPointer, String phrase) {
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
			Pair<Integer, Integer>[] occurrenceArray = (Pair<Integer, Integer>[]) new Pair[searchWords.length];
			int occurrencePointer = 0;
			int[] occurrenceIndex = this.wordTrie.findWordIndex(searchWords[0]);
			for (int i = 0; i<=occurrenceIndex.length; i++) {
				StringBuilder wordFound = new StringBuilder();
				for(int j = 0; j <= charLength; j++) {
					wordFound.append(this.documentData.toString().charAt(occurrenceIndex[i + j]));
				}
				if (wordFound.toString().contains(phrase)) {
					occurrenceArray[occurrencePointer] = occurence[i];
				}
			}
			occurence = occurrenceArray;
		}
		if (occurence == null) {
			return new ArrayList<Pair<Integer,Integer>>(null);
		} else {
			List<Pair<Integer,Integer>> listOccurrence = new ArrayList<Pair<Integer,Integer>>();
			for (int i = 0; i<occurence.length; i++) {
				if (occurence[i] != null) {
					listOccurrence.add(occurence[i]);
				}
			}
			return listOccurrence;
		}
	}
		
	public List<Pair<Integer,Integer>> prefixOccurrence(String prefix) throws IllegalArgumentException {
		this.argumentCheck(prefix);
		List<Pair<Integer,Integer>> prefixOccurrence = new ArrayList<Pair<Integer,Integer>>();
		TrieNode prefixNode = this.wordTrie.traverseTrie(prefix);
		prefixOccurrence = this.occurrenceTraversal(prefixNode, prefixOccurrence);
		return prefixOccurrence;
	}
	
	private List<Pair<Integer, Integer>> occurrenceTraversal(TrieNode root, List<Pair<Integer, Integer>> prefixOccurence) {
		if(!(root.hasChildren())) {
			this.visitNode(root, prefixOccurence);
			return prefixOccurence;
		}
		prefixOccurence = this.visitNode(root, prefixOccurence);
		for(int i = 0; i < root.getChildren().length; i++) {
			if (root.getElement(i) != null) {
				occurrenceTraversal(root.getElement(i), prefixOccurence);
			}
		}
		return prefixOccurence;
	}
	
	private List<Pair<Integer,Integer>> visitNode(TrieNode node, List<Pair<Integer,Integer>> prefixOccurence) {
		if (node.isEndOfWord()) {
			Pair<Integer, Integer>[] occurrences = node.returnInfoLeaf().pairs();
			for (int i = 0; i < node.returnInfoLeaf().appearances(); i++) {
				prefixOccurence.add(occurrences[i]);
			}
			return prefixOccurence;
		}
		return prefixOccurence;
	}


	public List<Integer> wordsOnLine(String[] words) throws IllegalArgumentException {
		String[] validWords = this.validWordChecker(words);
		int i = 0;

		while(validWords[i] == null) {
			i++;
		}
		Pair<Integer, Integer>[] occurrences = this.wordTrie.findWordPair(validWords[i]);
		String[] linesToCheck = this.lineBuilder(validWords[i]);

		return this.allWordsOnLines(linesToCheck, validWords, occurrences);
	}


	
	public List<Integer> someWordsOnLine(String[] words) throws IllegalArgumentException {
		List<Integer> foundWords = new ArrayList<>();
		String[] validWords = this.validWordChecker(words);

		for (int i = 0; i < validWords.length; i++){
			Pair<Integer, Integer>[] occurrences;
			if (validWords[i] != null) {
				 occurrences = this.wordTrie.findWordPair(validWords[i]);
				 for (int j = 0; j < occurrences.length; j++) {
				 	if (occurrences[j] != null) {
						if (!(foundWords.contains(occurrences[j].getLeftValue()))) {
							foundWords.add(occurrences[j].getLeftValue());
						}
					}
				 }
			}
		}
		return foundWords;
	}
	
	public List<Integer> wordsNotOnLine(String[] wordsRequired, String[] wordsExcluded) 
			throws IllegalArgumentException {
		String[] validWordsRequired = this.validWordChecker(wordsRequired);
		String[] validWordsExcluded = this.validWordChecker(wordsExcluded);
		int i = 0;

		while(validWordsRequired[i] == null) {
			i++;
		}
		Pair<Integer, Integer>[] occurrences = this.wordTrie.findWordPair(validWordsRequired[i]);
		String[] linesToCheck = this.lineBuilder(validWordsRequired[i]);
		for (int p = 0; p < linesToCheck.length; p++) {
			if (linesToCheck[p] != null) {
				for (int j = 0; j < validWordsExcluded.length; j++){
					if (validWordsExcluded[j] != null){
						if (linesToCheck[p].contains(validWordsExcluded[j])) {
							linesToCheck[p] = null;
						}
					}
				}
			}
		}
		return this.allWordsOnLines(linesToCheck, validWordsRequired, occurrences);
	}
	
	@SuppressWarnings("unchecked")
	public List<Triple<Integer,Integer,String>> simpleAndSearch(String[] titles, String[] words)
			throws IllegalArgumentException {
		if (words == null || words.length == 0) {
			throw new IllegalArgumentException();
		}
		boolean validTitles = true;
		if (titles == null || this.indexData == null || titles.length == 0) {
			validTitles = false;
		}
		if (validTitles) {
			/*
			 * TODO: you can return the list of all of the char indexes and you know what "Line" the start at
			 * so you need to compare to the next index and find out when the section stops.
			 */
			return null;
		} else {
			List<Triple<Integer,Integer,String>> foundLines = new ArrayList<>();
			Pair<Integer, Integer>[][] occurrences = (Pair<Integer, Integer>[][]) new 
					Pair[words.length][];
			for (int i = 0; i < words.length; i++) {
				occurrences[i] = this.wordTrie.findWordPair(words[i]);
			}
			for (int i = 0; i < occurrences.length; i++) {
				if (occurrences[i] != null) {
					for (int j = 0; j < occurrences[i].length; j++) {
						if (occurrences[i][j] != null) {
							foundLines.add(new 
									Triple<Integer,Integer,String>(occurrences[i][j].getLeftValue(),
											occurrences[i][j].getRightValue(), words[i]));
						}
					}
				}
			}
			return foundLines;
		}	
	}
	

	private String[] validWordChecker(String[] words) {
		String[] validWords = new String[words.length];
		if (this.stopWordsData != null) {
			for (int i = 0; i < words.length; i++) {
				if (!(this.stopWordsData.toString().contains(words[i]))) {
					validWords[i] = words[i];
				}
			}
		} else {
			for (int i = 0; i < words.length; i++) {
				validWords[i] = words[i];
			}
		}
		return validWords;
	}
	
	private String[] lineBuilder(String word) {
		TrieLeaf infoNode = this.wordTrie.wordInfo(word);
		Pair<Integer, Integer>[] lineIndex = infoNode.lineIndex();
		String[] lines = new String[lineIndex.length];
		
		for (int i = 0; i < lineIndex.length; i++) {
			StringBuffer line = new StringBuffer();
			if (lineIndex[i] != null) {
				for (int j = lineIndex[i].getLeftValue() - 1; j < lineIndex[i].getRightValue(); j++) {
					line.append(this.documentData.toString().charAt(j));
				}
				lines[i] = line.toString();
			}
		}
		return lines;
	}

	private List<Integer> allWordsOnLines(String[] linesToCheck, String[] validWords, Pair<Integer, Integer>[] occurrences){
		List<Integer> foundWords = new ArrayList<>();

		for (int j = 0; j < linesToCheck.length; j++) {
			if (linesToCheck[j] != null) {
				boolean checker = true;
				for (int p = 0; p < validWords.length; p++) {
					if (validWords[p] != null) {
						if (!(linesToCheck[j].contains(validWords[p]))) {
							checker = false;
						}
					}
				}
				if (checker) {
					foundWords.add(occurrences[j].getLeftValue());
				}
			}
		}
		return foundWords;
	}
}
