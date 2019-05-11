package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
		
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/

		HashMap<String, Occurrence> keywordsfromDoc = new HashMap<>();

		if (docFile == null) {
			throw new FileNotFoundException();
		}

		Scanner sc = new Scanner(new File(docFile));

		while (sc.hasNext()) {

			String word = sc.next();

			word = getKeyword(word);

			if (word != null) {

				if (keywordsfromDoc.containsKey(word)) {
					Occurrence T = keywordsfromDoc.get(word);
					T.frequency++;

				} else {
					Occurrence T = new Occurrence(docFile, 1);
					keywordsfromDoc.put(word, T);
				}

			}
		}
		return keywordsfromDoc;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		
		if(kws == null)
		{
			return;
		}

		for (String s : kws.keySet()) {
			Occurrence value = kws.get(s);
			if (keywordsIndex.containsKey(s)) {
				ArrayList<Occurrence> occurOfWords = keywordsIndex.get(s);
				occurOfWords.add(value);
				insertLastOccurrence(occurOfWords);
			} else {
				ArrayList<Occurrence> newList = new ArrayList<>();
				keywordsIndex.put(s, newList);
				newList.add(value);

			}
		}
		
		
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code

		if (word == null) {
			return null;
		}

		word = word.replaceFirst("^[.,?:;!]+", "");
		word = word.replaceAll("[.,?:;!]+$", "");

		for (int i = 0; i < word.length(); i++) {
			if (!Character.isLetter(word.charAt(i))) // if there are any chars in the word that are not letters
			{
				return null; // it is not a valid word
			}
		}

		word = word.toLowerCase();

		if (noiseWords.contains(word)) { // if this word is in the noise words list
			return null; // it is not a keyword, null is returned
		}
		

		return word; // return the word if it passed all tests
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		if (occs == null) {
			return null;
		}

		ArrayList<Integer> midpoints = new ArrayList<>();
		int lo = 0;
		int hi = occs.size() - 2;
		int mid = 0;

		while (lo < hi) {
			mid = lo + hi / 2;
			midpoints.add(mid);

			if (occs.get(mid).frequency == occs.get(occs.size() - 1).frequency) {
				break;
			}

			if (occs.get(mid).frequency < occs.get(occs.size() - 1).frequency) {
				hi = mid - 1;
			}

			if (occs.get(mid).frequency >= occs.get(occs.size() - 1).frequency) {
				lo = mid + 1;
			}
		}

		if (occs.get(mid).frequency < occs.get(occs.size() - 1).frequency) { // must check for the case of two things
																				// being in the list already in order
			Occurrence temp = occs.get(occs.size() - 1);
			occs.remove(occs.get(occs.size() - 1));
			occs.add(mid, temp);
		}

		return midpoints;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, returns null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		if (kw1 == null && kw2 == null) {
			return null;
		}

		if (!keywordsIndex.containsKey(kw1) && !keywordsIndex.containsKey(kw2)) {
			return null;
		}

		ArrayList<Occurrence> listOne = keywordsIndex.get(kw1);
		ArrayList<Occurrence> listTwo = keywordsIndex.get(kw2);
		ArrayList<String> top5Result = new ArrayList<>();

		if (kw1 == null && kw2 != null) {
			if (keywordsIndex.containsKey(kw2)) {
				int j = 0;
				while (j < listTwo.size()) {
					top5Result.add(listTwo.get(j).document);
					j++;
				}
			}
		}

		if (kw1 != null && kw2 == null) {
			if (keywordsIndex.containsKey(kw1)) {
				int i = 0;
				while (i < listOne.size()) {
					top5Result.add(listOne.get(i).document);
					i++;
				}
			}
		}

		if (keywordsIndex.containsKey(kw1) && !keywordsIndex.containsKey(kw2)) {
			int i = 0;
			while (i < listOne.size()) {
				top5Result.add(listOne.get(i).document);
				i++;
			}

		}

		if (!keywordsIndex.containsKey(kw1) && keywordsIndex.containsKey(kw2)) {
			int j = 0;
			while (j < listTwo.size()) {
				top5Result.add(listTwo.get(j).document);
				j++;
			}

		}

		else {

			int i = 0;
			int j = 0;

			while ((i < listOne.size() && j < listTwo.size())) {
				if (i == listOne.size() - 1 && j != listTwo.size() - 1) // if i is at the last element and j is not
				{
					if (listOne.get(i).frequency >= listTwo.get(i).frequency) {
						top5Result.add(listOne.get(i).document);
						i++;
					}

					int count = j;
					while (count < listTwo.size()) {
						top5Result.add(listTwo.get(count).document);
						count++;
					}

					j = count;
				}

				else if (j == listTwo.size() - 1 && i != listOne.size() - 1) // if j is at the last element and i is not
				{
					if (listTwo.get(j).frequency > listOne.get(i).frequency) {
						top5Result.add(listTwo.get(j).document);
						j++;
					}

					int count = i;
					while (count < listOne.size()) {
						top5Result.add(listOne.get(count).document);
						count++;
					}

					i = count;

				}

				else { // if they are both not at the last elements

					if (listOne.get(i).frequency >= listTwo.get(j).frequency) {
						top5Result.add(listOne.get(i).document);
						i++;
					}

					if (listOne.get(i).frequency < listTwo.get(j).frequency) {
						top5Result.add(listTwo.get(j).document);
						j++;
					}
				}
			}

		}
		while (top5Result.size() > 5) {
			top5Result.remove(top5Result.get(top5Result.size() - 1));
		}
		for (int i = 0; i < top5Result.size(); i++) {
			for (int k = top5Result.size() - 1; k >= 0; k--) {
				if (top5Result.get(i).equals(top5Result.get(k)) && i != k) {
					top5Result.remove(top5Result.get(k));
				}
			}
		}

		return top5Result;
	
	}
}
