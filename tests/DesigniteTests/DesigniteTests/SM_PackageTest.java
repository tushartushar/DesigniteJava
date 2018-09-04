package DesigniteTests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Before;
import org.junit.Test;

import Designite.InputArgs;
import Designite.SourceModel.SM_Package;
import Designite.SourceModel.SM_Project;

public class SM_PackageTest extends DesigniteTests {
	
	@Before
	public void setup() {
		
	}

	@Test
	public void SM_Package_positive_case() {
		SM_Project project = new SM_Project(new InputArgs(System.getProperty("user.dir"), getTestingPath()));
		project.parse();
		List<SM_Package> pkgList = project.getPackageList();

		for (SM_Package pkg : pkgList) {
			if (pkg.getName().equals("Designite"))
				assertEquals(pkg.getTypeList().size(), 2);
			if (pkg.getName().equals("Designite.SourceModel"))
				assertEquals(20, pkg.getTypeList().size());
		}
	}

	@Test
	// assert that every CU in pkgCUList is included in projectCUList
	public void SM_Package_cuList() {
		SM_Project project = new SM_Project(new InputArgs(getTestingPath() + File.separator + "test_package", getTestingPath()));
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
		SM_Package pkg = new SM_Package(null, null, null);
		assertEquals(pkg.getName(), null);
	}

	@Test
	public void SM_Package_countTypes() {
		SM_Project project = new SM_Project(new InputArgs(getTestingPath() + File.separator + "test_package", getTestingPath()));
		project.parse();
		List<SM_Package> pkgList = project.getPackageList();

		for (SM_Package pkg : pkgList) {
			if (pkg.getName().equals("(default package)")) {
				assertEquals(pkg.getTypeList().size(), 1);
			}
			// Empty class is not included while counting types
			if (pkg.getName().equals("test_package")) {
				assertEquals(6, pkg.getTypeList().size());
			}
		}
	}

	@Test
	public void SM_Package_getParent() {
		SM_Project project = new SM_Project(new InputArgs(getTestingPath() + File.separator + "test_package", getTestingPath()));
		project.parse();
		List<SM_Package> pkgList = project.getPackageList();

		for (SM_Package pkg : pkgList) {
			if (pkg.getName().equals("Designite"))
				assertEquals(pkg.getParentProject(), project);
		}
	}
}
