package metricsPackage;


public class Child1 extends TestMetricsClass {
	
	private String field1 = "foo";
	private String[] field2;
	private boolean bool = false;
	private int field3 = 0;
	
	public void foo() {
		int field3 = 5;
		field1 = "bar";
        String random = "random";
        for (String word : field2) {
        	if (bool) {
        		this.field3++;
        		field1 = "rab";
        		ForeignClass4.func();
        		ForeignClass5.ERROR;
        	}
        }
    }
	
}
