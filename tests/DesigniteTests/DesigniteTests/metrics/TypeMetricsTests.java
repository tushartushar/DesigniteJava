package DesigniteTests.metrics;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Designite.InputArgs;
import Designite.SourceModel.SM_Project;
import Designite.SourceModel.SM_Type;
import Designite.metrics.TypeMetrics;
import DesigniteTests.DesigniteTests;

public class TypeMetricsTests extends DesigniteTests {
	
	private SM_Project project;
	private TypeMetrics typeMetrics;
	
	@Before
	public void setUp() {
		createFileForArguments(METRICS_FILE_PATH, METRICS_FILE_CONTENT);
		project = new SM_Project(new InputArgs(METRICS_FILE_PATH));
		project.parse();
		project.resolve();
		project.extractMetrics();
		SM_Type type = project.getPackageList().get(0).getTypeList().get(7); 
		typeMetrics = type.getTypeMetrics();
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
		int expected = 33;
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
	
	public void testFanOutTypes() {
		int expected = 4;
		int actual = typeMetrics.getFanOutTypes();
		
		assertEquals(expected, actual);
	}
}
