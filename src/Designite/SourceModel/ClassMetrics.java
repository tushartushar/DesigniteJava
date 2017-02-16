package Designite.SourceModel;

import org.eclipse.jdt.core.dom.Modifier;

public class ClassMetrics {

	private int countMethods = 0;
	private int publicMethods = 0;
	private int countFields = 0;
	private int publicFields = 0;
	//private int properties = 0;
	
	public int getNoMethods() {
		return countMethods;
	}

	public int getPublicMethods() {
		return publicMethods;
	}

	public int getNoFields() {
		return countFields;
	}

	public int getPublicFields() {
		return publicFields;
	}

/*	public void setProperties() {
		properties = countMethods + countFields;
	}*/
	
	public int getProperties() {
		return countMethods + countFields;
	}

	public void computeMetrics(MethodVisitor visitor) {
		countMethods = visitor.countMethods();
		for (int i = 0; i < countMethods; i++) {
			if (visitor.methods.get(i).isPublic())
				publicMethods++;
		}
	}

	public void computeMetrics(FieldVisitor visitor) {
		countFields = visitor.countFields();
		for (int i = 0; i < countFields; i++) {
			int fieldModifier = visitor.fields.get(i).getModifiers();
			if (Modifier.isPublic(fieldModifier))
				publicFields++;
		}
	}

	public void printMetrics() {
		System.out.println("NOM: " + getNoMethods());
		System.out.println("NOPM: " + getPublicMethods());
		System.out.println("NOF: " + getNoFields());
		System.out.println("NOPF: " + getPublicFields());
		System.out.println("NOP: " + getProperties());
	}

}
