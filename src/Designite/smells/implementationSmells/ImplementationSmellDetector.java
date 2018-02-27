package Designite.smells.implementationSmells;

import java.util.ArrayList;
import java.util.List;

import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.MethodMetrics;
import Designite.metrics.TypeMetrics;
import Designite.smells.ThresholdsDTO;
import Designite.smells.models.DesignCodeSmell;
import Designite.smells.models.ImplementationCodeSmell;

public class ImplementationSmellDetector {
	
	private List<ImplementationCodeSmell> smells;
	
	private MethodMetrics methodMetrics;
	private SourceItemInfo info;
	private ThresholdsDTO thresholdsDTO;
	
	private static final String LONG_METHOD = "Long method";
	
	public ImplementationSmellDetector(MethodMetrics methodMetrics, SourceItemInfo info) {
		this.methodMetrics = methodMetrics;
		this.info = info;
		
		thresholdsDTO = new ThresholdsDTO();
		smells = new ArrayList<>();
	}
	
	public List<ImplementationCodeSmell> detectCodeSmells() {
		detectLongMethods();
		return smells;
	}
	
	public List<ImplementationCodeSmell> detectLongMethods() {
		if (hasLongMethod()) {
			addToSmells(initializeCodeSmell(LONG_METHOD));
		}
		return smells;
	}
	
	private boolean hasLongMethod() {
		return methodMetrics.getNumOfLines() >= thresholdsDTO.getLongMethod();
	}
	
	public List<ImplementationCodeSmell> getSmells() {
		return smells;
	}
	
	private ImplementationCodeSmell initializeCodeSmell(String smellName) {
		return new ImplementationCodeSmell(info.getProjectName()
				, info.getPackageName()
				, info.getTypeName()
				, info.getMethodName()
				, smellName);
	}
	
	private void addToSmells(ImplementationCodeSmell smell) {
		smells.add(smell);
	}

}
