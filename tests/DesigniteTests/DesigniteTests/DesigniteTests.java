package DesigniteTests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class DesigniteTests {
	
	protected static final String PARAMETER_TEST_INPUT_FILE_PATH = getTestingPath() + File.separator + "parameterTestInput.txt";
	protected static final String CALLED_METHOD_TEST_INPUT_FILE_PATH = getTestingPath() + File.separator + "calledMethodTestInput.txt";
	protected static final String TEST_BATCH_FILE_PATH = getTestingPath() + File.separator + "testBatchFile.txt";
	protected static final String IN_BATCH_FILE_PATH = getTestingPath() + File.separator + "inBatchFile.txt";
	protected static final String METRICS_FILE_PATH = getTestingPath() + File.separator + "metricsFile.txt";
	protected static final String CODE_SMELLS_FILE_PATH = getTestingPath() + File.separator + "codeSmellsFile.txt";
	
	protected static final String PARAMETER_TEST_INPUT_FILE_CONTENT = "[Source folder]\n"
			+ getTestingPath() + File.separator + "test_inputs";
	protected static final String CALLED_METHOD_TEST_INPUT_FILE_CONTENT = "[Source folder]\n"
			+ getTestingPath() + File.separator + "test_inputs2";
	protected static final String TEST_BATCH_FILE_CONTENT = "[Source folder]\n"
			+ getTestingPath() + File.separator + "test_package";
	protected static final String IN_BATCH_FILE_CONTENT = "[Source folder]\n"
			+ System.getProperty("user.dir") + "\n\n"
			+ "[Output folder]\n"
			+ System.getProperty("user.dir") + File.separator + "temp";
	protected static final String IN_BATCH_FILE_CONTENT_SRC = "[Source folder]\n"
			+ System.getProperty("user.dir") 
			+ File.separator + "src" + "\n\n"
			+ "[Output folder]\n"
			+ System.getProperty("user.dir") + File.separator + "temp";
	protected static final String IN_BATCH_FILE_CONTENT_SOURCE = "[Source folder]\n"
			+ System.getProperty("user.dir") 
			+ File.separator + "source" + "\n\n"
			+ "[Output folder]\n"
			+ System.getProperty("user.dir") + File.separator + "temp";
	protected static final String METRICS_FILE_CONTENT = "[Source folder]\n"
			+ getTestingPath() + File.separator + "metrics" + "\n\n"
			+ "[Output folder]\n"
			+ System.getProperty("user.dir") + File.separator + "temp";
	protected static final String CODE_SMELLS_FILE_CONTENT = "[Source folder]\n"
			+ getTestingPath() + File.separator + "codeSmells";
	
	protected static String getTestingPath() {
		String path = System.getProperty("user.dir") + 
				File.separator + "tests" + 
				File.separator + "TestFiles";
		
		return path;
	}
	
	protected void createFileForArguments(String path, String content) {
		try {
			File file = new File(path);
			if (!file.exists()) {
	            file.createNewFile(); 
	        }
			FileWriter fileWriter = new FileWriter(path, false);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(content);
			bufferedWriter.close();
		} catch(IOException e) {
			e.printStackTrace();
		} 
	}

}
