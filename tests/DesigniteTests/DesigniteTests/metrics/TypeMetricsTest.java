package DesigniteTests.metrics;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import Designite.InputArgs;
import Designite.SourceModel.SM_Package;
import Designite.SourceModel.SM_Project;
import Designite.SourceModel.SM_Type;
import Designite.metrics.TypeMetrics;
import DesigniteTests.DesigniteTests;

public class TypeMetricsTest extends DesigniteTests {
	
	private SM_Project project;
	private TypeMetrics typeMetrics;
	
	@Before
	public void setUp() {
		createFileForArguments(METRICS_FILE_PATH, METRICS_FILE_CONTENT);
		project = new SM_Project(new InputArgs(getTestingPath() + File.separator + "metrics", getTestingPath()));
		project.parse();
		project.resolve();
		project.computeMetrics();
		SM_Package pkg = project.getPackageList().get(0);
		SM_Type type = getSpecificType("TestMetricsClass");
		typeMetrics = pkg.getMetricsFromType(type);
	}
	
	private SM_Type getSpecificType(String name) {
		for(SM_Type type:project.getPackageList().get(0).getTypeList())
			if(type.getName().equals(name))
				return type;
		return null;
	}
	
	@Test
	public void testNumOfFieldsProperlyReturned() {
		int expected = 3;
		int actual = typeMetrics.getNumOfFields();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNumOfPublicFieldsProperlyReturned() {
		int expected = 1;
		int actual = typeMetrics.getNumOfPublicFields();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNumOfMethodsProperlyReturned() {
		int expected = 2;
		int actual = typeMetrics.getNumOfMethods();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNumOfPublicMethodsProperlyReturned() {
		int expected = 1;
		int actual = typeMetrics.getNumOfPublicMethods();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testInheritanceDepth() {
		int expected = 3;
		int actual = typeMetrics.getInheritanceDepth();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNumOfLines() {
		int expected = 34;
		int actual = typeMetrics.getNumOfLines();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNumOfChildren() {
		int expected = 2;
		int actual = typeMetrics.getNumOfChildren();
		
		assertEquals(expected, actual);

	}
	
	@Test
	public void testWeightedMethodsPerClass() {
		int expected = 10;
		int actual = typeMetrics.getWeightedMethodsPerClass();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testFanOutTypes() {
		int expected = 5;
		int actual = typeMetrics.getNumOfFanOutTypes();
		
		//SM_Type type = getSpecificType("TestMetricsClass");
		//System.out.println(type.getReferencedTypeList().toString());
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testFanInTypes() {
		int expected = 2;
		int actual = typeMetrics.getNumOfFanInTypes();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLCOMWhenInterface() {
		SM_Package pkg = project.getPackageList().get(0);
		SM_Type someInterface = pkg.getTypeList().get(0);
		double delta = 0.01;
		
		double expected = -1.0;
		double actual = pkg.getMetricsFromType(someInterface).getLcom();
		
		assertEquals(expected, actual, delta);
	}
	
	@Test
	public void testLCOMWhenNoFields() {
		SM_Package pkg = project.getPackageList().get(0);
		SM_Type foreignClass4 = pkg.getTypeList().get(10);
		double delta = 0.01;
		
		double expected = -1.0;
		double actual = pkg.getMetricsFromType(foreignClass4).getLcom();
		
		assertEquals(expected, actual, delta);
	}
	
	@Test
	public void testLCOMWhenNoMethods() {
		SM_Package pkg = project.getPackageList().get(0);
		//SM_Type foreignClass1 = pkg.getTypeList().get(9);
		SM_Type foreignClass1 = getSpecificType("ForeignClass1");
		double delta = 0.01;
		
		double expected = -1.0;
		double actual = pkg.getMetricsFromType(foreignClass1).getLcom();
		
		assertEquals(expected, actual, delta);
	}
	
	@Test
	public void testLCOMWhenHasTwoComponents() {
		double delta = 0.01;
		
		double expected = 1.0;
		double actual = typeMetrics.getLcom();
		
		assertEquals(expected, actual, delta);
	}
	
	@Test
	public void testLCOMWhenHasOneComponent() {
		SM_Package pkg = project.getPackageList().get(0);
		SM_Type child2 = getSpecificType("Child2");
		double delta = 0.01;
		
		double expected = 0.0;
		double actual = pkg.getMetricsFromType(child2).getLcom();
		
		assertEquals(expected, actual, delta);
	}
}
