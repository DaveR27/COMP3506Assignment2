package comp3506.assn2.application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import comp3506.assn2.adts.CustomArrayList;
import comp3506.assn2.adts.WordMatchTrie;
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
	private WordMatchTrie wordTrie;
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
		this.wordTrie = new WordMatchTrie(this.documentData.toString(), this.indexData.toString());
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
		String line = this.wordTrie.cleanLine(phrase);
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
		CustomArrayList<Integer> wordChecking = new CustomArrayList<>();
		List<Integer> foundWords = new ArrayList<>();
		String[] validWords = this.validWordChecker(words);

		for (int i = 0; i < validWords.length; i++){
			Pair<Integer, Integer>[] occurrences;
			if (validWords[i] != null) {
				 occurrences = this.wordTrie.findWordPair(validWords[i]);
				 for (int j = 0; j < occurrences.length; j++) {
				 	if (occurrences[j] != null) {
						if (!(wordChecking.contains(occurrences[j].getLeftValue()))) {
							wordChecking.add(occurrences[j].getLeftValue());
						}
					}
				 }
			}
		}

		Object[] foundLines = wordChecking.toArray();
		for (int i = 0; i < foundLines.length; i++) {
			if (foundLines[i] != null) {
				foundWords.add((Integer) foundLines[i]);
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


	public List<Triple<Integer,Integer,String>> simpleOrSearch(String[] titles, String[] words)
			throws IllegalArgumentException {
		List<Triple<Integer, Integer, String>> foundWords = new ArrayList<>();
		this.wordsToProcessCheck(words);
		boolean validTitles = this.validTitlesChecker(titles);
		String[] validWords = this.validWordChecker(words);
		
		if (validTitles) {
			for (int j = 0; j < titles.length; j++) {
				Triple<Integer, Integer, String> tripleNode = this.wordTrie.containsTitle(titles[j]);
				if (tripleNode != null) {
					int startLine = tripleNode.getLeftValue();
					int endLine = tripleNode.getCentreValue();
					foundWords = this.findWordsInSection(startLine, endLine, validWords, foundWords);
				}
			}
		} else {
			foundWords = this.addToFoundWords(validWords, foundWords);
		}
		return foundWords;
	}
	
	public List<Triple<Integer,Integer,String>> simpleAndSearch(String[] titles, String[] words)
			throws IllegalArgumentException {
		List<Triple<Integer, Integer, String>> foundWords = new ArrayList<>();
		this.wordsToProcessCheck(words);
		boolean validTitles = this.validTitlesChecker(titles);
		int wordAmount = 0;
		String[] validWords = this.validWordChecker(words);
		
		for (int i = 0; i < validWords.length; i++) {
			if (validWords[i] != null) {
				wordAmount++;
			}
		}
		if (validTitles) {
			for (int j = 0; j < titles.length; j++) {
				foundWords = this.andInSection(wordAmount, titles[j], validWords, foundWords);
			}
		} else {
			foundWords = this.andInDocument(wordAmount, validWords, foundWords);
		}
		return foundWords;
	}




	public List<Triple<Integer,Integer,String>> simpleNotSearch(String[] titles, String[] wordsRequired, 
			String[] wordsExcluded) throws IllegalArgumentException {
		List<Triple<Integer, Integer, String>> foundWords = new ArrayList<>();
		this.wordsToProcessCheck(wordsRequired);
		boolean validTitles = this.validTitlesChecker(titles);
		String[] validWordsRequired = this.validWordChecker(wordsRequired);
		String[] validWordsExcluded = this.validWordChecker(wordsExcluded);
		CustomArrayList<String> doNotSearchArea = new CustomArrayList<>();
		
		if (validTitles) {
			for (int j = 0; j < titles.length; j++) {
				Triple<Integer, Integer, String> tripleNode = this.wordTrie.containsTitle(titles[j]);
				if (tripleNode != null) {
					int startLine = tripleNode.getLeftValue();
					int endLine = tripleNode.getCentreValue();
					for (int i = 0; i < validWordsExcluded.length; i++) {
						if (validWordsExcluded[i] != null) {
							Pair<Integer, Integer>[] occurrences = this.wordTrie.findWordPair(validWordsExcluded[i]);
							for (int p = 0; p < occurrences.length; p++) {
								if (occurrences[p] != null) {
									if (((occurrences[p].getLeftValue() >= startLine) &&
											occurrences[p].getLeftValue() <= endLine)) {
                                        doNotSearchArea.add(tripleNode.getRightValue());
									}
								}
							}
						}
					}
				}
			}
			int wordAmount = 0;
			for (int i = 0; i < validWordsRequired.length; i++) {
				if (validWordsRequired[i] != null) {
					wordAmount++;
				}
			}
			for (int i = 0; i < titles.length; i++){
			    if (doNotSearchArea.contains(titles[i])){
			        titles[i] = null;
                }
            }
			for (int j = 0; j < titles.length; j++) {
				foundWords = this.andInSection(wordAmount, titles[j], validWordsRequired, foundWords);
			}
			return foundWords;
		} else {
			int wordAmount = 0;
			for (int i = 0; i < validWordsRequired.length; i++) {
				if (validWordsRequired[i] != null) {
					wordAmount++;
				}
			}
			for (int i = 0; i < validWordsExcluded.length; i++)  {
				if (validWordsExcluded[i] != null) {
					Pair<Integer, Integer>[] occurrences = this.wordTrie.findWordPair(validWordsExcluded[i]);
					if (occurrences == null) {
						return foundWords;
					}
				}
			}
			foundWords =  this.andInDocument(wordAmount, validWordsRequired, foundWords);
		}
		return foundWords;
	}

    public List<Triple<Integer,Integer,String>> compoundAndOrSearch(String[] titles, String[] wordsRequired,
                                                                     String[] orWords)
            throws IllegalArgumentException {
        List<Triple<Integer,Integer,String>> foundAndOr = new ArrayList<>();
        this.wordsToProcessCheck(wordsRequired);
        boolean validTitles = this.validTitlesChecker(titles);
        String[] validAndWords = this.validWordChecker(wordsRequired);
        String[] validOrWord = this.validWordChecker(orWords);
        int wordAmount = 0;

        for (int i = 0; i < validAndWords.length; i++) {
            if (validAndWords[i] != null) {
                wordAmount++;
            }

        }

        if (validTitles){
            for (int j = 0; j < titles.length; j++){
                Triple<Integer, Integer, String> tripleNode = this.wordTrie.containsTitle(titles[j]);
                if (tripleNode != null) {
                    int startLine = tripleNode.getLeftValue();
                    int endLine = tripleNode.getCentreValue();
                    int inSection = 0;
                    CustomArrayList<String> foundAreas = new CustomArrayList<>();
                    for (int i = 0; i < validAndWords.length; i++) {
                        if (validAndWords[i] != null) {
                            Pair<Integer, Integer>[] occurrences = this.wordTrie.findWordPair(validAndWords[i]);
                            for (int p = 0; p < occurrences.length; p++) {
                                if (occurrences[p] != null) {
                                    if ((occurrences[p].getLeftValue() >= startLine) &&
                                            occurrences[p].getLeftValue() <= endLine) {
                                        if (!(foundAreas.contains(validAndWords[i]))) {
                                            foundAreas.add(validAndWords[i]);
                                            inSection++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (wordAmount == inSection) {
                        for (int i = 0; i < validOrWord.length; i++) {
                            if (validOrWord[i] != null) {
                                Pair<Integer, Integer>[] occurrences = this.wordTrie.findWordPair(validOrWord[i]);
                                for (int p = 0; p < occurrences.length; p++) {
                                    if (occurrences[p] != null) {
                                        if ((occurrences[p].getLeftValue() >= startLine) &&
                                                occurrences[p].getLeftValue() <= endLine) {
                                            Triple<Integer, Integer, String> instance = new Triple<Integer, Integer, String>(
                                                    occurrences[p].getLeftValue(), occurrences[p].getRightValue(), validOrWord[i]);
                                            foundAndOr.add(instance);
                                            foundAndOr = this.andInSection(wordAmount, titles[j], validAndWords, foundAndOr);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            int inSection = 0;
            CustomArrayList<String> foundAreas = new CustomArrayList<>();
            for (int i = 0; i < validAndWords.length; i++) {
                if (validAndWords[i] != null) {
                    Pair<Integer, Integer>[] occurrences = this.wordTrie.findWordPair(validAndWords[i]);
                    if (occurrences != null) {
                        if (!(foundAreas.contains(validAndWords[i]))) {
                            foundAreas.add(validAndWords[i]);
                            inSection++;
                        }
                    }
                }
            }
            if (wordAmount == inSection) {
                for (int i = 0; i < validOrWord.length; i++) {
                    if (validOrWord[i] != null) {
                        Pair<Integer, Integer>[] occurrences = this.wordTrie.findWordPair(validOrWord[i]);
                        for (int p = 0; p < occurrences.length; p++) {
                            if (occurrences[p] != null) {
                                Triple<Integer, Integer, String> instance = new Triple<Integer, Integer, String>(
                                        occurrences[p].getLeftValue(), occurrences[p].getRightValue(), validOrWord[i]);
                                foundAndOr.add(instance);
                                foundAndOr = this.andInDocument(wordAmount, validAndWords, foundAndOr);
                            }
                        }
                    }
                }
            }
        }
        return foundAndOr;
    }

	private List<Triple<Integer,Integer,String>> andInDocument(int wordAmount,String[] words,List<Triple<Integer,Integer,String>> foundWords ) {
		int inSection = 0;
		CustomArrayList<String> foundAreas = new CustomArrayList<>();
		for (int i = 0; i < words.length; i++) {
			if (words[i] != null) {
				Pair<Integer, Integer>[] occurrences = this.wordTrie.findWordPair(words[i]);
				if (occurrences != null) {
					if (!(foundAreas.contains(words[i]))) {
						foundAreas.add(words[i]);
						inSection++;
					}
				}
			}
		}
		if (wordAmount == inSection) {
			foundWords = this.addToFoundWords(words, foundWords);
		}
		return foundWords;
	}

	private  List<Triple<Integer,Integer,String>> addToFoundWords(String[] words,List<Triple<Integer,Integer,String>> foundWords ) {
		for (int i = 0; i < words.length; i++) {
			if (words[i] != null) {
				Pair<Integer, Integer>[] occurrences = this.wordTrie.findWordPair(words[i]);
				for (int p = 0; p < occurrences.length; p++) {
					if (occurrences[p] != null) {
						Triple<Integer, Integer, String> instance = new Triple<Integer, Integer, String>(
								occurrences[p].getLeftValue(), occurrences[p].getRightValue(), words[i]);
						foundWords.add(instance);
					}
				}
			}
		}
		return foundWords;
	}

	private List<Triple<Integer, Integer, String>> andInSection(int wordAmount, String title, String[] words, List<Triple<Integer, Integer, String>> foundWords) {
		Triple<Integer, Integer, String> tripleNode = this.wordTrie.containsTitle(title);
		if (tripleNode != null) {
			int startLine = tripleNode.getLeftValue();
			int endLine = tripleNode.getCentreValue();
			int inSection = 0;
			CustomArrayList<String> foundAreas = new CustomArrayList<>();
			for (int i = 0; i < words.length; i++) {
				if (words[i] != null) {
					Pair<Integer, Integer>[] occurrences = this.wordTrie.findWordPair(words[i]);
					for (int p = 0; p < occurrences.length; p++) {
						if (occurrences[p] != null) {
							if ((occurrences[p].getLeftValue() >= startLine) &&
									occurrences[p].getLeftValue() <= endLine) {
								if (!(foundAreas.contains(words[i]))) {
									foundAreas.add(words[i]);
									inSection++;
								}
							}
						}
					}
				}
			}
			if (wordAmount == inSection) {
				foundWords = this.findWordsInSection(startLine, endLine, words, foundWords);
			}
		}
		return foundWords;
	}

	private List<Triple<Integer, Integer, String>> findWordsInSection (int startLine, int endLine, String[] words,List<Triple<Integer, Integer, String>> foundWords ){
			for (int i = 0; i < words.length; i++) {
				if (words[i] != null) {
					Pair<Integer, Integer>[] occurrences = this.wordTrie.findWordPair(words[i]);
					for (int p = 0; p < occurrences.length; p++) {
						if (occurrences[p] != null) {
							if ((occurrences[p].getLeftValue() >= startLine) &&
									occurrences[p].getLeftValue() <= endLine) {
								Triple<Integer, Integer, String> instance = new Triple<Integer, Integer, String>(
										occurrences[p].getLeftValue(), occurrences[p].getRightValue(), words[i]);
								foundWords.add(instance);
							}
						}
					}
				}
			}
			return foundWords;
		}

	private void wordsToProcessCheck(String[] words) {
		if (words == null || words.length == 0) {
			throw new IllegalArgumentException();
		}
	}
	
	private boolean validTitlesChecker(String[] titles) {
		if (titles == null || this.indexData == null || titles.length == 0) {
			return false;
		}
		return true;
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
