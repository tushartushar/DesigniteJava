package Designite.smells.designSmells;

import java.util.List;

import Designite.SourceModel.SM_Method;
import Designite.SourceModel.SM_Project;
import Designite.SourceModel.SM_Type;
import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.TypeMetrics;
import Designite.smells.models.DesignCodeSmell;
import Designite.utils.models.Graph;

public class EncapsulationSmellDetector extends DesignSmellDetector {
	
	private static final String DEFICIENT_ENCAPSULATION = "Deficient Encapsulation";
	private static final String UNEXPLOITED_ENCAPSULATION = "Unexploited Encapsulation";
	
	public EncapsulationSmellDetector(TypeMetrics typeMetrics
			, SourceItemInfo info) {
		super(typeMetrics, info);
	}
	
	@Override
	public List<DesignCodeSmell> detectCodeSmells() {
		detectDeficientEncapsulation();
		detectUnexploitedEncapsulation();
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
	
	public List<DesignCodeSmell> detectUnexploitedEncapsulation() {
		if (hasUnexploitedEncapsulation()) {
			addToSmells(initializeCodeSmell(UNEXPLOITED_ENCAPSULATION));
		}
		return getSmells();
	}
	
	private boolean hasUnexploitedEncapsulation() {
		for (SM_Method method : getTypeMetrics().getType().getMethodList()) {
			for (SM_Type type : method.getSMTypesInInstanceOf()) {
				for (SM_Type crossType : method.getSMTypesInInstanceOf()) {
					if (!type.equals(crossType) && inSameHierarchy(type, crossType)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean inSameHierarchy(SM_Type type, SM_Type crossType) {
		Graph hierarchyGraph = getProject(type).getHierarchyGraph();
		return hierarchyGraph.inSameConnectedComponent(type, crossType);
	}
	
	private SM_Project getProject(SM_Type type) {
		return type.getParentPkg().getParentProject();
	}
	
}
