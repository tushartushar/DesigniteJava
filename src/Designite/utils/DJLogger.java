package Designite.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DJLogger {

    // This variable must be private, making it public may expose sensitive information
    private String logFile;

    private final static DJLogger DJ_LOGGER = getInstance();

    public static DJLogger getInstance() {
        if (DJ_LOGGER == null) {
            return new DJLogger();
        }
        return DJ_LOGGER;
    }

    /**
     * <b>Ensure to set {@code outputDirectory} before program execution,
     * as it creates log files in the specified
     * directory. Failing to provide a valid directory may result in errors.</b>
     * @param outputDirectory
     * @throws FileNotFoundException
     */
    public void setOutputDirectory(String outputDirectory) throws FileNotFoundException {
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(Calendar.getInstance().getTime());
        String logAbsolutePath = outputDirectory + File.separator + "DesigniteLog" + timeStamp + ".txt";
        FileManager.getInstance().createFileIfNotExists(logAbsolutePath);
        this.logFile = logAbsolutePath;
    }

    private DJLogger() {
    }

    /**
     * Logs the {@code logMessage} into the file.
     * @param logMessage
     */
    public static void log(String logMessage) {
        System.out.println(logMessage);
        if (DJ_LOGGER.logFile == null) {
            //Commented the following line just to make the execution non-verbose
            //System.out.println("Log file path has been not set. Logging not support.");
            return;
        }
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(DJ_LOGGER.logFile, true), StandardCharsets.UTF_8))) {
            writer.write(logMessage + "\n");
            //	Redundant close -> try-catch closes the file after execution.
            //	writer.close();
        } catch (IOException ex) {
            System.out.println("Exception during logging. " + ex.getMessage());
        }
    }
}
