package Designite.metrics;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import Designite.SourceModel.SM_Field;
import Designite.SourceModel.SM_Parameter;
import Designite.SourceModel.SM_Type;
import Designite.visitors.MethodControlFlowVisitor;

public class MethodMetrics implements MetricExtractor {

	private int numOfParameters;
	private int cyclomaticComplexity;
	private int numOfLines;
	
	private MethodDeclaration methodDeclaration;
	private List<SM_Parameter> parameters;
	private List<SM_Field> directFieldAccesses;
	private List<SM_Type> smTypesInInstanceOf;
	
	public MethodMetrics(List<SM_Parameter> parameters
			, List<SM_Field> directFieldAccesses
			, List<SM_Type> smTypesInInstanceOf
			, MethodDeclaration methodDeclaration) {
		this.parameters = parameters;
		this.directFieldAccesses = directFieldAccesses;
		this.smTypesInInstanceOf = smTypesInInstanceOf;
		this.methodDeclaration = methodDeclaration;
	}
	
	@Override
	public void extractMetrics() {
		extractNumOfParametersMetrics();
		extractCyclomaticComplexity();
		extractNumberOfLines();
	}
	
	private void extractNumOfParametersMetrics() {
		numOfParameters = parameters.size();
	}
	
	private void extractCyclomaticComplexity() {
		cyclomaticComplexity = calculateCyclomaticComplexity();
	}
	
	private int calculateCyclomaticComplexity() {
		MethodControlFlowVisitor visitor = new MethodControlFlowVisitor();
		methodDeclaration.accept(visitor);
		return visitor.getNumOfIfStatements()
			 + visitor.getNumOfSwitchCaseStatements()
			 + visitor.getNumOfForStatements()
			 + visitor.getNumOfWhileStatements()
		     + visitor.getNumOfDoStatements()
			 + visitor.getNumOfForeachStatements()
			 + 1;
	}
	
	private void extractNumberOfLines() {
		if (methodHasBody()) {
			String body = methodDeclaration.getBody().toString();
			numOfLines = body.length() - body.replace("\n", "").length();
		}
	}
	
	private boolean methodHasBody() {
		return methodDeclaration.getBody() != null;
	}
	
	public int getNumOfParameters() {
		return numOfParameters;
	}

	public int getCyclomaticComplexity() {
		return cyclomaticComplexity;
	}

	public int getNumOfLines() {
		return numOfLines;
	}

	public List<SM_Field> getDirectFieldAccesses() {
		return directFieldAccesses;
	}
	
	public List<SM_Type> getSMTypesInInstanceOf() {
		return smTypesInInstanceOf;
	}

}
