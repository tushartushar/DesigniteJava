package Designite.smells.designSmells;

import java.util.ArrayList;
import java.util.List;

import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.TypeMetrics;
import Designite.smells.models.DesignCodeSmell;

public class DesignSmellDetector {
	
	private List<DesignCodeSmell> smells;
	
	private TypeMetrics typeMetrics;
	private SourceItemInfo info;
	
	public DesignSmellDetector(TypeMetrics typeMetrics, SourceItemInfo info) {
		this.typeMetrics = typeMetrics;
		this.info = info;
		
		smells = new ArrayList<>();
	}
	
	public List<DesignCodeSmell> getSmells() {
		return smells;
	}
	
	protected void addToSmells(DesignCodeSmell smell) {
		smells.add(smell);
	}
	
	protected TypeMetrics getTypeMetrics() {
		return typeMetrics;
	}
	
	protected SourceItemInfo getSourceItemInfo() {
		return info;
	}

}
