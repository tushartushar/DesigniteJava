package Designite.metrics;

import java.util.List;

import Designite.SourceModel.SM_Field;
import Designite.SourceModel.SM_Method;
import Designite.SourceModel.SM_Type;

public class MethodMetrics extends Metrics {

	private int numOfParameters;
	private int cyclomaticComplexity;
	private int numOfLines;
	private int commentLines;
	private int totalComments   = 0;
	private int goodComments    = 0;
	private int badComments     = 0;
	private int unclassifiedComments = 0;
	private String commentQuality = "NO_COMMENTS";
	private int llmGoodComments;
	private int llmBadComments;
	private int llmNeutralComments;
	private double commentQualityScore;
	private String cqiCategory;
	private SM_Method method;

	public int getNumOfParameters() {
		return numOfParameters;
	}

	public int getCyclomaticComplexity() {
		return cyclomaticComplexity;
	}

	public int getNumOfLines() {
		return numOfLines;
	}

	public int getCommentLines() {
		return commentLines;
	}

	public int getUnclassifiedComments() { 
		return unclassifiedComments; 
	}

	public int getTotalComments()     { return totalComments; }
	public int getGoodComments()      { return goodComments; }
	public int getBadComments()       { return badComments; }
	public String getCommentQuality() { return commentQuality; }
	public int getLlmGoodComments() { return llmGoodComments; }
	public int getLlmBadComments() { return llmBadComments; }
	public int getLlmNeutralComments() { return llmNeutralComments; }
	public double getCommentQualityScore() { return commentQualityScore; }
	public String getCqiCategory() { return cqiCategory; }

	public void setNumOfParameters(int numOfParameters) {
		this.numOfParameters = numOfParameters;
	}

	public void setCyclomaticComplexity(int cyclomaticComplexity) {
		this.cyclomaticComplexity = cyclomaticComplexity;
	}

	public void setNumOfLines(int numOfLines) {
		this.numOfLines = numOfLines;
	}

	public void setCommentLines(int commentLines) {
		this.commentLines = commentLines;
	}

	public void setUnclassifiedComments(int unclassifiedComments) { 
		this.unclassifiedComments = unclassifiedComments; 
	}

	public void setTotalComments(int totalComments)   { this.totalComments = totalComments; }
	public void setGoodComments(int goodComments)     { this.goodComments = goodComments; }
	public void setBadComments(int badComments)       { this.badComments = badComments; }
	public void setCommentQuality(String quality)     { this.commentQuality = quality; }
	public void setLlmGoodComments(int val) { this.llmGoodComments = val; }
	public void setLlmBadComments(int val) { this.llmBadComments = val; }
	public void setLlmNeutralComments(int val) { this.llmNeutralComments = val; }
	public void setCommentQualityScore(double val) { this.commentQualityScore = val; }
	public void setCqiCategory(String val) { this.cqiCategory = val; }

	public void setMethod(SM_Method method){
		this.method = method;
	}

	public SM_Method getMethod() {
		return method;
	}

	public List<SM_Field> getDirectFieldAccesses() {
		return method.getDirectFieldAccesses();
	}

	public List<SM_Type> getSMTypesInInstanceOf() {
		return method.getSMTypesInInstanceOf();
	}

}
