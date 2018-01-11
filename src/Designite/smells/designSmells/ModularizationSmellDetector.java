package Designite.smells.designSmells;

import java.util.List;

import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.TypeMetrics;
import Designite.smells.models.DesignCodeSmell;

public class ModularizationSmellDetector extends DesignSmellDetector {
	
	private static final String INSUFFICIENT_MODULARIZATION = "Insufficient Modularization";
	
	public ModularizationSmellDetector(TypeMetrics typeMetrics, SourceItemInfo info) {
		super(typeMetrics, info);
	}
	
	public List<DesignCodeSmell> detectCodeSmells() {
		detectInsufficientModularization();
		return getSmells();
	}
	
	private void detectInsufficientModularization() {
		if (hasInsufficientModularization()) {
			addToSmells(initializeCodeSmell(INSUFFICIENT_MODULARIZATION));
		}
	}
	
	private boolean hasInsufficientModularization() {
		return hasLargePublicInterface()
				|| hasLargeNumberOfMethods()
				|| hasHighComplexity();
	}
	
	private boolean hasLargePublicInterface() {
		return getTypeMetrics().getNumOfPublicMethods() 
				> getThresholdsDTO().getInsufficientModularizationLargePublicInterface();
	}
	
	private boolean hasLargeNumberOfMethods() {
		return getTypeMetrics().getNumOfMethods() 
				> getThresholdsDTO().getInsufficientModularizationLargeNumOfMethods();
	}
	
	private boolean hasHighComplexity() {
		return getTypeMetrics().getWeightedMethodsPerClass()
				> getThresholdsDTO().getInsufficientModularizationHighComplexity();
	}

}
