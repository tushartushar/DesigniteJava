package Designite.smells;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import Designite.utils.Logger;

public class ThresholdsParser {
	
	private static final String emptySpaceRegex = "^\\s+$";
	private static final String legalFormatRegex = "^\\w+[\\ \\t]*=[\\ \\t]*\\d+(.d+)?[\\ \\t]*\\n?$";
	
	private final Pattern emptySpacePattern = Pattern.compile(emptySpaceRegex);
	private final Pattern legalFormatPattern = Pattern.compile(legalFormatRegex);
	
	private ThresholdsDTO thresholds = new ThresholdsDTO();
	private File file;
	
	public ThresholdsParser(String thresholdPath) {
		file = new File(thresholdPath);
	}
	
	public void parseThresholds() throws FileNotFoundException, IOException, IllegalArgumentException {
		checkFileExists();
		parseLineByLine();
	}
	
	private void checkFileExists() throws FileNotFoundException {
		if (!file.exists()) {
			String message = "constants.txt file not found in project folder.";
			Logger.log(message);
			throw new FileNotFoundException(message);
		}
	}
	
	private void parseLineByLine() throws IOException, IllegalArgumentException {
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			if (isNotEmpty(line)) {
				if (!isWellFormatted(line)) {
					String message = "Line: " + line + "\nis not of the form 'someDescription' = 'someNumber'";
					Logger.log(message);
					bufferedReader.close();
					throw new IllegalArgumentException(message);
				}
				String[] decomposedLine = line.replaceAll("\\s", "").split("=");
				setThresholdsStrategy(decomposedLine[0], Double.parseDouble(decomposedLine[1]));
			}
		}
		bufferedReader.close();
	}
	
	private boolean isNotEmpty(String line) {
		return !emptySpacePattern.matcher(line).matches();
	}
	
	private boolean isWellFormatted(String line) {
		return legalFormatPattern.matcher(line).matches();
	}
	
	private void setThresholdsStrategy(String key, Double value) throws IllegalArgumentException {
		if (key.equals("deepHierarchy")) {
			thresholds.setDeepHierarchy(value.intValue());
		} else if (key.equals("wideHierarchy")) {
			thresholds.setWideHierarchy(value.intValue());
		} else if (key.equals("insufficientModularizationLargePublicInterface")) {
			thresholds.setInsufficientModularizationLargePublicInterface(value.intValue());
		} else if (key.equals("insufficientModularizationLargeNumOfMethods")) {
			thresholds.setInsufficientModularizationLargeNumOfMethods(value.intValue());
		} else if (key.equals("insufficientModularizationHighComplexity")) {
			thresholds.setInsufficientModularizationHighComplexity(value.intValue());
		} else if (key.equals("wideHierarchy")) {
			thresholds.setWideHierarchy(value.intValue());
		} else {
			String message = "No such threshold: " + key;
			Logger.log(message);
			throw new IllegalArgumentException(message);
		}
	}
	
	public ThresholdsDTO getThresholds() {
		return thresholds;
	}

}
