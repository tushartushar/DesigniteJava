package DesigniteTests.metrics;

import static org.junit.Assert.assertEquals;

import java.io.File;

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
		project = new SM_Project(new InputArgs(getTestingPath() + File.separator + "metrics", getTestingPath()));
		project.parse();
		project.resolve();
		project.computeMetrics();
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
