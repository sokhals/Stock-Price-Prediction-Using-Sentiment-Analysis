package data_preprocessing;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
}
