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
	public void testHasMultipathHierachyWhenHappyPathNoParent() {
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
	public void testHasMultipathHierachyWhenHappyPathWithParent() {
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
	public void testHasMultipathHierachyWhenSmellIsDetected() {
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
