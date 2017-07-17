package DesigniteTests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.junit.Before;
import org.junit.Test;

import Designite.InputArgs;
import Designite.SourceModel.SM_Field;
import Designite.SourceModel.SM_Method;
import Designite.SourceModel.SM_Package;
import Designite.SourceModel.SM_Project;
import Designite.SourceModel.SM_Type;
import Designite.SourceModel.SM_SourceItem.AccessStates;

public class SM_MethodTest{
	//Set this path before executing tests
	//private static String TESTS_PATH = "C:\\Users\\Alex\\workspace\\DesigniteJava\\tests\\TestFiles";
	private static String TESTS_PATH = "/Users/Tushar/Documents/Workspace/DesigniteJava/tests/TestFiles";
	private SM_Project project;
	private SM_Method newMethod;
	private SM_Type type;
	private List<SM_Method> methods;
	
	@Before
	public void setUp() {
		project = new SM_Project(new InputArgs(TESTS_PATH + File.separator + "testBatchFile.txt"));
		CompilationUnit unit = project.createCU(TESTS_PATH + File.separator + "test_package" + File.separator + "TestMethods.java");
		List<TypeDeclaration> typeList = unit.types();
		
		for(TypeDeclaration typeDecl: typeList){
			type = new SM_Type(typeDecl, unit, new SM_Package("Test", project));
			type.parse();
		}

		methods = type.getMethodList();
	}
	
	@Test
	public void SM_Method_checkMethodName() {
		assertEquals(methods.get(1).getName(), "publicMethod");
	}
	
	@Test
	public void SM_Method_checkAccess() {
		assertEquals(methods.get(1).getAccessModifier(), AccessStates.PUBLIC);
	}
	
	@Test
	public void SM_Method_isConstructor() {
		assertTrue(methods.get(0).isConstructor());
	}
	
	@Test
	public void SM_Method_isStatic() {
		if (methods.get(2).getName().equals("count"))
			assertTrue(methods.get(2).isStatic());
		else
			fail();
	}
	
	/*@Test
	public void SM_Method_getParent() {
		project.parse();
		
		for (SM_Package pkg: project.getPackageList()) {
			if (pkg.getName().equals("test_package")) {
				for (SM_Type type:pkg.getTypeList()) {
					if (type.getName().equals("TestMethods")) {
						for (SM_Method method:type.getMethodList()) {
							if (method.getName().equals("count"))
								assertEquals(method.getParent().getName(), "TestMethods");
						}
					}
				}
			}
		}
	}*/
}
