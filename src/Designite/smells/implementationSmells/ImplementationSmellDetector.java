package Designite.smells.implementationSmells;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.IfStatement;

import Designite.SourceModel.SM_Field;
import Designite.SourceModel.SM_LocalVar;
import Designite.SourceModel.SM_Method;
import Designite.SourceModel.SM_Parameter;
import Designite.SourceModel.SM_Type;
import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.MethodMetrics;
import Designite.smells.ThresholdsDTO;
import Designite.smells.models.ImplementationCodeSmell;
import Designite.visitors.MethodControlFlowVisitor;

public class ImplementationSmellDetector {
	
	private List<ImplementationCodeSmell> smells;
	
	private MethodMetrics methodMetrics;
	private SourceItemInfo info;
	private ThresholdsDTO thresholdsDTO;
	
	private static final String ABSTRACT_FUMCTION_CALL_FROM_CONSTRUCTOR = "Abstract Function Call From Constructor";
	private static final String COMPLEX_CONDITIONAL = "Complex Cnditional";
	private static final String COMPLEX_METHOD = "Complex Method";
	private static final String LONG_IDENTIFIER = "Long Identifier";
	private static final String LONG_METHOD = "Long Method";
	private static final String LONG_PARAMETER_LIST = "Long Parameter List";
	
	private static final String AND_OPERATOR_REGEX = "\\&\\&";
	private static final String OR_OPERATOR_REGEX = "\\|\\|";
	
	public ImplementationSmellDetector(MethodMetrics methodMetrics, SourceItemInfo info) {
		this.methodMetrics = methodMetrics;
		this.info = info;
		
		thresholdsDTO = new ThresholdsDTO();
		smells = new ArrayList<>();
	}
	
	public List<ImplementationCodeSmell> detectCodeSmells() {
		detectAbstractFunctionCallFromConstructor();
		detectComplexConditional();
		detectComplexMethod();
		detectLongIdentifier();
		detectLongMethod();
		detectLongParameterList();
		return smells;
	}
	
	public List<ImplementationCodeSmell> detectAbstractFunctionCallFromConstructor() {
		if (hasAbstractFunctionCallFromConstructor()) {
			addToSmells(initializeCodeSmell(ABSTRACT_FUMCTION_CALL_FROM_CONSTRUCTOR));
		}
		return smells;
	}
	
	private boolean hasAbstractFunctionCallFromConstructor() {
		SM_Method method = methodMetrics.getMethod();
		if (method.isConstructor()) {
			for (SM_Method calledMethod : method.getCalledMethods()) {
				if (calledMethod.isAbstract()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public List<ImplementationCodeSmell> detectComplexConditional() {
		if (hasComplexConditional()) {
			addToSmells(initializeCodeSmell(COMPLEX_CONDITIONAL));
		}
		return smells;
	}
	
	private boolean hasComplexConditional() {
		MethodControlFlowVisitor visitor = new MethodControlFlowVisitor();
		methodMetrics.getMethod().getMethodDeclaration().accept(visitor);
		if (hasComplexIfCondition(visitor)) {
			return true;
		}
		return false;
	}
	
	private boolean hasComplexIfCondition(MethodControlFlowVisitor visitor) {
		for (IfStatement ifStatement : visitor.getIfStatements()) {
			if (numOfBooleanSubExpressions(ifStatement) >=  thresholdsDTO.getComplexCondition()) {
				return true;
			}
		}
		return false;
	}
	
	private String getBooleaRegex() {
		return AND_OPERATOR_REGEX + "|" + OR_OPERATOR_REGEX;
	}
	
	private int numOfBooleanSubExpressions(IfStatement ifStatement) {
		return ifStatement.getExpression().toString().split(getBooleaRegex()).length;
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
