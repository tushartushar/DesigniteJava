package Designite.smells.designSmells;

import java.util.List;

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
		return hasNoSuperClassAndNoFanIn();
	}
	
	private boolean hasNoSuperClassAndNoFanIn() {
		return hasNoSuperClass() && getTypeMetrics().getNumOfFanInTypes() == 0; 
	}
	
	private boolean hasNoSuperClass() {
		return getTypeMetrics().getSuperTypes().size() == 0;
	}
}
