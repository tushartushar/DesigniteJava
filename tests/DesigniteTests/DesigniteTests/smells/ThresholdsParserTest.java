package DesigniteTests.smells;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import Designite.smells.ThresholdsDTO;
import Designite.smells.ThresholdsParser;
import DesigniteTests.DesigniteTests;

public class ThresholdsParserTest extends DesigniteTests {
	
	@Test
	public void testNumberOfThresholds() {
		ThresholdsDTO dto = new ThresholdsDTO();
		
		int expected = 19;
		int actual = dto.getClass().getDeclaredFields().length;
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testParseThresholdsHappyPath() {
		ThresholdsParser parser = new ThresholdsParser(getTestingPath() 
				+ File.separator + "codeSmells"
				+ File.separator + "thresholdsDefault.txt");
		try {
			parser.parseThresholds();
		} catch(Exception e) {
			fail();		
		}
	}
	
	@Test
	public void testParseThresholdsThrowsErrorWhenFileIsMissing() {
		ThresholdsParser parser = new ThresholdsParser(getTestingPath() 
				+ File.separator + "codeSmells"
				+ File.separator + "missingFile.txt");
		try {
			parser.parseThresholds();
			fail();
		} catch(FileNotFoundException e) {
				
		} catch(Exception e) {
			fail();	
		}
	}
	
	@Test
	public void testParseThresholdThrowsErrorWhenNoNumberAsValue() {
		ThresholdsParser parser = new ThresholdsParser(getTestingPath() 
				+ File.separator + "codeSmells"
				+ File.separator + "wrongRowFormatNoNumberAsKey.txt");
		try {
			parser.parseThresholds();
		} catch(IllegalArgumentException e) {
			String expected = "Line: insufficientModularizationLargePublicInterface = abc"
					+ "\nis not of the form 'someDescription' = 'someNumber'";
			String actual = e.getMessage();
			//System.out.println(actual);
			assertEquals(expected, actual);
		} catch(Exception e) {
			fail();	
		}
	}
	
	@Test
	public void testParseThresholdThrowsErrorWhenMissingEquals() {
		ThresholdsParser parser = new ThresholdsParser(getTestingPath() 
				+ File.separator + "codeSmells"
				+ File.separator + "wrongRowFormatNoEquals.txt");
		try {
			parser.parseThresholds();
		} catch(IllegalArgumentException e) {
			String expected = "Line: insufficientModularizationLargePublicInterface  20"
					+ "\nis not of the form 'someDescription' = 'someNumber'";
			String actual = e.getMessage();
			//System.out.println(actual);
			assertEquals(expected, actual);
		} catch(Exception e) {
			fail();	
		}
	}
	
	@Test
	public void testParseThresholdThrowsErrorWhenWrongKey() {
		ThresholdsParser parser = new ThresholdsParser(getTestingPath() 
				+ File.separator + "codeSmells"
				+ File.separator + "wrongRowFormatNoEquals.txt");
		try {
			parser.parseThresholds();
		} catch(IllegalArgumentException e) {
			String expected = "Line: insufficientModularizationLargePublicInterface  20"
					+ "\nis not of the form 'someDescription' = 'someNumber'";
			String actual = e.getMessage();
			//System.out.println(actual);
			assertEquals(expected, actual);
		} catch(Exception e) {
			fail();	
		}
	}
	
	@Test
	public void testParseThresholdThrowsErrorWhenThresholdDoesNotExist() {
		ThresholdsParser parser = new ThresholdsParser(getTestingPath() 
				+ File.separator + "codeSmells"
				+ File.separator + "notExistingThreshold.txt");
		try {
			parser.parseThresholds();
			fail();
		} catch(IllegalArgumentException e) {
			String expected = "No such threshold: myTheshold";
			String actual = e.getMessage();
			//System.out.println(actual);
			assertEquals(expected, actual);
		} catch(Exception e) {
			fail();	
		}
	}
	
	@Test
	public void testParseThresholdReadsSuccessfullyFromConfigFile() {
		ThresholdsParser parser = new ThresholdsParser(getTestingPath() 
				+ File.separator + "codeSmells"
				+ File.separator + "thresholdsNonDefault.txt");
		try {
			parser.parseThresholds();
			
			int expected = 14;
			int actual = parser
					.getThresholds()
					.getInsufficientModularizationLargePublicInterface();
			
			assertEquals(expected, actual);
		} catch(Exception e) {
			fail();	
		}
	}
	
}
