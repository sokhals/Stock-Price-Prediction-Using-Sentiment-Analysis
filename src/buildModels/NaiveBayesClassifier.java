package buildModels;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.SMO;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class NaiveBayesClassifier {

	public NGramTokenizer getTokenizer() {
		NGramTokenizer tokenizer = new NGramTokenizer();
		tokenizer.setNGramMinSize(1);
		tokenizer.setNGramMaxSize(1);
		tokenizer.setDelimiters("\\W");

		return tokenizer;
	}

	public static Instances readArff(String address) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(address));
		Instances data = new Instances(reader);
		// data.setClassIndex(data.numAttributes()-1);

		return data;

	}

	public NaiveBayesClassifier(String trainFile, String testFile) throws Exception {

		Instances trainInstance = readArff(trainFile);
		Instances testInstances = readArff(testFile);

		FilteredClassifier classifier = new FilteredClassifier();
		NGramTokenizer tokenizer = getTokenizer();

		StringToWordVector filter = getStringToVectorFilter(tokenizer);
		classifier.setClassifier(new SMO());
		classifier.setFilter(filter);

	startPrediction(trainInstance, testInstances, classifier);
	//	crossValidation(trainInstance, classifier);

	}

	public void startPrediction(Instances trainInstance, Instances testInstance, FilteredClassifier classifier)
			throws Exception {
		
		trainInstance.setClassIndex(trainInstance.numAttributes() - 1);
		classifier.buildClassifier(trainInstance);
		testInstance.setClassIndex(testInstance.numAttributes() - 1);
		BufferedReader bf=new BufferedReader(new FileReader(new File("sentimentAnalysisInput.csv")));
		BufferedWriter bw=new BufferedWriter(new FileWriter(new File("FinalsentimentAnalysisInput.csv")));
		bw.write(bf.readLine()+"\n");
		bf.readLine();
		for (int i = 0; i < testInstance.size(); i++) {
			int prediction = (int) classifier.classifyInstance(testInstance.get(i));
			String predicted = "";
			switch (prediction) {
			case 0:
				predicted="neutral";
				break;
			case 1:
				predicted="positive";
				break;
			case 2:
				predicted="negative";
				break;

			}
			String line=bf.readLine();
			System.out.println(line.substring(0, line.indexOf("neutral"))+predicted);
			bw.write(line.substring(0, line.indexOf("neutral"))+predicted+"\n");
			
		}
		bf.close();
		bw.flush();
		bw.close();

	}

	public void crossValidation(Instances finalDataSet, FilteredClassifier classifier) throws Exception {
		int seed = 35;
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

				System.out.println("prediction :" +prediction+" "+actual+" "+testInstance.toString());

				if (prediction == actual)
					accCnt += 1;
			}
			System.out.println("Accuracy is " + (double) accCnt / nTestIns);
			accuracy += ((double) accCnt / nTestIns);
		}

		System.out.println("Avg Accuracy = " + (accuracy / folds) * 100);
	}

	private StringToWordVector getStringToVectorFilter(NGramTokenizer tokenizer) {
		StringToWordVector filter = new StringToWordVector();
		filter.setTokenizer(tokenizer);
		filter.setWordsToKeep(1000000);
		filter.setDoNotOperateOnPerClassBasis(true);
		filter.setOutputWordCounts(false);
		filter.setLowerCaseTokens(true);
		filter.setInvertSelection(false);
		filter.setIDFTransform(false);
		filter.setTFTransform(false);
		return filter;
	}
}
