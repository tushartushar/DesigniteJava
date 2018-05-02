package Designite.metrics;

import java.util.List;

import Designite.SourceModel.SM_Field;
import Designite.SourceModel.SM_Method;
import Designite.SourceModel.SM_Type;
import Designite.visitors.MethodControlFlowVisitor;

public class MethodMetrics implements MetricExtractor {

	private int numOfParameters;
	private int cyclomaticComplexity;
	private int numOfLines;
	
	private SM_Method method;
	
	public MethodMetrics(SM_Method method) {
		this.method = method;
	}
	
	@Override
	public void extractMetrics() {
		extractNumOfParametersMetrics();
		extractCyclomaticComplexity();
		extractNumberOfLines();
	}
	
	private void extractNumOfParametersMetrics() {
		numOfParameters = method.getParameterList().size();
	}
	
	private void extractCyclomaticComplexity() {
		cyclomaticComplexity = calculateCyclomaticComplexity();
	}
	
	private int calculateCyclomaticComplexity() {
		MethodControlFlowVisitor visitor = new MethodControlFlowVisitor();
		method.getMethodDeclaration().accept(visitor);
		return visitor.getNumOfIfStatements()
			 + visitor.getNumOfSwitchCaseStatementsWitoutDefault()
			 + visitor.getNumOfForStatements()
			 + visitor.getNumOfWhileStatements()
		     + visitor.getNumOfDoStatements()
			 + visitor.getNumOfForeachStatements()
			 + 1;
	}
	
	private void extractNumberOfLines() {
		if (methodHasBody()) {
			String body = method.getMethodDeclaration().getBody().toString();
			numOfLines = body.length() - body.replace("\n", "").length();
		}
	}
	
	private boolean methodHasBody() {
		return method.getMethodDeclaration().getBody() != null;
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
		return method.getDirectFieldAccesses();
	}
	
	public List<SM_Type> getSMTypesInInstanceOf() {
		return method.getSMTypesInInstanceOf();
	}
	
	public SM_Method getMethod() {
		return method;
	}

}
