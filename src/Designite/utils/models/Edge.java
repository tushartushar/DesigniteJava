package Designite.utils.models;

import java.util.ArrayList;
import java.util.List;

public class Edge {

	private List<Vertex> edge;
	
	public Edge(Vertex firstVertex, Vertex secondVertex) {
		edge = new ArrayList<>();
		edge.add(firstVertex);
		edge.add(secondVertex);
	}
	
	public List<Vertex> getEdge() {
		return edge;
	}
	
	public boolean containsVertex(Vertex vertex) {
		return edge.contains(vertex);
	}
	
	public Vertex getOtherVertex(Vertex vertex) {
		if (edge.get(0).equals(vertex)) {
			return edge.get(1);
		} else if (edge.get(1).equals(vertex)) {
			return edge.get(0);
		} else {
			return null;
		}
	}
}
