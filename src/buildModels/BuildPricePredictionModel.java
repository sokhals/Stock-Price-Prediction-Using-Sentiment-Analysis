package buildModels;

import java.util.Random;

import IOtweets.CsvToArff;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.net.search.fixed.NaiveBayes;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;

public class BuildPricePredictionModel {

	String trainFile,testFile;
	
	public BuildPricePredictionModel() throws Exception
	{
		trainFile="outputFiles/StockPrediction_Train_Final.arff";
		testFile="outputFiles/StockPrediction_Test.arff";
		
		System.out.println("Running SVM");
		initiateModels((Classifier)new SMO());
		//System.out.println("Running Linear Regression");
		//initiateModels((Classifier)new LinearRegression());
		//System.out.println("Running Naive Bayes");
		//initiateModels((Classifier)new NaiveBayes());
		System.out.println("Running J48");
		initiateModels((Classifier)new J48());
		//System.out.println("Running RandomForest");
		//initiateModels((Classifier)new RandomForest());
	}
	
	public void initiateModels(Classifier classifier) throws Exception{
		Instances trainInstances=NaiveBayesClassifier.readArff(trainFile);
		trainInstances.setClassIndex(trainInstances.numAttributes()-1);
		//crossValidation(trainInstances, classifier);
		classifier.buildClassifier(trainInstances);
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
		System.out.println();
	}
	
	public static void crossValidation(Instances finalDataSet, Classifier classifier) throws Exception {
		int seed = 25;
		int folds = 5;
		int cIdx = finalDataSet.numAttributes() - 1;

		// Randomize the data
		Random rand = new Random(seed);
		Instances randomData = new Instances(finalDataSet);
		randomData.randomize(rand);

		double accuracy = 0;
		Instance testInstance;
		Instances train = null;
		Instances test = null;

		for (int i = 0; i < folds; i++) {
			train = randomData.trainCV(folds, i);
			test = randomData.testCV(folds, i);

			train.setClassIndex(train.numAttributes() - 1);
			test.setClassIndex(test.numAttributes() - 1);
			classifier.buildClassifier(train);

			int nTestIns = test.numInstances();

			int accCnt = 0;
			for (int j = 0; j < nTestIns; j++) {
				testInstance = test.instance(j);
				double prediction = classifier.classifyInstance(testInstance);
				double actual = testInstance.classValue();

//				System.out.println("prediction :" +prediction+" "+actual+" "+testInstance.toString());

				if (prediction == actual)
					accCnt += 1;
			}
			System.out.println("Accuracy is " + (double) accCnt / nTestIns);
			accuracy += ((double) accCnt / nTestIns);
		}

		System.out.println("Avg Accuracy = " + (accuracy / folds) * 100);
	}
	
	public static void main(String args[])
	{
		try {
			BuildPricePredictionModel bp = new BuildPricePredictionModel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}