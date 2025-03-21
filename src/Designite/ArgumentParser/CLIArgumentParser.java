package Designite.ArgumentParser;

import Designite.utils.DJLogger;
import org.apache.commons.cli.*;

/**
 * {@code CLIArgumentParser} is a subclass of {@code ArgumentParser} that requires arguments from
 * the console application in order to process the given arguments.
 */
public class CLIArgumentParser extends ArgumentParser {

    @Override
    public InputArgs parseArguments(String[] args) {
        Options argOptions = new Options();
        argOptions.addOption(this.createRequiredOption("i", "Input", "Input source folder path"));
        argOptions.addOption(this.createRequiredOption("o", "Output", "Path to the output folder"));
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(argOptions, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Designite", argOptions);
            DJLogger.log("Quitting..");
            System.exit(1);
        }
        String inputFolderPath = cmd.getOptionValue("Input");
        String outputFolderPath = cmd.getOptionValue("Output");
        try {
            return new InputArgs(inputFolderPath, outputFolderPath);
        } catch (IllegalArgumentException ex) {
            DJLogger.log(ex.getMessage());
            DJLogger.log("Quitting..");
            System.err.printf("The specified input path does not exist: %s%n", inputFolderPath);
            System.exit(3);
        }
        return null;
    }

}
