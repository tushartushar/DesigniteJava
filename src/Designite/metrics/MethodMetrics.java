package Designite.metrics;

import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import Designite.SourceModel.SM_Field;
import Designite.SourceModel.SM_Parameter;
import Designite.visitors.MethodControlFlowVisitor;

public class MethodMetrics implements MetricExtractor {

	private int numOfParameters;
	private int cyclomaticComplexity;
	private int numOfLines;
	
	private MethodDeclaration methodDeclaration;
	private List<SM_Parameter> parameters;
	private List<SM_Field> directFieldAccesses;
	
	public MethodMetrics(List<SM_Parameter> parameters, 
			MethodDeclaration methodDeclaration, 
			List<SM_Field> directFieldAccesses) {
		this.parameters = parameters;
		this.methodDeclaration = methodDeclaration;
		this.directFieldAccesses = directFieldAccesses;
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

}
