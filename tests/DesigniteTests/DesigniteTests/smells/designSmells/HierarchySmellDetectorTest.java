package DesigniteTests.smells.designSmells;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Designite.SourceModel.SM_Method;
import Designite.SourceModel.SM_Parameter;
import Designite.SourceModel.SM_Type;
import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.MethodMetrics;
import Designite.metrics.TypeMetrics;
import Designite.smells.ThresholdsDTO;
import Designite.smells.designSmells.HierarchySmellDetector;

public class HierarchySmellDetectorTest {
	
	private SourceItemInfo info = new SourceItemInfo("testProject", "testPackage", "testType");
	private ThresholdsDTO thresholds = new ThresholdsDTO();
	
	@Test
	public void testCyclicHierarchyWhenHappyPath() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		SM_Type type = mock(SM_Type.class);
		SM_Type superType = mock(SM_Type.class);
		SM_Type superSuperType = mock(SM_Type.class);
		List<SM_Type> superTypes = new ArrayList<>();
		superTypes.add(superType);
		List<SM_Type> superSuperTypes = new ArrayList<>();
		superSuperTypes.add(superSuperType); 
		when(metrics.getType()).thenReturn(type);
		when(type.getSuperTypes()).thenReturn(superTypes);
		when(superType.getSuperTypes()).thenReturn(superSuperTypes);
		when(superType.getName()).thenReturn("foo");
		when(superSuperType.getSuperTypes()).thenReturn(new ArrayList<>());
		when(superSuperType.getName()).thenReturn("bar");
		HierarchySmellDetector detector = new HierarchySmellDetector(metrics, info);
		

		int expected = 0;
		int actual = detector.detectCyclicHierarchy().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCyclicHierarchyWhenSmellIsDetected() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		SM_Type type = mock(SM_Type.class);
		SM_Type superType = mock(SM_Type.class);
		SM_Type superSuperType = mock(SM_Type.class);
		List<SM_Type> superTypes = new ArrayList<>();
		superTypes.add(superType);
		List<SM_Type> superSuperTypes = new ArrayList<>();
		superSuperTypes.add(superSuperType);
		when(metrics.getType()).thenReturn(type);
		when(type.getSuperTypes()).thenReturn(superTypes);
		when(superType.getSuperTypes()).thenReturn(superSuperTypes);
		when(superType.getName()).thenReturn("foo");
		when(superSuperType.getName()).thenReturn("testType");
		HierarchySmellDetector detector = new HierarchySmellDetector(metrics, info);
		

		int expected = 1;
		int actual = detector.detectCyclicHierarchy().size();
		
