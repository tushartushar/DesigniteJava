package DesigniteTests.smells.designSmells;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

import Designite.SourceModel.SM_Package;
import Designite.SourceModel.SM_Project;
import Designite.SourceModel.SM_Type;
import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.TypeMetrics;
import Designite.smells.ThresholdsDTO;
import Designite.smells.designSmells.ModularizationSmellDetector;
import Designite.utils.models.Graph;
import Designite.utils.models.Vertex;

public class ModularizationSmellDetectorTest {
	
	private SourceItemInfo info = new SourceItemInfo("testProject", "testPackage", "testType");
	private ThresholdsDTO thresholds = new ThresholdsDTO();
	
	@Test
	public void testBrokenModularizationWhenHappyPathWithAtLeastOneMethod() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getNumOfMethods())
			.thenReturn(1);
		when(metrics.getNumOfFields())
			.thenReturn(thresholds.getBrokenModularizationLargeFieldSet() + 1);
		ModularizationSmellDetector detector = new ModularizationSmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectBrokenModularization().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testBrokenModularizationWhenHappyPathWithFewFields() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getNumOfMethods())
			.thenReturn(0);
		when(metrics.getNumOfFields())
			.thenReturn(thresholds.getBrokenModularizationLargeFieldSet() - 1);
		ModularizationSmellDetector detector = new ModularizationSmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectBrokenModularization().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testBrokenModularizationWhenSmellIsDetected() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getNumOfMethods())
			.thenReturn(0);
		when(metrics.getNumOfFields())
			.thenReturn(thresholds.getBrokenModularizationLargeFieldSet() + 1);
		ModularizationSmellDetector detector = new ModularizationSmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectBrokenModularization().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCyclicDependentModularizationWhenHappyPath() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		SM_Type type = mock(SM_Type.class);
		SM_Package pkg = mock(SM_Package.class);
		SM_Project project = mock(SM_Project.class);
		Graph dependencyGraph = mock(Graph.class);
		List<Vertex> component = new ArrayList<>();
		component.add(type);
		when(metrics.getType()).thenReturn(type);
		when(type.getParentPkg()).thenReturn(pkg);
		when(pkg.getParentProject()).thenReturn(project);
		when(project.getDependencyGraph()).thenReturn(dependencyGraph);
		when(dependencyGraph.getStrongComponentOfVertex(type)).thenReturn(component);
		ModularizationSmellDetector detector = new ModularizationSmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectCyclicDependentModularization().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCyclicDependentModularizationWhenSmellOccurs() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		SM_Type type = mock(SM_Type.class);
		SM_Type cyclicDependentType = mock(SM_Type.class);
		SM_Package pkg = mock(SM_Package.class);
		SM_Project project = mock(SM_Project.class);
		Graph dependencyGraph = mock(Graph.class);
		List<Vertex> component = new ArrayList<>();
		component.add(type);
		component.add(cyclicDependentType);
		when(metrics.getType()).thenReturn(type);
		when(type.getParentPkg()).thenReturn(pkg);
		when(pkg.getParentProject()).thenReturn(project);
		when(project.getDependencyGraph()).thenReturn(dependencyGraph);
		when(dependencyGraph.getStrongComponentOfVertex(type)).thenReturn(component);
		ModularizationSmellDetector detector = new ModularizationSmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectCyclicDependentModularization().size();
		
		assertEquals(expected, actual);
	}
	
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
	
	@Test
	public void testHubLikeModularizationWhenHappyPathWithSmallFanIn() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getNumOfFanInTypes())
				.thenReturn(thresholds.getHubLikeModularizationLargeFanIn() - 1);
		when(metrics.getNumOfFanInTypes())
				.thenReturn(thresholds.getHubLikeModularizationLargeFanOut() + 1);
		ModularizationSmellDetector detector = new ModularizationSmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectHubLikeModularization().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testHubLikeModularizationWhenHappyPathWithSmallFanOut() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getNumOfFanInTypes())
				.thenReturn(thresholds.getHubLikeModularizationLargeFanIn() + 1);
		when(metrics.getNumOfFanInTypes())
				.thenReturn(thresholds.getHubLikeModularizationLargeFanOut() - 1);
		ModularizationSmellDetector detector = new ModularizationSmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectHubLikeModularization().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testHubLikeModularizationWhenSmellIsDetected() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		when(metrics.getNumOfFanInTypes())
				.thenReturn(thresholds.getHubLikeModularizationLargeFanIn() + 1);
		when(metrics.getNumOfFanOutTypes())
				.thenReturn(thresholds.getHubLikeModularizationLargeFanOut() + 1);
		ModularizationSmellDetector detector = new ModularizationSmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectHubLikeModularization().size();
		
		assertEquals(expected, actual);
	}

}
