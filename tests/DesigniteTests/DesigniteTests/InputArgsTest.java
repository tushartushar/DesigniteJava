package DesigniteTests;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import Designite.InputArgs;

public class InputArgsTest extends DesigniteTests {

	// Negative case- folder path specified rather than input batch file
	@Test(expected = IllegalArgumentException.class)
	public void testInputArgs_negative_folder() {
		new InputArgs(null, getTestingPath());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInputArgs_passingInvalidFilePath_ReturnsException() {
		new InputArgs(getTestingPath() + File.separator + "invalidFile.txt", getTestingPath());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInputArgs_passingFilePath_ReturnsException() {
		new InputArgs(getTestingPath() + File.separator + "TestFiles" + File.separator + "singleJavaFile.txt", getTestingPath());
	}
	
	@Test
	public void testInputArgs_getProjectName() {
		InputArgs args = new InputArgs(System.getProperty("user.dir"), getTestingPath());
		String currentProjectDir = new File(System.getProperty("user.dir")).getName();
		assertEquals(currentProjectDir, args.getProjectName());
	}
	
	@Test
	public void testInputArgs_getProjectName_src() {
		InputArgs args = new InputArgs(System.getProperty("user.dir") + File.separator + "src", getTestingPath());
		String currentProjectDir = new File(System.getProperty("user.dir")).getName();
		assertEquals(currentProjectDir, args.getProjectName());
	}
}