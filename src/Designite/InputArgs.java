package Designite;

import java.io.File;

public class InputArgs {
	private String sourceFolder;
	private String outputFolder;
	
	public InputArgs() {
		//It is invoked only in case of error
	}

	public InputArgs(String inputFolderPath, String outputFolderPath) {
		sourceFolder = inputFolderPath;
		outputFolder = outputFolderPath;
		checkEssentialInputs();
	}

	public String getSourceFolder() {
		return sourceFolder;
	}

	public String getOutputFolder() {
		return outputFolder;
	}

	//At least, the source folder must be specified
	private void checkEssentialInputs() {
		if (sourceFolder==null)
		{
			//System.out.println("Input source folder is not specified.");
			throw new IllegalArgumentException("Input source folder is not specified.");
		}
		File folder = new File(sourceFolder);
		if (!(folder.exists() && folder.isDirectory()))
		{
			//System.out.println("Input source folder path is not valid.");
			throw new IllegalArgumentException("Input source folder path is not valid.");
		}
		File outFolder = new File(outputFolder);
		if (outFolder.exists() && outFolder.isFile())
		{
			//System.out.println("The specified output folder path is not valid.");
			throw new IllegalArgumentException("The specified output folder path is not valid.");
		}
	}
	
	/***
	 * Analyzes the provided <b>sourceFolder</b> variable and 
	 * extracts the name of the project. 
	 * @return A String with the name of the project. 
	 * When the given sourceFolder has a value of <i>src</i> 
	 * or <i>source</i> then the method returns 
	 * the name of the direct parent directory
	 */
	public String getProjectName() {
		File temp = new File(sourceFolder);
		if (temp.getName().equals("src") || temp.getName().equals("source")) {
			return new File(temp.getParent()).getName();
		} else {
			return new File(sourceFolder).getName();
		}
	}
}
