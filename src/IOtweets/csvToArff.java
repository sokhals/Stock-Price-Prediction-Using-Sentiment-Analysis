package IOtweets;



import com.opencsv.CSVParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class csvToArff {

	/**
	 *
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
}
