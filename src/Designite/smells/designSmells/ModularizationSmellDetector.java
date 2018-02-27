package Designite.smells.designSmells;

import java.util.List;

import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.TypeMetrics;
import Designite.smells.models.DesignCodeSmell;
import Designite.utils.models.Graph;

public class ModularizationSmellDetector extends DesignSmellDetector {
	
	private static final String BROKEN_MODULARIZATION = "Broken Modularization";
	private static final String CYCLIC_DEPENDENT_MODULARIZATION = "Cyclic-Dependent Modularization";
	private static final String INSUFFICIENT_MODULARIZATION = "Insufficient Modularization";
	private static final String HUB_LIKE_MODULARIZATION = "Hub-like Modularization";
	
	public ModularizationSmellDetector(TypeMetrics typeMetrics, SourceItemInfo info) {
		super(typeMetrics, info);
	}
	
	@Override
	public List<DesignCodeSmell> detectCodeSmells() {
		detectBrokenModularization();
		detectCyclicDependentModularization();
		detectInsufficientModularization();
		detectHubLikeModularization();
		return getSmells();
	}
	
	public List<DesignCodeSmell> detectBrokenModularization() {
		if (hasBrokenModularization()) {
			addToSmells(initializeCodeSmell(BROKEN_MODULARIZATION));
		}
		return getSmells();
	}
	
	private boolean hasBrokenModularization() {
		return getTypeMetrics().getNumOfMethods() == 0
				&& getTypeMetrics().getNumOfFields() >= getThresholdsDTO().getBrokenModularizationLargeFieldSet();
	} 
	
	public List<DesignCodeSmell> detectCyclicDependentModularization() {
		if (hasCyclicDependentModularization()) {
			addToSmells(initializeCodeSmell(CYCLIC_DEPENDENT_MODULARIZATION));
		}
		return getSmells();
	}
	
	private boolean hasCyclicDependentModularization() {
		Graph dependencyGraph = getTypeMetrics().getType().getParentPkg().getParentProject().getDependencyGraph();
		if (dependencyGraph.getStrongComponentOfVertex(getTypeMetrics().getType()).size() > 1) {
			return true;
		}
		return false;
	}
	
	public List<DesignCodeSmell> detectInsufficientModularization() {
		if (hasInsufficientModularization()) {
			addToSmells(initializeCodeSmell(INSUFFICIENT_MODULARIZATION));
		}
		return getSmells();
	}
	
	private boolean hasInsufficientModularization() {
		return hasLargePublicInterface()
				|| hasLargeNumberOfMethods()
				|| hasHighComplexity();
	}
	
	private boolean hasLargePublicInterface() {
		return getTypeMetrics().getNumOfPublicMethods() 
				>= getThresholdsDTO().getInsufficientModularizationLargePublicInterface();
	}
	
	private boolean hasLargeNumberOfMethods() {
		return getTypeMetrics().getNumOfMethods() 
				>= getThresholdsDTO().getInsufficientModularizationLargeNumOfMethods();
	}
	
	private boolean hasHighComplexity() {
		return getTypeMetrics().getWeightedMethodsPerClass()
				>= getThresholdsDTO().getInsufficientModularizationHighComplexity();
	}
	
	public List<DesignCodeSmell> detectHubLikeModularization() {
		if (hasHubLikeModularization()) {
			addToSmells(initializeCodeSmell(HUB_LIKE_MODULARIZATION));
		}
		return getSmells();
	}
	
	private boolean hasHubLikeModularization() {
		return getTypeMetrics().getNumOfFanInTypes()
				>= getThresholdsDTO().getHubLikeModularizationLargeFanIn()
				&& getTypeMetrics().getNumOfFanOutTypes()
				>= getThresholdsDTO().getHubLikeModularizationLargeFanOut();
	}

}
