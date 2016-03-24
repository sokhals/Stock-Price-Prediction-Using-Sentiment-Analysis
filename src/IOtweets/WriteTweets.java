/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IOtweets;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

/**
 *
 * @author surindersokhal
 */
public class WriteTweets {

    public static void writeTweets(HashMap<String, TweetData> map,String fileName) {
        try {
            File file = new File("inputFiles/"+fileName+".csv");
            BufferedWriter writer=new BufferedWriter(new FileWriter(file,true));
            writer.write("UserID,Time,Tweet,Retweet_Count");
            for(String userID:map.keySet()){
            	TweetData data=map.get(userID);
            	writer.write("\n"+userID+","+data.Time+","+data.Tweet+","+data.ReTweetCount);
            	
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
