package metricsPackage;

public class TestMetricsClass extends AnotherClass implements InterfaceGrandChild, InterfaceChild2 {
	
    private String foo;
    public String[] bar;

    public void publicMethod(String parameter1, int parameter2, YetAnotherClass parameter3) {
        
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
