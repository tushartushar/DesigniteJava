package DesigniteTests.smells.designSmells;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.TypeMetrics;
import Designite.smells.ThresholdsDTO;
import Designite.smells.designSmells.ModularizationSmellDetector;

public class ModularizationSmellDetectorTest {
	
	private SourceItemInfo info = new SourceItemInfo("testProject", "testPackage", "testType");
	private ThresholdsDTO thresholds = new ThresholdsDTO();
	
	@Test
	public void testInsufficientModularizationHappyPath() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getNumOfPublicMethods())
				.thenReturn(thresholds.getInsufficientModularizationLargePublicInterface() - 1);
		when(metrics.getNumOfMethods())
				.thenReturn(thresholds.getInsufficientModularizationLargeNumOfMethods() - 1);
		when(metrics.getWeightedMethodsPerClass())
				.thenReturn(thresholds.getInsufficientModularizationHighComplexity() - 1);
		ModularizationSmellDetector detector = new ModularizationSmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectInsufficientModularization().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testInsufficientModularizationWhenLargePublicInteface() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getNumOfPublicMethods())
				.thenReturn(thresholds.getInsufficientModularizationLargePublicInterface() + 1);
		ModularizationSmellDetector detector = new ModularizationSmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectInsufficientModularization().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testInsufficientModularizationWhenLargeNumOfMethods() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getNumOfMethods())
				.thenReturn(thresholds.getInsufficientModularizationLargeNumOfMethods() + 1);
		ModularizationSmellDetector detector = new ModularizationSmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectInsufficientModularization().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testInsufficientModularizationWhenHighComplexity() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getWeightedMethodsPerClass())
				.thenReturn(thresholds.getInsufficientModularizationHighComplexity() + 1);
		ModularizationSmellDetector detector = new ModularizationSmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectInsufficientModularization().size();
		
		assertEquals(expected, actual);
	}
}
