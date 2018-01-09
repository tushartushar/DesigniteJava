package DesigniteTests.smells.designSmells;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.TypeMetrics;
import Designite.smells.designSmells.ModularizationSmellDetector;

public class ModularizationSmellDetectorTest {
	
	private SourceItemInfo info = new SourceItemInfo("testProject", "testPackage", "testType");
	
	@Test
	public void testInsufficientModularizationForm1WhenTrue() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getNumOfPublicMethods()).thenReturn(25);
		ModularizationSmellDetector detector = new ModularizationSmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectCodeSmells().size();
		
		assertEquals(expected, actual);
	}
}
