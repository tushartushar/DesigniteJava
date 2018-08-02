package Designite.smells;

public class ThresholdsDTO {
	
	private int complexCondition = 3;
	private int complexMethod = 8;
	private int longIdentifier = 30;
	private int longMethod = 100;
	private int longParameterList = 5;
	private int longStatement = 120;
	
	private int imperativeAbstractionLargeNumOfLines = 50;
	private double multifacetedAbstractionLargeLCOM = 0.8;
	private int multifacetedAbstractionManyFields = 7;
	private int multifacetedAbstractionManyMethods = 7;
	private int unnecessaryAbstractionFewFields = 5;
	
	private int deepHierarchy = 6;
	private int wideHierarchy = 10;
	
	private int brokenModularizationLargeFieldSet = 5;
	private int hubLikeModularizationLargeFanIn = 20;
	private int hubLikeModularizationLargeFanOut = 20;
	private int insufficientModularizationLargePublicInterface = 20;
	private int insufficientModularizationLargeNumOfMethods = 30;
	private int insufficientModularizationHighComplexity = 100;
	
	public int getComplexCondition() {
		return complexCondition;
	}

	public void setComplexCondition(int complexCondition) {
		this.complexCondition = complexCondition;
	}

	public int getComplexMethod() {
		return complexMethod;
	}

	public void setComplexMethod(int complexMethod) {
		this.complexMethod = complexMethod;
	}

	public int getLongIdentifier() {
		return longIdentifier;
	}

	public void setLongIdentifier(int longIdentifier) {
		this.longIdentifier = longIdentifier;
	}

	public int getLongMethod() {
		return longMethod;
	}

	public void setLongMethod(int longMethod) {
		this.longMethod = longMethod;
	}
	
	public int getLongParameterList() {
		return longParameterList;
	}

	public void setLongParameterList(int longParameterList) {
		this.longParameterList = longParameterList;
	}
	
	public void setLongStatement(int longStatement) {
		this.longStatement = longStatement;
	}
	
	public int getLongStatement() {
		return this.longStatement;
	}

	public int getImperativeAbstractionLargeNumOfLines() {
		return imperativeAbstractionLargeNumOfLines;
	}

	public void setImperativeAbstractionLargeNumOfLines(int imperativeAbstractionLargeNumOfLines) {
		this.imperativeAbstractionLargeNumOfLines = imperativeAbstractionLargeNumOfLines;
	}

	public double getMultifacetedAbstractionLargeLCOM() {
		return multifacetedAbstractionLargeLCOM;
	}

	public void setMultifacetedAbstractionLargeLCOM(double multifacetedAbstractionLargeLCOM) {
		this.multifacetedAbstractionLargeLCOM = multifacetedAbstractionLargeLCOM;
	}

	public int getMultifacetedAbstractionManyFields() {
		return multifacetedAbstractionManyFields;
	}

	public void setMultifacetedAbstractionManyFields(int multifacetedAbstractionManyFields) {
		this.multifacetedAbstractionManyFields = multifacetedAbstractionManyFields;
	}

	public int getMultifacetedAbstractionManyMethods() {
		return multifacetedAbstractionManyMethods;
	}

	public void setMultifacetedAbstractionManyMethods(int multifacetedAbstractionManyMethods) {
		this.multifacetedAbstractionManyMethods = multifacetedAbstractionManyMethods;
	}

	public int getUnnecessaryAbstractionFewFields() {
		return unnecessaryAbstractionFewFields;
	}

	public void setUnnecessaryAbstractionFewFields(int unnecessaryAbstractionFewFields) {
		this.unnecessaryAbstractionFewFields = unnecessaryAbstractionFewFields;
	}

	public int getDeepHierarchy() {
		return deepHierarchy;
	}
	
	public void setDeepHierarchy(int deepHierarchy) {
		this.deepHierarchy = deepHierarchy;
	}
	
	public int getWideHierarchy() {
		return wideHierarchy;
	}
	
	public void setWideHierarchy(int wideHierarchy) {
		this.wideHierarchy = wideHierarchy;
	}
	
	public int getBrokenModularizationLargeFieldSet() {
		return brokenModularizationLargeFieldSet;
	}

	public void setBrokenModularizationLargeFieldSet(int brokenModularizationLargeFieldSet) {
		this.brokenModularizationLargeFieldSet = brokenModularizationLargeFieldSet;
	}
	
	public int getHubLikeModularizationLargeFanIn() {
		return hubLikeModularizationLargeFanIn;
	}

	public void setHubLikeModularizationLargeFanIn(int hubLikeModularizationLargeFanIn) {
		this.hubLikeModularizationLargeFanIn = hubLikeModularizationLargeFanIn;
	}

	public int getHubLikeModularizationLargeFanOut() {
		return hubLikeModularizationLargeFanOut;
	}

	public void setHubLikeModularizationLargeFanOut(int hubLikeModularizationLargeFanOut) {
		this.hubLikeModularizationLargeFanOut = hubLikeModularizationLargeFanOut;
	}

	public int getInsufficientModularizationLargePublicInterface() {
		return insufficientModularizationLargePublicInterface;
	}
	
	public void setInsufficientModularizationLargePublicInterface(int insufficientModularizationLargePublicInterface) {
		this.insufficientModularizationLargePublicInterface = insufficientModularizationLargePublicInterface;
	}
	
	public int getInsufficientModularizationLargeNumOfMethods() {
		return insufficientModularizationLargeNumOfMethods;
	}
	
	public void setInsufficientModularizationLargeNumOfMethods(int insufficientModularizationLargeNumOfMethods) {
		this.insufficientModularizationLargeNumOfMethods = insufficientModularizationLargeNumOfMethods;
	}
	
	public int getInsufficientModularizationHighComplexity() {
		return insufficientModularizationHighComplexity;
	}
	
	public void setInsufficientModularizationHighComplexity(int insufficientModularizationHighComplexity) {
		this.insufficientModularizationHighComplexity = insufficientModularizationHighComplexity;
	}

}
