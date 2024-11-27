package Designite.metrics;

import java.util.ArrayList;
import java.util.List;

import Designite.SourceModel.AccessStates;
import Designite.SourceModel.SM_Field;
import Designite.SourceModel.SM_Method;
import Designite.SourceModel.SM_Type;

import Designite.utils.models.Edge;
import Designite.utils.models.Graph;
import Designite.utils.models.Vertex;

public class TypeMetrics extends Metrics{
	
	private int numOfFields;
	private int numOfPublicFields;
	private int numOfMethods;
	private int numOfPublicMethods;
	private int depthOfInheritance;
	private int numOfLines;
	private int numOfChildren;
	private int weightedMethodsPerClass;
	private int numOfFanOutTypes;
	private int numOfFanInTypes;
	private double lcom;
	private SM_Type type;

	public void setNumOfFields(int numOfFields) {
		this.numOfFields = numOfFields;
	}

	public void setNumOfPublicFields(int numOfPublicFields) {
		this.numOfPublicFields = numOfPublicFields;
	}

	public void setNumOfMethods(int numOfMethods) {
		this.numOfMethods = numOfMethods;
	}

	public void setNumOfPublicMethods(int numOfPublicMethods) {
		this.numOfPublicMethods = numOfPublicMethods;
	}

	public void setDepthOfInheritance(int depthOfInheritance) {
		this.depthOfInheritance = depthOfInheritance;
	}

	public void setNumOfLines(int numOfLines) {
		this.numOfLines = numOfLines;
	}

	public void setNumOfChildren(int numOfChildren) {
		this.numOfChildren = numOfChildren;
	}

	public void setWeightedMethodsPerClass(int weightedMethodsPerClass) {
		this.weightedMethodsPerClass = weightedMethodsPerClass;
	}

	public void setNumOfFanOutTypes(int numOfFanOutTypes) {
		this.numOfFanOutTypes = numOfFanOutTypes;
	}

	public void setNumOfFanInTypes(int numOfFanInTypes) {
		this.numOfFanInTypes = numOfFanInTypes;
	}

	public void setLcom(double lcom) {
		this.lcom = lcom;
	}

	public int getNumOfFields() {
		return numOfFields;
	}

	public int getNumOfPublicFields() {
		return numOfPublicFields;
	}
	
	public int getNumOfMethods() {
		return numOfMethods;
	}
	
	public int getNumOfPublicMethods() {
		return numOfPublicMethods;
	}
	
	public int getInheritanceDepth() {
		return depthOfInheritance;
	}

	public int getNumOfLines() {
		return numOfLines;
	}

	public int getNumOfChildren() {
		return numOfChildren;
	}

	public int getWeightedMethodsPerClass() {
		return weightedMethodsPerClass;
	}

	public int getNumOfFanOutTypes() {
		return numOfFanOutTypes;
	}

	public int getNumOfFanInTypes() {
		return numOfFanInTypes;
	}

	public double getLcom() {
		return lcom;
	}
	public SM_Type getType() {
		return type;
	}
	public void setType(SM_Type type){
		this.type = type;
	}

}
