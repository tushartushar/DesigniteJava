package Designite;

import java.io.FileNotFoundException;
import java.io.IOException;
import Designite.SourceModel.*;
import Designite.utils.Logger;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 
 * This is the start of the project
 */
public class Designite {
	public static void main(String[] args) throws IOException {
		// args[0]: filePath
		if (args.length != 1) {
			usage();
			System.out.println("Quitting...");
			System.exit(1);
			//throw new IllegalArgumentException();
		}
		InputArgs argsObj = new InputArgs(args[0]);
		SM_Project project = new SM_Project(argsObj);
		// set the logFile path to enable logging
		Logger.logFile = getlogFileName(argsObj);
		project.parse();
		project.resolve();
		project.extractMetrics();
		project.extractCodeSmells();
		PrintWriter writer = getDebugLogStream(argsObj);
		project.printDebugLog(writer);
		if (writer != null)
			writer.close();
		Logger.log("Done.");
	}

	private static String getlogFileName(InputArgs argsObj) {
		String file = null;
		//DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HHmm");
		//Date date = new Date(0);
		String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(Calendar.getInstance().getTime());
		file = argsObj.getOutputFolder() + "DesigniteLog" + timeStamp + ".txt";
		
		return file;
	}

	private static PrintWriter getDebugLogStream(InputArgs argsObj) {
		PrintWriter writer = null;
		if (!argsObj.getOutputFolder().equals("")) {
			String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(Calendar.getInstance().getTime());
			String filename = argsObj.getOutputFolder() + "DesigniteDebugLog" + timeStamp + ".txt";
			try {
				writer = new PrintWriter(filename);
			} catch (FileNotFoundException ex) {
				// log the exception
			}
		}
		return writer;
	}

	private static void usage() {
		System.err.println("First argument needs to be the path to a batch input file.");
		System.out.println("Usage instructions:");
		System.out.println("java -jar DesigniteJava<version>.jar <Path to a batch input file>\n");
	}
}
