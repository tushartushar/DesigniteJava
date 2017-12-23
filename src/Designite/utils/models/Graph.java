package Designite.utils.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
	
	private List<Vertex> vertices;
	private List<Edge> edges;
	private Map<Vertex, List<Vertex>> adjacencyList;
	private Map<Vertex, Boolean> visitedVertices;
	private List<List<Vertex>> connectedComponents;
	
	public Graph(List<Vertex> vertices, List<Edge> edges) {
		this.vertices = vertices;
		this.edges = edges;
		
		adjacencyList = new HashMap<>();
		initializeAdjacencyList();
		
		visitedVertices = new HashMap<>();
		connectedComponents = new ArrayList<>();
		setConnectedComponents();
	}
	
	private void initializeAdjacencyList() {
		for (Vertex vertex : vertices) {
			List<Vertex> adjacentVertices = new ArrayList<>();
			adjacencyList.put(vertex, adjacentVertices);			
		}
		for (Edge edge : edges) {
			for (Vertex vertex : edge.getEdge()) {
				List<Vertex> adjacentVertices = adjacencyList.get(vertex);
				adjacentVertices.add(edge.getOtherVertex(vertex));
				adjacencyList.put(vertex, adjacentVertices);
			}
		}
	}
	
	public void setConnectedComponents() {
		initializeVisitedVerices();
		for (Vertex vertex : vertices) {
			if (!visitedVertices.get(vertex)) {
				visitedVertices.put(vertex, true);
				List<Vertex> connectedComponent = new ArrayList<>();
				depthFirstSearch(connectedComponent, vertex);
				connectedComponents.add(connectedComponent);
			}
		}
	}
	
	private void initializeVisitedVerices() {
		for (Vertex vertex : vertices) {
			visitedVertices.put(vertex, false);
		}
	}
	
	private void depthFirstSearch(List<Vertex> connectedComponent, Vertex vertex) {
		connectedComponent.add(vertex);
		for (Vertex adjacentVertex : getAdjacentVertices(vertex)) {
			if (!visitedVertices.get(adjacentVertex)) {
				visitedVertices.put(adjacentVertex, true);
				depthFirstSearch(connectedComponent, adjacentVertex);
			}
		}
	}
	
	public List<Vertex> getVertices() {
		return vertices;
	}
	
	public List<Vertex> getAdjacentVertices(Vertex vertex) {
		return adjacencyList.get(vertex);
	}
	
	public List<List<Vertex>> getConnectedComponnents() {
		return connectedComponents;
	}
}
