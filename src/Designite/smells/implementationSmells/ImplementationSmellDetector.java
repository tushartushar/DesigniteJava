package Designite.smells.implementationSmells;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;

import Designite.SourceModel.SM_Field;
import Designite.SourceModel.SM_LocalVar;
import Designite.SourceModel.SM_Method;
import Designite.SourceModel.SM_Parameter;
import Designite.SourceModel.SourceItemInfo;
import Designite.metrics.MethodMetrics;
import Designite.smells.ThresholdsDTO;
import Designite.smells.models.ImplementationCodeSmell;
import Designite.utils.Logger;
import Designite.visitors.MethodControlFlowVisitor;
import Designite.visitors.NumberLiteralVisitor;

public class ImplementationSmellDetector {
	
	private List<ImplementationCodeSmell> smells;
	
	private MethodMetrics methodMetrics;
	private SourceItemInfo info;
	private ThresholdsDTO thresholdsDTO;
	
	private static final String ABSTRACT_FUMCTION_CALL_FROM_CONSTRUCTOR = "Abstract Function Call From Constructor";
	private static final String COMPLEX_CONDITIONAL = "Complex Conditional";
	private static final String COMPLEX_METHOD = "Complex Method";
	private static final String EMPTY_CATCH_CLAUSE = "Empty catch clause";
	private static final String LONG_IDENTIFIER = "Long Identifier";
	private static final String LONG_METHOD = "Long Method";
	private static final String LONG_PARAMETER_LIST = "Long Parameter List";
	private static final String LONG_STATEMENT = "Long Statement";
	private static final String MAGIC_NUMBER = "Magic Number";
	private static final String MISSING_DEFAULT = "Missing default";
	
