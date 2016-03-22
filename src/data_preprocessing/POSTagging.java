package data_preprocessing;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class POSTagging {

	public static String POSTagged()
	{
		String a = "I love my country";	
		MaxentTagger tagger =  new MaxentTagger("/models/english-left3words-distsim.tagger");
		String tagged = tagger.tagString(a);
		System.out.println(tagged);
		return tagged;
	}

}