package data_preprocessing;

import java.util.ArrayList;

import IOtweets.TweetData;
import IOtweets.TweetData.TaggedValues;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class POSTagging {

	public static TweetData POSTagged(TweetData data)
	{	ArrayList<TaggedValues> list=new ArrayList<TaggedValues>();
		String tweet=data.Tweet;
		MaxentTagger tagger =  new MaxentTagger("/models/english-left3words-distsim.tagger");
		
		String tagged = tagger.tagString(tweet);
		
		String array[]=tagged.split(" ");
		for(int i=0;i<array.length;i++){
			TaggedValues tv= data.new TaggedValues();
			tv.tag=array[i].split("_")[1];
			tv.value=array[i].split("_")[0];
			list.add(tv);
		}
		data.tagedList=list;
		return data;
	}

}

