package DesigniteTests.metrics;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Designite.InputArgs;
import Designite.SourceModel.SM_Project;
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
		typeMetrics = project.getPackageList().get(0).getTypeList().get(0).getTypeMetrics();
	}
	
	@Test
	public void testNumOfFieldsProperlyReturned() {
		int expected = 2;
		int actual = typeMetrics.getNumOfFields();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNumOfPublicFieldsProperlyReturned() {
		int expected = 1;
		int actual = typeMetrics.getNumOfPublicFields();
		
		assertEquals(expected, actual);
	}
	
}
