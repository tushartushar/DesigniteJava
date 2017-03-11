package DesigniteTests;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.junit.Before;
import org.junit.Test;

import Designite.InputArgs;
import Designite.SourceModel.SM_Parameter;
import Designite.SourceModel.SM_Project;

public class SM_ParameterTest {
	//Set this path before executing tests
	private static String TESTS_PATH = "C:\\Users\\Alex\\workspace\\DesigniteJava\\tests\\TestFiles";
	private SM_Project project;
	private SM_Parameter newParameter;
	private MethodDeclaration[] methods;
	private List<SingleVariableDeclaration> parameters;
	
	@Before
	public void setUp() {
		project = new SM_Project(new InputArgs(TESTS_PATH + "\\testBatchFile.txt"));
		CompilationUnit unit = project.createCU(TESTS_PATH + "\\test_package\\TestMethods.java");
		List<TypeDeclaration> typeList = unit.types();
		
		TypeDeclaration type = typeList.get(0);
		methods = type.getMethods();
	}
	
	@Test
	public void SM_Parameter_check_getName() {
		parameters = methods[0].parameters();	
		newParameter = new SM_Parameter(parameters.get(0), methods[0]);
		assertEquals(newParameter.getName(), "name");
	}
	
	@Test
	public void SM_Parameter_getType() {
		parameters = methods[0].parameters();	
		newParameter = new SM_Parameter(parameters.get(0), methods[0]);
		assertEquals(newParameter.getType().toString(), "String");
	}
	
	@Test //is a List considered as SingleVariableDeclaration?
	public void SM_Parameter_check_listParameter() {
		parameters = methods[3].parameters();
		newParameter = new SM_Parameter(parameters.get(0), methods[0]);
		assertEquals(newParameter.getType().toString(), "List");
	}
}
