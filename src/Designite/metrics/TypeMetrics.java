package Designite.metrics;

import java.util.List;

import org.eclipse.jdt.core.dom.TypeDeclaration;

import Designite.SourceModel.AccessStates;
import Designite.SourceModel.SM_Field;
import Designite.SourceModel.SM_Method;
import Designite.SourceModel.SM_Type;

public class TypeMetrics implements MetricExtractor {
	
	private int numOfFields;
	private int numOfPublicFields;
	private int numOfMethods;
	private int numOfPublicMethods;
	private int depthOfInheritance;
	private int numOfLines;
	
	private List<SM_Field> fieldList;
	private List<SM_Method> methodList;
	private List<SM_Type> superTypes;
	private TypeDeclaration typeDeclaration;
	
	public TypeMetrics(List<SM_Field> fieldList
			, List<SM_Method> methodList
			, List<SM_Type> superTypes
			, TypeDeclaration typeDeclaration) {
		this.fieldList = fieldList;
		this.methodList = methodList;
		this.superTypes = superTypes;
		this.typeDeclaration = typeDeclaration;
	}
	
	@Override
	public void extractMetrics() {
		extractNumOfFieldMetrics();
		extractNumOfMethodsMetrics();
		extractDepthOfInheritance();
		extractNumberOfLines();
	}
	
	private void extractNumOfFieldMetrics() {
		for (SM_Field field : fieldList) {
			numOfFields++;
			if (field.getAccessModifier() == AccessStates.PUBLIC) {
				numOfPublicFields++;
			}	
		}
	}
	
	private void extractNumOfMethodsMetrics() {
		for (SM_Method method : methodList) {
			numOfMethods++;
			if (method.getAccessModifier() == AccessStates.PUBLIC) {
				numOfPublicMethods++;
			}
		}
	}
	
	private void extractDepthOfInheritance() {
		depthOfInheritance += findInheritanceDepth(superTypes);
	}
	
	private void extractNumberOfLines() {
		String body = typeDeclaration.toString();
		numOfLines = body.length() - body.replace("\n", "").length();
	}
	
	private int findInheritanceDepth(List<SM_Type> superTypes) {
		if (superTypes.size() == 0) {
			return 0;
		}
		return findInheritanceDepth(superTypes.get(0).getSuperTypes()) + 1;
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
	
}