	private static final String AND_OPERATOR_REGEX = "\\&\\&";
	private static final String OR_OPERATOR_REGEX = "\\|\\|";
	private static final Pattern EMPTY_BODY_PATTERN = Pattern.compile("^\\{\\s*\\}\\s*$");
	
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
		detectEmptyCatchBlock();
		detectLongIdentifier();
		detectLongMethod();
		detectLongParameterList();
		detectLongStatement();
		detectMagicNumber();
		detectMissingDefault();
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
		hasComplexConditional();
		return smells;
	}
	
	private void hasComplexConditional() {
		MethodControlFlowVisitor visitor = new MethodControlFlowVisitor();
		methodMetrics.getMethod().getMethodDeclaration().accept(visitor);
		for (IfStatement ifStatement : visitor.getIfStatements()) {
			if (numOfBooleanSubExpressions(ifStatement) >=  thresholdsDTO.getComplexCondition()) {
				addToSmells(initializeCodeSmell(COMPLEX_CONDITIONAL));
			}
		}
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
	
	public List<ImplementationCodeSmell> detectEmptyCatchBlock() {
		MethodControlFlowVisitor visitor = new MethodControlFlowVisitor();
		methodMetrics.getMethod().getMethodDeclaration().accept(visitor);
		for (TryStatement tryStatement : visitor.getTryStatements()) {
			for (Object catchClause : tryStatement.catchClauses()) {
				if (!hasBody((CatchClause) catchClause)) {
					addToSmells(initializeCodeSmell(EMPTY_CATCH_CLAUSE));
				}
			}
		}
		return smells;
	}
	
	public boolean hasBody(CatchClause catchClause) {
		String body = catchClause.getBody().toString();
		return !EMPTY_BODY_PATTERN.matcher(body).matches();
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
	
	public List<ImplementationCodeSmell> detectLongStatement() {
		SM_Method currentMethod = methodMetrics.getMethod();
		if(currentMethod.hasBody()) {
			String methodBody = currentMethod.getMethodBody();
			hasLongStatement(methodBody);
		}
		
		return smells;
	}
	
	private void hasLongStatement(String methodBody) {
		//FIXME is there another non-hard-coded to replace the "\n"
		String[] methodStatements = methodBody.split("\n");
		
		for(String singleMethodStatement : methodStatements) {
			singleMethodStatement = singleMethodStatement.trim().replaceAll("\\s+", " ");
			if(isLongStatement(singleMethodStatement)) {
				addToSmells(initializeCodeSmell(LONG_STATEMENT));
			}
		}
	}
	
	private boolean isLongStatement(String statement) {
		return statement.length() > this.thresholdsDTO.getLongStatement();
	}
	
	public List<ImplementationCodeSmell> detectMagicNumber() {
		hasMagicNumbers();
		return smells;
	}
	
	private void hasMagicNumbers() {
		NumberLiteralVisitor visitor = new NumberLiteralVisitor();
		methodMetrics.getMethod().getMethodDeclaration().accept(visitor);
		List<NumberLiteral> literals = visitor.getNumberLiteralsExpressions();
		
		if( literals.size() < 1 ) {
			return;
		}
		
		for(NumberLiteral singleNumberLiteral : literals) {
			if( isLiteralValid(singleNumberLiteral) ) {
				addToSmells(initializeCodeSmell(MAGIC_NUMBER));
			}
		}
	}
	
	private boolean isLiteralValid(NumberLiteral singleNumberLiteral) {
		boolean isValid = isNotZeroOrOne(singleNumberLiteral) && isNotArrayInitialization(singleNumberLiteral);
		return isValid;
	}
	
	// 0s and 1s are not considered as Magic Numbers
	private boolean isNotZeroOrOne(NumberLiteral singleNumberLiteral) {
		String numberToString = singleNumberLiteral.toString().toLowerCase().replaceAll("_", "");
		double literalValue = 0.0;
		try {
			// hex case
			if(numberToString.startsWith("0x")) {
				literalValue = (double)(Long.parseLong(numberToString.replaceAll("0x", "").replaceAll("l", ""),16));
			// long case
			} else if(numberToString.endsWith("l")) {
				literalValue = (double)(Long.parseLong(numberToString.replaceAll("l", "")));
			// float case
			} else if(numberToString.endsWith("f")) {
				literalValue = Float.parseFloat(numberToString.replaceAll("f", ""));
			}
			// double case
			else {
				literalValue = Double.parseDouble(numberToString);
			}
		} catch (NumberFormatException ex) {
			String logMessage = "Exception while parsing literal number (during Magic Number detection). " + ex.getMessage();
			Logger.log(logMessage);
			literalValue = 0.0;
		}
		return literalValue != 0.0 && literalValue != 1.0;
	}
	
	
	// Literals in array initializations (such as int[] arr = {0,1};) are not considered as Magic Numbers
	private boolean isNotArrayInitialization(NumberLiteral singleNumberLiteral) {
		return singleNumberLiteral.getParent().getNodeType() != ASTNode.ARRAY_INITIALIZER;
	}
	
	public List<ImplementationCodeSmell> detectMissingDefault() {
		hasMissingDefaults();
		return smells;
	}
	
	private void hasMissingDefaults() {
		MethodControlFlowVisitor visitor = new MethodControlFlowVisitor();
		methodMetrics.getMethod().getMethodDeclaration().accept(visitor);
		List<SwitchStatement> switchStatements = visitor.getSwitchStatements();
		for(SwitchStatement singleSwitchStatement : switchStatements) {
			if(switchIsMissingDefault(singleSwitchStatement)) {
				addToSmells(initializeCodeSmell(MISSING_DEFAULT));
			}
		}
	}
	
	private boolean switchIsMissingDefault(SwitchStatement switchStatement) {
		List<Statement> statetmentsOfSwitch = switchStatement.statements();
		for(Statement stm : statetmentsOfSwitch) {
			if ((stm instanceof SwitchCase) && ((SwitchCase)stm).isDefault()) {
				return true;
			}
		}
		return false;			
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
