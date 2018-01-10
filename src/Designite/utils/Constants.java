package Designite.utils;

import java.io.File;

public class Constants {
	
	public static final String PATH_OF_THRESHOLDS = System.getProperty("user.dir") 
			+ File.separator
			+ "thresholds.txt";
	
	public static final String CSV_DIRECTORY_PATH = System.getProperty("user.dir") 
			+ File.separator + "csv";
	
	public static final String TYPE_METRICS_PATH_SUFFIX = "typeMetrics.csv";
	public static final String METHOD_METRICS_PATH_SUFFIX = "methodMetrics.csv";
}
