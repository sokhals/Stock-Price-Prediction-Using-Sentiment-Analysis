package buildModels;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.net.search.fixed.NaiveBayes;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

public class BuildPricePredictionModel {

	String trainFile,testFile;
	
	public BuildPricePredictionModel(String trF,String teF) throws Exception{
		trainFile=trF;
		testFile=teF;
		System.out.println("Running Naive Bayes");
		initiateModels((Classifier)new NaiveBayes());
		System.out.println("Running Linear Regression");
		initiateModels((Classifier)new LinearRegression());
		System.out.println("Running J48");
		initiateModels((Classifier)new J48());
		System.out.println("Running RandomForest");
		initiateModels((Classifier)new RandomForest());
		System.out.println("Running SVM");
		initiateModels((Classifier)new SMO());
	}
	
	public void initiateModels(Classifier classifier) throws Exception{
		Instances trainInstance=NaiveBayesClassifier.readArff(trainFile);
		trainInstance.setClassIndex(trainInstance.numAttributes()-1);
		classifier.buildClassifier(trainInstance);
		Instances testInstance=NaiveBayesClassifier.readArff(testFile);
		testInstance.setClassIndex(testInstance.numAttributes()-1);
		int correct=0;
		for(int i=0;i<testInstance.size();i++){
			double predicted=classifier.classifyInstance(testInstance.get(i));
			double actual=testInstance.get(i).classValue();
			System.out.println("Pred:Act "+predicted+":"+actual);
			correct=predicted==actual?correct+1:correct;
		}
		
		System.out.println("Total accurate predicted= "+correct+"/"+testInstance.size());
		System.out.println("Accuracy= "+((correct/(testInstance.size()*1.0))*100)+"\n\n");
	}
	
	
	
	
}
