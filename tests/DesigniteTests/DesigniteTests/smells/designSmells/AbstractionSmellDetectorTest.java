package DesigniteTests.smells.designSmells;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import Designite.SourceModel.AccessStates;
import Designite.SourceModel.SM_Method;
import Designite.SourceModel.SM_Type;
import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.MethodMetrics;
import Designite.metrics.TypeMetrics;
import Designite.smells.ThresholdsDTO;
import Designite.smells.designSmells.AbstractionSmellDetector;

public class AbstractionSmellDetectorTest {
	
	private SourceItemInfo info = new SourceItemInfo("testProject", "testPackage", "testType");
	private ThresholdsDTO thresholds = new ThresholdsDTO();
	
	@Test
	public void testImperativeAbstractionWhenHappyPathWithEnoughPublicMethods() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getNumOfPublicMethods())
			.thenReturn(2);
		when(metrics.getNumOfLines())
			.thenReturn(thresholds.getImperativeAbstractionLargeNumOfLines() + 1);
		AbstractionSmellDetector detector = new AbstractionSmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectImperativeAbstraction().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testImperativeAbstractionWhenHappyPathWithFewLines() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getNumOfPublicMethods())
			.thenReturn(1);
		
		SM_Method method = mock(SM_Method.class);
		when(method.getAccessModifier())
			.thenReturn(AccessStates.PUBLIC);
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		when(methodMetrics.getNumOfLines())
			.thenReturn(thresholds.getImperativeAbstractionLargeNumOfLines() - 1);
		SM_Type type = mock(SM_Type.class);
		when(type.getMetricsFromMethod(any(SM_Method.class)))
			.thenReturn(methodMetrics);
		when(metrics.getType())
			.thenReturn(type);
		when(type.getMethodList())
			.thenReturn(new ArrayList<>(Arrays.asList(method)));
		AbstractionSmellDetector detector = new AbstractionSmellDetector(metrics, info);		
		
		int expected = 0;
		int actual = detector.detectImperativeAbstraction().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testImperativeAbstractionWhenHasSmell() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getNumOfPublicMethods())
			.thenReturn(1);
		
		SM_Method method = mock(SM_Method.class);
		when(method.getAccessModifier())
			.thenReturn(AccessStates.PUBLIC);
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		when(methodMetrics.getNumOfLines())
			.thenReturn(thresholds.getImperativeAbstractionLargeNumOfLines() + 1);
		SM_Type type = mock(SM_Type.class);
		when(type.getMetricsFromMethod(any(SM_Method.class)))
			.thenReturn(methodMetrics);
		when(metrics.getType())
			.thenReturn(type);
		when(type.getMethodList())
			.thenReturn(new ArrayList<>(Arrays.asList(method)));
		AbstractionSmellDetector detector = new AbstractionSmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectImperativeAbstraction().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMultifacetedAbstractionWhenHappyPathWithEnoughLCOM() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getLcom())
			.thenReturn(thresholds.getMultifacetedAbstractionLargeLCOM() - 1);
		when(metrics.getNumOfFields())
			.thenReturn(thresholds.getMultifacetedAbstractionManyFields() + 1);
		when(metrics.getNumOfMethods())
			.thenReturn(thresholds.getMultifacetedAbstractionManyMethods() + 1);
		AbstractionSmellDetector detector = new AbstractionSmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectMultifacetedAbstraction().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMultifacetedAbstractionWhenHappyPathWithFewFields() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getLcom())
			.thenReturn(thresholds.getMultifacetedAbstractionLargeLCOM() + 1);
		when(metrics.getNumOfFields())
			.thenReturn(thresholds.getMultifacetedAbstractionManyFields() - 1);
		when(metrics.getNumOfMethods())
			.thenReturn(thresholds.getMultifacetedAbstractionManyMethods() + 1);
		AbstractionSmellDetector detector = new AbstractionSmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectMultifacetedAbstraction().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMultifacetedAbstractionWhenHappyPathWithFewMethods() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getLcom())
			.thenReturn(thresholds.getMultifacetedAbstractionLargeLCOM() + 1);
		when(metrics.getNumOfFields())
			.thenReturn(thresholds.getMultifacetedAbstractionManyFields() + 1);
		when(metrics.getNumOfMethods())
			.thenReturn(thresholds.getMultifacetedAbstractionManyMethods() - 1);
		AbstractionSmellDetector detector = new AbstractionSmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectMultifacetedAbstraction().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMultifacetedAbstractonWhenHasSmell() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getLcom())
			.thenReturn(thresholds.getMultifacetedAbstractionLargeLCOM() + 1);
		when(metrics.getNumOfFields())
			.thenReturn(thresholds.getMultifacetedAbstractionManyFields() + 1);
		when(metrics.getNumOfMethods())
			.thenReturn(thresholds.getMultifacetedAbstractionManyMethods() + 1);
		AbstractionSmellDetector detector = new AbstractionSmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectMultifacetedAbstraction().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testUnnecessaryAbstractionWhenHappyPath() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getNumOfMethods())
			.thenReturn(1);
		when(metrics.getNumOfFields())
			.thenReturn(thresholds.getUnnecessaryAbstractionFewFields() + 1);
		AbstractionSmellDetector detector = new AbstractionSmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectUnnecessaryAbstraction().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testUnnecessaryAbstractionWhenHasSmell() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getNumOfMethods())
			.thenReturn(0);
		when(metrics.getNumOfFields())
			.thenReturn(thresholds.getUnnecessaryAbstractionFewFields() - 1);
		AbstractionSmellDetector detector = new AbstractionSmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectUnnecessaryAbstraction().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testUnutilizedAbstractionNoSuperClassWhenHappyPath() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		SM_Type type = mock(SM_Type.class);
		List<SM_Type> superTypes = mock(List.class);
		when(metrics.getNumOfFanInTypes()).thenReturn(1);
		when(metrics.getType()).thenReturn(type);
		when(type.getSuperTypes()).thenReturn(superTypes);
		when(superTypes.size()).thenReturn(0);
		AbstractionSmellDetector detector = new AbstractionSmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectUnutilizedAbstraction().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testUnutilizedAbstractionNoSuperClassWhenNoFanIn() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		SM_Type type = mock(SM_Type.class);
		List<SM_Type> superTypes = mock(List.class);
		when(metrics.getNumOfFanInTypes()).thenReturn(0);
		when(metrics.getType()).thenReturn(type);
		when(type.getSuperTypes()).thenReturn(superTypes);
		when(superTypes.size()).thenReturn(0);
		AbstractionSmellDetector detector = new AbstractionSmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectUnutilizedAbstraction().size();
		
		assertEquals(expected, actual);
	}
	
