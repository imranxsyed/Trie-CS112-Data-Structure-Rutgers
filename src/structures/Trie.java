package structures;

import java.util.ArrayList;

/**
 * confirmation This class implements a compressed trie. Each node of the tree is a CompressedTrieNode, with fields for
 * indexes, first child and sibling.
 * 
 * @author Sesh Venugopal
 *modifiedConfirmation
 */
public class Trie {
	
	/**
	 * Words indexed by this trie.
	 */
	ArrayList<String> words;
	
	/**
	 * Root node of this trie.
	 */
	TrieNode root;
	
	/**
	 * Initializes a compressed trie with words to be indexed, and root node set to
	 * null fields.
	 * 
	 * @param words
	 */
	public Trie() {
		root = new TrieNode(null, null, null);
		words = new ArrayList<String>();
	}
	
	/**
	 * Inserts a word into this trie. Converts to lower case before adding.
	 * The word is first added to the words array list, then inserted into the trie.
	 * 
	 * @param word Word to be inserted.
	 */
	public void insertWord(String word) {
		word= word.toLowerCase();
		if (word.length()<2){
			root = addRear(root,word,-1);
			return;
		}
		internalNodes(root,word,-1);
	}
	/**
	 * all helper methods for insertWord method
	 * @param root
	 * @param input
	 * @param x
	 */
	private void internalNodes(TrieNode root, String input, int x) {
		boolean check =false;
//checks the roots
		if (root.firstChild == null) {
			// words.add(input);

			root = root.substr == null || root.firstChild == null ?addRear(root, input, -1)
					: addRear(root, input, root.substr.endIndex);

			return;
		}

		TrieNode ptr = root.firstChild;

		TrieNode internal = null;

		TrieNode internalPrev = null;

		TrieNode prev = root;

		int max = x;

		String temp = "";

		while (ptr != null) {

			temp = words.get(ptr.substr.wordIndex);

			if (ptr.firstChild == null) {

				if (temp.charAt(0) == input.charAt(0)) {

					int track = indexToBeInserted(temp, input, 0);

					if (track > x) {
						max = track;
						internalPrev = prev;

						internal = ptr;
					}
				}

			}

			else if (ptr.substr.endIndex < input.length() - 1 && ptr.substr.endIndex > max
					&& temp.substring(0, ptr.substr.endIndex + 1).equals(input.substring(0, ptr.substr.endIndex + 1))) {
				check = true;
				
				internalPrev = prev;
				
				internal = ptr;

				max = ptr.substr.endIndex;

			} else  if(ptr.firstChild!=null && temp.charAt(0)== input.charAt(0)){
				
				int track1= indexToBeInserted(temp,input,0);
				
				if (track1>x){
					
					internalPrev = prev;
					
					internal = ptr;
					max= track1;
				}
			} 
			prev = ptr;
			ptr = ptr.sibling;
		} 

		if (internal == null) {
			
			root = root.substr == null || root.firstChild == null ? addRear(root, input, -1)
					
					: addRear(root, input, root.substr.endIndex);

			return;

		}
		else if (internal.firstChild == null) {
			
			temp = words.get(internal.substr.wordIndex);
			words.add(input);
			int sameIndex = internal.substr.wordIndex;
			
			Indexes toInsert = new Indexes(sameIndex, (short) (max + 1), (short) (temp.length() - 1));
			int indexOfNewChild = words.indexOf(input);
			
			Indexes rightChild = new Indexes(indexOfNewChild, (short) (max + 1), (short) (input.length() - 1));
			internal.firstChild = new TrieNode(toInsert, null, new TrieNode(rightChild, null, null));
			toInsert = new Indexes(sameIndex, (short) (x + 1), (short) (max));
			
			if ( internalPrev.firstChild!=null&&internalPrev.firstChild.equals( internal)) {

				internalPrev.firstChild = new TrieNode(toInsert, internal.firstChild, internal.sibling);
			} else {

				internalPrev.sibling = new TrieNode(toInsert, internal.firstChild, internal.sibling);
			}

			
		}else if(internal.firstChild!= null && internal.substr != null && (check!=true)){
			
			words.add(input);
			Indexes indexes = new Indexes(internal.substr.wordIndex,(short)(internal.substr.startIndex),(short)(max));
			internal.substr.startIndex= (short)(max+1);
			TrieNode sibling = new TrieNode(new Indexes(words.indexOf(input),(short)(max+1),(short)(input.length()-1)),null,null);
			
			
			if (internalPrev.firstChild!=null&&internalPrev.firstChild.equals(internal)){
			
			TrieNode firstChild= internalPrev.firstChild;
			TrieNode toInsert = new TrieNode(indexes,firstChild,internal.sibling);
			internalPrev.firstChild = toInsert;
			internalPrev.firstChild.firstChild.sibling= sibling;
			
			}else{
				
				TrieNode firstChild= internalPrev.sibling;
				TrieNode toInsert = new TrieNode(indexes,firstChild,internal.sibling);
				internalPrev.sibling= toInsert;
				internalPrev.sibling.firstChild.sibling= sibling;
				
			}
		}
		else {
			internalNodes(internal, input, max);
		}
		return;

	}
private  int indexToBeInserted(String compareTo, String input, int index) {
		
		int i;

		for ( i = index + 1; i < compareTo.length()  && i < input.length(); i++) {

			if (compareTo.charAt(i) != input.charAt(i)) {

				break;
			}

		}
		if (i== input.length() || i ==compareTo.length()){
			
			return i-2;
		}
		return i-1;

	}
private TrieNode addRear(TrieNode root, String input,int index){
	
	if (root.firstChild== null){
		
		words.add(input);
		
		Indexes toInsert= new Indexes(0,(short)(index+1),(short)(input.length()-1));
		
		root.firstChild= new TrieNode(toInsert,null,null);
		
		return root;
	}
	
	TrieNode ptr= root.firstChild;
	TrieNode prev = null;
	
	while(ptr != null){
		
		prev = ptr;
		ptr = ptr.sibling;
	}
	words.add (input);
	
	Indexes toInsert= new Indexes(words.indexOf(input),(short)(index+1),(short)(input.length()-1));
	
	prev.sibling= new TrieNode(toInsert,null,null);
	
	return root;
	
}
	
	
	
