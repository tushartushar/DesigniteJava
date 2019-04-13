package Designite.smells.designSmells;

import java.util.ArrayList;
import java.util.List;

import Designite.SourceModel.AccessStates;
import Designite.SourceModel.SM_Method;
import Designite.SourceModel.SM_Type;
import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.MethodMetrics;
import Designite.metrics.TypeMetrics;
import Designite.smells.models.DesignCodeSmell;

public class HierarchySmellDetector extends DesignSmellDetector {
	
	private static final String BROKEN_HIERARCHY = "Broken Hierarchy";
	private static final String CYCLIC_HIERARCHY = "Cyclic Hierarchy";
	private static final String DEEP_HIERARCHY = "Deep Hierarchy";
	private static final String MISSING_HIERARCHY = "Missing Hierarchy";
	private static final String MULTIPATH_HIERARCHY = "Multipath Hierarchy";
	private static final String REBELIOUS_HIERARCHY = "Rebellious Hierarchy";
	private static final String WIDE_HIERARCHY = "Wide Hierarchy";
	
	private static final int EMPTY_BODY = 0;
	private static final int ONLY_ONE_STATEMENT = 1;
	private static final int INSTANCE_OF_TYPES_NOT_IN_HIERARCHY_THRESHOLD = 2;
	
	public HierarchySmellDetector(TypeMetrics typeMetrics, SourceItemInfo info) {
		super(typeMetrics, info);
	}
	
	@Override
	public List<DesignCodeSmell> detectCodeSmells() {
		detectBrokenHierarchy();
		detectCyclicHierarchy();
		detectDeepHierarchy();
		detectMissingHierarchy();
		detectMultipathHierarchy();
		detectRebeliousHierarchy();
		detectWideHierarchy();
		return getSmells();
	}
	
	public List<DesignCodeSmell> detectBrokenHierarchy() {
		if (hasBrokenHierarchy()) {
			addToSmells(initializeCodeSmell(BROKEN_HIERARCHY));
		}
		return getSmells();
	}
	
