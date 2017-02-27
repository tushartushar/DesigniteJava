package Designite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class InputArgs {

	private static final String SOURCE = "[SOURCE FOLDER]";
	private static final String OUTPUT = "[OUTPUT FOLDER]";
	private String batchFilePath;
	private String sourceFolder;
	private String outputFolder;
	
	public String getSourceFolder() {
		return sourceFolder;
	}

	public String getOutputFolder() {
		return outputFolder;
	}

	public InputArgs(String batchFilePath) {
		this.batchFilePath = batchFilePath;
		readInputArgs();
		checkEssentialInputs();
	}
	
	//At least, the source folder must be specified
	private void checkEssentialInputs() {
		if (sourceFolder==null)
		{
			System.out.println("Input source folder is not specified.");
			throw new IllegalArgumentException();
		}
		File file = new File (sourceFolder);
		if (!(file.exists() && file.isDirectory()))
		{
			System.out.println("Input source folder path is not valid.");
			throw new IllegalArgumentException();
		}
	}

	private void readInputArgs() {
		File file = new File(batchFilePath);
		if(file.exists() && !file.isDirectory()) { 
		    readInputArgsFromFile(file);
		}
		else
		{
			System.out.println("The specified file doesn't exists. Please provide absolute path of batch input file.");
			throw new IllegalArgumentException();
		}
		
	}

	private void readInputArgsFromFile(File file) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = null;
			InputStates presentState = InputStates.DEFAULT;
			while ((line = br.readLine()) != null) {
				switch(line.trim().toUpperCase()){
					case SOURCE:
						presentState = InputStates.SOURCE;
						break;
					case OUTPUT:
						presentState = InputStates.OUTPUT;
						break;
					default:
						if (presentState.equals(InputStates.SOURCE)){
							if(line.trim().length()>0)
								sourceFolder = line.trim();
						}
						if (presentState.equals(InputStates.OUTPUT)){
							if(line.trim().length()>0)
								outputFolder = line.trim();
						}
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private enum InputStates{
		SOURCE,
		OUTPUT,
		DEFAULT
	}
}
