package Designite.utils.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
	
	private List<Vertex> vertices = new ArrayList<>();
	private Map<Vertex, List<Vertex>> adjacencyList = new HashMap<>();
	private Map<Vertex, List<Vertex>> directedAdjacencyList = new HashMap<>();
	private Map<Vertex, List<Vertex>> reversedDirectedAdjacencyList = new HashMap<>();
	private Map<Vertex, Boolean> visitedVertices = new HashMap<>();
	private List<List<Vertex>> connectedComponents = new ArrayList<>();
	private Map<Vertex, List<Vertex>> connectedComponnentsMapping = new HashMap<>();
	private List<List<Vertex>> stronglyConnectedComponents = new ArrayList<>();
	private Map<Vertex, List<Vertex>> stronglyConnectedComponnentsMapping = new HashMap<>();
	private List<Vertex> helperVertexList = new ArrayList<>();
	
	public void computeConnectedComponents() {
		initializeVisitedVerices();
		for (Vertex vertex : vertices) {
			if (!visitedVertices.get(vertex)) {
				List<Vertex> connectedComponent = new ArrayList<>();
				depthFirstSearch(connectedComponent, vertex, GraphAlingment.UNDIRECTED);
				connectedComponents.add(connectedComponent);
				updateMapping(connectedComponnentsMapping, connectedComponent);
			}
		}
	}
	
	public void computeStronglyConnectedComponents() {
		reversePassDFS();
		straightPassDFS();
	}
	
	public void reversePassDFS() {
		initializeVisitedVerices();
		for (int i = vertices.size()-1; i >= 0 ; i--) { 
			if (!visitedVertices.get(vertices.get(i))) {
				depthFirstSearch(new ArrayList<>(), vertices.get(i), GraphAlingment.REVERSE_DIRECTED);
			}
		}
	}
	
	public void straightPassDFS() {
		initializeVisitedVerices();
		for (int i = vertices.size()-1; i >= 0 ; i--) {
			if (!visitedVertices.get(helperVertexList.get(i))) {
				List<Vertex> stronglyConnectedComponent = new ArrayList<>();
				depthFirstSearch(stronglyConnectedComponent, helperVertexList.get(i), GraphAlingment.DIRECTED);
				stronglyConnectedComponents.add(stronglyConnectedComponent);
				updateMapping(stronglyConnectedComponnentsMapping, stronglyConnectedComponent);
			}
		}
	}
	
	private void initializeVisitedVerices() {
		for (Vertex vertex : vertices) {
			visitedVertices.put(vertex, false);
		}
	}
	
	private void updateMapping(Map<Vertex, List<Vertex>> mapping, List<Vertex> component) {
		for (Vertex vertex : component) {
			mapping.put(vertex, component);
		}
	}
	
	private void depthFirstSearch(List<Vertex> connectedComponent, Vertex vertex, GraphAlingment align) {
		visitedVertices.put(vertex, true);
		if (align != GraphAlingment.REVERSE_DIRECTED) {
			connectedComponent.add(vertex);
		}
		for (Vertex adjacentVertex : getAdjacentVertices(vertex, align)) {
			if (!visitedVertices.get(adjacentVertex)) {
				depthFirstSearch(connectedComponent, adjacentVertex, align);
			}
		}
		if (align == GraphAlingment.REVERSE_DIRECTED) {
			helperVertexList.add(vertex);
		}
	}
	
	public void addVertex(Vertex vertex) {
		if (!vertices.contains(vertex)) {
			initializeVertex(vertex);
		}
	}
	
	public void addEdge(Edge edge) {
		initializeEdge(edge);
		addDirectedEdge(edge);
		addUndirectedEdge(edge);
	}
	
	private void initializeEdge(Edge edge) {
		if (!adjacencyList.containsKey(edge.getFirstVertex())) {
			initializeVertex(edge.getFirstVertex());
		}
		if (!adjacencyList.containsKey(edge.getSecondVertex())) {
			initializeVertex(edge.getSecondVertex());
		}
	}
	
	private void initializeVertex(Vertex vertex) {
		adjacencyList.put(vertex, new ArrayList<>());
		directedAdjacencyList.put(vertex, new ArrayList<>());
		reversedDirectedAdjacencyList.put(vertex, new ArrayList<>());
		vertices.add(vertex);
	}
	
	private void addDirectedEdge(Edge edge) {
		List<Vertex> adjacentVertices = directedAdjacencyList.get(edge.getFirstVertex());
		if (!adjacentVertices.contains(edge.getSecondVertex())) {
			adjacentVertices.add(edge.getSecondVertex());
			directedAdjacencyList.put(edge.getFirstVertex(), adjacentVertices);
			addReverseDirectedEdge(edge);
		}
	}
	
	private void addReverseDirectedEdge(Edge edge) {
		List<Vertex> adjacentVertices = reversedDirectedAdjacencyList.get(edge.getSecondVertex());
		adjacentVertices.add(edge.getFirstVertex());
		reversedDirectedAdjacencyList.put(edge.getSecondVertex(), adjacentVertices);
	}
	
	private void addUndirectedEdge(Edge edge) {
		for (Vertex vertex : edge.getVertices()) {
			List<Vertex> adjacentVertices = adjacencyList.get(vertex);
			if (!adjacentVertices.contains(edge.getOtherVertex(vertex))) {
				adjacentVertices.add(edge.getOtherVertex(vertex));
				adjacencyList.put(vertex, adjacentVertices);
			}
		}
	}
	
	private List<Vertex> getAdjacentVertices(Vertex vertex, GraphAlingment align) {
		if (align == GraphAlingment.UNDIRECTED) {
			return adjacencyList.get(vertex);
		} else if (align == GraphAlingment.DIRECTED) {
			return directedAdjacencyList.get(vertex);
		} else if (align == GraphAlingment.REVERSE_DIRECTED) {
			return reversedDirectedAdjacencyList.get(vertex);
		}
		return null;
	}
	
	public List<List<Vertex>> getConnectedComponnents() {
		return connectedComponents;
	}
	
	public List<List<Vertex>> getStronglyConnectedComponents() {
		return stronglyConnectedComponents;
	}
	
	public List<Vertex> getComponentOfVertex(Vertex vertex) {
		return connectedComponnentsMapping.get(vertex);
	}
	
	public List<Vertex> getStrongComponentOfVertex(Vertex vertex) {
		return stronglyConnectedComponnentsMapping.get(vertex);
	}
	
	public boolean inSameConnectedComponent(Vertex vertex1, Vertex vertex2) {
		return getComponentOfVertex(vertex1).equals(getComponentOfVertex(vertex2));
	}
	
	private enum GraphAlingment {
		UNDIRECTED, DIRECTED, REVERSE_DIRECTED
	}

}
