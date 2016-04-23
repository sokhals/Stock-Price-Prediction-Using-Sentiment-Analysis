/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IOtweets;

import java.util.ArrayList;

/**
 *
 * @author surindersokhal
 */
public class TweetData implements java.io.Serializable {
    
    public String Time;
    public String Tweet;
    public int ReTweetCount;
    public String sentiment="";
    public String companyName="";
    public ArrayList<TaggedValues> tagedList=null;
    
    public class TaggedValues{
    	public String tag;
    	public String value;
    }
}
 
