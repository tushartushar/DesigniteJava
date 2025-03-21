package Designite.smells.designSmells;

import Designite.smells.models.CodeSmell;

import java.util.ArrayList;
import java.util.List;

public abstract class CodeSmellDetector<T extends CodeSmell> {

    protected List<T> smells = new ArrayList<>();

    protected void addToSmells(T smell) {
        smells.add(smell);
    }

    public abstract T initializeCodeSmell(String smellName);

}
