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
		InputArgs args = new InputArgs(getTestingPath() + File.separator + "inBatchFile.txt");
		//assertEquals("C:\\Users\\Alex\\workspace\\DesigniteJava\\src", args.getSourceFolder());
//		assertEquals("/Users/Tushar/Documents/Workspace/DesigniteJava/", args.getSourceFolder());
		assertEquals("/home/thodoras/Documents/workspace-sts-3.8.4.RELEASE/DesigniteJava/", args.getSourceFolder());
	}

	@Test
	public void testInputArgs_outputFolder() {
		InputArgs args = new InputArgs(getTestingPath() + File.separator + "inBatchFile.txt");
		//assertEquals("C:\\Users\\Alex\\workspace\\DesigniteJava\\tests\\temp", args.getOutputFolder());
		assertEquals("/home/thodoras/Documents/workspace-sts-3.8.4.RELEASE/temp/", args.getOutputFolder());
	}
}