import java.util.*;

public class nGrams {
	 public static List<List<String>> ngrams(int n, HashMap<Integer,String> str) {
	        List<List<String>> ngrams = new ArrayList<List<String>>();
	        for (int i = 0; i < str.size() - n + 1; i++){
	        	List<String> gram = new ArrayList<String>();
	        	for(int j=0; j<n; j++)
	        		gram.add(str.get(i+j));
	        	ngrams.add(gram);
	        	}
	        return ngrams;
	    }

	    public static void main(String[] args) {
	    	HashMap<Integer, String> myMap = new HashMap<Integer, String>();
	    	myMap.put(0, "This");
	    	myMap.put(1, "is");
	    	myMap.put(2, "a");
	    	myMap.put(3, "bird");
	    	myMap.put(4, "That");
	    	myMap.put(5, "cant");
	    	myMap.put(6, "fly");
	    	//change the value of n to get the no.of ngrams you want
	    	int n = 3;
	    		List<List<String>> ngram = ngrams(n, myMap);
	                System.out.println(ngram);
	    }
}
