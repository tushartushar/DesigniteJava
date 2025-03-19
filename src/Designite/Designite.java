package Designite;

import Designite.ArgumentParser.ArgumentParser;
import Designite.ArgumentParser.CLIArgumentParser;
import Designite.ArgumentParser.InputArgs;
import Designite.ArgumentParser.RegularArgumentParser;
import Designite.SourceModel.SM_Project;
import Designite.utils.Constants;
import Designite.utils.DJLogger;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This is the start of the project
 */
public class Designite {
    public static void main(String[] args) throws FileNotFoundException {
        ArgumentParser argumentParser = (Constants.DEBUG) ? new RegularArgumentParser() : new CLIArgumentParser();
        InputArgs argsObj = argumentParser.parseArguments(args);
        DJLogger.getInstance().setOutputDirectory(argsObj.getOutputFolder());
        SM_Project project = new SM_Project(argsObj);
        project.parse();
        project.resolve();
        project.computeMetrics();
        project.detectCodeSmells();
        writeDebugLog(argsObj, project);
        DJLogger.log("Done.");
    }

    private static void writeDebugLog(InputArgs argsObj, SM_Project project) {
        if (Constants.DEBUG) {
            PrintWriter writer = getDebugLogStream(argsObj);
            project.printDebugLog(writer);
            if (writer != null) writer.close();
        }
    }

    private static PrintWriter getDebugLogStream(InputArgs argsObj) {
        PrintWriter writer = null;
        if (!argsObj.getOutputFolder().equals("")) {
            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(Calendar.getInstance().getTime());
            String filename = argsObj.getOutputFolder() + "DesigniteDebugLog" + timeStamp + ".txt";
            try {
                writer = new PrintWriter(filename);
            } catch (FileNotFoundException ex) {
                DJLogger.log(ex.getMessage());
            }
        }
        return writer;
    }
}
