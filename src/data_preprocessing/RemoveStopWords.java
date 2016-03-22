package data_preprocessing;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import IOtweets.TweetData;
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
    
    public static void reaDataFile(){
        try{
            InputStream file = new FileInputStream("inputFiles/output_priyanka.txt");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream (buffer);
            HashMap<String,TweetData> data=(HashMap<String,TweetData>)input.readObject();
            for(String userID:data.keySet()){
                TweetData tweet=data.get(userID);
                String text=tweet.Tweet;
                String cleanedTweet[]=cleanData(text);
                
            }
            input.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static String[] cleanData(String tweet){
        String split[]=tweet.split(" ");
        String[] tokens=containStopWords(split);
        tokens=Stemming.performStemming(tokens);
        tokens=CleaningOOVWords.cleanOOVWords(tokens);
        return tokens;
    }
}
