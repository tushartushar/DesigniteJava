package DesigniteTests;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Designite.InputArgs;
import Designite.SourceModel.SM_Method;
import Designite.SourceModel.SM_Parameter;
import Designite.SourceModel.SM_Project;

public class SM_ParameterTest extends DesigniteTests {
	
	private SM_Project project;
	private SM_Parameter newParameter;
	private List<SM_Method> methods;
	private List<SM_Parameter> parameters;

	@Before
	public void setUp() {
		createFileForArguments(PARAMETER_TEST_INPUT_FILE_PATH, PARAMETER_TEST_INPUT_FILE_CONTENT);
		project = new SM_Project(new InputArgs(PARAMETER_TEST_INPUT_FILE_PATH));
		//CompilationUnit unit = project.createCU(TESTS_PATH + File.separator + "test_package" + File.separator + "TestMethods.java");
		project.parse();
		project.resolve();
		methods = project.getPackageList().get(0).getTypeList().get(0).getMethodList();
	}

	@Test
	public void SM_Parameter_check_getName() {
		parameters = methods.get(0).getParameterList();
		newParameter = parameters.get(0);
		assertEquals(newParameter.getName(), "name");
	}

	@Test
	public void SM_Parameter_getType() {
		parameters = methods.get(0).getParameterList();
		newParameter = parameters.get(0);
		assertEquals(newParameter.isPrimitive(), true);
		assertEquals(newParameter.getPrimitiveType(), "String");
	}

	@Test
	public void SM_Parameter_getType_from_source() {
		parameters = methods.get(1).getParameterList();
		newParameter = parameters.get(0);
		assertEquals(newParameter.isPrimitive(), false);
		assertEquals(newParameter.getType().getName(), "TestMethods");
	}
	@Test // is a List considered as SingleVariableDeclaration?
	public void SM_Parameter_check_listParameter() {
		parameters = methods.get(3).getParameterList();
		newParameter = parameters.get(0);
		assertEquals(newParameter.getPrimitiveType(), "List<String>");
	}
}
