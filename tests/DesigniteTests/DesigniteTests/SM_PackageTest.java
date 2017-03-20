package DesigniteTests;

import static org.junit.Assert.*;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Before;
import org.junit.Test;

import Designite.InputArgs;
import Designite.SourceModel.SM_Package;
import Designite.SourceModel.SM_Project;

public class SM_PackageTest {
	// Set this path before executing tests
	private static String TESTS_PATH = "C:\\Users\\Alex\\workspace\\DesigniteJava\\tests\\TestFiles";
	
	@Test
	public void SM_Package_positive_case() {
		SM_Project project = new SM_Project(new InputArgs(TESTS_PATH + "\\inBatchFile.txt"));
		project.parse();
		List<SM_Package> pkgList = project.getPackageList();

		for (SM_Package pkg : pkgList) {
			if (pkg.getName().equals("Designite"))
				assertEquals(pkg.countTypes(), 2);
			if (pkg.getName().equals("Designite.SourceModel"))
				assertEquals(pkg.countTypes(), 13);
		}
	}
	
	@Test
	//assert that every CU in pkgCUList is included in projectCUList
	public void SM_Package_cuList() {
		SM_Project project = new SM_Project(new InputArgs(TESTS_PATH + "\\testBatchFile.txt"));
		project.parse();

		List<CompilationUnit> projectCUList = project.getCompilationUnitList();
		List<SM_Package> pkgList = project.getPackageList();

		for (SM_Package pkg : pkgList) {
			List<CompilationUnit> pkgCUList = pkg.getCompilationUnitList();
			for (CompilationUnit pkgUnit : pkgCUList)
				assertTrue(projectCUList.contains(pkgUnit));
		}
	}

	@Test
	public void SM_Package_nullInput() {
		SM_Package pkg = new SM_Package(null);
		assertEquals(pkg.getName(), null);
	}

	@Test
	public void SM_Package_countTypes() {
		SM_Project project = new SM_Project(new InputArgs(TESTS_PATH + "\\testBatchFile.txt"));
		project.parse();
		List<SM_Package> pkgList = project.getPackageList();

		for (SM_Package pkg : pkgList) {
			if (pkg.getName().equals("(default package)")) {
				assertEquals(pkg.countTypes(), 1);
			}
			// Empty class is not included while counting types
			if (pkg.getName().equals("test_package")) {
				assertEquals(pkg.countTypes(), 7);
			}
		}
	}
	
	@Test
	public void SM_Package_getParent() {
		SM_Project project = new SM_Project(new InputArgs(TESTS_PATH + "\\testBatchFile.txt"));
		project.parse();
		List<SM_Package> pkgList = project.getPackageList();

		for (SM_Package pkg : pkgList) {
			if (pkg.getName().equals("Designite"))
				assertEquals(pkg.getParent(), project);
		}
	}
}
