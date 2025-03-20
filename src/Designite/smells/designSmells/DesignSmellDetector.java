package Designite.smells.designSmells;

import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.TypeMetrics;
import Designite.smells.ThresholdsDTO;
import Designite.smells.models.DesignCodeSmell;

import java.util.ArrayList;
import java.util.List;

public abstract class DesignSmellDetector extends CodeSmellDetector<DesignCodeSmell> {


    private final TypeMetrics typeMetrics;
    private final SourceItemInfo info;
    private final ThresholdsDTO thresholdsDTO;

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
        return new DesignCodeSmell(getSourceItemInfo().getProjectName(), getSourceItemInfo().getPackageName(), getSourceItemInfo().getTypeName(), smellName);
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