		assertEquals(expected, actual);
	}
	
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
	public void testDeepHierarchyWhenSmellIsDetected() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getInheritanceDepth())
				.thenReturn(thresholds.getDeepHierarchy() + 1);
		HierarchySmellDetector detector = new HierarchySmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectDeepHierarchy().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMissingHierarchyWhenHappyPath() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		SM_Type type = mock(SM_Type.class);
		SM_Type superType = mock(SM_Type.class);
		SM_Method typeMethod = mock(SM_Method.class);
		List<SM_Method> methods = new ArrayList<>();
		methods.add(typeMethod);
		List<SM_Type> superTypes = new ArrayList<>();
		superTypes.add(superType);
		List<SM_Type> instanceOfTypes = new ArrayList<>();
		instanceOfTypes.add(superType);
		when(metrics.getType()).thenReturn(type);
		when(type.getMethodList()).thenReturn(methods);
		when(type.getSuperTypes()).thenReturn(superTypes);
		when(typeMethod.getSMTypesInInstanceOf()).thenReturn(instanceOfTypes);
		HierarchySmellDetector detector = new HierarchySmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectMissingHierarchy().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMissingHierarchyWhenSmellIsDetected() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		SM_Type type = mock(SM_Type.class);
		SM_Type otherType = mock(SM_Type.class);
		SM_Type otherType2 = mock(SM_Type.class);
		SM_Method typeMethod = mock(SM_Method.class);
		List<SM_Method> methods = new ArrayList<>();
		methods.add(typeMethod);
		List<SM_Type> instanceOfTypes = new ArrayList<>();
		instanceOfTypes.add(otherType);
		instanceOfTypes.add(otherType2);
		when(metrics.getType()).thenReturn(type);
		when(type.getMethodList()).thenReturn(methods);
		when(typeMethod.getSMTypesInInstanceOf()).thenReturn(instanceOfTypes);
		HierarchySmellDetector detector = new HierarchySmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectMissingHierarchy().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMultipathHierachyWhenHappyPathNoParent() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		SM_Type type = mock(SM_Type.class);
		List<SM_Type> superTypes = new ArrayList<>();
		when(metrics.getType()).thenReturn(type);
		when(type.getSuperTypes()).thenReturn(superTypes);
		HierarchySmellDetector detector = new HierarchySmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectMultipathHierarchy().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMultipathHierachyWhenHappyPathWithParent() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		SM_Type type = mock(SM_Type.class);
		SM_Type superType = mock(SM_Type.class);
		List<SM_Type> superTypes = new ArrayList<>();
		superTypes.add(superType);
		when(metrics.getType()).thenReturn(type);
		when(type.getSuperTypes()).thenReturn(superTypes);
		HierarchySmellDetector detector = new HierarchySmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectMultipathHierarchy().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMultipathHierachyWhenSmellIsDetected() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		SM_Type type = mock(SM_Type.class);
		SM_Type superType = mock(SM_Type.class);
		SM_Type intefaceType = mock(SM_Type.class);
		List<SM_Type> superTypes = new ArrayList<>();
		superTypes.add(superType);
		superTypes.add(intefaceType);
		List<SM_Type> superSuperTypes = new ArrayList<>();
		superSuperTypes.add(intefaceType);
		when(metrics.getType()).thenReturn(type);
		when(type.getSuperTypes()).thenReturn(superTypes);
		when(superType.getSuperTypes()).thenReturn(superSuperTypes);
		HierarchySmellDetector detector = new HierarchySmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectMultipathHierarchy().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testRebeliousHierarchyWhenSmellIsDetectedWithEmptyBody() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		SM_Type type = mock(SM_Type.class);
		SM_Method typeMethod = mock(SM_Method.class);
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		SM_Type superType = mock(SM_Type.class);
		SM_Method superTypeMethod = mock(SM_Method.class);
		List<SM_Method> typeMethods = new ArrayList<>();
		typeMethods.add(typeMethod);
		List<SM_Method> superTypeMethods = new ArrayList<>();
		superTypeMethods.add(superTypeMethod);
		List<SM_Type> superTypes = new ArrayList<>();
		superTypes.add(superType);
		List<SM_Parameter> parameters = new ArrayList<>();
		when(metrics.getType()).thenReturn(type);
		when(type.getMethodList()).thenReturn(typeMethods);
		when(type.getMetricsFromMethod(typeMethod)).thenReturn(methodMetrics);
		when(type.getSuperTypes()).thenReturn(superTypes);
		when(type.getMetricsFromMethod(typeMethod)).thenReturn(methodMetrics);
		when(superType.getMethodList()).thenReturn(superTypeMethods);
		when(typeMethod.getName()).thenReturn("foo");
		when(superTypeMethod.getName()).thenReturn("foo");
		when(typeMethod.getParameterList()).thenReturn(parameters);
		when(superTypeMethod.getParameterList()).thenReturn(parameters);
		when(methodMetrics.getNumOfLines()).thenReturn(0);
		HierarchySmellDetector detector = new HierarchySmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectRebeliousHierarchy().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testRebeliousHierarchyWhenSmellIsDetectedWithOneLineThrowable() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		SM_Type type = mock(SM_Type.class);
		SM_Method typeMethod = mock(SM_Method.class);
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		SM_Type superType = mock(SM_Type.class);
		SM_Method superTypeMethod = mock(SM_Method.class);
		List<SM_Method> typeMethods = new ArrayList<>();
		typeMethods.add(typeMethod);
		List<SM_Method> superTypeMethods = new ArrayList<>();
		superTypeMethods.add(superTypeMethod);
		List<SM_Type> superTypes = new ArrayList<>();
		superTypes.add(superType);
		List<SM_Parameter> parameters = new ArrayList<>();
		when(metrics.getType()).thenReturn(type);
		when(type.getMethodList()).thenReturn(typeMethods);
		when(type.getMetricsFromMethod(typeMethod)).thenReturn(methodMetrics);
		when(type.getSuperTypes()).thenReturn(superTypes);
		when(type.getMetricsFromMethod(typeMethod)).thenReturn(methodMetrics);
		when(superType.getMethodList()).thenReturn(superTypeMethods);
		when(typeMethod.getName()).thenReturn("foo");
		when(typeMethod.throwsException()).thenReturn(true);
		when(superTypeMethod.getName()).thenReturn("foo");
		when(typeMethod.getParameterList()).thenReturn(parameters);
		when(superTypeMethod.getParameterList()).thenReturn(parameters);
		when(methodMetrics.getNumOfLines()).thenReturn(1);
		HierarchySmellDetector detector = new HierarchySmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectRebeliousHierarchy().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testRebeliousHierarchyWhenHappyPathIsOverridenWithOneLineThrowable() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		SM_Type type = mock(SM_Type.class);
		SM_Method typeMethod = mock(SM_Method.class);
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		SM_Type superType = mock(SM_Type.class);
		SM_Method superTypeMethod = mock(SM_Method.class);
		List<SM_Method> typeMethods = new ArrayList<>();
		typeMethods.add(typeMethod);
		List<SM_Method> superTypeMethods = new ArrayList<>();
		superTypeMethods.add(superTypeMethod);
		List<SM_Type> superTypes = new ArrayList<>();
		superTypes.add(superType);
		List<SM_Parameter> parameters = new ArrayList<>();
		when(metrics.getType()).thenReturn(type);
		when(type.getMethodList()).thenReturn(typeMethods);
		when(type.getMetricsFromMethod(typeMethod)).thenReturn(methodMetrics);
		when(type.getSuperTypes()).thenReturn(superTypes);
		when(type.getMetricsFromMethod(typeMethod)).thenReturn(methodMetrics);
		when(superType.getMethodList()).thenReturn(superTypeMethods);
		when(typeMethod.getName()).thenReturn("foo");
		when(typeMethod.throwsException()).thenReturn(false);
		when(superTypeMethod.getName()).thenReturn("foo");
		when(typeMethod.getParameterList()).thenReturn(parameters);
		when(superTypeMethod.getParameterList()).thenReturn(parameters);
		when(methodMetrics.getNumOfLines()).thenReturn(1);
		HierarchySmellDetector detector = new HierarchySmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectRebeliousHierarchy().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testRebeliousHierarchyWhenHappyPathIsOverridenWithMoreLines() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		SM_Type type = mock(SM_Type.class);
		SM_Method typeMethod = mock(SM_Method.class);
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		SM_Type superType = mock(SM_Type.class);
		SM_Method superTypeMethod = mock(SM_Method.class);
		List<SM_Method> typeMethods = new ArrayList<>();
		typeMethods.add(typeMethod);
		List<SM_Method> superTypeMethods = new ArrayList<>();
		superTypeMethods.add(superTypeMethod);
		List<SM_Type> superTypes = new ArrayList<>();
		superTypes.add(superType);
		List<SM_Parameter> parameters = new ArrayList<>();
		when(metrics.getType()).thenReturn(type);
		when(type.getMethodList()).thenReturn(typeMethods);
		when(type.getMetricsFromMethod(typeMethod)).thenReturn(methodMetrics);
		when(type.getSuperTypes()).thenReturn(superTypes);
		when(type.getMetricsFromMethod(typeMethod)).thenReturn(methodMetrics);
		when(superType.getMethodList()).thenReturn(superTypeMethods);
		when(typeMethod.getName()).thenReturn("foo");
		when(typeMethod.throwsException()).thenReturn(false);
		when(superTypeMethod.getName()).thenReturn("foo");
		when(typeMethod.getParameterList()).thenReturn(parameters);
		when(superTypeMethod.getParameterList()).thenReturn(parameters);
		when(methodMetrics.getNumOfLines()).thenReturn(2);
		HierarchySmellDetector detector = new HierarchySmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectRebeliousHierarchy().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testRebeliousHierarchyWhenHappyPathIsNotOverriden() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		SM_Type type = mock(SM_Type.class);
		SM_Method typeMethod = mock(SM_Method.class);
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		SM_Type superType = mock(SM_Type.class);
		SM_Method superTypeMethod = mock(SM_Method.class);
		List<SM_Method> typeMethods = new ArrayList<>();
		typeMethods.add(typeMethod);
		List<SM_Method> superTypeMethods = new ArrayList<>();
		superTypeMethods.add(superTypeMethod);
		List<SM_Type> superTypes = new ArrayList<>();
		superTypes.add(superType);
		List<SM_Parameter> parameters = new ArrayList<>();
		when(metrics.getType()).thenReturn(type);
		when(type.getMethodList()).thenReturn(typeMethods);
		when(type.getMetricsFromMethod(typeMethod)).thenReturn(methodMetrics);
		when(type.getSuperTypes()).thenReturn(superTypes);
		when(type.getMetricsFromMethod(typeMethod)).thenReturn(methodMetrics);
		when(superType.getMethodList()).thenReturn(superTypeMethods);
		when(typeMethod.getName()).thenReturn("foo");
		when(typeMethod.throwsException()).thenReturn(false);
		when(superTypeMethod.getName()).thenReturn("bar");
		when(typeMethod.getParameterList()).thenReturn(parameters);
		when(superTypeMethod.getParameterList()).thenReturn(parameters);
		when(methodMetrics.getNumOfLines()).thenReturn(0);
		HierarchySmellDetector detector = new HierarchySmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectRebeliousHierarchy().size();
		
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
	public void testWideHierarchyWhenSmellIsDetected() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getNumOfChildren())
				.thenReturn(thresholds.getWideHierarchy() + 1);
		HierarchySmellDetector detector = new HierarchySmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectWideHierarchy().size();
		
		assertEquals(expected, actual);
	}
	
}
