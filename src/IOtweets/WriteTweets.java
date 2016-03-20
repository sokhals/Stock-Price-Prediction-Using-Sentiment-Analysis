/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IOtweets;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
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
            OutputStream file = new FileOutputStream("inputFiles/"+fileName+".txt");
            System.out.println(file.toString());
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutputStream output = new ObjectOutputStream(buffer);
            output.writeObject(map);
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
