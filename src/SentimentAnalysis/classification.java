package SentimentAnalysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

public class classification {

	public Instances createDataSetfromCSV(String fileName) throws IOException
	{
		CSVLoader loader = new CSVLoader();
		File file = new File(fileName);
		loader.setSource(file);
		Instances dataset = loader.getDataSet();	//get instances object
		
		int numOfInstances = dataset.numInstances();		
		
		//for dates
		/***********************************************************************/
		ArrayList<String> datesList = new ArrayList<String>();
		for(int i=0;i<numOfInstances;i++)
			datesList.add(dataset.instance(i).stringValue(1).substring(4, 10));
		
		Set<String> dates = new HashSet<String>(datesList);
		System.out.println(dates);
		/***********************************************************************/
		
		ArrayList<String> tweetList = new ArrayList<String>();
		for(int i=0;i<numOfInstances;i++)
			tweetList.add(dataset.instance(i).stringValue(2));
		
        Attribute tweet = new Attribute("tweet", tweetList);
        
        ArrayList<String> classVal = new ArrayList<String>();
        classVal.add("positive");
        classVal.add("negative");
        classVal.add("neutral");
        Attribute sentiment = new Attribute("sentiment",classVal);
        
        ArrayList<Attribute> attributes = new ArrayList<Attribute>(2);
        attributes.add(tweet);
        attributes.add(sentiment);
        
        Instances finalDataSet = new Instances("inputDataset",attributes, 0);
       
        for(int i=0;i<numOfInstances;i++)
        {
	        double[] instance = new double[finalDataSet.numAttributes()];
	
	        //instance[0] = finalDataSet.attribute(0).addStringValue(dataset.instance(i).stringValue(2));
	        instance[0] = i;
	        if(dataset.instance(i).stringValue(4).equals("positive"))
	        	instance[1] = 0;
	        else if(dataset.instance(i).stringValue(4).equals("negative"))
	        	instance[1] = 1;
	        else
	        	instance[1] = 2;
	
	        finalDataSet.add(new DenseInstance(1.0, instance));
        }
        
        System.out.println("\n");
        System.out.println("Final Data Set for Date :" );
        System.out.println(finalDataSet);
        
        return finalDataSet;
	}
	
	public void crossvalidation(Instances finalDataSet) throws Exception
	{
		//SMO svm = new SMO();
		NaiveBayes nb = new NaiveBayes();
		
		int seed = 35;
    	int folds = 5;
        int cIdx = finalDataSet.numAttributes()-1;
    	
        //Randomize the data
    	Random rand = new Random(seed);
    	Instances randomData = new Instances(finalDataSet);
    	randomData.randomize(rand);
    	
    	double accuracy=0;
		Instance testInstance;
		Instances train = null;
		Instances test = null;
		
    	for(int i=0;i<folds;i++)
    	{
    		train = randomData.trainCV(folds, i);
    		test = randomData.testCV(folds, i);
    		
    		train.setClassIndex(cIdx);
    		test.setClassIndex(cIdx);
    		
    		//svm.buildClassifier(train);
    		nb.buildClassifier(train);
    		
    		int nTestIns = test.numInstances();
    		
    		int accCnt = 0;
    		for(int j=0;j<nTestIns;j++)
    		{	
    			testInstance = test.instance(j);
    			double prediction = nb.classifyInstance(testInstance);
    			double actual = testInstance.classValue();
    			
    			System.out.println("prediction :" +prediction);
    			System.out.println("actual :" +actual);
    			if(prediction == actual)
    				accCnt +=1;
    		}
    		accuracy += ((double)accCnt/nTestIns);
    	}
    	
    	System.out.println("Avg Accuracy = " + (accuracy/folds) * 100);
	}
	
	
	public void sentimentAnalysis(Instances train,Instances test) throws Exception
	{
		Map<Double,Integer> predictionMap = new HashMap<Double,Integer>(); 
		NaiveBayes nb = new NaiveBayes();
		nb.buildClassifier(train);
		
		Instance testInstance;
		for(int i=0;i<test.numInstances();i++)
		{
			testInstance = test.instance(i);
			double prediction = nb.classifyInstance(testInstance);
			Integer retcnt = Integer.parseInt(test.instance(i).stringValue(3));
		/*	if(!predictionMap.containsKey(prediction))
				predictionMap.put(prediction, retcnt);
			else
				predictionMap.put(prediction, predictionMap.get(prediction)+retcnt); */
		}
		
		/*
		for(Map.Entry<Double, Integer> entry : predictionMap.entrySet())
		{
			Integer max = Integer.MIN_VALUE;
			Double key = entry.getKey();
			Integer weight = entry.getValue();
			
			if(max < weight)
				max = weight;
			else
			{
				System.out.println("Sentiment : " + key);
			}
		}
		*/
	}
	
	public static void main(String args[]) throws Exception
	{
		classification c = new classification();
		
		Instances finalDataSet = c.createDataSetfromCSV("inputFiles/csv/wfc.csv");
		//Instances train = c.createDataSetfromCSV("inputFiles/csv/train.csv");
		//Instances test = c.createDataSetfromCSV("inputFiles/csv/test.csv");
		
		c.crossvalidation(finalDataSet);
		//c.sentimentAnalysis(train,test);
	}
}