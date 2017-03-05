package DesigniteTests;

import static org.junit.Assert.*;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.junit.Before;
import org.junit.Test;

import Designite.InputArgs;
import Designite.SourceModel.SM_Package;
import Designite.SourceModel.SM_Project;
import Designite.SourceModel.SM_Type;
import Designite.SourceModel.TypeVisitor;

public class SM_TypeTest {
	//Set this path before executing tests
	private static String TESTS_PATH = "C:\\Users\\Alex\\workspace\\DesigniteJava\\tests\\TestFiles";
	private List<SM_Package> packageList;
	
	@Before
	public void setUp() {
		SM_Project project = new SM_Project(new InputArgs(TESTS_PATH + "\\testBatchFile.txt"));
		project.parse();
		packageList = project.getPackageList();
	}
	
/*	@Test
	public void SM_Type_positive_case() {
		SM_Project project = new SM_Project(new InputArgs(TESTS_PATH + "\\testBatchFile.txt"));

		CompilationUnit unit = project.createCU(TESTS_PATH + "\\test_package\\TestClass.java");
		TypeVisitor visitor = new TypeVisitor(unit);
		unit.accept(visitor);
		List<SM_Type> list = visitor.getTypeList();
		for (SM_Type type:list) {
			assertEquals(type.getName(), "TestClass");
		}
		
	}*/
	
	@Test
	public void SM_Type_sizeOfTypeList() {
		for (SM_Package pkg: packageList) {
			if (pkg.getName().equals("(default package)"))
				assertEquals(pkg.getTypeList().size(), 1);
			if (pkg.getName().equals("test_package"))
				assertEquals(pkg.getTypeList().size(), 1);
		}
	}
}