	private boolean hasBrokenHierarchy() {
		SM_Type type = getTypeMetrics().getType();
		if (hasSuperTypes(type) && hasPublicMethods()) {
			for (SM_Type superType : type.getSuperTypes()) {
				if (!methodIsOverriden(type, superType)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean hasSuperTypes(SM_Type type) {
		return !type.getSuperTypes().isEmpty();
	}
	
	private boolean hasPublicMethods() {
		return getTypeMetrics().getNumOfPublicMethods() > 0;
	}
	
	private boolean methodIsOverriden(SM_Type type, SM_Type superType) {
		boolean overrides = false;
		for (SM_Method superMethod : superType.getMethodList()) {
			if (superMethod.getAccessModifier() == AccessStates.PUBLIC || superMethod.isAbstract() || superType.isInterface()) {
				for (SM_Method method : type.getMethodList()) {
					if (method.getAccessModifier() == AccessStates.PUBLIC && shareTheSameName(method, superMethod)) {
						overrides = true;
					}
				}
			}
		}
		return overrides;
	}
	
	private boolean shareTheSameName(SM_Method method, SM_Method superMethod) {
		return method.getName().equals(superMethod.getName());
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
		// FIXME : switch to iterative process to avoid stack overflows
		try {
			if (superType.getName().equals(getSourceItemInfo().getTypeName())) {
				return true;
			}
			for (SM_Type superSuperType : superType.getSuperTypes()) {
				if (hasCyclicDependency(superSuperType)) {
					return true;
				}
			}
			return false;
		} catch (StackOverflowError er) {
			System.err.println("Cyclic dependency analysis skipped due to memory overflow.");
			return false;
		}
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
	
	public List<DesignCodeSmell> detectMissingHierarchy() {
		if (hasMissingHierarchy()) {
			addToSmells(initializeCodeSmell(MISSING_HIERARCHY));
		}
		return getSmells();
	}
	
	private boolean hasMissingHierarchy() {
		SM_Type type = getTypeMetrics().getType();
		for (SM_Method method : type.getMethodList()) {
			List<SM_Type> listOfInstanceOfTypes = method.getSMTypesInInstanceOf();
			List<SM_Type> allAncestors = getAllAncestors(type, new ArrayList<>());
			if (setDifference(listOfInstanceOfTypes, allAncestors).size() 
					>= INSTANCE_OF_TYPES_NOT_IN_HIERARCHY_THRESHOLD) {
				return true;
			}
		}
		return false;
	}
	
	private List<SM_Type> getAllAncestors(SM_Type type, List<SM_Type> ancestors) {
		//FIXME : replace recursion with iterative loop to avoid stack overflows.
		try {
			for (SM_Type superType : type.getSuperTypes()) {
				if (!ancestors.contains(superType)) {
					ancestors.add(superType);
				}
				getAllAncestors(superType, ancestors);
			}
			return ancestors;
		} catch (StackOverflowError er) {
			System.err.println("Ancestors analysis skipped due to memory overflow.");
			return ancestors;
		}
	}
	
	private List<SM_Type> setDifference(List<SM_Type> oneList, List<SM_Type> otherList) {
		List<SM_Type> outcome = new ArrayList<>();
		for (SM_Type type : oneList) {
			if (!otherList.contains(type)) {
				outcome.add(type);
			}
		}
		return outcome;
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
			//This looks crazy but in some cases 'getSuperTypes()' call returns itself as supertype
			//The following check is to avoid StackOverFlow Exception.
			if (ancestorType == deeperAncestor)
				return false;
			if (sameAsSomeAncestor(targetType, deeperAncestor)) {
				return true;
			}
		}
		return false;
	}
	
	public List<DesignCodeSmell> detectRebeliousHierarchy() {
		if (hasRebeliousHierarchy()) {
			addToSmells(initializeCodeSmell(REBELIOUS_HIERARCHY));
		}
		return getSmells();
	}
	
	private boolean hasRebeliousHierarchy() {
		for (SM_Method method : getTypeMetrics().getType().getMethodList()) {
			MethodMetrics methodMetrics = getTypeMetrics().getType().getMetricsFromMethod(method);
			if (hasSuperType()) {
				if (hasEmptyBody(methodMetrics) || hasOnlyAThrowStatement(methodMetrics, method)) {
					for (SM_Type superType : getTypeMetrics().getType().getSuperTypes()) {
						if (methodIsOverriden(method, superType)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean hasSuperType() {
		return getTypeMetrics().getType().getSuperTypes().size() > 0;
	}
	
	private boolean hasEmptyBody(MethodMetrics methodMetrics) {
		return methodMetrics.getNumOfLines() == EMPTY_BODY;
	}
	
	private boolean hasOnlyAThrowStatement(MethodMetrics methodMetrics, SM_Method method) {
		return methodMetrics.getNumOfLines() == ONLY_ONE_STATEMENT
				&& method.throwsException();
	}
	
	private boolean methodIsOverriden(SM_Method method, SM_Type type) {
		if (existMethodWithSameSignature(method, type)) {
			return true;
		}
		boolean flag = false;
		// FIXME : switch to iterative process to avoid stack overflows
		for (SM_Type superType : type.getSuperTypes()) {
			try {
				flag = methodIsOverriden(method, superType);
			} catch (StackOverflowError er) {
				System.err.println("Method is overriden analysis skipped due to memory overflow");
			}
		}
		return flag;
	}
	
	private boolean existMethodWithSameSignature(SM_Method method, SM_Type type) {
		for (SM_Method otherMethod : type.getMethodList()) {
			if (haveSameSignature(method, otherMethod)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean haveSameSignature(SM_Method method, SM_Method otherMethod) {
		return method.getName().equals(otherMethod.getName())
				&& haveSameArguments(method, otherMethod);
	}
	
	private boolean haveSameArguments(SM_Method method, SM_Method otherMethod) {
		if (method.getParameterList().size() != otherMethod.getParameterList().size()) {
			return false;
		}
		for (int i = 0; i < method.getParameterList().size(); i++) {
			if (!getParameterTypeFromIndex(method, i).equals(getParameterTypeFromIndex(otherMethod, i))) {
				return false;
			}
		}
		return true;
	}
	
	private String getParameterTypeFromIndex(SM_Method method, int index) {
		return method.getParameterList().get(index).getTypeOverallToString();
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
