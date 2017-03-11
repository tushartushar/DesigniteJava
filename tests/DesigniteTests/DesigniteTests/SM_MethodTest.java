package DesigniteTests;

import static org.junit.Assert.*;

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
	private static String TESTS_PATH = "C:\\Users\\Alex\\workspace\\DesigniteJava\\tests\\TestFiles";
	private SM_Project project;
	private SM_Method newMethod;
	private TypeDeclaration type;
	private MethodDeclaration[] methods;
	
	@Before
	public void setUp() {
		project = new SM_Project(new InputArgs(TESTS_PATH + "\\testBatchFile.txt"));
		CompilationUnit unit = project.createCU(TESTS_PATH + "\\test_package\\TestMethods.java");
		List<TypeDeclaration> typeList = unit.types();
		
		type = typeList.get(0);
		methods = type.getMethods();
	}
	
	@Test
	public void SM_Method_checkMethodName() {
		newMethod = new SM_Method(methods[1], type);
		assertEquals(newMethod.getName(), "publicMethod");
	}
	
	@Test
	public void SM_Method_checkAccess() {
		newMethod = new SM_Method(methods[1], type);
		assertEquals(newMethod.getAccessModifier(), AccessStates.PUBLIC);
	}
	
	@Test
	public void SM_Method_isConstructor() {
		newMethod = new SM_Method(methods[0], type);
		assertTrue(newMethod.isConstructor());
	}
	
	@Test
	public void SM_Method_isStatic() {
		newMethod = new SM_Method(methods[2], type);
		if (newMethod.getName().equals("count"))
			assertTrue(newMethod.isStatic());
		else
			fail();
	}
}
