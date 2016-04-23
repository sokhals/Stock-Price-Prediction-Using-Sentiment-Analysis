import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
//import org.apache.hadoop.mapreduce.Mapper.Context;
//import org.apache.hadoop.mapreduce.Reducer;
//import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.SerializationHelper;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;



@SuppressWarnings("unused")
public class Model {

	static class OriginMapper extends Mapper<LongWritable, Text, Text, Text>{
		public void map(LongWritable file, Text text, Context context) throws IOException, InterruptedException{
			String line = text.toString();
			line = line.replaceAll("\"", "");
			line = line.replaceAll(", ", "#");
			String[] airlinedata = line.split(",");
			Integer month = null;
			Integer day_of_month = null;
			Integer day_of_week = null;
			String carrierID = "";
			Integer originAirportID = null;
			Integer year = null;
			String flightDate = "";
			Integer destAirportID = null;
			Integer CRSDeptTime = null;
			Integer CRSArrTime = null;
			String flightNumber = "";
			double delay = 0.0;
			Boolean emptyRow = true;
			Boolean holiday = true;
			Boolean flag = true;

			try{
				month = Integer.valueOf(airlinedata[2]);
				day_of_month = Integer.valueOf(airlinedata[3]);
				day_of_week = Integer.valueOf(airlinedata[4]);
				carrierID = airlinedata[6];
				year = Integer.valueOf(airlinedata[0]);
				flightDate = airlinedata[5];
				CRSDeptTime = Integer.valueOf(airlinedata[29]);
				CRSArrTime = Integer.valueOf(airlinedata[40]);
				originAirportID = Integer.valueOf(airlinedata[11]);
				destAirportID = Integer.valueOf(airlinedata[20]);
				//year = Integer.valueOf(airlinedata[0]);
				flightNumber = airlinedata[10];
				if(airlinedata[43].trim().length() >= 1) { 
				delay = Double.parseDouble(airlinedata[43].trim());
				}
				if (delay > 0.0) 
				{
					flag = true;
				}
				else
				{
					flag = false;
				}
				
				String[] checkDate ;
				String checkMonth; 
				String checkDay;
				checkDate = flightDate.split("-");
				checkMonth = checkDate[1];
				checkDay = checkDate[2];
				if(holidays(checkDay,checkMonth)){
					holiday = true;
				}
				else{
					holiday = false;
				}
					

			}
			catch (Exception e){
			//	e.printStackTrace();
				//System.out.println("<><><><><><><><><><<><><><><<><<><><><><><><><><><><> " +delay);
				emptyRow = false; 
				//e.printStackTrace();
			}




			if(airlinedata.length == 110 && emptyRow ) {
				String outValue = day_of_month.toString() + " " + year.toString() + " " + day_of_week.toString() + " "
						+ carrierID + " " + originAirportID.toString() + " " + destAirportID.toString()
						+ " " + CRSDeptTime.toString() + " "  +  CRSArrTime.toString() + " "+flightNumber + " " + flag.toString() + " " + holiday + " " ; 
				Text outvalue = new Text(outValue);
				Text outkey = new Text(month + "");
				//Text outvalue = new Text(month + ":" + deptTime + ":" +CRSDeptTime +" G");
				context.write(outkey, outvalue);

			}
		}

		private boolean holidays(String checkDay, String checkMonth) {
			int day = Integer.valueOf(checkDay);
			int month = Integer.valueOf(checkMonth);
			if(month == 12 && (day == 31 | day ==25)){
				return true;
			}
			else if((month == 7 && day ==4) | (month ==1 && day == 1) | (month == 11 && day == 24)){
				return true;
			}
			
			
			
			return false;
		}


	}

	public static class OriginReducer extends Reducer<Text,Text,Text,Text>{

		public void reduce(Text key,Iterable<Text> values,Context context) throws IOException,InterruptedException{
			// Creating  Training Model
			ArrayList<Attribute> attribute = new ArrayList<Attribute>();
			attribute.add(new Attribute("day_of_month"));
			attribute.add(new Attribute("day_of_week"));
			attribute.add(new Attribute("originAirportID"));
			attribute.add(new Attribute("destAirportID"));
			attribute.add(new Attribute("CRSDeptTime"));
			attribute.add(new Attribute("CRSArrTime"));
			//attribute.add(new Attribute("Flight_No"));
			Attribute carrierCode = new Attribute("carrierID");
			

			List<String> flagList = new ArrayList<String>(2);
			flagList.add("true");
			flagList.add("false");
			Attribute delay = new Attribute ("delay",flagList);
			
			List<String> holidayList = new ArrayList<String>(2);
			holidayList.add("true");
			holidayList.add("false");
			Attribute holiday = new Attribute ("holiday",holidayList);
			
			
			attribute.add(carrierCode);
			attribute.add(delay);
			attribute.add(holiday);
			Instances TrainingSet = new Instances("TrainModel", attribute, 0);
			TrainingSet.setClassIndex(7);

			for(Text value: values){
				String[] valueSplitter = value.toString().split(" ");
				Instance iExample = new DenseInstance(9);
				iExample.setValue(attribute.get(0), Integer.parseInt(valueSplitter[0])); // day_of_month
				iExample.setValue(attribute.get(1), Integer.parseInt(valueSplitter[2])); // day_of_week
				iExample.setValue(attribute.get(2), Integer.parseInt(valueSplitter[4])); // originAirportID
				iExample.setValue(attribute.get(3), Integer.parseInt(valueSplitter[5])); // destAirportID
				iExample.setValue(attribute.get(4), Integer.parseInt(valueSplitter[6])); // CRSDeptTime
				iExample.setValue(attribute.get(5), Integer.parseInt(valueSplitter[7])); // CRSArrTime
				//iExample.setValue(attribute.get(6), Integer.parseInt(valueSplitter[8])); // Flight_no
				iExample.setValue(attribute.get(6),  valueSplitter[3].hashCode()); // CarrierID
				iExample.setValue(attribute.get(7), Boolean.parseBoolean(valueSplitter[9]) ? 1 : 0); // flag
				iExample.setValue(attribute.get(8), Boolean.parseBoolean(valueSplitter[10]) ? 1 : 0); // holiday
				
				TrainingSet.add(iExample);
			}

			Classifier classifier = (Classifier) new NaiveBayes();
			try {
				classifier.buildClassifier(TrainingSet);
			ByteArrayOutputStream byteWritable = new ByteArrayOutputStream();
			//SerializationHelper sh = new SerializationHelper();
			
			SerializationHelper.write(byteWritable, classifier);
			byte[] array=byteWritable.toByteArray();
			String encoded = Base64.getEncoder().encodeToString(array);
			context.write(key, new Text (" >> " + encoded));
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

		if (args.length != 2) {
			System.err.println("Usage: Prediction <input-path> <output-path>");
			System.exit(1);
			return;
		}
		Configuration conf = new Configuration();
		@SuppressWarnings("deprecation")
		Job job = new Job(conf, "Average Flight Price");
		job.setJarByClass(Model.class);
		job.setMapperClass(OriginMapper.class);
		job.setReducerClass(OriginReducer.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}