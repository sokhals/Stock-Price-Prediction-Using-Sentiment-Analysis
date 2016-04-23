package IOtweets;

import weka.core.Instances;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.apache.commons.io.FileUtils;

public class Utility {
    public static BufferedReader readFile(String filename){
        BufferedReader inputReader=null;
        try{
            inputReader = new BufferedReader(new FileReader(filename));
        }
        catch(FileNotFoundException ex){
            System.err.println("File not found: "+filename);
        }
        return inputReader;
    }

    public static String getClass(Instances data, double[] distribution) {
        double maxProb = distribution[0];
        int maxIndex = 0;
        for(int j=0; j<distribution.length;j++){
            if(distribution[j] > maxProb) {
                maxProb = distribution[j];
                maxIndex = j;
            }
        }
        return data.classAttribute().value(maxIndex);
    }
    
    public static void deleteFiles(){
    	try{
    		(new File("sentimentAnalysisInput.csv")).delete();
    		FileUtils.deleteDirectory(new File("output"));
    		FileUtils.deleteDirectory(new File("outputFiles"));
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public static void createNewFolders(){
    	try{
    		(new File("output")).mkdir();
    		(new File("outputFiles")).mkdir();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
}
