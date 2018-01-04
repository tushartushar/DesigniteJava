package Designite.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVUtils {
	
	private static final String CSV_DIRECTORY_PATH = System.getProperty("user.dir") 
			+ File.separator + "csv";
	
	public static final String TYPE_METRICS_PATH = CSV_DIRECTORY_PATH + File.separator + "typeMetrics.csv";
	public static final String METHOD_METRICS_PATH = CSV_DIRECTORY_PATH + File.separator + "methodMetrics.csv";
	
	private static final String TYPE_METRICS_HEADER = "Package Name"
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
	
	private static final String METHOD_METRICS_HEADER = "Package Name"
			+ ",Type Name"
			+ ",MethodName"
			+ ",LOC"
			+ ",CC"
			+ ",PC"
			+ "\n";
	
	public static void initializeCSVDirectory() {
		File dir = new File(CSV_DIRECTORY_PATH);
		createDirIfNotExists(dir);
		cleanup(dir);
		initializeNeededFiles();
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
	
	private static void initializeNeededFiles() {
		createCSVFile(TYPE_METRICS_PATH, TYPE_METRICS_HEADER);
		createCSVFile(METHOD_METRICS_PATH, METHOD_METRICS_HEADER);
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
