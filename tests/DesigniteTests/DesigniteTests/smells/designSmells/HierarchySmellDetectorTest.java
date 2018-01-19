package DesigniteTests.smells.designSmells;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.TypeMetrics;
import Designite.smells.ThresholdsDTO;
import Designite.smells.designSmells.HierarchySmellDetector;

public class HierarchySmellDetectorTest {
	
	private SourceItemInfo info = new SourceItemInfo("testProject", "testPackage", "testType");
	private ThresholdsDTO thresholds = new ThresholdsDTO();
	
	@Test
	public void testDeepHierarchyHappyPath() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getInheritanceDepth())
				.thenReturn(thresholds.getDeepHierarchy() - 1);
		HierarchySmellDetector detector = new HierarchySmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectDeepHierarchy().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDeepHierarchyHappyPositive() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getInheritanceDepth())
				.thenReturn(thresholds.getDeepHierarchy() + 1);
		HierarchySmellDetector detector = new HierarchySmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectDeepHierarchy().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testWideHierarchyHappyPath() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getNumOfChildren())
				.thenReturn(thresholds.getWideHierarchy() - 1);
		HierarchySmellDetector detector = new HierarchySmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectWideHierarchy().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testWideHierarchyPositive() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getNumOfChildren())
				.thenReturn(thresholds.getWideHierarchy() + 1);
		HierarchySmellDetector detector = new HierarchySmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectWideHierarchy().size();
		
		assertEquals(expected, actual);
	}
	
}
