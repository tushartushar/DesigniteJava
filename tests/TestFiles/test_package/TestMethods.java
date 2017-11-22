package test_package2;

import java.util.List;

public class TestMethods {
	public TestClass publicField;
	private static int counter = 0;
	private String name, id;
	final int CONSTANT = 7;
	
	TestMethods(String name) {
		this.name = name;
		counter++;
	}
	
	public int publicMethod(TestMethods t) {
		return counter;
	}
	
	public static void count(int a) {
		
	}
	
	public List getList(List<String> list) {
		return list;
		
	}
	
	public void print() {
		System.out.println("Name: " + name);
		System.out.println("Id: " + id);
	}
}
