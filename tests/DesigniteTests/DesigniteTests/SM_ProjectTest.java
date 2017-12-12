package DesigniteTests;

import static org.junit.Assert.*;

import org.junit.Test;

import Designite.InputArgs;
import Designite.SourceModel.SM_Project;

public class SM_ProjectTest extends DesigniteTests {
	
	@Test
	public void testSM_Project_positive_case() {
		createFileForArguments(IN_BATCH_FILE_PATH, IN_BATCH_FILE_CONTENT);
		SM_Project project = new SM_Project(new InputArgs(IN_BATCH_FILE_PATH));
		project.parse();

		assertEquals(11, project.getPackageCounter());

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
		createFileForArguments(TEST_BATCH_FILE_PATH, TEST_BATCH_FILE_CONTENT);
		SM_Project project = new SM_Project(new InputArgs(TEST_BATCH_FILE_PATH));
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
		createFileForArguments(TEST_BATCH_FILE_PATH, TEST_BATCH_FILE_CONTENT);
		SM_Project project = new SM_Project(new InputArgs(TEST_BATCH_FILE_PATH));
		project.parse();
		assertEquals(7, project.getSourceFileList().size());
	}
	
	@Test
	public void testSM_Project_cuCounter() {
		createFileForArguments(TEST_BATCH_FILE_PATH, TEST_BATCH_FILE_CONTENT);
		SM_Project project = new SM_Project(new InputArgs(TEST_BATCH_FILE_PATH));
		project.parse();
		assertEquals(7, project.getCompilationUnitList().size());
	}
	
	@Test
	public void testSM_Project_packageCounter() {
		createFileForArguments(TEST_BATCH_FILE_PATH, TEST_BATCH_FILE_CONTENT);
		SM_Project project = new SM_Project(new InputArgs(TEST_BATCH_FILE_PATH));
		project.parse();
		assertEquals(2, project.getPackageCounter());
	}
}
