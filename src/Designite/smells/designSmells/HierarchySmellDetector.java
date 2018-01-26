package Designite.smells.designSmells;

import java.util.List;

import Designite.SourceModel.SM_Type;
import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.TypeMetrics;
import Designite.smells.models.DesignCodeSmell;
import net.bytebuddy.asm.Advice.This;

public class HierarchySmellDetector extends DesignSmellDetector {
	
	private static final String DEEP_HIERARCHY = "Deep Hierarchy";
	private static final String CYCLIC_HIERARCHY = "Cyclic Hierarchy";
	private static final String WIDE_HIERARCHY = "Wide Hierarchy";
	
	public HierarchySmellDetector(TypeMetrics typeMetrics, SourceItemInfo info) {
		super(typeMetrics, info);
	}
	
	public List<DesignCodeSmell> detectCodeSmells() {
		detectDeepHierarchy();
		detectCyclicHierarchy();
		detectWideHierarchy();
		return getSmells();
	}
	
	public List<DesignCodeSmell> detectDeepHierarchy() {
		if (hasDeepHierarchy()) {
			addToSmells(initializeCodeSmell(DEEP_HIERARCHY));
		}
		return getSmells();
	}
	
	private boolean hasDeepHierarchy() {
		return getTypeMetrics().getInheritanceDepth()
				> getThresholdsDTO().getDeepHierarchy();
	}
	
	public List<DesignCodeSmell> detectCyclicHierarchy() {
		if (hasCyclicDependency()) {
			addToSmells(initializeCodeSmell(CYCLIC_HIERARCHY));
		}
		return getSmells();
	}
	
	private boolean hasCyclicDependency() {
		for (SM_Type superType : getTypeMetrics().getType().getSuperTypes()) {
			if (hasCyclicDependency(superType)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean hasCyclicDependency(SM_Type superType) {
		if (superType.getName().equals(getSourceItemInfo().getTypeName())) {
			return true;
		}
		for (SM_Type superSuperType : superType.getSuperTypes()) {
			if (hasCyclicDependency(superSuperType)) {
				return true;
			}
		}
		return false;
	}
	
	public List<DesignCodeSmell> detectWideHierarchy() {
		if (hasWideHierarchy()) {
			addToSmells(initializeCodeSmell(WIDE_HIERARCHY));
		}
		return getSmells();
	}
	
	private boolean hasWideHierarchy() {
		return getTypeMetrics().getNumOfChildren() 
				> getThresholdsDTO().getWideHierarchy();
	}

}
