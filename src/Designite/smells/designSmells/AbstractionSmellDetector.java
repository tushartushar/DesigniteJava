package Designite.smells.designSmells;

import java.util.List;

import Designite.SourceModel.SM_Type;
import Designite.SourceModel.SourceItemInfo;
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
		return getTypeMetrics().getNumOfPublicFields() == 1 && 
				getTypeMetrics().getNumOfLines() <= getThresholdsDTO().getImperativeAbstractionLargeNumOfLines();
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
		return getTypeMetrics().getSuperTypes().size() > 0; 
	}
	
	private boolean hasSuperTypeWithFanIn() {
		for (SM_Type superType : getTypeMetrics().getSuperTypes()) {
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
