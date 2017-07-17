package DesigniteTests;

import static org.junit.Assert.*;

import java.io.File;
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
	//private static String TESTS_PATH = "C:\\Users\\Alex\\workspace\\DesigniteJava\\tests\\TestFiles";
	private static String TESTS_PATH = "/Users/Tushar/Documents/Workspace/DesigniteJava/tests/TestFiles";
	private SM_Project project;
	private SM_Field newField;
	private SM_Type type;
	private List<SM_Field> fields;
	List<VariableDeclarationFragment> fieldList;
	
	@Before
	public void setUp() {
		String inFile = TESTS_PATH + File.separator + "testBatchFile.txt";
		project = new SM_Project(new InputArgs(inFile));
		CompilationUnit unit = project.createCU(TESTS_PATH + File.separator + "test_package" + File.separator + "TestMethods.java");
		List<TypeDeclaration> typeList = unit.types();
		
		for(TypeDeclaration typeDecl: typeList){
			type = new SM_Type(typeDecl, unit, new SM_Package("Test", project));
			type.parse();
		}

		fields = type.getFieldList();
	}
	
	@Test
	public void SM_Field_getName() {
		assertEquals(fields.get(0).getName(), "publicField");	
	}
	
	@Test
	public void SM_Field_checkAccess() {
		if (fields.get(0).getName().equals("publicField"))
			assertEquals(fields.get(0).getAccessModifier(), AccessStates.PUBLIC);
		else 
			fail();
	}
	
	@Test
	public void SM_Field_checkType() {		
		if (fields.get(0).getName().equals("publicField"))
			assertEquals(fields.get(0).getParentType().getName(), "TestMethods");
		else
			fail();
	}
	
	@Test
	public void SM_Field_check_StaticField() {
		if (fields.get(1).getName().equals("counter"))
			assertTrue(fields.get(1).isStatic());
		else
			fail();
	}
	
	@Test
	public void SM_Field_check_FinalField() {
		if (fields.get(4).getName().equals("CONSTANT"))
			assertTrue(fields.get(4).isFinal());
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
								assertEquals(field.getParentType().getParentPkg().getParentProject().getName(), "Project");
						}
					}
				}
			}
		}
	}
}
