package IOtweets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.xml.crypto.Data;

import com.opencsv.CSVParser;

import IOtweets.TweetData;
import data_preprocessing.RemoveStopWords;


/**
 * 
 * @author Lakshit Gaur
 *
 */
public class CleanPreprocessedData {

	public void readData() throws IOException {
		
		File codeFolder=new File("codes/companylist.csv");
		BufferedReader br2 = new BufferedReader(new FileReader(codeFolder));
		String line2="";
		HashSet<String> set=new HashSet<String>();
		while((line2=br2.readLine())!=null){
			String records2[]=line2.split(",");
			set.add(records2[0]);
			set.add(records2[1]);
		}
		
		
		String search="";
		TweetData data = null;
		File folder = new File("outputFiles/");
		File files[] = folder.listFiles();
		BufferedReader br = null;
		
		String line = "";
		//boolean flag = false;
		int counter=0;
		CSVParser parser=new CSVParser(';');
		
		for (File file : files) {
			if (file.getName().endsWith(".csv")) {
				HashMap<String, TweetData> map = new HashMap<String, TweetData>();
				String fileName = file.getName().replaceAll("[.][^.]+$", "");
				String[] codes = fileName.split("_");
				
				//System.out.println(file.getName());
				try {
					System.out.println(file);
					br = new BufferedReader(new FileReader(file));
					if(counter!=0){
						br.readLine();
						}
					counter++;
					while ((line = br.readLine()) != null) {
						//System.out.println(line);
						if (line.contains("Retweet_Count") || line.equals("")) {
							continue;
						}
						
						if(!line.endsWith("0")){
							//System.out.println("Line "+line);
							
							while(!line.trim().endsWith("0")){
								line+=br.readLine()+" ";
								
							}
							
						//System.out.println(line);
							
						}
						
						if(!line.trim().endsWith("0")){
							System.out.println("line "+line);
						}
						// System.out.println(line);
						String records[]=parser.parseLine(line.trim());
						
						String updatedRecord = records[2];
						// String firstCallData="";
						//System.out.println(updatedRecord);
						updatedRecord = checkCode(updatedRecord, codes[0],
								records[2]);
						updatedRecord = checkCode(updatedRecord, codes[1],
								records[2]);
						updatedRecord = updatedRecord.replaceAll(
								"(?i)" + Pattern.quote("$" + codes[0]), " ");
						updatedRecord = updatedRecord.replaceAll(
								"(?i)" + Pattern.quote(codes[0]), " ");
						updatedRecord = updatedRecord.replaceAll("http?://\\S+\\s?", " ");
						updatedRecord = updatedRecord.replaceAll(
								"(?i)" + Pattern.quote("$" + codes[1]), " ");
						updatedRecord = updatedRecord.replaceAll(
								"(?i)" + Pattern.quote(codes[1]), " ");
						updatedRecord = updatedRecord.replaceAll("https?://\\S+\\s?", " ");
						
						updatedRecord = updatedRecord.replaceAll("[^a-zA-Z0-9_-_+]", " ");
						updatedRecord = updatedRecord.replaceAll("\\d", " ");
						updatedRecord = updatedRecord.replaceAll("	^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}$", " ");
						updatedRecord = updatedRecord.replaceAll("	^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}$", " ");
						
						updatedRecord=
					updatedRecord.replaceAll("^\\d{4}-\\d{2}-\\d{2}$"," ");	
						updatedRecord=
								updatedRecord.replaceAll("(?:[01]\\d|2[0123]):(?:[012345]\\d):(?:[012345]\\d)"," ");	
						
						updatedRecord=
								updatedRecord.replace("+"," ");	
						
						updatedRecord=
								updatedRecord.replace("-"," ");	
						
						Iterator<String> it=set.iterator();
						while(it.hasNext()){
							//System.out.println(it.next());
							
							search=it.next().replace("\"", "");
							
							if(updatedRecord.toLowerCase().contains(" "+search.toLowerCase()+" "))
							{
								//System.out.println(updatedRecord);
								updatedRecord=updatedRecord.replaceAll(" "+search.toLowerCase()+" ", "");
								//System.out.println(updatedRecord);
								//System.out.println("--------------------");
								//System.out.print(updatedRecord);
							}
							
							//if(updatedRecord.toLowerCase().contains(it.next().toLowerCase())){
								//System.out.println("true");
								
								//updatedRecord.replaceAll(it.next().toLowerCase(), "");
							//}
						}
						updatedRecord="\""+updatedRecord+"\"";
						data = new TweetData();
						data.Time = records[1];
						data.Tweet = updatedRecord;
						data.ReTweetCount = Integer.parseInt(records[3]);
						if (records[4].trim().equals("-1")) {
							data.sentiment = "negative";
						}
						if (records[4].trim().equals("0")) {
							data.sentiment = "neutral";
						}
						if (records[4].trim().equals("1")) {
							data.sentiment = "positive";
						}
						data.companyName = codes[1];
						map.put(records[0], data);
					}
					WriteTweets.writeTweets(map, "output/updated_"
							+ fileName,true);
					
					//file.delete();

				} catch (Exception e) {
					System.out.println(line);
					e.printStackTrace();
				}

			}
		}
		
	}

