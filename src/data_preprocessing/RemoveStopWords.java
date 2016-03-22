package data_preprocessing;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import IOtweets.TweetData;
import IOtweets.WriteTweets;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author surindersokhal
 */
public class RemoveStopWords {
    
    static boolean CHECK_READ_FILE=false;
    static HashSet<String> stopList=null;
    public static void readStopWordList(){
        try{
            BufferedReader bf=new BufferedReader(new FileReader(new File("../../inputFiles/stopWords.txt")));
            String read="";
            while((read=bf.readLine())!=null){
                stopList.add(read.trim().toLowerCase());
            }
            bf.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static String[] containStopWords(String [] words){
        HashSet<String> set=new HashSet<String>();
        for(int i=0;i<words.length;i++){
            if(!stopList.contains(words[i].trim().toLowerCase()))
                set.add(words[i]);
        }
        return set.toArray(new String[set.size()]);
    }
    
    public static void reaDataFile(String fileName){
        try{
            InputStream file = new FileInputStream("inputFiles/"+fileName+".txt");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream (buffer);
            HashMap<String,TweetData> data=(HashMap<String,TweetData>)input.readObject();
            System.out.println("Filtering data");
            for(String userID:data.keySet()){
                TweetData tweet=data.get(userID);
                tweet=cleanData(tweet);
                data.put(userID, tweet);
            }
            System.out.println("Done Filtering data");
            System.out.println("Writing data");
            WriteTweets.writeTweets(data, fileName);
            System.out.println("Writing done");
            input.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static TweetData cleanData(TweetData data){
    	String tweet=data.Tweet;
        String split[]=tweet.split(" ");
        String[] tokens=containStopWords(split);
        tokens=Stemming.performStemming(tokens);
        tokens=CleaningOOVWords.cleanOOVWords(tokens);
        data.Tweet=buildString(tokens);
        data=POSTagging.POSTagged(data);
        
        return data;
    }
    public static String buildString(String[] tokens){
    	StringBuilder build=new StringBuilder();
    	for(int i=0;i<tokens.length;i++){
    		build.append(tokens[i]+" ");
    	}
    	return build.toString();
    }
}
