package Designite.smells.designSmells;

import java.util.List;

import Designite.SourceModel.AccessStates;
import Designite.SourceModel.SM_Method;
import Designite.SourceModel.SM_Type;
import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.MethodMetrics;
import Designite.metrics.TypeMetrics;
import Designite.smells.models.DesignCodeSmell;

public class AbstractionSmellDetector extends DesignSmellDetector {
	
	private static final String IMPERATIVE_ABSTRACTION = "Imperative Abstraction";
	private static final String MULTIFACETED_ABSTRACTION = "Multifaceted Abstraction";
	private static final String UNNECESSARY_ABSTRACTION = "Unnecessary Abstraction";
	private static final String UNUTILIZED_ABSTRACTION = "Unutilized Abstraction";
	
	public AbstractionSmellDetector(TypeMetrics typeMetrics
			, SourceItemInfo info) {
		super(typeMetrics, info);
	}

	@Override
	public List<DesignCodeSmell> detectCodeSmells() {
		detectImperativeAbstraction();
		detectMultifacetedAbstraction();
		detectUnnecessaryAbstraction();
		detectUnutilizedAbstraction();
		return getSmells();
	}
	
	public List<DesignCodeSmell> detectImperativeAbstraction() {
		if (hasImperativeAbstraction()) {
			addToSmells(initializeCodeSmell(IMPERATIVE_ABSTRACTION));
		}
		return getSmells();
	}
	
	public boolean hasImperativeAbstraction() {
		SM_Type currentType = getTypeMetrics().getType();
		if(getTypeMetrics().getNumOfPublicMethods() != 1 ) {
			return false;
		} else {
			List<SM_Method> methods = currentType.getMethodList();
			for(SM_Method method : methods) {
				if (method.getAccessModifier() == AccessStates.PUBLIC) {
					MethodMetrics metrics = currentType.getMetricsFromMethod(method);
					if(metrics.getNumOfLines() > getThresholdsDTO().getImperativeAbstractionLargeNumOfLines()){
						return true;
					}
				}
			}
			return false;
		}
	}
	
	public List<DesignCodeSmell> detectMultifacetedAbstraction() {
		if (hasMultifacetedAbstraction()) {
			addToSmells(initializeCodeSmell(MULTIFACETED_ABSTRACTION));
		}
		return getSmells();
	}
	
	private boolean hasMultifacetedAbstraction() {
		return getTypeMetrics().getLcom() >= getThresholdsDTO().getMultifacetedAbstractionLargeLCOM()
				&& getTypeMetrics().getNumOfFields() >= getThresholdsDTO().getMultifacetedAbstractionManyFields()
				&& getTypeMetrics().getNumOfMethods() >= getThresholdsDTO().getMultifacetedAbstractionManyMethods();
	}
	
	public List<DesignCodeSmell> detectUnnecessaryAbstraction() {
		if (hasUnnecessaryAbstraction()) {
			addToSmells(initializeCodeSmell(UNNECESSARY_ABSTRACTION));
		}
		return getSmells();
	}
	
	private boolean hasUnnecessaryAbstraction() {
		return getTypeMetrics().getNumOfMethods() == 0
				&& getTypeMetrics().getNumOfFields() 
					<= getThresholdsDTO().getUnnecessaryAbstractionFewFields();
	}
	
	public List<DesignCodeSmell> detectUnutilizedAbstraction() {
		if (hasUnutilizedAbstraction()) {
			addToSmells(initializeCodeSmell(UNUTILIZED_ABSTRACTION));
		}
		return getSmells();
	}
	
	private boolean hasUnutilizedAbstraction() {
		if (hasSuperTypes()) {
			return !hasSuperTypeWithFanIn() || !hasFanIn(getTypeMetrics());
		}
		return !hasFanIn(getTypeMetrics());
	}
	
	private boolean hasSuperTypes() {
		return getTypeMetrics().getType().getSuperTypes().size() > 0; 
	}
	
	private boolean hasSuperTypeWithFanIn() {
		for (SM_Type superType : getTypeMetrics().getType().getSuperTypes()) {
			if (hasFanIn(superType.getParentPkg().getMetricsFromType(superType))) {
				return true;
			}
		}
		return false;
	}
	
	private boolean hasFanIn(TypeMetrics metrics) {
		return metrics.getNumOfFanInTypes() > 0;
	}

}
