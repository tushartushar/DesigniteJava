package Designite.metrics;

import Designite.SourceModel.AccessStates;
import Designite.SourceModel.SM_Field;
import Designite.SourceModel.SM_Method;
import Designite.SourceModel.SM_Type;
import Designite.utils.models.Edge;
import Designite.utils.models.Graph;
import Designite.utils.models.Vertex;

import java.util.ArrayList;
import java.util.List;

public class TypeMetricsExtractor implements MetricExtractor{

    private SM_Type type;

    private Graph graph;

    private TypeMetrics typeMetrics;

    public TypeMetricsExtractor(SM_Type type){
        this.type = type;
    }

    @Override
    public TypeMetrics extractMetrics() {
        typeMetrics = new TypeMetrics();
        extractNumOfFieldMetrics();
        extractNumOfMethodsMetrics();
        extractDepthOfInheritance();
        extractNumberOfLines();
        extractNumberOfChildren();
        extractWeightedMethodsPerClass();
        extractNumOfFanOutTypes();
        extractNumOfFanInTypes();
        extractLCOM();
        typeMetrics.setType(this.type);
        return typeMetrics;
    }

    private void extractNumOfFieldMetrics() {
        for (SM_Field field : type.getFieldList()) {
            typeMetrics.setNumOfFields(typeMetrics.getNumOfFields()+1);
            if (field.getAccessModifier() == AccessStates.PUBLIC) {
                // do not calculate fields that belong to a nested class with a stricter access modifier
                SM_Type nestedParent = field.getNestedParent();
                if(nestedParent != null && nestedParent.getAccessModifier() != AccessStates.PUBLIC) {
                    continue;
                }
                typeMetrics.setNumOfPublicFields(typeMetrics.getNumOfPublicFields()+1);
            }
        }
    }

    private void extractNumOfMethodsMetrics() {
        for (SM_Method method : type.getMethodList()) {
            typeMetrics.setNumOfMethods(typeMetrics.getNumOfMethods()+1);
            if (method.getAccessModifier() == AccessStates.PUBLIC) {
                typeMetrics.setNumOfPublicMethods(typeMetrics.getNumOfPublicMethods()+1);
            }
        }
    }

    private void extractDepthOfInheritance() {
        int depthOfInheritance = typeMetrics.getInheritanceDepth();
        depthOfInheritance += findInheritanceDepth(type.getSuperTypes());
        typeMetrics.setDepthOfInheritance(depthOfInheritance);
    }

    private void extractNumberOfLines() {
        String body = type.getTypeDeclaration().toString();
        typeMetrics.setNumOfLines(body.length() - body.replace("\n", "").length());
    }

    private void extractNumberOfChildren() {
        typeMetrics.setNumOfChildren(type.getSubTypes().size());
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
        int weightedMethodsPerClass = typeMetrics.getWeightedMethodsPerClass();
        for (SM_Method method : type.getMethodList()) {
            weightedMethodsPerClass += type.getMetricsFromMethod(method).getCyclomaticComplexity();
        }
        typeMetrics.setWeightedMethodsPerClass(weightedMethodsPerClass);
    }

    private void extractNumOfFanOutTypes() {
        int numOfFanOutTypes = typeMetrics.getNumOfFanOutTypes();
        numOfFanOutTypes += type.getReferencedTypeList().size();
        typeMetrics.setNumOfFanOutTypes(numOfFanOutTypes);
    }

    private void extractNumOfFanInTypes() {
        int numOfFanInTypes = typeMetrics.getNumOfFanInTypes();
        numOfFanInTypes += type.getTypesThatReferenceThis().size();
        typeMetrics.setNumOfFanInTypes(numOfFanInTypes);
    }

    private void extractLCOM() {
        if (isNotLcomComputable()) {
            typeMetrics.setLcom(-1.0);
            return;
        }
        initializeGraph();
        typeMetrics.setLcom(computeLCOM());

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

}
