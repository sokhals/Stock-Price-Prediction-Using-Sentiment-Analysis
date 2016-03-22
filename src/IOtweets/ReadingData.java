/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IOtweets;

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

	HashMap<String, TweetData> map = null;

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
		map = new HashMap<String, TweetData>();
		System.out.println("Reading Tweets");
		BufferedReader bf = new BufferedReader(
				new FileReader(new File("/Users/surindersokhal/Documents/" + fileName + ".json")));
		JSONObject jsonObject = null;
		while ((read = bf.readLine()) != null) {
			TweetData data = new TweetData();

			if (read.trim().length() > 10) {
				try {
					jsonObject = new JSONObject(read);
					String text = jsonObject.getString("text");
					System.out.println(text);
					if ((text.contains("stock") && text.contains(companyID))
							|| (text.contains("stock") && text.contains(company)) || text.contains("stocks")) {
						System.out.println(text + "\n");
						if (jsonObject.has("retweeted_status")) {
							getRetweetCount(jsonObject, map, text);
						} else {
							addTweetToMap(jsonObject, map, text);
						}
					}
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}
		System.out.println("Done Reading Tweets\n Writing Tweets");
		System.out.println(map.get("555296885").Tweet);
		WriteTweets.writeTweets(map, fileName);
		System.out.println("Done Writing Tweets");

	}

	/**
	 *
	 * @param jSONObject
	 * @param map
	 * @param text
	 */
	public void addTweetToMap(JSONObject jSONObject, HashMap<String, TweetData> map, String text) {
		try {
			JSONObject id = jSONObject.getJSONObject("user");
			if (id.getString("lang").equalsIgnoreCase("en")) {
				String user_ID = id.getString("id_str");
				fillMap(map, 0, user_ID, text);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param jsonObject
	 * @param map
	 */
	public void getRetweetCount(JSONObject jsonObject, HashMap<String, TweetData> map, String text) {
		try {
			JSONObject arr = jsonObject.getJSONObject("retweeted_status");
			if (arr.getString("lang").equalsIgnoreCase("en")) {
				int retweetCount = arr.getInt("retweet_count");
				JSONObject id = arr.getJSONObject("user");
				String user_ID = id.getString("id_str");
				fillMap(map, retweetCount, user_ID, text);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param map
	 * @param count
	 * @param id
	 * @param text
	 */
	public void fillMap(HashMap<String, TweetData> map, int count, String id, String text) {
		TweetData data = new TweetData();
		if (map.containsKey(id)) {
			data = map.get(id);
		}
		count = Math.max(count, data.ReTweetCount);
		data.Tweet = data.Tweet == null ? text : data.Tweet;
		map.put(id, data);
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
