package Designite.smells.designSmells;

import java.util.ArrayList;
import java.util.List;

import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.TypeMetrics;
import Designite.smells.models.DesignCodeSmell;

public class DesignSmellFacade {
	
	private ModularizationSmellDetector modularizationSmellDetector;
	
	private List<DesignCodeSmell> smells;
	
	public DesignSmellFacade(TypeMetrics typeMetrics, SourceItemInfo info) {
		
		modularizationSmellDetector = new ModularizationSmellDetector(typeMetrics, info);
		
		smells = new ArrayList<>();
	}
	
	public List<DesignCodeSmell> detectCodeSmells() {
		smells.addAll(modularizationSmellDetector.detectCodeSmells());
		
		return smells;
	}
	
}
