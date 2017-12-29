package Designite.metrics;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.TypeDeclaration;

import Designite.SourceModel.AccessStates;
import Designite.SourceModel.SM_Field;
import Designite.SourceModel.SM_LocalVar;
import Designite.SourceModel.SM_Method;
import Designite.SourceModel.SM_Parameter;
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
	private int fanOutTypes;
	private int fanInTypes;
	private double lcom;
	
	private List<SM_Field> fieldList;
	private List<SM_Method> methodList;
	private List<SM_Type> superTypes;
	private List<SM_Type> subTypes;
	private List<SM_Type> referencedTypeList;
	private List<SM_Type> typesThatReferenceThisList;
	private TypeDeclaration typeDeclaration;
	
	private Graph graph;
	
	public TypeMetrics(List<SM_Field> fieldList
			, List<SM_Method> methodList
			, List<SM_Type> superTypes
			, List<SM_Type> subTypes
			, List<SM_Type> referencedTypeList
			, List<SM_Type> typesThatReferenceThisList
			, TypeDeclaration typeDeclaration) {
		this.fieldList = fieldList;
		this.methodList = methodList;
		this.superTypes = superTypes;
		this.subTypes = subTypes;
		this.typeDeclaration = typeDeclaration;
		this.referencedTypeList = referencedTypeList;
		this.typesThatReferenceThisList = typesThatReferenceThisList;
		
		subTypes = new ArrayList<>();
	}
	
	@Override
	public void extractMetrics() {
		extractNumOfFieldMetrics();
		extractNumOfMethodsMetrics();
		extractDepthOfInheritance();
		extractNumberOfLines();
		extractNumberOfChildren();
		extractWeightedMethodsPerClass();
		extractFanOutTypes();
		extractFanInTypes();
		extractLCOM();
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
	
	private void extractNumberOfChildren() {
		numOfChildren = subTypes.size();
	}
	
	private int findInheritanceDepth(List<SM_Type> superTypes) {
		if (superTypes.size() == 0) {
			return 0;
		}
		List<SM_Type> deeperSuperTypes = new ArrayList<>();
		for (SM_Type superType : superTypes) {
			deeperSuperTypes.addAll(superType.getSuperTypes());
		}
		return findInheritanceDepth(deeperSuperTypes) + 1;
	}
	
	private void extractWeightedMethodsPerClass() {
		for (SM_Method method : methodList) {
			weightedMethodsPerClass += method.getMethodMetrics().getCyclomaticComplexity();
		} 
	}
	
	private void extractFanOutTypes() {
		fanOutTypes += referencedTypeList.size();
	}
	
	private void extractFanInTypes() {
		fanInTypes += typesThatReferenceThisList.size();
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
		return typeDeclaration.isInterface() 
				|| fieldList.size() == 0 
				|| methodList.size() == 0; 
	}
	
	private void initializeGraph() {
		initializeVertices();
		initializeEdges();
	}
	
	private void initializeVertices() {
		List<Vertex> vertices = new ArrayList<>();
		vertices.addAll(methodList);
		vertices.addAll(fieldList);
		graph = new Graph(vertices);
	}
	
	private void initializeEdges() {
		for (SM_Method method : methodList) {
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
		for (SM_Method methodVertex : methodList) {
			if (!method.equals(methodVertex) && method.getCalledMethods().contains(methodVertex)) {
				graph.addEdge(new Edge(method, methodVertex));
			}
		}
	}
	
	private double computeLCOM() {
		graph.computeConnectedComponents();
		List<List<Vertex>> nonSingleElementFieldComponents = getNonSingleElementFieldComponents();
		if (nonSingleElementFieldComponents.size() > 1) {
			return ((double) getNonSingleElementFieldComponents().size()) / methodList.size();
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

	public int getFanOutTypes() {
		return fanOutTypes;
	}

	public int getFanInTypes() {
		return fanInTypes;
	}

	public double getLcom() {
		return lcom;
	}

}
