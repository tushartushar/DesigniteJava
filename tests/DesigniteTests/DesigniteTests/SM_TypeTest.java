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
import Designite.SourceModel.SM_SourceItem.AccessStates;
import Designite.SourceModel.SM_Type;

public class SM_TypeTest {
	//Set this path before executing tests
	private static String TESTS_PATH = "C:\\Users\\Alex\\workspace\\DesigniteJava\\tests\\TestFiles";
	SM_Project project = new SM_Project(new InputArgs(TESTS_PATH + "\\testBatchFile.txt"));
	private List<SM_Package> packageList;
	private SM_Type type;
	
	@Test
	public void SM_Type_getName() {
		CompilationUnit unit = project.createCU(TESTS_PATH + "\\test_package\\TestClass.java");
		List<TypeDeclaration> listOfTypes = unit.types();
		
		if(listOfTypes.size() == 1) {
			type = new SM_Type(listOfTypes.get(0), unit);
			assertEquals(type.getName(), "TestClass");
		}		
	}
	
	//TODO check nested classes
	@Test
	public void SM_Type_nestedClass() {
		CompilationUnit unit = project.createCU(TESTS_PATH + "\\test_package\\NestedClass.java");
		List<TypeDeclaration> listOfTypes = unit.types();
		
		if(listOfTypes.size() == 1) {
			type = new SM_Type(listOfTypes.get(0), unit);
			assertEquals(type.getName(), "NestedClass");
		}		
	}

	@Test
	public void SM_Type_checkDefaultAccess() {
		CompilationUnit unit = project.createCU(TESTS_PATH + "\\test_package\\DefaultClass.java");
		List<TypeDeclaration> listOfTypes = unit.types();
		
		if(listOfTypes.size() == 1) {
			type = new SM_Type(listOfTypes.get(0), unit);
			assertEquals(type.getAccessModifier(), AccessStates.DEFAULT);
		}		
	}
	
	@Test(expected = NullPointerException.class)
	public void SM_Type_nullTypeDeclaration() {
		CompilationUnit unit = project.createCU(TESTS_PATH + "\\test_package\\TestClass.java");
		type = new SM_Type(null, unit);	
	}
	
	@Test(expected = NullPointerException.class)
	public void SM_Type_nullCompilationUnit() {
		CompilationUnit unit = project.createCU(TESTS_PATH + "\\test_package\\TestClass.java");
		List<TypeDeclaration> listOfTypes = unit.types();
		
		if(listOfTypes.size() == 1) {
			type = new SM_Type(listOfTypes.get(0), null);
		}	
	}
	
	@Test
	public void SM_Type_sizeOfTypeList() {
		SM_Project project = new SM_Project(new InputArgs(TESTS_PATH + "\\testBatchFile.txt"));
		project.parse();
		packageList = project.getPackageList();
		
		for (SM_Package pkg: packageList) {
			if (pkg.getName().equals("(default package)"))
				assertEquals(pkg.getTypeList().size(), 1);
			if (pkg.getName().equals("test_package"))
				assertEquals(pkg.getTypeList().size(), 4);
		}
	}
}
