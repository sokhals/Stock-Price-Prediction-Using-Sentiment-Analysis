/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package data_preprocessing;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author surindersokhal
 */
public class CleaningOOVWords {
    
	
	public CleaningOOVWords() 
		// TODO Auto-generated constructor stub
	{try {
		new ProcessBuilder("chmod", "777","script/mergedFiles.sh").start();
		ProcessBuilder builder=new ProcessBuilder("script/mergedFiles.sh" , "inputFiles2/hello","outputFiles");
		
			 File outputFile = new File("outpus.txt");
			 File outputFile2 = new File("outpus2.txt");
			 builder.redirectOutput(outputFile);
			 builder.redirectError(outputFile2);
			 Process process=builder.start();
				int errCode = process.waitFor();
			System.out.println("Error: "+(errCode==0?"No":"Yes"));
			
		} catch (IOException | InterruptedException e) {
			
			e.printStackTrace();
		}
		
		
	}

    public static void main(String arg[]){
    	new CleaningOOVWords();
    }
}
