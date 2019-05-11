package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
private static String compareLengths(String str1, String str2)
{
	if(str1.length() < str2.length()) {
		return str1;
	}
	else {
		return str2;
	}
}
private static boolean hascommonChar(String str1)
{
	for(int i = 0; i < str1.length() - 1; i++)
	{
		for(int j = str1.length() - 1; j >= 0; j--)
		{
			if(i != j && str1.charAt(i) == str1.charAt(j))
			{
				return true;
			}
		}
	}
	
	return false;
}
private static int commonChar(String str1, char lastLetter)
{	
	int skipIndex = -1;
	
	for(int i = 0; i < str1.length() - 1; i++ )
	{
		if(str1.charAt(i) == lastLetter)
		{
			skipIndex = i;
		}
	}
	
	if(skipIndex == -1)
	{
		return -1;
	}  
	
	return skipIndex;
	
}


private static short findendIndex(boolean isDuplicate, String wholeWord, String unlikeLetters) {
	short endFound = -1;
	short skip = -1;
if(isDuplicate) 
{
	skip = (short) commonChar(wholeWord, unlikeLetters.charAt( unlikeLetters.length() - 1 ) );
}	
if(isDuplicate && skip != -1)
{
	endFound = (short) wholeWord.indexOf(unlikeLetters.charAt(unlikeLetters.length() - 1), skip+1);
}
else 
{
	endFound = (short) wholeWord.indexOf(unlikeLetters.charAt(unlikeLetters.length() - 1)); 
}

return endFound;
}


public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
	
Indexes firstChildIndexes = new Indexes(0, (short) 0, (short) ((short) allWords[0].length() - 1)); // create indexes of first word in arr
TrieNode firstNode = new TrieNode(firstChildIndexes, null, null); // make a trienode
TrieNode root = new TrieNode(null, null, null); // empty root
String sub = "";	

				
for(int i = 0; i < allWords.length; i ++) // run the string array through a for loop
{				
	if(root.firstChild == null) // if the tree has no children (i.e tree is empty)
	{
	root.firstChild = firstNode;// make this node the firstChild of the tree
	}
			
	else {
				
	//first compare the current word to first child of root (actual root)
	boolean sibling = false;
	int startLetter = 0; // when we find similarities in letters at a ptr, we do not want to take this same letter into account for next ptr substr
	int count = -1;
	int index = 0;
				
	TrieNode prev = null; 
	TrieNode ptr = root.firstChild; // create pointer that points to first node in tree 
	
				
	while (ptr != null) {
					
	//figure out which one will be smaller to end loop -- sponge and spongebob
	String smaller = compareLengths(allWords[i].substring(startLetter), allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex+1));
	sub = "";
	count = -1;
	index = 0; 	
	while(index < smaller.length() && allWords[i].substring(startLetter).charAt(index) == allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex+1).charAt(index)) //check to find similar characters
	{
		sub += allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex).charAt(index); // if it is similar, put letter in this substring
		count++;
		index++;
	}
	
	if( index+startLetter < allWords[i].length() )
	{
		startLetter += index; // make startLetter equal to index where it wasnt the same char
	}
			
			
	if(sub.length() == 0) // if nothing in common
	{	sibling = true;
	
	if(prev!= null) {
		
		TrieNode checkEnd = prev;
		TrieNode updateStart = ptr;
		while((updateStart != null) && (checkEnd.substr.endIndex + 1 < updateStart.substr.startIndex))// if the diff between the end index of prev and start of curr is greater than one
		{
			updateStart.substr.startIndex = (short) (checkEnd.substr.endIndex + 1); // update start of all siblings
			updateStart = updateStart.sibling;
		}
	}
	
		prev = ptr;
		ptr = ptr.sibling; // go to next sibling, repeat process
	}
			
	if(sub.length() > 0 && sub.length() < allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex+1).length()) // if there were similar chars, but sub 
	{	
		sibling = false;
		
		if(count+1 < startLetter) {
		ptr.substr.endIndex = (short) (startLetter-1); // update end index of substring in TrieNode
		}
		else {
			ptr.substr.endIndex = (short) count;
		}
		
		if(ptr.substr.endIndex < ptr.substr.startIndex)
		{
			ptr.substr.endIndex = ptr.substr.startIndex;
			
			if((startLetter + 1) <=  (allWords[i].length() - 1))
			{
				startLetter++;
			}
		}
		
					
		prev = ptr; // prev points to node previous to ptr
		ptr = ptr.firstChild; // set ptr to subling
		
		if(prev!= null) {
			if (ptr!= null && prev.substr.endIndex + 1 < ptr.substr.startIndex) // if the diff between the end index of prev and start of curr is greater than one
			{
				short start = (short) (prev.substr.endIndex + 1);
				short end = (short) (ptr.substr.startIndex - 1);
				Indexes nodeInd = new Indexes(prev.substr.wordIndex, start, end);
				TrieNode nodeTrie = new TrieNode(nodeInd, null, null );
				nodeTrie.firstChild = ptr;
				prev.firstChild = nodeTrie; 
				ptr = prev.firstChild;
			}
		}
	}	 
		
		else if(sub.length() > 0 && sub.length() >= allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex+1).length()) //if sub is greater than or equal to the substring at ptr in length
		{
			sibling = false; // it is not a sibling, 
			prev = ptr;
			ptr = ptr.firstChild; // go to the next child
		}				
	}
	
	short start = 0;
	short end = 0;
	boolean duplicate = false;
				
