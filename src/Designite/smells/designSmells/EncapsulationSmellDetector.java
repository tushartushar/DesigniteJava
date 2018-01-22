package Designite.smells.designSmells;

import java.util.List;

import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.TypeMetrics;
import Designite.smells.models.DesignCodeSmell;

public class EncapsulationSmellDetector extends DesignSmellDetector {
	
	private static final String DEFICIENT_ENCAPSULATION = "Deficient Encapsulation";
	
	public EncapsulationSmellDetector(TypeMetrics typeMetrics
			, SourceItemInfo info) {
		super(typeMetrics, info);
	}
	
	public List<DesignCodeSmell> detectCodeSmells() {
		return getSmells();
	}
	
	public List<DesignCodeSmell> detectDeficientEncapsulation() {
		if (hasDeficientEncapsulation()) {
			addToSmells(initializeCodeSmell(DEFICIENT_ENCAPSULATION));
		}
		return getSmells();
	}
	
	private boolean hasDeficientEncapsulation() {
		return getTypeMetrics().getNumOfPublicFields() > 0;
	}
	
}