//	@Test
//	public void testUnutilizedAbstractionWithSuperClassWhenHappyPath() {
//		TypeMetrics metrics = mock(TypeMetrics.class);
//		TypeMetrics superTypeMetrics = mock(TypeMetrics.class);
//		SM_Type superType = mock(SM_Type.class);
//		List<SM_Type> superTypes = new ArrayList<>();
//		superTypes.add(superType);
//		when(metrics.getSuperTypes()).thenReturn(superTypes);
//		when(superType.getTypeMetrics()).thenReturn(superTypeMetrics);
//		when(metrics.getNumOfFanInTypes()).thenReturn(1);
//		when(superTypeMetrics.getNumOfFanInTypes()).thenReturn(1);
//		AbstractionSmellDetector detector = new AbstractionSmellDetector(metrics, info);
//		
//		int expected = 0;
//		int actual = detector.detectUnutilizedAbstraction().size();
//		
//		assertEquals(expected, actual);
//	}
//	
//	@Test
//	public void testUnutilizedAbstractionWithSuperClassWhenTypeNoFanIn() {
//		TypeMetrics metrics = mock(TypeMetrics.class);
//		TypeMetrics superTypeMetrics = mock(TypeMetrics.class);
//		SM_Type superType = mock(SM_Type.class);
//		List<SM_Type> superTypes = new ArrayList<>();
//		superTypes.add(superType);
//		
//		when(metrics.getSuperTypes()).thenReturn(superTypes);
//		when(superType.getTypeMetrics()).thenReturn(superTypeMetrics);
//		when(metrics.getNumOfFanInTypes()).thenReturn(0);
//		when(superTypeMetrics.getNumOfFanInTypes()).thenReturn(1);
//		AbstractionSmellDetector detector = new AbstractionSmellDetector(metrics, info);
//		
//		int expected = 1;
//		int actual = detector.detectUnutilizedAbstraction().size();
//		
//		assertEquals(expected, actual);
//	}
//	
//	@Test
//	public void testUnutilizedAbstractionWithSuperClassWhenSuperTypeNoFanIn() {
//		TypeMetrics metrics = mock(TypeMetrics.class);
//		TypeMetrics superTypeMetrics = mock(TypeMetrics.class);
//		SM_Type superType = mock(SM_Type.class);
//		List<SM_Type> superTypes = new ArrayList<>();
//		superTypes.add(superType);
//		when(metrics.getSuperTypes()).thenReturn(superTypes);
//		when(superType.getTypeMetrics()).thenReturn(superTypeMetrics);
//		when(metrics.getNumOfFanInTypes()).thenReturn(1);
//		when(superTypeMetrics.getNumOfFanInTypes()).thenReturn(0);
//		AbstractionSmellDetector detector = new AbstractionSmellDetector(metrics, info);
//		
//		int expected = 1;
//		int actual = detector.detectUnutilizedAbstraction().size();
//		
//		assertEquals(expected, actual);
//	}
}
