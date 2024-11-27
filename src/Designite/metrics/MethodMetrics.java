package Designite.metrics;

import java.util.List;

import Designite.SourceModel.SM_Field;
import Designite.SourceModel.SM_Method;
import Designite.SourceModel.SM_Type;

public class MethodMetrics extends Metrics {

	private int numOfParameters;
	private int cyclomaticComplexity;
	private int numOfLines;
	private SM_Method method;

	public int getNumOfParameters() {
		return numOfParameters;
	}

	public int getCyclomaticComplexity() {
		return cyclomaticComplexity;
	}

	public int getNumOfLines() {
		return numOfLines;
	}

	public void setNumOfParameters(int numOfParameters) {
		this.numOfParameters = numOfParameters;
	}

	public void setCyclomaticComplexity(int cyclomaticComplexity) {
		this.cyclomaticComplexity = cyclomaticComplexity;
	}

	public void setNumOfLines(int numOfLines) {
		this.numOfLines = numOfLines;
	}

	public void setMethod(SM_Method method){
		this.method = method;
	}

	public SM_Method getMethod() {
		return method;
	}

	public List<SM_Field> getDirectFieldAccesses() {
		return method.getDirectFieldAccesses();
	}

	public List<SM_Type> getSMTypesInInstanceOf() {
		return method.getSMTypesInInstanceOf();
	}

}
