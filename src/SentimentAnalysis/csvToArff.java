package SentimentAnalysis;

import org.apache.commons.io.FilenameUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import SentimentAnalysis.Utility;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class csvToArff {

	/**
	 *
	 * @param fileName
	 * @throws IOException
	 */
	public void CSVToArff(String fileName) throws IOException
	{
		// load CSV
		CSVLoader loader = new CSVLoader();
		String arffFile = "";
		
		File folder = new File("inputFiles/" + fileName);
		File files[] = folder.listFiles();
		for (File file : files) {
			if(file.getName().endsWith(".csv"))
			{
				loader.setSource(file);
				Instances data = loader.getDataSet();	//get instances object
				
				// save ARFF
				ArffSaver saver = new ArffSaver();
				saver.setInstances(data);
				
				// and save as ARFF
				String fileNameWithOutExt = FilenameUtils.removeExtension(file.getName());
				arffFile = arffFile + "outputFiles/" + fileNameWithOutExt + ".arff";
				
				saver.setFile(new File(arffFile));
				saver.writeBatch();
				
				readArffFile(arffFile);
			}
		}
	}
	
	//Added for testing
	/**
	 *
	 * @param fileName
	 * @throws IOException
	 */
	public void readArffFile(String arffFile) throws IOException
	{
		System.out.println(arffFile);
		BufferedReader file = Utility.readFile(arffFile);
    	
        Instances data = new Instances(file);
        
        System.out.println("Number of data instances : " +data.numInstances());
        data.setClassIndex(data.numAttributes()-1);
        for(int i=0;i<data.numInstances();i++)
        {
        	Instance ins = data.instance(i);
        	System.out.println((int)ins.classValue());
        }
	}
	
	
	/**
	 *
	 * @param arg
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException
	{
		Scanner sc = new Scanner(System.in);
		csvToArff cta = new csvToArff();
		String fileName = sc.nextLine();
		cta.CSVToArff(fileName);	
	}
}
