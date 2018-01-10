package Designite.smells;

public class ThresholdsDTO {
	
	private int insufficientModularizationLargePublicInterface = 20;
	private int insufficientModularizationLargeNumOfMethods = 30;
	private int insufficientModularizationHighComplexity = 100;
	
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
