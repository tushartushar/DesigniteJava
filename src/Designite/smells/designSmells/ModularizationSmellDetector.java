package Designite.smells.designSmells;

import java.util.List;

import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.TypeMetrics;
import Designite.smells.models.DesignCodeSmell;

public class ModularizationSmellDetector extends DesignSmellDetector {
	
	private static final String INSUFFICINT_MODULARIZATION = "Insufficient Modularization";
	
	public ModularizationSmellDetector(TypeMetrics typeMetrics, SourceItemInfo info) {
		super(typeMetrics, info);
	}
	
	public List<DesignCodeSmell> detectCodeSmells() {
		detectInsufficientModularization();
		return getSmells();
	}
	
	private void detectInsufficientModularization() {
		if (hasInsufficientModularization()) {
			addToSmells(new DesignCodeSmell(getSourceItemInfo().getProjectName()
					, getSourceItemInfo().getPackageName()
					, getSourceItemInfo().getTypeName()
					, INSUFFICINT_MODULARIZATION));
		}
	}
	
	private boolean hasInsufficientModularization() {
		return hasLargePublicInterface();
	}
	
	private boolean hasLargePublicInterface() {
		return getTypeMetrics().getNumOfPublicMethods() > 20;
	}

}
