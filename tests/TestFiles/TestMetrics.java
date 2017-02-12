//NOM: 3
//NOPM: 2

public class TestMetrics {
	private int i = 0;
	private int j = 0;
	public static int counter = 0;

	public void inceaseCounter(int i, int j){
		counter++;
	}
	public int modify() {
		return counter;
	}

	private void privateMethod() {
		i++;
	}
}
