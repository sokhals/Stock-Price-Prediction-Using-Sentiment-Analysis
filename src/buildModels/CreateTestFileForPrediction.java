package buildModels;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.opencsv.CSVParser;

/**
 * 
 * @author Priyanka Patil & Mounika Dantuluru
 *
 */

class sentimentValue
{
	int pos;
	int neg;
}

public class CreateTestFileForPrediction{	
	
	public static String convertDate(String dateField) throws ParseException
	{
		Date strToDate = null;

		SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
		strToDate = format.parse(dateField);
		
		format = new SimpleDateFormat("M/d/yy");
		String DateToStr = format.format(strToDate);
		
		return DateToStr;
	}
	
	
	public static void createTestFile(String predictionFile,String sentimentAnalysisOut,String predictionTest) throws IOException
	{
		BufferedReader pred = new BufferedReader(new FileReader(new File(predictionFile)));	
		BufferedReader sent = new BufferedReader(new FileReader(new File(sentimentAnalysisOut)));
		BufferedWriter predTest = new BufferedWriter(new FileWriter(new File(predictionTest)));
		
		Map<String,sentimentValue> sentimentsMap = new LinkedHashMap<String,sentimentValue>();
		
		String sentiDate = null;
		
		String line="";
		
		CSVParser parser=new CSVParser();
		
		while((line = sent.readLine())!=null)
		{
			sentimentValue sv = new sentimentValue();
			
			String[] fields = parser.parseLine(line);
			
			try {
				sentiDate = convertDate(fields[1].replace("+0000", "GMT"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String companyName = fields[fields.length-2];
			String sentiment = fields[fields.length-1];
			Integer rtCount = Integer.parseInt(fields[fields.length-3]);
		
			String k = companyName +","+sentiDate;
			
			if(!sentimentsMap.containsKey(k))
			{
				if(sentiment.equals("negative"))
					sv.neg = rtCount + 1;
				else
					sv.pos = rtCount + 1;
				
				sentimentsMap.put(k, sv);
//				System.out.println(k + "neg rt count :" +sv.neg);
//				System.out.println(k + "pos rt count :" +sv.pos);
			}
			else
			{
				if(sentiment.equals("negative"))
				{
					sv = sentimentsMap.get(k);
					sv.neg += rtCount + 1;
//					System.out.println(k + "neg rt count :" +sv.neg);
					sentimentsMap.put(k,sv);
				}
				else
				{
					sv = sentimentsMap.get(k);
					sv.pos += rtCount + 1;
//					System.out.println(k + "pos rt count :" +sv.pos);
					sentimentsMap.put(k,sv);
				}
			}
		}
		sent.close();
		System.out.println(sentimentsMap);
		
		int Y = 0;
		
		predTest.write(pred.readLine()+","+"Sentiment\n");
		while((line = pred.readLine())!=null)
		{
			String[] fields = parser.parseLine(line);
			
			String pk = fields[0] +","+ fields[1];
			
			if(sentimentsMap.containsKey(pk))
			{
//				System.out.println(sentimentsMap.get(pk).neg + " " +sentimentsMap.get(pk).pos);
				Y = (sentimentsMap.get(pk).neg > sentimentsMap.get(pk).pos) ? -1 : 1;
				predTest.write(line+","+Y+"\n");
			}
		}
		pred.close();
		predTest.flush();
		predTest.close();
	}
	
	public static void main(String args[]) throws IOException, ParseException
	{
		String predictionFile= "outputFiles/PredictionInputTest.csv";
		String sentimentAnalysisOut = "outputFiles/FinalsentimentAnalysisInput.csv";
		String predictionTest = "outputFiles/StockPrediction_Test.csv";
		createTestFile(predictionFile,sentimentAnalysisOut,predictionTest);
	}
}