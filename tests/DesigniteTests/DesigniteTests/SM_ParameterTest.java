package DesigniteTests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.junit.Before;
import org.junit.Test;

import Designite.InputArgs;
import Designite.SourceModel.SM_Method;
import Designite.SourceModel.SM_Package;
import Designite.SourceModel.SM_Parameter;
import Designite.SourceModel.SM_Project;
import Designite.SourceModel.SM_Type;

public class SM_ParameterTest implements TestPath {
	
//	private SM_Project project;
//	private SM_Parameter newParameter;
//	private List<SM_Method> methods;
//	private List<SM_Parameter> parameters;
//
//	@Before
//	public void setUp() {
//		project = new SM_Project(new InputArgs(TESTS_PATH + File.separator + "parameterTestInput.txt"));
//		//CompilationUnit unit = project.createCU(TESTS_PATH + File.separator + "test_package" + File.separator + "TestMethods.java");
//		project.parse();
//		project.resolve();
//		methods = project.getPackageList().get(0).getTypeList().get(0).getMethodList();
//	}
//
//	@Test
//	public void SM_Parameter_check_getName() {
//		parameters = methods.get(0).getParameterList();
//		newParameter = parameters.get(0);
//		assertEquals(newParameter.getName(), "name");
//	}
//
//	@Test
//	public void SM_Parameter_getType() {
//		parameters = methods.get(0).getParameterList();
//		newParameter = parameters.get(0);
//		assertEquals(newParameter.isPrimitive(), true);
//		assertEquals(newParameter.getVarPrimitiveType(), "String");
//	}
//
//	@Test
//	public void SM_Parameter_getType_from_source() {
//		parameters = methods.get(1).getParameterList();
//		newParameter = parameters.get(0);
//		assertEquals(newParameter.isPrimitive(), false);
//		assertEquals(newParameter.getVariableType().getName(), "TestMethods");
//	}
//	@Test // is a List considered as SingleVariableDeclaration?
//	public void SM_Parameter_check_listParameter() {
//		parameters = methods.get(3).getParameterList();
//		newParameter = parameters.get(0);
//		assertEquals(newParameter.isParameterized(), true);
//		assertEquals(newParameter.getVarPrimitiveType(), "String");
//	}
}
