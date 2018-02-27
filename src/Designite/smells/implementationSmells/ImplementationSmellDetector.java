package Designite.smells.implementationSmells;

import java.util.ArrayList;
import java.util.List;

import Designite.SourceModel.SM_Field;
import Designite.SourceModel.SM_LocalVar;
import Designite.SourceModel.SM_Parameter;
import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.MethodMetrics;
import Designite.smells.ThresholdsDTO;
import Designite.smells.models.ImplementationCodeSmell;

public class ImplementationSmellDetector {
	
	private List<ImplementationCodeSmell> smells;
	
	private MethodMetrics methodMetrics;
	private SourceItemInfo info;
	private ThresholdsDTO thresholdsDTO;
	
	private static final String COMPLEX_METHOD = "Complex Method";
	private static final String LONG_IDENTIFIER = "Long Identifier";
	private static final String LONG_METHOD = "Long Method";
	private static final String LONG_PARAMETER_LIST = "Long Parameter List";
	
	public ImplementationSmellDetector(MethodMetrics methodMetrics, SourceItemInfo info) {
		this.methodMetrics = methodMetrics;
		this.info = info;
		
		thresholdsDTO = new ThresholdsDTO();
		smells = new ArrayList<>();
	}
	
	public List<ImplementationCodeSmell> detectCodeSmells() {
		detectComplexMethod();
		detectLongIdentifier();
		detectLongMethod();
		detectLongParameterList();
		return smells;
	}
	
	public List<ImplementationCodeSmell> detectComplexMethod() {
		if (hasComplexMethod()) {
			addToSmells(initializeCodeSmell(COMPLEX_METHOD));
		}
		return smells;
	}
	
	private boolean hasComplexMethod() {
		return methodMetrics.getCyclomaticComplexity() >= thresholdsDTO.getComplexMethod();
	}
	
	public List<ImplementationCodeSmell> detectLongIdentifier() {
		if (hasLongIdentifier()) {
			addToSmells(initializeCodeSmell(LONG_IDENTIFIER));
		}
		return smells;
	}
	
	private boolean hasLongIdentifier() {
		return hasLongParameter() || hasLongLocalVar() || hasLongFieldAccess();
	}
	
	private boolean hasLongParameter() {
		for (SM_Parameter parameter : methodMetrics.getMethod().getParameterList()) {
			if (parameter.getName().length() >= thresholdsDTO.getLongIdentifier()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean hasLongLocalVar() {
		for (SM_LocalVar var : methodMetrics.getMethod().getLocalVarList()) {
			if (var.getName().length() >= thresholdsDTO.getLongIdentifier()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean hasLongFieldAccess() {
		for (SM_Field field : methodMetrics.getMethod().getDirectFieldAccesses()) {
			if (field.getName().length() >= thresholdsDTO.getLongIdentifier()) {
				return true;
			}
		}
		return false;
	}
	
	public List<ImplementationCodeSmell> detectLongMethod() {
		if (hasLongMethod()) {
			addToSmells(initializeCodeSmell(LONG_METHOD));
		}
		return smells;
	}
	
	private boolean hasLongMethod() {
		return methodMetrics.getNumOfLines() >= thresholdsDTO.getLongMethod();
	}
	
	public List<ImplementationCodeSmell> detectLongParameterList() {
		if (hasLongParameterList()) {
			addToSmells(initializeCodeSmell(LONG_PARAMETER_LIST));
		}
		return smells;
	}
	
	private boolean hasLongParameterList() {
		return methodMetrics.getNumOfParameters() >= thresholdsDTO.getLongParameterList();
	}
	
	public List<ImplementationCodeSmell> getSmells() {
		return smells;
	}
	
	private ImplementationCodeSmell initializeCodeSmell(String smellName) {
		return new ImplementationCodeSmell(info.getProjectName()
				, info.getPackageName()
				, info.getTypeName()
				, info.getMethodName()
				, smellName);
	}
	
	private void addToSmells(ImplementationCodeSmell smell) {
		smells.add(smell);
	}

}
