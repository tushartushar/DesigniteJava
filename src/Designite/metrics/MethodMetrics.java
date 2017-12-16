package Designite.metrics;

import java.util.List;

import Designite.SourceModel.SM_Parameter;

public class MethodMetrics implements MetricExtractor {

	private int numOfParameters;
	
	private List<SM_Parameter> parameters;
	
	public MethodMetrics(List<SM_Parameter> parameters) {
		this.parameters = parameters;
	}
	
	@Override
	public void extractMetrics() {
		NumOfParameters();		
	}
	
	private void NumOfParameters() {
		numOfParameters = parameters.size();
	}
	
	public int getNumOfParameters() {
		return numOfParameters;
	}
}
