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

public class TypeMetrics implements MetricExtractor {
	
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
	
	private Graph graph;
	
	public TypeMetrics(SM_Type type) {
		
		this.type = type;
	}
	
	@Override
	public void extractMetrics() {
		extractNumOfFieldMetrics();
		extractNumOfMethodsMetrics();
		extractDepthOfInheritance();
		extractNumberOfLines();
		extractNumberOfChildren();
		extractWeightedMethodsPerClass();
		extractNumOfFanOutTypes();
		extractNumOfFanInTypes();
		extractLCOM();
	}
	
	private void extractNumOfFieldMetrics() {
		for (SM_Field field : type.getFieldList()) {
			numOfFields++;
			if (field.getAccessModifier() == AccessStates.PUBLIC) {
				// do not calculate fields that belong to a nested class with a stricter access modifier
				SM_Type nestedParent = field.getNestedParent();
				if(nestedParent != null && nestedParent.getAccessModifier() != AccessStates.PUBLIC) {
					continue;
				}
				numOfPublicFields++;
			}	
		}
	}
	
	private void extractNumOfMethodsMetrics() {
		for (SM_Method method : type.getMethodList()) {
			numOfMethods++;
			if (method.getAccessModifier() == AccessStates.PUBLIC) {
				numOfPublicMethods++;
			}
		}
	}
	
	private void extractDepthOfInheritance() {
		depthOfInheritance += findInheritanceDepth(type.getSuperTypes());
	}
	
	private void extractNumberOfLines() {
		String body = type.getTypeDeclaration().toString();
		numOfLines = body.length() - body.replace("\n", "").length();
	}
	
	private void extractNumberOfChildren() {
		numOfChildren = type.getSubTypes().size();
	}
	
	private int findInheritanceDepth(List<SM_Type> superTypes) {
		if (superTypes.size() == 0) {
			return 0;
		}
		List<SM_Type> deeperSuperTypes = new ArrayList<>();
		for (SM_Type superType : superTypes) {
			deeperSuperTypes.addAll(superType.getSuperTypes());
		}
		// FIXME : switch to iterative process to avoid stack overflows
		try {
			return findInheritanceDepth(deeperSuperTypes) + 1;
		} catch (StackOverflowError ex) {
			System.err.println("Inheritance depth analysis step skipped due to memory overflow.");
			return 0;
		}
	}
	
	private void extractWeightedMethodsPerClass() {
		for (SM_Method method : type.getMethodList()) {
			weightedMethodsPerClass += type.getMetricsFromMethod(method).getCyclomaticComplexity();
		} 
	}
	
	private void extractNumOfFanOutTypes() {
		numOfFanOutTypes += type.getReferencedTypeList().size();
	}
	
	private void extractNumOfFanInTypes() {
		numOfFanInTypes += type.getTypesThatReferenceThis().size();
	}
	
	private void extractLCOM() {
		if (isNotLcomComputable()) {
			lcom = -1.0;
			return;
		}
		initializeGraph();
		lcom = computeLCOM();

	}
	
	private boolean isNotLcomComputable() {
		return type.isInterface() 
				|| type.getFieldList().size() == 0 
				|| type.getMethodList().size() == 0; 
	}
	
	private void initializeGraph() {
		initializeVertices();
		initializeEdges();
	}
	
	private void initializeVertices() {
		//List<Vertex> vertices = new ArrayList<>();
		graph = new Graph();
		for (SM_Method method : type.getMethodList()) {
			graph.addVertex(method);
		}
		for (SM_Field field : type.getFieldList()) {
			graph.addVertex(field);
		}
	}
	
	private void initializeEdges() {
		for (SM_Method method : type.getMethodList()) {
			addAdjacentFields(method);
			addAdjacentMethods(method);
		}
	}
	
	private void addAdjacentFields(SM_Method method) {
		for (SM_Field fieldVertex : method.getDirectFieldAccesses()) {
			graph.addEdge(new Edge(method, fieldVertex));
		}
	}
	
	private void addAdjacentMethods(SM_Method method) {
		for (SM_Method methodVertex : type.getMethodList()) {
			if (!method.equals(methodVertex) && method.getCalledMethods().contains(methodVertex)) {
				graph.addEdge(new Edge(method, methodVertex));
			}
		}
	}
	
	private double computeLCOM() {
		graph.computeConnectedComponents();
		List<List<Vertex>> nonSingleElementFieldComponents = getNonSingleElementFieldComponents();
		if (nonSingleElementFieldComponents.size() > 1) {
			return ((double) getNonSingleElementFieldComponents().size()) / type.getMethodList().size();
		}
		return 0.0;
	}
	
	private List<List<Vertex>> getNonSingleElementFieldComponents() {
		List<List<Vertex>> cleanComponents = new ArrayList<>();;
		for (List<Vertex> component : graph.getConnectedComponnents()) {
			if (component.size() != 1 || !(component.get(0) instanceof SM_Field)) {
				cleanComponents.add(component);
			}
		}
		return cleanComponents;
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

}
