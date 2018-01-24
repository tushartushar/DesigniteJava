package Designite.utils.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
	
	private List<Vertex> vertices;
	private Map<Vertex, List<Vertex>> adjacencyList;
	private Map<Vertex, List<Vertex>> directedAdjacencyList;
	private Map<Vertex, Boolean> visitedVertices;
	private List<List<Vertex>> connectedComponents;
	
	public Graph() {
		
		vertices = new ArrayList<>();
		
		adjacencyList = new HashMap<>();
		directedAdjacencyList = new HashMap<>();
		
		visitedVertices = new HashMap<>();
		connectedComponents = new ArrayList<>();
	}
	
	public void computeConnectedComponents() {
		initializeVisitedVerices();
		for (Vertex vertex : vertices) {
			if (!visitedVertices.get(vertex)) {
				visitedVertices.put(vertex, true);
				List<Vertex> connectedComponent = new ArrayList<>();
				depthFirstSearch(connectedComponent, vertex, true);
				connectedComponents.add(connectedComponent);
			}
		}
		System.out.println(toString());
	}
	
	private void initializeVisitedVerices() {
		for (Vertex vertex : vertices) {
			visitedVertices.put(vertex, false);
		}
	}
	
	private void depthFirstSearch(List<Vertex> connectedComponent, Vertex vertex, boolean undirected) {
		connectedComponent.add(vertex);
		for (Vertex adjacentVertex : getAdjacentVertices(vertex, undirected)) {
			if (!visitedVertices.get(adjacentVertex)) {
				visitedVertices.put(adjacentVertex, true);
				depthFirstSearch(connectedComponent, adjacentVertex, undirected);
			}
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
		vertices.add(vertex);
	}
	
	private void addDirectedEdge(Edge edge) {
		List<Vertex> adjacentVertices = directedAdjacencyList.get(edge.getFirstVertex());
		if (!adjacentVertices.contains(edge.getSecondVertex())) {
			adjacentVertices.add(edge.getSecondVertex());
			directedAdjacencyList.put(edge.getFirstVertex(), adjacentVertices);
		}
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
	
	public List<Vertex> getVertices() {
		return vertices;
	}
	
	public List<Vertex> getAdjacentVertices(Vertex vertex) {
		return getAdjacentVertices(vertex, false);
	}
	
	public List<Vertex> getAdjacentVertices(Vertex vertex, boolean undirected) {
		if (undirected) {
			return adjacencyList.get(vertex);
		}
		return directedAdjacencyList.get(vertex);
	}
	
	public List<List<Vertex>> getConnectedComponnents() {
		return connectedComponents;
	}

}
