package DesigniteTests;

import static org.junit.Assert.*;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.junit.Before;
import org.junit.Test;

import Designite.InputArgs;
import Designite.SourceModel.SM_Field;
import Designite.SourceModel.SM_Package;
import Designite.SourceModel.SM_Project;
import Designite.SourceModel.SM_Type;
import Designite.SourceModel.SM_SourceItem.AccessStates;

public class SM_FieldTest {
	//Set this path before executing tests
	private static String TESTS_PATH = "C:\\Users\\Alex\\workspace\\DesigniteJava\\tests\\TestFiles";
	private SM_Project project;
	private SM_Field newField;
	private TypeDeclaration type;
	private FieldDeclaration[] fields;
	List<VariableDeclarationFragment> fieldList;
	
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
		fieldList = fields[0].fragments();
		if(fieldList.size() == 1) {
			newField = new SM_Field(fields[0], type);
			newField.setName(fieldList.get(0).getName().toString());
		} else
			fail();
		
		assertEquals(newField.getName(), "publicField");	
	}
	
	@Test
	public void SM_Field_checkAccess() {
		fieldList = fields[0].fragments();
		if(fieldList.size() == 1) {
			newField = new SM_Field(fields[0], type);
			newField.setName(fieldList.get(0).getName().toString());
		}

		if (newField.getName().equals("publicField"))
			assertEquals(newField.getAccessModifier(), AccessStates.PUBLIC);
		else 
			fail();
	}
	
	@Test
	public void SM_Field_checkType() {
		fieldList = fields[0].fragments();
		if(fieldList.size() == 1) {
			newField = new SM_Field(fields[0], type);
			newField.setName(fieldList.get(0).getName().toString());
		}
		
		if (newField.getName().equals("publicField"))
			assertEquals(newField.getType().toString(), "TestClass");
		else
			fail();
	}
	
	@Test
	public void SM_Field_check_StaticField() {
		fieldList = fields[1].fragments();
		if(fieldList.size() == 1) {
			newField = new SM_Field(fields[1], type);
			newField.setName(fieldList.get(0).getName().toString());
		}
		
		if (newField.getName().equals("counter"))
			assertTrue(newField.isStatic());
		else
			fail();
	}
	
	@Test
	public void SM_Field_check_FinalField() {
		fieldList = fields[3].fragments();
		if(fieldList.size() == 1) {
			newField = new SM_Field(fields[3], type);
			newField.setName(fieldList.get(0).getName().toString());
		}
		if (newField.getName().equals("CONSTANT"))
			assertTrue(newField.isFinal());
		else
			fail();
	}
	
	@Test
	public void SM_Method_getParent() {
		project.parse();
		
		for (SM_Package pkg: project.getPackageList()) {
			if (pkg.getName().equals("test_package")) {
				for (SM_Type type:pkg.getTypeList()) {
					if (type.getName().equals("TestMethods")) {
						for (SM_Field field:type.getFieldList()) {
							if (field.getName().equals("counter"))
								assertEquals(field.getParent().getName(), "TestMethods");
						}
					}
				}
			}
		}
	}
}
