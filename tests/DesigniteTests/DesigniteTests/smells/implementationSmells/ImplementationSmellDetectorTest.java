package DesigniteTests.smells.implementationSmells;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Designite.SourceModel.SM_Field;
import Designite.SourceModel.SM_LocalVar;
import Designite.SourceModel.SM_Method;
import Designite.SourceModel.SM_Parameter;
import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.MethodMetrics;
import Designite.smells.ThresholdsDTO;
import Designite.smells.implementationSmells.ImplementationSmellDetector;

public class ImplementationSmellDetectorTest {
	
	private SourceItemInfo info = new SourceItemInfo("testProject", "testPackage", "testType", "testMethod");
	private ThresholdsDTO thresholds = new ThresholdsDTO();
	
	@Test
	public void testAbstractFunctionCallFromConstructorWhenHappyPath() {
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		SM_Method method = mock(SM_Method.class);
		SM_Method calledMethod = mock(SM_Method.class);
		List<SM_Method> calledMethods = new ArrayList<>();
		calledMethods.add(calledMethod);
		when(methodMetrics.getMethod()).thenReturn(method);
		when(method.isConstructor()).thenReturn(true);
		when(method.getCalledMethods()).thenReturn(calledMethods);
		when(calledMethod.isAbstract()).thenReturn(false);
		ImplementationSmellDetector detector = new ImplementationSmellDetector(methodMetrics, info);
		
		int expected = 0;
		int actual = detector.detectAbstractFunctionCallFromConstructor().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testAbstractFunctionCallFromConstructorWhenSmellIsDetected() {
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		SM_Method method = mock(SM_Method.class);
		SM_Method calledMethod = mock(SM_Method.class);
		List<SM_Method> calledMethods = new ArrayList<>();
		calledMethods.add(calledMethod);
		when(methodMetrics.getMethod()).thenReturn(method);
		when(method.isConstructor()).thenReturn(true);
		when(method.getCalledMethods()).thenReturn(calledMethods);
		when(calledMethod.isAbstract()).thenReturn(true);
		ImplementationSmellDetector detector = new ImplementationSmellDetector(methodMetrics, info);
		
		int expected = 1;
		int actual = detector.detectAbstractFunctionCallFromConstructor().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testComplexMethodWhenHappyPath() {
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		when(methodMetrics.getCyclomaticComplexity()).thenReturn(thresholds.getComplexMethod() - 1);
		ImplementationSmellDetector detector = new ImplementationSmellDetector(methodMetrics, info);
		
		int expected = 0;
		int actual = detector.detectComplexMethod().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testComplexMethodWhenSmellIsDetected() {
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		when(methodMetrics.getCyclomaticComplexity()).thenReturn(thresholds.getComplexMethod() + 1);
		ImplementationSmellDetector detector = new ImplementationSmellDetector(methodMetrics, info);
		
		int expected = 1;
		int actual = detector.detectComplexMethod().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLongIdentifierWhenHappyPath() {
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		SM_Method method = mock(SM_Method.class);
		SM_Parameter parameter = mock(SM_Parameter.class);
		SM_LocalVar localVar = mock(SM_LocalVar.class);
		SM_Field field = mock(SM_Field.class);
		String name1 = initializeName(thresholds.getLongIdentifier() - 1);
		String name2 = initializeName(thresholds.getLongIdentifier() - 1);
		String name3 = initializeName(thresholds.getLongIdentifier() - 1);
		List<SM_Parameter> parameters = new ArrayList<>();
		parameters.add(parameter);
		List<SM_LocalVar> localVars = new ArrayList<>();
		localVars.add(localVar);
		List<SM_Field> fields = new ArrayList<>();
		fields.add(field);
		when(methodMetrics.getMethod()).thenReturn(method);
		when(method.getParameterList()).thenReturn(parameters);
		when(method.getLocalVarList()).thenReturn(localVars);
		when(method.getDirectFieldAccesses()).thenReturn(fields);
		when(parameter.getName()).thenReturn(name1);
		when(localVar.getName()).thenReturn(name2);
		when(field.getName()).thenReturn(name3);
		ImplementationSmellDetector detector = new ImplementationSmellDetector(methodMetrics, info);
		
		int expected = 0;
		int actual = detector.detectLongIdentifier().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLongIdentifierWhenLongParameter() {
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		SM_Method method = mock(SM_Method.class);
		SM_Parameter parameter = mock(SM_Parameter.class);
		SM_LocalVar localVar = mock(SM_LocalVar.class);
		SM_Field field = mock(SM_Field.class);
		String name1 = initializeName(thresholds.getLongIdentifier() + 1);
		String name2 = initializeName(thresholds.getLongIdentifier() - 1);
		String name3 = initializeName(thresholds.getLongIdentifier() - 1);
		List<SM_Parameter> parameters = new ArrayList<>();
		parameters.add(parameter);
		List<SM_LocalVar> localVars = new ArrayList<>();
		localVars.add(localVar);
		List<SM_Field> fields = new ArrayList<>();
		fields.add(field);
		when(methodMetrics.getMethod()).thenReturn(method);
		when(method.getParameterList()).thenReturn(parameters);
		when(method.getLocalVarList()).thenReturn(localVars);
		when(method.getDirectFieldAccesses()).thenReturn(fields);
		when(parameter.getName()).thenReturn(name1);
		when(localVar.getName()).thenReturn(name2);
		when(field.getName()).thenReturn(name3);
		ImplementationSmellDetector detector = new ImplementationSmellDetector(methodMetrics, info);
		
		int expected = 1;
		int actual = detector.detectLongIdentifier().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLongIdentifierWhenLongLocalVar() {
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		SM_Method method = mock(SM_Method.class);
		SM_Parameter parameter = mock(SM_Parameter.class);
		SM_LocalVar localVar = mock(SM_LocalVar.class);
		SM_Field field = mock(SM_Field.class);
		String name1 = initializeName(thresholds.getLongIdentifier() - 1);
		String name2 = initializeName(thresholds.getLongIdentifier() + 1);
		String name3 = initializeName(thresholds.getLongIdentifier() - 1);
		List<SM_Parameter> parameters = new ArrayList<>();
		parameters.add(parameter);
		List<SM_LocalVar> localVars = new ArrayList<>();
		localVars.add(localVar);
		List<SM_Field> fields = new ArrayList<>();
		fields.add(field);
		when(methodMetrics.getMethod()).thenReturn(method);
		when(method.getParameterList()).thenReturn(parameters);
		when(method.getLocalVarList()).thenReturn(localVars);
		when(method.getDirectFieldAccesses()).thenReturn(fields);
		when(parameter.getName()).thenReturn(name1);
		when(localVar.getName()).thenReturn(name2);
		when(field.getName()).thenReturn(name3);
		ImplementationSmellDetector detector = new ImplementationSmellDetector(methodMetrics, info);
		
		int expected = 1;
		int actual = detector.detectLongIdentifier().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLongIdentifierWhenLongField() {
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		SM_Method method = mock(SM_Method.class);
		SM_Parameter parameter = mock(SM_Parameter.class);
		SM_LocalVar localVar = mock(SM_LocalVar.class);
		SM_Field field = mock(SM_Field.class);
		String name1 = initializeName(thresholds.getLongIdentifier() - 1);
		String name2 = initializeName(thresholds.getLongIdentifier() - 1);
		String name3 = initializeName(thresholds.getLongIdentifier() + 1);
		List<SM_Parameter> parameters = new ArrayList<>();
		parameters.add(parameter);
		List<SM_LocalVar> localVars = new ArrayList<>();
		localVars.add(localVar);
		List<SM_Field> fields = new ArrayList<>();
		fields.add(field);
		when(methodMetrics.getMethod()).thenReturn(method);
		when(method.getParameterList()).thenReturn(parameters);
		when(method.getLocalVarList()).thenReturn(localVars);
		when(method.getDirectFieldAccesses()).thenReturn(fields);
		when(parameter.getName()).thenReturn(name1);
		when(localVar.getName()).thenReturn(name2);
		when(field.getName()).thenReturn(name3);
		ImplementationSmellDetector detector = new ImplementationSmellDetector(methodMetrics, info);
		
		int expected = 1;
		int actual = detector.detectLongIdentifier().size();
		
		assertEquals(expected, actual);
	}
	
	private String initializeName(int iterations) {
		String outcome = "";
		for (int i = 0; i < iterations; i++) {
			outcome += "a";
		}
		return outcome;
	}
	
	@Test
	public void testComplexMethoWhenSmellIsDetected() {
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		when(methodMetrics.getCyclomaticComplexity()).thenReturn(thresholds.getComplexMethod() + 1);
		ImplementationSmellDetector detector = new ImplementationSmellDetector(methodMetrics, info);
		
		int expected = 1;
		int actual = detector.detectComplexMethod().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLongMethodWhenHappyPath() {
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		when(methodMetrics.getNumOfLines()).thenReturn(thresholds.getLongMethod() - 1);
		ImplementationSmellDetector detector = new ImplementationSmellDetector(methodMetrics, info);
		
		int expected = 0;
		int actual = detector.detectLongMethod().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLongMethodWhenSmellIsDetected() {
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		when(methodMetrics.getNumOfLines()).thenReturn(thresholds.getLongMethod() + 1);
		ImplementationSmellDetector detector = new ImplementationSmellDetector(methodMetrics, info);
		
		int expected = 1;
		int actual = detector.detectLongMethod().size();
		
		assertEquals(expected, actual);
	}

	@Test
	public void testLongParameterListWhenHappyPath() {
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		when(methodMetrics.getNumOfParameters()).thenReturn(thresholds.getLongParameterList() - 1);
		ImplementationSmellDetector detector = new ImplementationSmellDetector(methodMetrics, info);
		
		int expected = 0;
		int actual = detector.detectLongParameterList().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLongParameterListWhenSmellIsDetected() {
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		when(methodMetrics.getNumOfParameters()).thenReturn(thresholds.getComplexMethod() + 1);
		ImplementationSmellDetector detector = new ImplementationSmellDetector(methodMetrics, info);
		
		int expected = 1;
		int actual = detector.detectLongParameterList().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLongStatement() {
		String myDummyMethod = "public void foo() {\n" 
				+ "\tCSVUtils.addAllToCSVFile(Constants.CSV_DIRECTORY_PATH + File.separator + getParentPkg().getParentProject().getName()+ File.separator+ Constants.IMPLEMENTATION_CODE_SMELLS_PATH_SUFFIX,smellMapping.get(method));\n"
				+ "\treturn new ImplementationCodeSmell(info.getProjectName(),info.getPackageName(),info.getTypeName(),info.getMethodName(),smellName);"
				+ "}";
		SM_Method method = mock(SM_Method.class);
		when(method.hasBody()).thenReturn(true);
		when(method.getMethodBody()).thenReturn(myDummyMethod);
		MethodMetrics methodMetrics = mock(MethodMetrics.class);
		when(methodMetrics.getMethod()).thenReturn(method);
		
		ImplementationSmellDetector detector = new ImplementationSmellDetector(methodMetrics, info);
		int expected = 2;
		int actual = detector.detectLongStatement().size();
		assertEquals(expected, actual);
	}

}
