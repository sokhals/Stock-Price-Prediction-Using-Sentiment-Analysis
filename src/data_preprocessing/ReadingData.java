/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_preprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import org.json.JSONObject;

/**
 *
 * @author surindersokhal
 */
public class ReadingData {

    /**
     * 
     * @param fileName
     * @param company
     * @param companyID
     * @throws IOException 
     */
    public void readData(String fileName, String company, String companyID) throws IOException {
        String read = "";
        int count = 0;
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        BufferedReader bf = new BufferedReader(new FileReader(new File("/Users/surindersokhal/Documents/output_priyanka.json")));
        JSONObject jsonObject = null;
        while ((read = bf.readLine()) != null) {
            if (read.trim().length() > 10) {
                try {
                    jsonObject = new JSONObject(read);
                    String text = jsonObject.getString("text");
                    if(jsonObject.has("retweeted_status"))
                        getRetweetCount(jsonObject, map);
                    if ((text.contains("stock") &&text.contains(companyID)) || (text.contains("stock") && text.contains(company)) || text.contains("stocks")) {
                        System.out.println(text);
                    }
                } catch (Exception e) {

                }
            }
        }
        System.out.println(count);

    }

    /**
     * 
     * @param jsonObject
     * @param map 
     */
    public void getRetweetCount(JSONObject jsonObject, HashMap<String, Integer> map) {
        try {
            JSONObject arr = jsonObject.getJSONObject("retweeted_status");
            int retweetCount = arr.getInt("retweet_count");
            JSONObject id = arr.getJSONObject("user");
            String user_ID = id.getString("id_str");
            fillMap(map, retweetCount, user_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param map
     * @param count
     * @param id 
     */
    public void fillMap(HashMap<String, Integer> map, int count, String id) {
        if (map.containsKey(id)) {
            count = Math.max(count, map.get(id));
        }
        map.put(id, count);
    }

    /**
     * 
     * @param arg
     * @throws IOException 
     */
    public static void main(String arg[]) throws IOException {
        Scanner sc = new Scanner(System.in);
        ReadingData rd = new ReadingData();
        String fileName = sc.nextLine();
        String comapny = sc.nextLine();
        String companyID = sc.nextLine();
        rd.readData(fileName, comapny, companyID);
    }
}
