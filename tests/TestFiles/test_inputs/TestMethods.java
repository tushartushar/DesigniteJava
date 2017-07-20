package test_inputs;

import java.util.List;

public class TestMethods {
	//public TestClass publicField;
	private static int counter = 0;
	private List<String> name, id;
	final int CONSTANT = 7;
	public TestMethods publicField;
	
	TestMethods(String name) {
		int a = 0;
		counter++;
	}
	
	public int publicMethod(TestMethods t) {
		TestMethods temp = t;
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
