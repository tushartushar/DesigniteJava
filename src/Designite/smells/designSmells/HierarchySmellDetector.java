package Designite.smells.designSmells;

import java.util.List;

import Designite.SourceModel.SM_Type;
import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.TypeMetrics;
import Designite.smells.models.DesignCodeSmell;
import net.bytebuddy.asm.Advice.This;

public class HierarchySmellDetector extends DesignSmellDetector {
	
	private static final String CYCLIC_HIERARCHY = "Cyclic Hierarchy";
	private static final String DEEP_HIERARCHY = "Deep Hierarchy";
	private static final String MULTIPATH_HIERARCHY = "Multipath Hierarchy";
	private static final String WIDE_HIERARCHY = "Wide Hierarchy";
	
	public HierarchySmellDetector(TypeMetrics typeMetrics, SourceItemInfo info) {
		super(typeMetrics, info);
	}
	
	public List<DesignCodeSmell> detectCodeSmells() {
		detectCyclicHierarchy();
		detectDeepHierarchy();
		detectMultipathHierarchy();
		detectWideHierarchy();
		return getSmells();
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
	
	public List<DesignCodeSmell> detectMultipathHierarchy() {
		if (hasMultipathHierarchy()) {
			addToSmells(initializeCodeSmell(MULTIPATH_HIERARCHY));
		}
		return getSmells();
	}
	
	private boolean hasMultipathHierarchy() {
		for (SM_Type superType : getTypeMetrics().getType().getSuperTypes()) {
			for (SM_Type otherSuperType : getTypeMetrics().getType().getSuperTypes()) {
				if (!superType.equals(otherSuperType)) {
					for (SM_Type ancestorType : otherSuperType.getSuperTypes()) {
						if (sameAsSomeAncestor(superType, ancestorType)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean sameAsSomeAncestor(SM_Type targetType, SM_Type ancestorType) {
		if (targetType.equals(ancestorType)) {
			return true;
		}
		for (SM_Type deeperAncestor : ancestorType.getSuperTypes()) {
			if (sameAsSomeAncestor(targetType, deeperAncestor)) {
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
