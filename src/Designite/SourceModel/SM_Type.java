package Designite.SourceModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class SM_Type extends SM_SourceItem{

	private int countMethods = 0;
	private int publicMethods = 0;
	private int countFields = 0;
	private int publicFields = 0;
	private TypeDeclaration typeDeclaration;
	private CompilationUnit compilationUnit;
	private List<SM_Method> methodList = new ArrayList<SM_Method>();
	private List<SM_Field> fieldList = new ArrayList<SM_Field>();

	SM_Type(TypeDeclaration typeDeclaration, CompilationUnit compilationUnit) {
		name = typeDeclaration.getName().toString();
		this.typeDeclaration = typeDeclaration;
		this.compilationUnit = compilationUnit;
	}

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

	void computeMetrics(MethodVisitor visitor) {
		countMethods = visitor.countMethods();
		for (int i = 0; i < countMethods; i++) {
			if (visitor.methods.get(i).isPublic())
				publicMethods++;
		}
	}

/*	void computeMetrics(FieldVisitor visitor) {
		countFields = visitor.countFields();
		for (int i = 0; i < countFields; i++) {
			int fieldModifier = visitor.fields.get(i).getModifiers();
			if (Modifier.isPublic(fieldModifier))
				publicFields++;
		}
	}*/

	//This has to be changed.
	void parse() {		
		MethodVisitor methodVisitor = new MethodVisitor(typeDeclaration);
 		typeDeclaration.accept(methodVisitor);
 		List<SM_Method> mList = methodVisitor.getMethods();
 		if (mList.size()>0)
			methodList.addAll(mList);
 		parseMethods();
 		
 		//computeMetrics(methodVisitor);
 
 		FieldVisitor fieldVisitor = new FieldVisitor(typeDeclaration);
 		typeDeclaration.accept(fieldVisitor);
 		List<SM_Field> fList = fieldVisitor.getFields();
 		if (fList.size()>0)
 			fieldList.addAll(fList);
 		
 		//computeMetrics(fieldVisitor);
 		
	}
	
	private void parseMethods() {
		for(SM_Method method: methodList)
			method.parse();
	}

	@Override
	public void print() {
		System.out.println("Type: " + name);
		for(SM_Method method:methodList)
			method.print();
		for(SM_Field field:fieldList)
			field.print();
	}

}
