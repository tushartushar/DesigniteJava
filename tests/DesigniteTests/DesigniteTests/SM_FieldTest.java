package DesigniteTests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.junit.Before;
import org.junit.Test;

import Designite.InputArgs;
import Designite.SourceModel.AccessStates;
import Designite.SourceModel.SM_Field;
import Designite.SourceModel.SM_Package;
import Designite.SourceModel.SM_Project;
import Designite.SourceModel.SM_Type;

public class SM_FieldTest extends DesigniteTests {

	private SM_Project project;
	private SM_Field newField;
	private List<SM_Field> fields;
	List<VariableDeclarationFragment> fieldList;

	@Before
	public void setUp() {
		project = new SM_Project(new InputArgs(getTestingPath() + File.separator + "test_inputs", getTestingPath()));
		project.parse();
		project.resolve();
		fields = project.getPackageList().get(0).getTypeList().get(0).getFieldList();
	}

	@Test
	public void SM_Field_getName() {
		assertEquals(fields.get(4).getName(), "publicField");
	}

	@Test
	public void SM_Field_checkAccess() {
		if (fields.get(4).getName().equals("publicField"))
			assertEquals(fields.get(4).getAccessModifier(), AccessStates.PUBLIC);
		else
			fail();
	}

	@Test
	public void SM_Field_checkParentType() {
		if (fields.get(4).getName().equals("publicField"))
			assertEquals(fields.get(4).getParentType().getName(), "TestMethods");
		else
			fail();
	}

	@Test
	public void SM_Field_check_StaticField() {
		if (fields.get(0).getName().equals("counter"))
			assertTrue(fields.get(0).isStatic());
		else
			fail();
	}

	@Test
	public void SM_Field_check_FinalField() {
		if (fields.get(3).getName().equals("CONSTANT"))
			assertTrue(fields.get(3).isFinal());
		else
			fail();
	}

	@Test
	public void SM_Method_getParent() {
		project.parse();

		for (SM_Package pkg : project.getPackageList()) {
			if (pkg.getName().equals("test_package")) {
				for (SM_Type type : pkg.getTypeList()) {
					if (type.getName().equals("TestMethods")) {
						for (SM_Field field : type.getFieldList()) {
							if (field.getName().equals("counter"))
								assertEquals(field.getParentType().getParentPkg().getParentProject().getName(),
										"Project");
						}
					}
				}
			}
		}
	}

	@Test
	public void SM_Method_getType() {
		newField = fields.get(0);
		assertEquals(newField.isPrimitiveType(), true);
		assertEquals(newField.getPrimitiveType(), "int");
	}

	@Test
	public void SM_Method_getType_from_source() {
		newField = fields.get(4);
		assertEquals(newField.isPrimitiveType(), false);
		assertEquals(newField.getType().getName(), "TestMethods");
	}

	@Test // is a List considered as SingleVariableDeclaration?
	public void SM_Method_check_listParameter() {
		newField = fields.get(1);
		assertEquals(newField.getPrimitiveType(), "List<String>");
	}
}
