package IOtweets;
import com.opencsv.CSVParser;

import buildModels.BuildPricePredictionModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CsvToArff {

	/**
	 * @author Priyanka Patil
	 * @param fileName
	 * @throws IOException
	 */
	public void CSVToArff(String fileName) throws IOException
	{
		// load CSV
		CSVParser parser=new CSVParser();
		try{
			BufferedReader bf=new BufferedReader(new FileReader(new File(fileName)));
			BufferedWriter bw=new BufferedWriter(new FileWriter(new File(fileName.substring(0,fileName.indexOf(".csv"))+"_Test.arff")));
			bw.write("@relation sentimentAnalysis\n");
			bw.write("@attribute Tweet_Text string\n");
			bw.write("@attribute sentiment_class {neutral, positive, negative}\n\n");
			bw.write("@data\n\n");
			String line="";
			bf.readLine();
			bf.readLine();
			while((line=bf.readLine())!=null){
				String array[]=parser.parseLine(line);
				bw.write("\""+array[2]+"\","+array[5]+"\n");
			}
			bf.close();
			bw.flush();
			bw.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void CSVToArffForPredictionTrain(String fileName) throws IOException
	{
		// load CSV
		CSVParser parser=new CSVParser();
		String line="";
		try{
			BufferedReader bf=new BufferedReader(new FileReader(new File(fileName)));
			BufferedWriter bw=new BufferedWriter(new FileWriter(new File(fileName.substring(0,fileName.indexOf(".csv"))+".arff")));
			bw.write("@relation prediction\n");
			bw.write("@attribute open numeric\n");
			bw.write("@attribute high numeric\n");
			bw.write("@attribute low numeric\n");
			bw.write("@attribute close numeric\n");
			//bw.write("@attribute adjClose numeric\n");
			bw.write("@attribute sentiment {1,-1}\n");
			bw.write("@attribute prediction_class {1,-1}\n\n");
			bw.write("@data\n\n");
			bf.readLine();
			bf.readLine();
			while((line=bf.readLine())!=null){
				String array[]=parser.parseLine(line);
				if(line.endsWith("0"))
					continue;
				bw.write(array[2]+","+array[3]+","+array[4]+","+array[5]+","+array[9]+","+array[10]+"\n");
			}
			bf.close();
			bw.flush();
			bw.close();
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(line);
		}
	}
	
	public static void CSVToArffForPredictionTest(String fileName) throws IOException
	{
		// load CSV
		CSVParser parser=new CSVParser();
		try{
			BufferedReader bf=new BufferedReader(new FileReader(new File(fileName)));
			BufferedWriter bw=new BufferedWriter(new FileWriter(new File(fileName.substring(0,fileName.indexOf(".csv"))+".arff")));
			bw.write("@relation prediction\n");
			bw.write("@attribute open numeric\n");
			bw.write("@attribute high numeric\n");
			bw.write("@attribute low numeric\n");
			bw.write("@attribute close numeric\n");
			//bw.write("@attribute adjClose numeric\n");
			bw.write("@attribute sentiment {1,-1}\n");
			bw.write("@attribute prediction_class {1,-1}\n\n");
			bw.write("@data\n\n");
			String line="";
			bf.readLine();
			bf.readLine();
			while((line=bf.readLine())!=null){
				String array[]=parser.parseLine(line);
				if(line.endsWith("0"))
					continue;
				bw.write(array[2]+","+array[3]+","+array[4]+","+array[5]+","+array[8]+","+array[9]+"\n");
			}
			bf.close();
			bw.flush();
			bw.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String args[])
	{
		try {
			CSVToArffForPredictionTrain("outputFiles/StockPrediction_Train_Final.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			CSVToArffForPredictionTest("outputFiles/StockPrediction_Test.csv");
			try {
				BuildPricePredictionModel bp = new BuildPricePredictionModel();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
