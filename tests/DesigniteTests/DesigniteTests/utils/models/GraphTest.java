package DesigniteTests.utils.models;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import Designite.utils.models.Edge;
import Designite.utils.models.Graph;
import Designite.utils.models.Vertex;

public class GraphTest {
	
	private Graph graph;
	
	@Before
	public void setup() {
		Vertex vertex1 = new Vertex(){};
		Vertex vertex2 = new Vertex(){};
		Vertex vertex3 = new Vertex(){};
		Vertex vertex4 = new Vertex(){};
		Vertex vertex5 = new Vertex(){};
		Vertex vertex6 = new Vertex(){};
		Vertex vertex7 = new Vertex(){};
		Vertex vertex8 = new Vertex(){};
		Vertex vertex9 = new Vertex(){};
		Vertex vertex10 = new Vertex(){};
		
		Edge edge1 = new Edge(vertex1, vertex2);
		Edge edge2 = new Edge(vertex4, vertex1);
		Edge edge3 = new Edge(vertex2, vertex3);
		Edge edge4 = new Edge(vertex2, vertex4);
		Edge edge5 = new Edge(vertex3, vertex4);
		Edge edge6 = new Edge(vertex5, vertex6);
		Edge edge7 = new Edge(vertex6, vertex9);
		Edge edge8 = new Edge(vertex7, vertex8);
		Edge edge9 = new Edge(vertex9, vertex7);
		Edge edge10 = new Edge(vertex8, vertex9);
		
		graph = new Graph();
		graph.addVertex(vertex1);
		graph.addVertex(vertex2);
		graph.addVertex(vertex3);
		graph.addVertex(vertex4);
		graph.addVertex(vertex5);
		graph.addVertex(vertex6);
		graph.addVertex(vertex7);
		graph.addVertex(vertex8);
		graph.addVertex(vertex9);
		graph.addVertex(vertex10);
		
		graph.addEdge(edge1);
		graph.addEdge(edge2);
		graph.addEdge(edge3);
		graph.addEdge(edge4);
		graph.addEdge(edge5);
		graph.addEdge(edge6);
		graph.addEdge(edge7);
		graph.addEdge(edge8);
		graph.addEdge(edge9);
		graph.addEdge(edge10);
	
		graph.computeConnectedComponents();
		graph.computeStronglyConnectedComponents();
	}
	
	@Test
	public void testNumberOfComponents() {
		int expected = 3;
		int actual = graph.getConnectedComponnents().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDegreeOfFirstComponent() {
		int expected = 4;
		int actual = graph.getConnectedComponnents().get(0).size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDegreeOfSecondComponent() {
		int expected = 5;
		int actual = graph.getConnectedComponnents().get(1).size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDegreeOfThirdComponent() {
		int expected = 1;
		int actual = graph.getConnectedComponnents().get(2).size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNumberOfStrongComponents() {
		int expected = 5;
		int actual = graph.getStronglyConnectedComponents().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDegreeOfFirstStrongComponte() {
		int expected = 4;
		int actual = graph.getStronglyConnectedComponents().get(0).size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDegreeOfSecondStrongComponte() {
		int expected = 3;
		int actual = graph.getStronglyConnectedComponents().get(1).size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDegreeOfThirdStrongComponte() {
		int expected = 1;
		int actual = graph.getStronglyConnectedComponents().get(2).size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDegreeOfFourthStrongComponte() {
		int expected = 1;
		int actual = graph.getStronglyConnectedComponents().get(3).size();
		
		assertEquals(expected, actual);
	}
}
