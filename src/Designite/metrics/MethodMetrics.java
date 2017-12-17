package Designite.metrics;

import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import Designite.SourceModel.SM_Parameter;
import Designite.visitors.MethodControlFlowVisitor;

public class MethodMetrics implements MetricExtractor {

	private int numOfParameters;
	private int cyclomaticComplexity;
	
	private MethodDeclaration methodDeclaration;
	private List<SM_Parameter> parameters;
	
	public MethodMetrics(List<SM_Parameter> parameters, MethodDeclaration methodDeclaration) {
		this.parameters = parameters;
		this.methodDeclaration = methodDeclaration;
	}
	
	@Override
	public void extractMetrics() {
		extractNumOfParametersMetrics();
		extractCyclomaticComplexity();
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
	
	public int getNumOfParameters() {
		return numOfParameters;
	}

	public int getCyclomaticComplexity() {
		return cyclomaticComplexity;
	}
	
	
}
