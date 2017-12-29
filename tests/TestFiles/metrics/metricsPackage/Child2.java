package metricsPackage;

public class Child2 extends TestMetricsClass {
	
	private int field1;
	private int field2;
	private int field3;
	private int field4;
	
	public void method1() {
		field1++;
		field2++;
	}
	
	public void method2() {
		field3++
	}
	
	public void method3() {
		method1();
		field3++;
	}
}
