package DesigniteTests.metrics;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import Designite.InputArgs;
import Designite.SourceModel.SM_Project;
import Designite.metrics.MethodMetrics;
import DesigniteTests.DesigniteTests;

public class MethodMetricsTests extends DesigniteTests {
	
	private SM_Project project;
	private MethodMetrics methodMetrics;
	
	@Before
	public void setUp() {
		createFileForArguments(METRICS_FILE_PATH, METRICS_FILE_CONTENT);
		project = new SM_Project(new InputArgs(METRICS_FILE_PATH));
		project.parse();
		project.resolve();
		project.extractMetrics();
		methodMetrics = project.getPackageList().get(0).getTypeList().get(1).getMethodList().get(0).getMethodMetrics();
	}
	
	@Test
	public void testNumOfParameters() {
		int expected = 3;
		int actual = methodMetrics.getNumOfParameters();
		
		assertEquals(expected, actual);
	}
}
