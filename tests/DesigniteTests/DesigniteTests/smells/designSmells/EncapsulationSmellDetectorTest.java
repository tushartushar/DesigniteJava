package DesigniteTests.smells.designSmells;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Designite.SourceModel.SM_Method;
import Designite.SourceModel.SM_Package;
import Designite.SourceModel.SM_Project;
import Designite.SourceModel.SM_Type;
import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.TypeMetrics;
//import Designite.smells.ThresholdsDTO;
import Designite.smells.designSmells.EncapsulationSmellDetector;
import Designite.utils.models.Graph;

public class EncapsulationSmellDetectorTest {

	private SourceItemInfo info = new SourceItemInfo("testProject", "testPackage", "testType");
	//private ThresholdsDTO thresholds = new ThresholdsDTO();
	
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
	
	@Test
	public void testUnexploitedEncapsulationWhenHappyPath() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		SM_Type type = mock(SM_Type.class);
		SM_Method method = mock(SM_Method.class);
		SM_Type type1 = mock(SM_Type.class);
		SM_Type type2 = mock(SM_Type.class);
		SM_Package pkg = mock(SM_Package.class);
		SM_Project project = mock(SM_Project.class);
		Graph hierarchyGraph = mock(Graph.class);
		List<SM_Method> methodList = new ArrayList<>();
		methodList.add(method);
		List<SM_Type> instanceOfTypes = new ArrayList<>();
		instanceOfTypes.add(type1);
		instanceOfTypes.add(type2);
		when(metrics.getType()).thenReturn(type);
		when(type.getMethodList()).thenReturn(methodList);
		when(method.getSMTypesInInstanceOf()).thenReturn(instanceOfTypes);
		when(type1.getParentPkg()).thenReturn(pkg);
		when(type2.getParentPkg()).thenReturn(pkg);
		when(pkg.getParentProject()).thenReturn(project);
		when(project.getHierarchyGraph()).thenReturn(hierarchyGraph);
		when(hierarchyGraph.inSameConnectedComponent(type1, type2)).thenReturn(false);
		EncapsulationSmellDetector detector = new EncapsulationSmellDetector(metrics, info);
		
		int expected = 0;
		int actual = detector.detectUnexploitedEncapsulation().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testUnexploitedEncapsulationWhenSmellOccurs() {
		TypeMetrics metrics = mock(TypeMetrics.class);
		SM_Type type = mock(SM_Type.class);
		SM_Method method = mock(SM_Method.class);
		SM_Type type1 = mock(SM_Type.class);
		SM_Type type2 = mock(SM_Type.class);
		SM_Package pkg = mock(SM_Package.class);
		SM_Project project = mock(SM_Project.class);
		Graph hierarchyGraph = mock(Graph.class);
		List<SM_Method> methodList = new ArrayList<>();
		methodList.add(method);
		List<SM_Type> instanceOfTypes = new ArrayList<>();
		instanceOfTypes.add(type1);
		instanceOfTypes.add(type2);
		when(metrics.getType()).thenReturn(type);
		when(type.getMethodList()).thenReturn(methodList);
		when(method.getSMTypesInInstanceOf()).thenReturn(instanceOfTypes);
		when(type1.getParentPkg()).thenReturn(pkg);
		when(type2.getParentPkg()).thenReturn(pkg);
		when(pkg.getParentProject()).thenReturn(project);
		when(project.getHierarchyGraph()).thenReturn(hierarchyGraph);
		when(hierarchyGraph.inSameConnectedComponent(type1, type2)).thenReturn(true);
		EncapsulationSmellDetector detector = new EncapsulationSmellDetector(metrics, info);
		
		int expected = 1;
		int actual = detector.detectUnexploitedEncapsulation().size();
		
		assertEquals(expected, actual);
	}
}
