package DesigniteTests.metrics;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import Designite.InputArgs;
import Designite.SourceModel.SM_Method;
import Designite.SourceModel.SM_Project;
import Designite.SourceModel.SM_Type;
import Designite.metrics.MethodMetrics;
import DesigniteTests.DesigniteTests;

public class MethodMetricsTest extends DesigniteTests {
	
	private SM_Project project;
	private MethodMetrics methodMetrics;
	
	@Before
	public void setUp() {
		createFileForArguments(METRICS_FILE_PATH, METRICS_FILE_CONTENT);
		project = new SM_Project(new InputArgs(METRICS_FILE_PATH));
		project.parse();
		project.resolve();
		project.extractMetrics();
		//SM_Type type = project.getPackageList().get(0).getTypeList().get(7);
		//SM_Method method = type.getMethodList().get(0);
		SM_Type type = getSpecificType("TestMetricsClass");
		SM_Method method = getSpecificMethod(type, "publicMethod");
		methodMetrics = type.getMetricsFromMethod(method);
	}
	
	@Test
	public void testNumOfParameters() {
		int expected = 5;
		int actual = methodMetrics.getNumOfParameters();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCyclomaicComplexity() {
		int expected = 9;
		int actual = methodMetrics.getCyclomaticComplexity();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNumOfLines() {
		int expected = 27;
		int actual = methodMetrics.getNumOfLines();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNumberOfDirectFieldsUsed() {
		SM_Type type = getSpecificType("TestMetricsClass");
		SM_Method method = getSpecificMethod(type, "publicMethod");
		//SM_Type child1 = project.getPackageList().get(0).getTypeList().get(2);
		//SM_Method method = child1.getMethodList().get(0);
		
		int expected = 2;
		int actual = type.getMetricsFromMethod(method).getDirectFieldAccesses().size();
		
		assertEquals(expected, actual);
	}

	private SM_Method getSpecificMethod(SM_Type type, String methodName) {
		for(SM_Method method:type.getMethodList())
			if(method.getName().equals(methodName))
				return method;
		return null;
	}

	private SM_Type getSpecificType(String name) {
		for(SM_Type type:project.getPackageList().get(0).getTypeList())
			if(type.getName().equals(name))
				return type;
		return null;
	}
}