	public static String checkCode(String updatedRecord, String code,
			String record) {
		if (record.toLowerCase().contains(code.toLowerCase())) {

			int start = record.toLowerCase().indexOf(code.toLowerCase());
			int ends = start + code.length();

			if (ends < updatedRecord.length() - 2) {

				if (record.charAt(ends) == '+') {
					String temp = updatedRecord.substring(ends).replaceFirst(
							"[^+][0-9]", "UP");
					updatedRecord = updatedRecord.substring(0, ends - 1) + temp;
				}
				if (record.charAt(ends + 1) == '+') {
					String temp = updatedRecord.substring(ends + 1)
							.replaceFirst("[^+][0-9]", "UP");
					updatedRecord = updatedRecord.substring(0, ends - 1) + temp;
				}
				if (record.charAt(ends + 2) == '+') {
					String temp = updatedRecord.substring(ends + 2)
							.replaceFirst("[^+][0-9]", "UP");
					updatedRecord = updatedRecord.substring(0, ends - 1) + temp;
				}
				if (record.charAt(ends) == '-') {
					String temp = updatedRecord.substring(ends).replaceFirst(
							"[^-][0-9]", "DOWN");
					updatedRecord = updatedRecord.substring(0, ends - 1) + temp;

				}
				if (record.charAt(ends + 1) == '-') {
					String temp = updatedRecord.substring(ends + 1)
							.replaceFirst("[^-][0-9]", "DOWN");
					updatedRecord = updatedRecord.substring(0, ends) + temp;
				}
				if (record.charAt(ends + 2) == '-') {
					String temp = updatedRecord.substring(ends + 2)
							.replaceFirst("[^-][0-9]", "DOWN");
					updatedRecord = updatedRecord.substring(0, ends - 1) + temp;

				}

			}
		}

		return updatedRecord;
	}


	
	public  void runMergingProcess(String inputAddress,String outputAddress) {
		try {
			new ProcessBuilder("chmod", "777","script/mergedFiles.sh").start();
			ProcessBuilder builder=new ProcessBuilder("script/mergedFiles.sh" , inputAddress,outputAddress);
			
				 File outputFile = new File("outpus.txt");
				 File outputFile2 = new File("outpus2.txt");
				 builder.redirectOutput(outputFile);
				 builder.redirectError(outputFile2);
				 Process process=builder.start();
				int errCode = process.waitFor();
				System.out.println("Error: "+(errCode==0?"No":"Yes"));
				
			} catch (IOException | InterruptedException e) {
				
				e.printStackTrace();
			}
			
	
		
	}
	
	
	public void removeStopWords(RemoveStopWords stopWordsObject){
		stopWordsObject.readStopWordList();
		stopWordsObject.readDataFile("sentimentAnalysisInput");
	}

}
