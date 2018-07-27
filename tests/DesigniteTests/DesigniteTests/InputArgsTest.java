package DesigniteTests;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Ignore;
import org.junit.Test;
import Designite.InputArgs;
import Designite.utils.Constants;

public class InputArgsTest extends DesigniteTests {

	// Negative case- folder path specified rather than input batch file
	@Test(expected = IllegalArgumentException.class)
	public void testInputArgs_negative_folder() {
		new InputArgs(getTestingPath());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInputArgs_negative_invalidPath() {
		new InputArgs(getTestingPath() + File.separator + "invalidFile.txt");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInputArgs_negative_notFolderPath() {
		new InputArgs(getTestingPath() + File.separator + "TestFiles" + File.separator + "singleJavaFile.txt");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInputArgs_negative_invalidContents() {
		new InputArgs(getTestingPath()+ File.separator + "TestFiles" + File.separator + "invalidBatchFile.txt");
	}

	@Test
	public void testInputArgs_sourceFolder() {
		createFileForArguments(IN_BATCH_FILE_PATH, IN_BATCH_FILE_CONTENT);
		InputArgs args = new InputArgs(IN_BATCH_FILE_PATH);
		assertEquals(System.getProperty("user.dir"), args.getSourceFolder());
	}

	@Test
	public void testInputArgs_outputFolder() {
		createFileForArguments(IN_BATCH_FILE_PATH, IN_BATCH_FILE_CONTENT);
		InputArgs args = new InputArgs(IN_BATCH_FILE_PATH);
		assertEquals(System.getProperty("user.dir") + File.separator + "temp", args.getOutputFolder());
	}
	
	@Test
	public void testInputArgs_getProjectName() {
		createFileForArguments(IN_BATCH_FILE_PATH, IN_BATCH_FILE_CONTENT);
		InputArgs args = new InputArgs(IN_BATCH_FILE_PATH);
		String currentProjectDir = new File(System.getProperty("user.dir")).getName();
		assertEquals(currentProjectDir, args.getProjectName());
	}
	
	@Test
	public void testInputArgs_getProjectName_src() {
		createFileForArguments(IN_BATCH_FILE_PATH, IN_BATCH_FILE_CONTENT_SRC);
		InputArgs args = new InputArgs(IN_BATCH_FILE_PATH);
		String currentProjectDir = new File(System.getProperty("user.dir")).getName();
		assertEquals(currentProjectDir, args.getProjectName());
	}
	
	@Test @Ignore
	public void testInputArgs_getProjectName_source() {
		createFileForArguments(IN_BATCH_FILE_PATH, IN_BATCH_FILE_CONTENT_SOURCE);
		InputArgs args = new InputArgs(IN_BATCH_FILE_PATH);
		String currentProjectDir = new File(System.getProperty("user.dir")).getName();
		//System.out.println(currentProjectDir + " | " + args.getProjectName());
		assertEquals(currentProjectDir, args.getProjectName());
	}
	
	@Test 
	public void testInputArgs_setCsvDirectoryPath() {
		createFileForArguments(IN_BATCH_FILE_PATH, IN_BATCH_FILE_CONTENT);
		InputArgs args = new InputArgs(IN_BATCH_FILE_PATH);
		String outputFolder = args.getOutputFolder() +
				File.separator + args.getProjectName() +
				"_csv";
		/*
		 * Testing the private method <b>setCsvDirectoryPath</b> 
		 * using the reflection feature
		 */
		try {
			Method method = InputArgs.class.getDeclaredMethod("setCsvDirectoryPath");
			method.setAccessible(true);
			method.invoke(args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		assertEquals(outputFolder, Constants.CSV_DIRECTORY_PATH);
	}
	
}