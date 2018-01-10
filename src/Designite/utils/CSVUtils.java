package Designite.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVUtils {
	
	private static final String TYPE_METRICS_HEADER = "Project Name"
			+ ",Package Name"
			+ ",Type Name"
			+ ",NOF"
			+ ",NOPF"
			+ ",NOM"
			+ ",NOPM"
			+ ",LOC"
			+ ",WMC"
			+ ",NC"
			+ ",DIT"
			+ ",LCOM"
			+ ",FANIN"
			+ ",FANOUT"
			+ "\n";
	
	private static final String METHOD_METRICS_HEADER = "Project Name"
			+ ",Package Name"
			+ ",Type Name"
			+ ",MethodName"
			+ ",LOC"
			+ ",CC"
			+ ",PC"
			+ "\n";
	
	public static void initializeCSVDirectory(String projectName) {
		File dir = new File(Constants.CSV_DIRECTORY_PATH 
				+ File.separator
				+ projectName);
		createDirIfNotExists(dir);
		cleanup(dir);
		initializeNeededFiles(dir);
	}
	
	private static void createDirIfNotExists(File dir) {
		if (!dir.exists()) {
			try {
				dir.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void cleanup(File dir) {
		for (File file : dir.listFiles()) {
			file.delete();
		}
	}
	
	private static void initializeNeededFiles(File dir) {
		createCSVFile(dir.getPath() + File.separator + Constants.TYPE_METRICS_PATH_SUFFIX, TYPE_METRICS_HEADER);
		createCSVFile(Constants.METHOD_METRICS_PATH_SUFFIX, METHOD_METRICS_HEADER);
	}
	
	private static void createCSVFile(String path, String header) {
		try {
			File file = new File(path);
	        file.createNewFile(); 
			FileWriter fileWriter = new FileWriter(file, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.append(header);
			bufferedWriter.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
