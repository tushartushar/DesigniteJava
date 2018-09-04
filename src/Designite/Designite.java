package Designite;

import java.io.FileNotFoundException;
import java.io.IOException;
import Designite.SourceModel.*;
import Designite.utils.Logger;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.commons.cli.*;

/**
 * 
 * This is the start of the project
 */
public class Designite {
	public static void main(String[] args) throws IOException {
		InputArgs argsObj = parseArguments(args);
		SM_Project project = new SM_Project(argsObj);
		Logger.logFile = getlogFileName(argsObj);
		//TODO: log the version number
		project.parse();
		project.resolve();
		project.computeMetrics();
		project.detectCodeSmells();
		writeDebugLog(argsObj, project);
		Logger.log("Done.");
	}

	private static void writeDebugLog(InputArgs argsObj, SM_Project project) {
		PrintWriter writer = getDebugLogStream(argsObj);
		project.printDebugLog(writer);
		if (writer != null)
			writer.close();
	}

	private static InputArgs parseArguments(String[] args) {
		Options argOptions = new Options();

        Option input = new Option("i", "Input", true, "Input source folder path");
        input.setRequired(true);
        argOptions.addOption(input);

        Option output = new Option("o", "Output", true, "Path to the output folder");
        output.setRequired(true);
        argOptions.addOption(output);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(argOptions, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Designite", argOptions);
            Logger.log("Quitting..");
            System.exit(1);
        }
        if(cmd==null)
        {
        	System.out.println("Couldn't parse the command line arguments.");
        	formatter.printHelp("Designite", argOptions);
        	Logger.log("Quitting..");
        	System.exit(2);
        }
        
        	String inputFolderPath = cmd.getOptionValue("Input");
        String outputFolderPath = cmd.getOptionValue("Output");
        
        InputArgs inputArgs= null;
        try
        {
        	inputArgs = new InputArgs(inputFolderPath, outputFolderPath);
        }
        catch(IllegalArgumentException ex)
        {
        		Logger.log(ex.getMessage());
        		Logger.log("Quitting..");
        		System.exit(3);
        }
        return inputArgs;
	}

	private static String getlogFileName(InputArgs argsObj) {
		String file = null;
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
				Logger.log(ex.getMessage());
			}
		}
		return writer;
	}
}
