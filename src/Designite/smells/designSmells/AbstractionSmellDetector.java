package Designite.smells.designSmells;

import java.util.List;

import Designite.SourceModel.SM_Type;
import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.TypeMetrics;
import Designite.smells.models.DesignCodeSmell;

public class AbstractionSmellDetector extends DesignSmellDetector {
	
	private static final String UNUTILIZED_ABSTRACTION = "Unutilized abstraction";
	
	public AbstractionSmellDetector(TypeMetrics typeMetrics
			, SourceItemInfo info) {
		super(typeMetrics, info);
	}
	
	public List<DesignCodeSmell> detectCodeSmells() {
		detectUnutilizedAbstraction();
		return getSmells();
	}
	
	public void detectUnutilizedAbstraction() {
		if (hasUnutilizedAbstraction()) {
			addToSmells(initializeCodeSmell(UNUTILIZED_ABSTRACTION));
		}
	}
	
	private boolean hasUnutilizedAbstraction() {
		if (hasSuperTypes()) {
			return !hasSuperTypeWithFanIn() || !hasFanIn(getTypeMetrics());
		}
		return !hasFanIn(getTypeMetrics());
	}
	
	private boolean hasSuperTypes() {
		return getTypeMetrics().getSuperTypes().size() > 0; 
	}
	
	private boolean hasSuperTypeWithFanIn() {
		for (SM_Type superType : getTypeMetrics().getSuperTypes()) {
			if (hasFanIn(superType.getTypeMetrics())) {
				return true;
			}
		}
		return false;
	}
	
	private boolean hasFanIn(TypeMetrics metrics) {
		return metrics.getNumOfFanInTypes() > 0;
	}

}