if(sibling) {
	
	String word = allWords[i].substring(startLetter);
	duplicate = hascommonChar(allWords[i]); // check for dupilcate letters 
	start = (short) prev.substr.startIndex; 

	if(duplicate)
	{
		end = findendIndex(true, allWords[i], word);
	}
	else {
		end = findendIndex(false, allWords[i], word);
	}
	
	Indexes unlikeInd1  = new Indexes(i, start, end);
	TrieNode unlikeSub1 = new TrieNode(unlikeInd1, null, null);
	prev.sibling = unlikeSub1;
}

else {
	
	if(prev.firstChild == null) // if there is only one node in the tree, we have to make unlike substr of prev first child before curr word
	{	 
		String unlike =  allWords[prev.substr.wordIndex].substring(prev.substr.startIndex).substring(sub.length());
		
		if(unlike.length() > 0) 
		{
			duplicate = hascommonChar(allWords[prev.substr.wordIndex]); // check for dupilcate letters 
			start = (short) (prev.substr.endIndex+1); 

			if(duplicate)
			{
				end = findendIndex(true, allWords[prev.substr.wordIndex], unlike);
			}
			else {
				end = findendIndex(false, allWords[prev.substr.wordIndex], unlike);
			}
			
			
			Indexes unlikeInd  = new Indexes(prev.substr.wordIndex, start, end);
			TrieNode unlikeSub = new TrieNode(unlikeInd, null, null);
			prev.firstChild = unlikeSub;
			prev = prev.firstChild;
			
			unlike = allWords[i].substring(startLetter);
			duplicate = hascommonChar(allWords[i]);
			start = (short) prev.substr.startIndex; 

			if(duplicate)
			{
				end = findendIndex(true, allWords[i], unlike);
			}
			else {
				end = findendIndex(false, allWords[i], unlike);
			}
			
			Indexes unlikeInd1  = new Indexes(i, start, end);
			TrieNode unlikeSub1 = new TrieNode(unlikeInd1, null, null);
			prev.sibling = unlikeSub1;
		}
	} else {

	String unlike = allWords[i].substring(startLetter).substring(sub.length());
	
	if(unlike.length() > 0 ) {
		
	start = (short) prev.substr.endIndex; 

	if(duplicate)
	{
		end = findendIndex(true, allWords[i], unlike);
	}
	else {
		end = findendIndex(false, allWords[i], unlike);
	}
	
	
	Indexes unlikeInd1  = new Indexes(i, start, end);
	TrieNode unlikeSub1 = new TrieNode(unlikeInd1, null, null);
	prev.firstChild = unlikeSub1;
}
}
}
				
}
		
}


	return root;
		
}
	
	

	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
private static ArrayList<TrieNode> findLeafNodes(ArrayList<TrieNode> addleafNodes, TrieNode root,  boolean isRoot ){
	
	while(root!= null)
	{	
		if(root.firstChild != null) {
			root = root.firstChild;
			isRoot = false;
			findLeafNodes(addleafNodes, root, isRoot);
			
		}
		else {
			
			if(!addleafNodes.contains(root)) {
			addleafNodes.add(root);
			}
		}
		if(!isRoot) {
		root = root.sibling;
		}
		else { root = root.firstChild; }
	}
	
	return addleafNodes;
	
}

	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/

		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		ArrayList<TrieNode> prefixWords = new ArrayList<TrieNode>();
		TrieNode prefixNode = root.firstChild;
		int startLetter = 0;
		int index = 0;
		String sub = "";
		String prefixMatcher = "";
		boolean istheRoot = true;

		while (prefixNode != null) { // find prefix
			
			sub = "";
			index = 0;
			String smaller = compareLengths(allWords[prefixNode.substr.wordIndex].substring(prefixNode.substr.startIndex, prefixNode.substr.endIndex + 1), prefix.substring(startLetter));
			while( (index < smaller.length()) && ( allWords[prefixNode.substr.wordIndex].substring(prefixNode.substr.startIndex, prefixNode.substr.endIndex + 1).charAt(index) == prefix.substring(startLetter).charAt(index)) ) //check to find similar characters
			{
				sub += prefix.substring(startLetter).charAt(index); // if it is similar, put letter in this substring
				index++;
			}
			prefixMatcher+= sub;
			if(startLetter+index < prefix.length()) {
			startLetter += index;
			}
			if(sub.length() == 0)
			{
				prefixNode = prefixNode.sibling;
			}
			if( sub.length() > 0 && prefixMatcher.length() < prefix.length() )  
			{
				prefixNode = prefixNode.firstChild;
			}
			if(sub.length() > 0 && prefixMatcher.length() == prefix.length()) {
					break;
				} 
			} 

		prefixWords = findLeafNodes(prefixWords, prefixNode, istheRoot);
		
		if(prefixWords.isEmpty())
		{
			return null;
		}

		return prefixWords;
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
