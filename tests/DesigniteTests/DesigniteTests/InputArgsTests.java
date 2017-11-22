package DesigniteTests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import Designite.InputArgs;

public class InputArgsTests extends DesigniteTests {

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
		assertEquals("/home/thodoras/Documents/workspace-sts-3.8.4.RELEASE/DesigniteJava/../temp/", args.getOutputFolder());
	}
}