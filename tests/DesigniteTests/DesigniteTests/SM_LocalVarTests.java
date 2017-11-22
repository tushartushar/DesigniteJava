package DesigniteTests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.junit.Before;
import org.junit.Test;

import Designite.InputArgs;
import Designite.SourceModel.SM_Field;
import Designite.SourceModel.SM_LocalVar;
import Designite.SourceModel.SM_Method;
import Designite.SourceModel.SM_Project;
import Designite.SourceModel.SM_Type;

public class SM_LocalVarTests {

	// Set this path before executing tests
	// private static String TESTS_PATH =
	// "C:\\Users\\Alex\\workspace\\DesigniteJava\\tests\\TestFiles";
	private static String TESTS_PATH = "/Users/Tushar/Documents/Workspace/DesigniteJava/tests/TestFiles";
	private SM_Project project;
	private SM_LocalVar newLocalVar;
	private SM_Type type;
	private List<SM_Method> methods;
	List<VariableDeclarationFragment> fieldList;

	@Before
	public void setUp() {
		project = new SM_Project(new InputArgs(TESTS_PATH + File.separator + "parameterTestInput.txt"));
		project.parse();
		project.resolve();
		methods = project.getPackageList().get(0).getTypeList().get(0).getMethodList();
	}
	
	@Test
	public void SM_LocalVar_getType() {
		newLocalVar = methods.get(0).getLocalVarList().get(0);
		assertEquals(newLocalVar.isPrimitive(), true);
		assertEquals(newLocalVar.getPrimitiveType(), "int");
	}

	@Test
	public void SM_LocalVar_getType_from_source() {
		newLocalVar = methods.get(1).getLocalVarList().get(0);
		assertEquals(newLocalVar.isPrimitive(), false);
		assertEquals(newLocalVar.getType().getName(), "TestMethods");
	}
}
