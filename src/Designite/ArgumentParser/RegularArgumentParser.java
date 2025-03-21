package Designite.ArgumentParser;

import Designite.utils.DJLogger;

import java.io.File;


/**
 * {@code RegularArgumentParser} is a subclass of {@code ArgumentParser} that does not require any
 * arguments. By default, it will use the current working directory (cwd) of the Java project
 * to identify or analyze the smells in the current working project itself.
 */
public class RegularArgumentParser extends ArgumentParser {

    @Override
    public InputArgs parseArguments(String[] args) {
        String cwd = System.getProperty("user.dir");
        String inputFolderPath = String.join(File.separator, cwd, "src","Designite");
        String outputFolderPath = String.join(File.separator, cwd, "output");
        try {
            return new InputArgs(inputFolderPath, outputFolderPath);
        } catch (IllegalArgumentException ex) {
            DJLogger.log(ex.getMessage());
            DJLogger.log("Quitting..");
            System.exit(3);
        }
        return null;
    }

}
