package DesigniteTests.smells.designSmells;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.TypeMetrics;
import Designite.smells.ThresholdsDTO;
import Designite.smells.designSmells.AbstractionSmellDetector;
import Designite.smells.designSmells.EncapsulationSmellDetector;

public class EncapsulationSmellDetectorTest {

	private SourceItemInfo info = new SourceItemInfo("testProject", "testPackage", "testType");
	private ThresholdsDTO thresholds = new ThresholdsDTO();
	
	@Test
	public void testDeficientEncapsulationWhenHappyPath() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getNumOfPublicFields()).thenReturn(0);
		EncapsulationSmellDetector detector = new EncapsulationSmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectDeficientEncapsulation().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDeficientEncapsulationWhenSmellIsDetected() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getNumOfPublicFields()).thenReturn(1);
		EncapsulationSmellDetector detector = new EncapsulationSmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectDeficientEncapsulation().size();
		
		assertEquals(expected, actual);
	}
}
