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
import java.io.FileWriter;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.io.BufferedWriter;

/**
 *
 * @author surindersokhal
 */
public class RemoveStopWords {
    
    static boolean CHECK_READ_FILE=false;
    static HashSet<String> stopList = new HashSet<String>();
    
    public static void readStopWordList(){
        try{
            BufferedReader bf=new BufferedReader(new FileReader(new File("inputFiles/stopWords.txt")));
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
        	String line = "";
        	int counter = 0;
        	BufferedReader br = new BufferedReader(new FileReader(new File("inputFiles/"+fileName+".csv")));
        	BufferedWriter writer = new BufferedWriter(new FileWriter(new File("outputFiles/"+fileName+".csv")));
        	Map<String,String[]> tweetMap = new LinkedHashMap<String,String[]>();
        	int p,n,neg=0;
        	while((line = br.readLine())!=null)
        	{
        		if(counter == 0)
        			System.out.println("Skip Header");
        		else
        		{
	        		String[] parts = line.split(",");
	        		String tweet = parts[2].replace("\"", "");
	        		tweet = cleanData(tweet).trim();		
	        		if(tweetMap.containsKey(tweet))
	        		{
	        			String[] valParts = tweetMap.get(tweet);
        				valParts[3] = String.valueOf(Integer.parseInt(valParts[3]) + 1);
        				tweetMap.put(tweet, valParts);
        			}	
	        		else
	        		{
	        			tweetMap.put(tweet, parts);
	        		}
        		}
                counter++;
        	}
        	
        	for(Map.Entry<String,String[]> entry : tweetMap.entrySet())
        	{
        		String[] parts = entry.getValue();
        		String key = entry.getKey();
        		writer.write(parts[0]+","+parts[1]+","+"\""+key.trim()+"\""+","+parts[3]+","+parts[4]+","+parts[5]+"\n");
        	}
        	
        	System.out.println("Writing done");
        	br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static String cleanData(String data){
    	String tweet=data;
        String split[]=tweet.split(" ");
        String[] tokens=containStopWords(split);
        tokens=Stemming.performStemming(tokens);
        /*for(int i=0;i<tokens.length;i++)
        	System.out.println(tokens[i]);	
    	System.out.println("\n");*/
        data=buildString(tokens);
        
        return data;
    }
    
    public static String buildString(String[] tokens){
    	StringBuilder build=new StringBuilder();
    	for(int i=0;i<tokens.length-1;i++){
    		build.append(tokens[i]+" ");
    	}
    	build.append(tokens[tokens.length-1]);
    	return build.toString();
    }
    
    public static void main(String args[])
    {
    	readStopWordList();
    	RemoveStopWords.reaDataFile("sentimentAnalysisInput");
    }

}

