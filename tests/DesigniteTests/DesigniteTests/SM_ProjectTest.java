package DesigniteTests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;

import org.junit.Test;

import Designite.InputArgs;
import Designite.SourceModel.SM_Package;
import Designite.SourceModel.SM_Project;

public class SM_ProjectTest extends DesigniteTests {
	
	@Test
	public void testSM_Project_positive_case() {
		SM_Project project = new SM_Project(new InputArgs( System.getProperty("user.dir"), getTestingPath()));
		project.parse();

		/*for (SM_Package pkg : project.getPackageList())
			System.out.println(pkg.getName());*/
		assertEquals(21, project.getPackageCount());

	}
	
	@Test(expected = NullPointerException.class)
	public void testSM_Project_nullCU() {
		SM_Project project = new SM_Project(new InputArgs(getTestingPath() + File.separator + "test_package", getTestingPath()));
		project.setCompilationUnitList(null);
		project.parse();
	}
	
	@Test
	public void testSM_Project_sourceFilesCounter() {
		SM_Project project = new SM_Project(new InputArgs(getTestingPath() + File.separator + "test_package", getTestingPath()));
		project.parse();
		assertEquals(7, project.getSourceFileList().size());
	}
	
	@Test
	public void testSM_Project_cuCounter() {
		SM_Project project = new SM_Project(new InputArgs(getTestingPath() + File.separator + "test_package", getTestingPath()));
		project.parse();
		assertEquals(7, project.getCompilationUnitList().size());
	}
	
	@Test
	public void testSM_Project_packageCounter() {
		SM_Project project = new SM_Project(new InputArgs(getTestingPath() + File.separator + "test_package", getTestingPath()));
		project.parse();
		assertEquals(2, project.getPackageCount());
	}
}
