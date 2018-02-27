package DesigniteTests.smells.implementationSmells;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.MethodMetrics;
import Designite.smells.ThresholdsDTO;
import Designite.smells.implementationSmells.ImplementationSmellDetector;

public class ImplementationSmellDetectorTest {
	
	private SourceItemInfo info = new SourceItemInfo("testProject", "testPackage", "testType", "testMethod");
	private ThresholdsDTO thresholds = new ThresholdsDTO();
	
	@Test
	public void testComplexMethodWhenHappyPath() {
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		when(methodMetrics.getCyclomaticComplexity()).thenReturn(thresholds.getComplexMethod() - 1);
		ImplementationSmellDetector detector = new ImplementationSmellDetector(methodMetrics, info);
		
		int expected = 0;
		int actual = detector.detectComplexMethod().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testComplexMethodWhenSmellIsDetected() {
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		when(methodMetrics.getCyclomaticComplexity()).thenReturn(thresholds.getComplexMethod() + 1);
		ImplementationSmellDetector detector = new ImplementationSmellDetector(methodMetrics, info);
		
		int expected = 1;
		int actual = detector.detectComplexMethod().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLongMethodWhenHappyPath() {
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		when(methodMetrics.getNumOfLines()).thenReturn(thresholds.getLongMethod() - 1);
		ImplementationSmellDetector detector = new ImplementationSmellDetector(methodMetrics, info);
		
		int expected = 0;
		int actual = detector.detectLongMethod().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLongMethodWhenSmellIsDetected() {
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		when(methodMetrics.getNumOfLines()).thenReturn(thresholds.getLongMethod() + 1);
		ImplementationSmellDetector detector = new ImplementationSmellDetector(methodMetrics, info);
		
		int expected = 1;
		int actual = detector.detectLongMethod().size();
		
		assertEquals(expected, actual);
	}

	@Test
	public void testLongParameterListWhenHappyPath() {
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		when(methodMetrics.getNumOfParameters()).thenReturn(thresholds.getLongParameterList() - 1);
		ImplementationSmellDetector detector = new ImplementationSmellDetector(methodMetrics, info);
		
		int expected = 0;
		int actual = detector.detectLongParameterList().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLongParameterListWhenSmellIsDetected() {
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		when(methodMetrics.getNumOfParameters()).thenReturn(thresholds.getComplexMethod() + 1);
		ImplementationSmellDetector detector = new ImplementationSmellDetector(methodMetrics, info);
		
		int expected = 1;
		int actual = detector.detectLongParameterList().size();
		
		assertEquals(expected, actual);
	}

}
