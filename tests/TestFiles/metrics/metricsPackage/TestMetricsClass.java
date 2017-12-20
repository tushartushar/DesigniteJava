package metricsPackage;

public class TestMetricsClass extends AnotherClass implements InterfaceGrandChild, InterfaceChild2 {
	
    private String foo;
    public String[] bar;
    private ForeignClass1 fc1; 

    public void publicMethod(String parameter1, int parameter2, ForeignClass2 parameter3) {
        
    	ForeignClass3 fc3 = new ForeignClass3();
    	ForeignClass4.func();
    	
        // To test cyclomatic complexity
        if (true) {
            for (int i = 0; i < 5; i++) {
                for (String b : bar) {
                
                }
            }
        } else if (true || false) {
            while (false) {
                do {
 
                } while (false);
            }
        } else {
            switch (foo) {
                 case "foo":
                     break;
                 case "bar":
                     break;
                 default:
            }
        }
    }

    private void privateMethod() {
    
    }

}
