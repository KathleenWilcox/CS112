package lse;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class LSEDriver {

	static Scanner sc = new Scanner(System.in);
	static LittleSearchEngine lse = new LittleSearchEngine();
	
	
	public static void main(String[] args) throws FileNotFoundException {
		// Initialize the lse with the docsfile and the noisewords file
		System.out.println("Enter name of the docs file:");
		String docsfile = sc.next();
		System.out.println("Enter name of the noise words file:");
		String noisefile = sc.next();
		
		lse.makeIndex(docsfile, noisefile);
		
		System.out.println("Enter name of file to load from:");
		String sfile = sc.next();
		HashMap<String, Occurrence> rmap = lse.loadKeywordsFromDocument(sfile);
		
		for(Map.Entry<String, Occurrence> k : rmap.entrySet()) {
			System.out.println("Key: "+k.getKey()+" | Value: "+k.getValue());
		}
		
		System.out.println("Please choose a method:");
		System.out.println("(1) getKeyWord  (2) top5search (3) mergeKeyWords ");
		int num = sc.nextInt();
		
	while(num >= 1 && num <= 4) {
		
		// take number from 1 to 5
		// if not a number from 1 to 5, keep asking for valid input. or just quit lol
		
		if(num == 1) {
			
			System.out.println("Please type a word: ");
			String word = sc.next();
			System.out.println("RESULT: " + lse.getKeyword(word)); 
			
		}
		if(num == 2)
		{
			System.out.println("Input word 1: ");
			String first = sc.next();	
			
			System.out.println("Input word 2: ");
			String second = sc.next();
			
			ArrayList<String> result = lse.top5search(first, second);
			for(String item: result)
			{
				System.out.println(item);
			}
		}
	/*
		if(num == 3)
		{
			System.out.println("Enter a document name");
			String file = sc.next();
			
			HashMap<String, Occurrence> resultmap = lse.loadKeywordsFromDocument(file);
			
			for(Map.Entry<String, Occurrence> k : resultmap.entrySet()) {
				System.out.println("Key: "+k.getKey()+" | Value: "+k.getValue());
			}
		}
		*/
		
		if(num == 3)
		{
			System.out.println("Enter a document name");
			String file = sc.next();
			
			HashMap<String, Occurrence> resultmap = lse.loadKeywordsFromDocument(file);
			
			lse.mergeKeywords(resultmap);
			
			for(Map.Entry<String,ArrayList<Occurrence>> k : lse.keywordsIndex.entrySet()) {
				System.out.println("Key: "+k.getKey()+" | Value: "+ k.getValue());
			}

		}
		
		System.out.println("Please choose a method:");
		System.out.println("(1) getKeyWord  (2) top5search (3) mergeKeyWords ");
		num = sc.nextInt();
	}
	}
}