	/**
	 * Given a string prefix, returns its "completion list", i.e. all the words in the trie
	 * that start with this prefix. For instance, if the tree had the words bear, bull, stock, and bell,
	 * the completion list for prefix "b" would be bear, bull, and bell; for prefix "be" would be
	 * bear and bell; and for prefix "bell" would be bell. (The last example shows that a prefix can be
	 * an entire word.) The order of returned words DOES NOT MATTER. So, if the list contains bear and
	 * bell, the returned list can be either [bear,bell] or [bell,bear]
	 * 
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all words in tree that start with the prefix, order of words in list does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public ArrayList<String> completionList(String prefix) {
		/** COMPLETE THIS METHOD **/
		
		ArrayList<String> output = new ArrayList<>();
		
			if (root.firstChild==null){
				
				return output;
			}
			
			TrieNode ptr = this.root.firstChild;
			
			while(ptr !=null){
			
				String value = words.get(ptr.substr.wordIndex);
				
				if (value.charAt(0)== prefix.charAt(0)){
					
					if (value.contains(prefix)){
						if (ptr.firstChild==null &&( prefix.length()<=value.length()&&prefix.equals(value.substring(0,prefix.length())))){
							output.add(value);
							return output;
						}else{
							output = getWordsFromPrfx(ptr.firstChild,output,prefix);
							
							return output;
						}
					}
					output = getWordsFromPrfx(ptr.firstChild,output,prefix);
				}
				ptr = ptr.sibling;
			}
		return output;
	}

	//helper method for completionList
	
	private ArrayList<String> getWordsFromPrfx(TrieNode input, ArrayList<String> arrL,String prefix){
		
		String value = words.get(input.substr.wordIndex);
		
		if (input.firstChild==null && ( prefix.length()<=value.length()&&prefix.equals(value.substring(0,prefix.length())))){
			
			arrL.add(words.get(input.substr.wordIndex));
			
			if (input.sibling!=null){
				
				arrL=	getWordsFromPrfx(input.sibling,arrL,prefix);
				}
			return arrL;
			
		} else if(input.firstChild!=null){
			
			arrL = getWordsFromPrfx(input.firstChild,arrL,prefix);
		}if  (input.sibling!=null){
			
			arrL = getWordsFromPrfx(input.sibling,arrL,prefix);
		}
		return arrL;
	}
	
	
	public void print() {
		print(root, 1, words);
	}
	
	private static void print(TrieNode root, int indent, ArrayList<String> words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			System.out.println("      " + words.get(root.substr.wordIndex));
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		System.out.println("(" + root.substr + ")");
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
