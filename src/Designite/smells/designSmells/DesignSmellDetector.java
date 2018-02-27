package Designite.smells.designSmells;

import java.util.ArrayList;
import java.util.List;

import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.TypeMetrics;
import Designite.smells.ThresholdsDTO;
import Designite.smells.models.DesignCodeSmell;

public abstract class DesignSmellDetector {
	
	private List<DesignCodeSmell> smells;
	
	private TypeMetrics typeMetrics;
	private SourceItemInfo info;
	private ThresholdsDTO thresholdsDTO;
	
	public DesignSmellDetector(TypeMetrics typeMetrics, SourceItemInfo info) {
		this.typeMetrics = typeMetrics;
		this.info = info;
		
		thresholdsDTO = new ThresholdsDTO();
		smells = new ArrayList<>();
	}
	
	abstract public List<DesignCodeSmell> detectCodeSmells();
	
	public List<DesignCodeSmell> getSmells() {
		return smells;
	}
	
	public DesignCodeSmell initializeCodeSmell(String smellName) {
		return new DesignCodeSmell(getSourceItemInfo().getProjectName()
				, getSourceItemInfo().getPackageName()
				, getSourceItemInfo().getTypeName()
				, smellName);
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
	
	protected ThresholdsDTO getThresholdsDTO() {
		return thresholdsDTO;
	}

}
