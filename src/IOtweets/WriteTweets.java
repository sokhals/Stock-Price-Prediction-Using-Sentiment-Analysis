/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IOtweets;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author surindersokhal
 */
public class WriteTweets {

	static HashSet<String> pos=new HashSet<String>();
	static HashSet<String> neg=new HashSet<String>();
	static HashSet<String> negation=new HashSet<String>();
	
	public static void readData(BufferedReader bf, HashSet<String> set) throws IOException{
		String read="";
		 HashSet<String> temp=new HashSet<String>();
		while((read=bf.readLine())!=null){
			temp.addAll(Arrays.asList(read.split(";")));
		}
	
		Iterator<String> iterator = temp.iterator();
	    while (iterator.hasNext())
	    {
	        set.add(iterator.next().toLowerCase().trim());
	    }
	}
	
    public static void writeTweets(HashMap<String, TweetData> map,String fileName,boolean isCleanedProcessed) {
        try {
            File file = new File(fileName+".csv");
            BufferedWriter writer=new BufferedWriter(new FileWriter(file,true));
            
            if(!isCleanedProcessed)
            	writer.write("UserID;Time;Tweet;Retweet_Count;sentiment");
            else
            	writer.write("UserID;Time;Tweet;Retweet_Count;Company_Name;sentiment");
            
            for(String userID:map.keySet()){
            	TweetData data=map.get(userID);
            	String text=data.Tweet;
            	//int val=getSentiment(text);
            	if(!isCleanedProcessed)
            		{
            		writer.write("\n"+userID+";"+data.Time+";"+data.Tweet.replaceAll("\"","").replaceAll(";", "")+";"+data.ReTweetCount+";"+"0");
            		}
            	else
                	writer.write("\n"+userID+";"+data.Time+";"+data.Tweet.replaceAll("\"","")+";"+data.ReTweetCount+";"+data.companyName+";"+data.sentiment);
            }
            
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public static int getSentiment(String tweet){
    	
    	String split[]=tweet.toLowerCase().split(" ");
    	boolean check=false;
    	boolean k=false;
    	for(int i=0;i<split.length;i++){
    		if(pos.contains(split[i].trim())){
    			check=true;
    			k=true;
    			break;
    			
    		}
    	}
    	
    	for(int i=0;i<split.length;i++){
    		if(neg.contains(split[i].trim())){
    			check=false;
    			k=true;
    			break;
    		}
    	}
    	
    	for(int i=0;i<split.length;i++){
    		if(pos.contains(split[i].trim())){
    			check=!check;
    			
    		}
    	}
    	if(k){
    		return check==false?-1:1;
    	}
    	return 0;
    }

}
