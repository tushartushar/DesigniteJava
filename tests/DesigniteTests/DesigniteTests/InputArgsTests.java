package DesigniteTests;

import static org.junit.Assert.*;
import org.junit.Test;
import Designite.InputArgs;

public class InputArgsTests {
	//Set this path before executing tests
	private static String TESTS_PATH = "C:\\Users\\Alex\\workspace\\DesigniteJava\\tests";
	
	//Negative case- folder path specified rather than input batch file
	@Test(expected = IllegalArgumentException.class)
	public void testInputArgs_negative_folder() {
		new InputArgs(TESTS_PATH);
	}
	@Test(expected = IllegalArgumentException.class)
	public void testInputArgs_negative_invalidPath() {
		new InputArgs(TESTS_PATH+"\\invalidFile.txt");
	}
	@Test(expected = IllegalArgumentException.class)
	public void testInputArgs_negative_invalidContents() {
		new InputArgs(TESTS_PATH+"\\TestFiles\\invalidBatchFile.txt");
	}
	@Test
	public void testInputArgs_sourceFolder() {
		InputArgs args = new InputArgs(TESTS_PATH + "\\TestFiles\\inBatchFile.txt");
		assertEquals("C:\\Users\\Alex\\workspace\\DesigniteJava\\src", args.getSourceFolder());
	}
	@Test
	public void testInputArgs_outputFolder() {
		InputArgs args = new InputArgs(TESTS_PATH + "\\TestFiles\\inBatchFile.txt");
		assertEquals("C:\\Users\\Alex\\workspace\\DesigniteJava\\tests\\temp", args.getOutputFolder());
	}
}