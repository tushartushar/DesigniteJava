package DesigniteTests.smells.designSmells;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;

import Designite.SourceModel.SM_Type;
import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.TypeMetrics;
import Designite.smells.ThresholdsDTO;
import Designite.smells.designSmells.AbstractionSmellDetector;
import Designite.smells.designSmells.ModularizationSmellDetector;

public class AbstractionSmellDetectorTest {
	
	private SourceItemInfo info = new SourceItemInfo("testProject", "testPackage", "testType");
	
	@Test
	public void testUnutilizedAbstractionNoSuperClassWhenHappyPath() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		List<SM_Type> superTypes = mock(List.class);
		when(metrics.getNumOfFanInTypes()).thenReturn(1);
		when(metrics.getSuperTypes()).thenReturn(superTypes);
		when(superTypes.size()).thenReturn(0);
		AbstractionSmellDetector detector = new AbstractionSmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectCodeSmells().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testUnutilizedAbstractionNoSuperClassWhenNoFanIn() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		List<SM_Type> superTypes = mock(List.class);
		when(metrics.getNumOfFanInTypes()).thenReturn(0);
		when(metrics.getSuperTypes()).thenReturn(superTypes);
		when(superTypes.size()).thenReturn(0);
		AbstractionSmellDetector detector = new AbstractionSmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectCodeSmells().size();
		
		assertEquals(expected, actual);
	}
}
