package Designite.smells.designSmells;

import java.util.ArrayList;
import java.util.List;

import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.TypeMetrics;
import Designite.smells.models.DesignCodeSmell;

public class DesignSmellFacade {
	
	private AbstractionSmellDetector abstractionSmellDetector;
	private EncapsulationSmellDetector encapsulationSmellDetector;
	private HierarchySmellDetector hierarchySmellDetector;
	private ModularizationSmellDetector modularizationSmellDetector;
	
	private List<DesignCodeSmell> smells;
	
	public DesignSmellFacade(TypeMetrics typeMetrics, SourceItemInfo info) {
		
		abstractionSmellDetector = new AbstractionSmellDetector(typeMetrics, info);
		encapsulationSmellDetector = new EncapsulationSmellDetector(typeMetrics, info);
		hierarchySmellDetector = new HierarchySmellDetector(typeMetrics, info);
		modularizationSmellDetector = new ModularizationSmellDetector(typeMetrics, info);
		
		smells = new ArrayList<>();
	}
	
	public List<DesignCodeSmell> detectCodeSmells() {
		smells.addAll(abstractionSmellDetector.detectCodeSmells());
		smells.addAll(encapsulationSmellDetector.detectCodeSmells());
		smells.addAll(hierarchySmellDetector.detectCodeSmells());
		smells.addAll(modularizationSmellDetector.detectCodeSmells());
		
		return smells;
	}
	
}
