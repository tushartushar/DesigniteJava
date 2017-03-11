package DesigniteTests;

import static org.junit.Assert.*;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.junit.Before;
import org.junit.Test;

import Designite.InputArgs;
import Designite.SourceModel.SM_Field;
import Designite.SourceModel.SM_Project;
import Designite.SourceModel.SM_SourceItem.AccessStates;

public class SM_FieldTest {
	//Set this path before executing tests
	private static String TESTS_PATH = "C:\\Users\\Alex\\workspace\\DesigniteJava\\tests\\TestFiles";
	private SM_Project project;
	private SM_Field newField;
	private TypeDeclaration type;
	private FieldDeclaration[] fields;
	
	@Before
	public void setUp() {
		project = new SM_Project(new InputArgs(TESTS_PATH + "\\testBatchFile.txt"));
		CompilationUnit unit = project.createCU(TESTS_PATH + "\\test_package\\TestMethods.java");
		List<TypeDeclaration> typeList = unit.types();
		
		type = typeList.get(0);
		fields = type.getFields();
	}
	
	@Test
	public void SM_Field_getName() {
		newField = new SM_Field(fields[0], type);
		assertEquals(newField.getName(), "publicField");	
	}
	
	@Test
	public void SM_Field_checkAccess() {
		newField = new SM_Field(fields[0], type);
		if (newField.getName().equals("publicField"))
			assertEquals(newField.getAccessModifier(), AccessStates.PUBLIC);
		else 
			fail();
	}
	
	@Test
	public void SM_Field_checkType() {
		newField = new SM_Field(fields[0], type);
		if (newField.getName().equals("publicField"))
			assertEquals(newField.getType().toString(), "TestClass");
		else
			fail();
	}
	
	@Test
	public void SM_Field_check_StaticField() {
		newField = new SM_Field(fields[1], type);
		if (newField.getName().equals("counter"))
			assertTrue(newField.isStatic());
		else
			fail();
	}
	
	@Test
	public void SM_Field_check_FinalField() {
		newField = new SM_Field(fields[3], type);
		if (newField.getName().equals("CONSTANT"))
			assertTrue(newField.isFinal());
		else
			fail();
	}

}
