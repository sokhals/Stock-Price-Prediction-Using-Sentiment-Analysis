package SentimentAnalysis;

import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

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
}
