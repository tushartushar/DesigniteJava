package test_inputs2;

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
		print();
		return counter;
	}
	
	public static void count(int a) {
		Logger.log("nothing");
	}
	
	public List getList(List<String> list) {
		return list;
		
	}
	
	public void print() {
		TestMethods2 obj = new TestMethods2();
		obj.print2();
		System.out.println("Name: " + name);
		System.out.println("Id: " + id);
	}
	
	public void magicNumberDemo() {
		int[] arr = {0,1};
		int x = 1;
		int z = Integer.valueOf(2);
		int y = "aes".length();
		int k = arr[1];
		
		if ( x == 10 && x > 5);
		if ( x != 10 ); 
		if ( x > 10 );
		if ( x < 10 );
		if ( x >= 10 );
		if ( x <= 10 );
		if ( z > arr[0] );
		if ("test".charAt(Integer.valueOf(0)));

		List<Integer> list = new ArrayList<>(new int[] {0,1});
		int l = list.get(0);
	}
}

class Logger{
	
	public static void log(String msg)
	{
		
	}
	
}
