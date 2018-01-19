package DesigniteTests.smells.designSmells;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
	private ThresholdsDTO thresholds = new ThresholdsDTO();
	
	@Test
	public void testMultifacetedAbstractionWhenHappyPath() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getLcom())
			.thenReturn(thresholds.getMultifacetedAbstractionLargeLCOM() - 1);
		when(metrics.getNumOfFields())
			.thenReturn(thresholds.getMultifacetedAbstractionManyFields() - 1);
		when(metrics.getNumOfMethods())
			.thenReturn(thresholds.getMultifacetedAbstractionManyMethods() - 1);
		AbstractionSmellDetector detector = new AbstractionSmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectMultifacetedAbstraction().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testUnutilizedAbstractionNoSuperClassWhenHappyPath() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		List<SM_Type> superTypes = mock(List.class);
		when(metrics.getNumOfFanInTypes()).thenReturn(1);
		when(metrics.getSuperTypes()).thenReturn(superTypes);
		when(superTypes.size()).thenReturn(0);
		AbstractionSmellDetector detector = new AbstractionSmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectUnutilizedAbstraction().size();
		
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
		int actual = detector.detectUnutilizedAbstraction().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testUnutilizedAbstractionWithSuperClassWhenHappyPath() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		TypeMetrics superTypeMetrics = mock(TypeMetrics.class);
		SM_Type superType = mock(SM_Type.class);
		List<SM_Type> superTypes = new ArrayList<>();
		superTypes.add(superType);
		when(metrics.getSuperTypes()).thenReturn(superTypes);
		when(superType.getTypeMetrics()).thenReturn(superTypeMetrics);
		when(metrics.getNumOfFanInTypes()).thenReturn(1);
		when(superTypeMetrics.getNumOfFanInTypes()).thenReturn(1);
		AbstractionSmellDetector detector = new AbstractionSmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectUnutilizedAbstraction().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testUnutilizedAbstractionWithSuperClassWhenTypeNoFanIn() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		TypeMetrics superTypeMetrics = mock(TypeMetrics.class);
		SM_Type superType = mock(SM_Type.class);
		List<SM_Type> superTypes = new ArrayList<>();
		superTypes.add(superType);
		when(metrics.getSuperTypes()).thenReturn(superTypes);
		when(superType.getTypeMetrics()).thenReturn(superTypeMetrics);
		when(metrics.getNumOfFanInTypes()).thenReturn(0);
		when(superTypeMetrics.getNumOfFanInTypes()).thenReturn(1);
		AbstractionSmellDetector detector = new AbstractionSmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectUnutilizedAbstraction().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testUnutilizedAbstractionWithSuperClassWhenSuperTypeNoFanIn() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		TypeMetrics superTypeMetrics = mock(TypeMetrics.class);
		SM_Type superType = mock(SM_Type.class);
		List<SM_Type> superTypes = new ArrayList<>();
		superTypes.add(superType);
		when(metrics.getSuperTypes()).thenReturn(superTypes);
		when(superType.getTypeMetrics()).thenReturn(superTypeMetrics);
		when(metrics.getNumOfFanInTypes()).thenReturn(1);
		when(superTypeMetrics.getNumOfFanInTypes()).thenReturn(0);
		AbstractionSmellDetector detector = new AbstractionSmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectUnutilizedAbstraction().size();
		
		assertEquals(expected, actual);
	}
}
