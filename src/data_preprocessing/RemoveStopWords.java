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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.opencsv.CSVParser;

import java.io.BufferedWriter;

/**
 *
 * @author surindersokhal
 */
public class RemoveStopWords {

	static boolean CHECK_READ_FILE = false;
	static HashSet<String> stopList = new HashSet<String>();

	public void readStopWordList() {
		try {
			BufferedReader bf = new BufferedReader(new FileReader(new File("codes/stopWords.txt")));
			String read = "";
			while ((read = bf.readLine()) != null) {
				// System.out.println(read.trim().toLowerCase());
				stopList.add(read.trim().toLowerCase());
			}
			ArrayList<String> list = new ArrayList(stopList);
			Collections.sort(list);
			System.out.println(list);
			bf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String[] containStopWords(String[] words) {
		HashSet<String> set = new HashSet<String>();
		for (int i = 0; i < words.length; i++) {
			if (!stopList.contains(words[i].trim().toLowerCase()))
				set.add(words[i]);
		}

		return set.toArray(new String[set.size()]);
	}

	public void readDataFile(String fileName) {
		String parts[]=null;
		try {
			String line = "";
			int counter = 0;
			File file = new File("outputFiles/"+fileName + ".csv");
			
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileName + ".csv")));
			Map<String, String[]> tweetMap = new LinkedHashMap<String, String[]>();
			int p, n, neg = 0;

			br.readLine();
			System.out.println("Skipping header");
			while ((line = br.readLine()) != null) {
				
				//System.out.println(line);

				 parts = line.split(";");
				String tweet = parts[2];// .replace("\"", "");

				tweet = cleanData(tweet).trim();
				if (tweet.length() > 1) {

					if (tweetMap.containsKey(tweet)) {
						String[] valParts = tweetMap.get(tweet);
						valParts[3] = String.valueOf(Integer.parseInt(valParts[3]) + 1);
						tweetMap.put(tweet, valParts);
					} else {
						tweetMap.put(tweet, parts);
					}

				}
			}
			
			counter = 0;
			br.close();
			for (Map.Entry<String, String[]> entry : tweetMap.entrySet()) {
				parts = entry.getValue();
				
				String key = entry.getKey();
				if (parts[4].trim().equalsIgnoreCase("")) {
					continue;
				}
				
				
					writer.write(parts[0] + "," + parts[1] + "," + "\"" + key.trim() + "\"" + "," + parts[3] + ","
							+ parts[4] + "," + parts[5] + "\n");

				

			}
			
			System.out.println("Writing done");
			writer.flush();
			writer.close();
			
		} catch (Exception e) {
			System.out.println("String issue "+Arrays.toString(parts));
			e.printStackTrace();
		}
	}

	public static String cleanData(String data) {
		String tweet = data;
		String split[] = tweet.split(" ");
		String[] tokens = containStopWords(split);
		
		data = buildString(tokens);

		return data;
	}

	public static String buildString(String[] tokens) {
		StringBuilder build = new StringBuilder("");
		if (tokens.length > 1) {

			for (int i = 0; i < tokens.length - 1; i++) {
				build.append(tokens[i] + " ");
			}
			build.append(tokens[tokens.length - 1]);
			return build.toString();
		}
		return build.toString();
	}

}
