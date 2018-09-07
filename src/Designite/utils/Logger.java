package Designite.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class Logger {
	public static String logFile = null;

	public static void log(String str) {
		System.out.println(str);
		if (logFile == null) {
			//Commented the following line just to make the execution non-verbose
			//System.out.println("Log file path has been not set. Logging not support.");
			return;
		}
		try (Writer writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(logFile, true), StandardCharsets.UTF_8))) {
			writer.write(str + "\n");
			writer.close();
		} catch (IOException ex) {
			System.out.println("Exception during logging. " + ex.getMessage());
		}
	}
}
