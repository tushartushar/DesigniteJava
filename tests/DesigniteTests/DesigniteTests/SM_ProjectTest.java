package DesigniteTests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import Designite.InputArgs;
import Designite.SourceModel.SM_Project;

public class SM_ProjectTest extends DesigniteTests {
	
	@Test
	public void testSM_Project_positive_case() {
		SM_Project project = new SM_Project(new InputArgs(getTestingPath() + File.separator + "inBatchFile.txt"));
		project.parse();

		assertEquals(9, project.getPackageCounter());

	}
	
/*	@Test
	public void testSM_Project_input_nullObj() {
		SM_Project project = new SM_Project(null);
		project.parse();
	}
	
	@Test
	public void testSM_Project_nullSourceFileList() {
		SM_Project project = new SM_Project(new InputArgs(TESTS_PATH + "\\testBatchFile.txt"));
		project.setSourceFileList(null);
		project.parse();
	}*/
	
	@Test(expected = NullPointerException.class)
	public void testSM_Project_nullCU() {
		SM_Project project = new SM_Project(new InputArgs(getTestingPath() + File.separator + "testBatchFile.txt"));
		project.setCompilationUnitList(null);
		project.parse();
	}
	
/*	@Test
	public void testSM_Project_null() {
		SM_Project project = new SM_Project(new InputArgs("C:\\Users\\Alex\\Desktop\\null.txt"));
		project.parse();
	}*/
	
	@Test
	public void testSM_Project_sourceFilesCounter() {
		SM_Project project = new SM_Project(new InputArgs(getTestingPath() + File.separator + "testBatchFile.txt"));
		project.parse();
		assertEquals(7, project.getSourceFileList().size());
	}
	
	@Test
	public void testSM_Project_cuCounter() {
		SM_Project project = new SM_Project(new InputArgs(getTestingPath() + File.separator + "testBatchFile.txt"));
		project.parse();
		assertEquals(7, project.getCompilationUnitList().size());
	}
	
	@Test
	public void testSM_Project_packageCounter() {
		SM_Project project = new SM_Project(new InputArgs(getTestingPath() + File.separator + "testBatchFile.txt"));
		project.parse();
		assertEquals(2, project.getPackageCounter());
	}
}
