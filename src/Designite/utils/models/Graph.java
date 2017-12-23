package Designite.utils.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
	
	private List<Vertex> vertices;
	private List<Edge> edges;
	private Map<Vertex, List<Vertex>> adjacencyList;
	
	public Graph(List<Vertex> vertices, List<Edge> edges) {
		this.vertices = vertices;
		this.edges = edges;
		
		adjacencyList = new HashMap<>();
		initializeAdjacencyList();
	}
	
	private void initializeAdjacencyList() {
		for (Edge edge : edges) {
			for (Vertex vertex : edge.getEdge()) {
				List<Vertex> adjacenVertices;
				if (adjacencyList.containsKey(vertex)) {
					adjacenVertices = adjacencyList.get(vertex);
				} else {
					adjacenVertices = new ArrayList<>();
				}
				adjacenVertices.add(edge.getOtherVertex(vertex));
				adjacencyList.put(vertex, adjacenVertices);
			}
		}
	}
	
	public List<Vertex> getVertices() {
		return vertices;
	}
	
	public List<Vertex> getAdjacentVertices(Vertex vertex) {
		return adjacencyList.get(vertex);
	}

}
