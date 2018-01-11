package Designite.smells.designSmells;

import java.util.List;

import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.TypeMetrics;
import Designite.smells.models.DesignCodeSmell;

public class HierarchySmellDetector extends DesignSmellDetector {
	
	private static final String DEEP_HIERARCHY = "Deep Hierarchy";
	private static final String WIDE_HIERARCHY = "Wide Hierarchy";
	
	public HierarchySmellDetector(TypeMetrics typeMetrics, SourceItemInfo info) {
		super(typeMetrics, info);
	}
	
	public List<DesignCodeSmell> detectCodeSmells() {
		detectDeepHierarchy();
		detectWideHierarchy();
		return getSmells();
	}
	
	private void detectDeepHierarchy() {
		if (hasDeepHierarchy()) {
			addToSmells(initializeCodeSmell(DEEP_HIERARCHY));
		}
	}
	
	private boolean hasDeepHierarchy() {
		return getTypeMetrics().getInheritanceDepth()
				> getThresholdsDTO().getDeepHierarchy();
	}
	
	private void detectWideHierarchy() {
		if (hasWideHierarchy()) {
			addToSmells(initializeCodeSmell(WIDE_HIERARCHY));
		}
	}
	
	private boolean hasWideHierarchy() {
		return getTypeMetrics().getNumOfChildren() 
				> getThresholdsDTO().getWideHierarchy();
	}

}
