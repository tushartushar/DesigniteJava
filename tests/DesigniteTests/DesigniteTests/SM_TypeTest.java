package DesigniteTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.junit.Before;
import org.junit.Test;

import Designite.InputArgs;
import Designite.SourceModel.AccessStates;
import Designite.SourceModel.SM_Package;
import Designite.SourceModel.SM_Project;
import Designite.SourceModel.SM_Type;

public class SM_TypeTest extends DesigniteTests {
	
	private SM_Project project;
	private SM_Type type;
	
	@Before
	public void setUp() {
		project = new SM_Project(new InputArgs(getTestingPath() + File.separator + "test_package", getTestingPath()));
	}
	
	@Test
	public void SM_Type_getName() {
		CompilationUnit unit = project.createCU(getTestingPath() + File.separator + "test_package" +File.separator + "TestClass.java");
		List<TypeDeclaration> typeList = unit.types();

		SM_Type type = null;
		for (TypeDeclaration typeDecl : typeList)
			type = new SM_Type(typeDecl, unit, new SM_Package("Test", project, null), null);
			
		assertEquals(type.getName(), "TestClass");		
	}

	@Test
	public void SM_Type_checkDefaultAccess() {
		CompilationUnit unit = project.createCU(getTestingPath() + File.separator + "test_package" +File.separator + "DefaultClass.java");
		List<TypeDeclaration> typeList = unit.types();

		SM_Type type = null;
		for (TypeDeclaration typeDecl : typeList)
			type = new SM_Type(typeDecl, unit, new SM_Package("Test", project, null), null);
		assertEquals(type.getAccessModifier(), AccessStates.DEFAULT);		
	}
	
	@Test
	public void SM_Type_check_isAbstract() {
		CompilationUnit unit = project.createCU(getTestingPath() + File.separator +"test_package" + File.separator + "AbstractClass.java");
		List<TypeDeclaration> typeList = unit.types();

		SM_Type type = null;
		for (TypeDeclaration typeDecl : typeList)
			type = new SM_Type(typeDecl, unit, new SM_Package("Test", project, null), null);
		assertTrue(type.isAbstract());		
	}
	
	@Test
	public void SM_Type_check_isInterface() {
		CompilationUnit unit = project.createCU(getTestingPath() + File.separator +"test_package"+File.separator +"Interface.java");
		List<TypeDeclaration> typeList = unit.types();

		SM_Type type = null;
		for (TypeDeclaration typeDecl : typeList)
			type = new SM_Type(typeDecl, unit, new SM_Package("Test", project, null), null);
		assertTrue(type.isInterface());		
	}
	
	@Test //too complicated for the moment 
	public void SM_Type_check_isNestedClass() {		
		SM_Project project = new SM_Project(new InputArgs(getTestingPath() + File.separator +"test_package", getTestingPath()));
		project.parse();
		List<SM_Package> packageList = project.getPackageList();
		
		for (SM_Package pkg: packageList) {
			if (pkg.getName().equals("test_package")) {
				List<SM_Type> list = pkg.getTypeList();
				for (SM_Type type:list) {
					if (type.getName().equals("InnerClass")) 
						assertTrue(type.isNestedClass());
				}
			}
		}
	}
	
	@Test(expected = NullPointerException.class)
	public void SM_Type_nullTypeDeclaration() {
		CompilationUnit unit = project.createCU(getTestingPath() + File.separator +"test_package"+File.separator +"TestClass.java");
		type = new SM_Type(null, unit, null, null);	
	}
	
	@Test(expected = NullPointerException.class)
	public void SM_Type_nullCompilationUnit() {
		CompilationUnit unit = project.createCU(getTestingPath() + File.separator +"test_package"+File.separator +"TestClass.java");
		List<TypeDeclaration> listOfTypes = unit.types();
		
		type = new SM_Type(listOfTypes.get(0), null, null, null);	
	}
	
	@Test
	public void SM_Type_countFields() {
		project.parse();
		List<SM_Package> packageList = project.getPackageList();
		
		for (SM_Package pkg: packageList) {
			if (pkg.getName().equals("test_package")) {
				List<SM_Type> list = pkg.getTypeList();
				for (SM_Type type:list) {
					if (type.getName().equals("TestMethods")) 
						assertEquals(type.getFieldList().size(), 5);
				}
			}
		}
	}
	
	@Test
	public void SM_Type_countMethods() {
		SM_Project project = new SM_Project(new InputArgs(getTestingPath() + File.separator +"test_package", getTestingPath()));
		project.parse();
		List<SM_Package> packageList = project.getPackageList();
		
		for (SM_Package pkg: packageList) {
			if (pkg.getName().equals("test_package")) {
				List<SM_Type> list = pkg.getTypeList();
				for (SM_Type type:list) {
					if (type.getName().equals("TestMethods")) 
						assertEquals(type.getMethodList().size(), 5);
				}
			}
		}
	}
	
	@Test
	public void testHierarchyGraph() {
		project = new SM_Project(new InputArgs(getTestingPath() + File.separator + "metrics", getTestingPath()));
		project.parse();
		project.resolve();
		
		int expected = 6;
		int actual = project.getHierarchyGraph().getConnectedComponnents().size();
		
		assertEquals(expected, actual);
	}
}
