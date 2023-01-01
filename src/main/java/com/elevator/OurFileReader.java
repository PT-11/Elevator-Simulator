package com.elevator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class OurFileReader {
	
	private ArrayList<String> attributes = new ArrayList<String>();
	private static final Logger LOG = LogManager.getLogger(OurFileReader.class);	
	
	
	public ArrayList<String> populateAttributes (String fileInput) {
		//ArrayList<String> attributes = new ArrayList<String>();
		try  
		{  
			File file = new File(fileInput);    //creates a new file instance  
			FileReader fileReader = new FileReader(file);   //reads the file  
			BufferedReader bufferedReader = new BufferedReader(fileReader);  //creates a buffering character input stream  
			String line;
			
			while((line = bufferedReader.readLine())!=null) {  
				List<String> commandLine = Arrays.asList(line.split(":"));
				attributes.add(commandLine.get(1));
				LOG.info("Attribute " + commandLine.get(0) + ": " + commandLine.get(1) + " retrieved successfully");
			}  
			
			fileReader.close();    //closes the stream and release the resources   
		}  catch(IOException e)  {  
			LOG.fatal(e.getMessage());
			e.printStackTrace();  
		}  
		
		return attributes; 
	}
	
}