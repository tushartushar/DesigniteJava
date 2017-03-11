package Designite.SourceModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

//TODO check EnumDeclaration, AnnotationTypeDeclaration and nested classes
public class SM_Type extends SM_SourceItem {

	private int publicMethods = 0;
	private int publicFields = 0;
	private boolean isAbstract = false;
	private boolean isInterface = false;
	private TypeDeclaration typeDeclaration;
	private TypeDeclaration referredClass;
	private boolean nestedClass;
	private Type superclass;
	private CompilationUnit compilationUnit;
	private List<SM_Method> methodList = new ArrayList<SM_Method>();
	private List<SM_Field> fieldList = new ArrayList<SM_Field>();

	public SM_Type(TypeDeclaration typeDeclaration, CompilationUnit compilationUnit) {
		//It has been checked earlier too
		if (typeDeclaration == null || compilationUnit == null) 
			throw new NullPointerException();
		
		name = typeDeclaration.getName().toString();
		this.typeDeclaration = typeDeclaration;
		this.compilationUnit = compilationUnit;
		setTypeInfo();
		setAccessModifier(typeDeclaration.getModifiers());
		setSuperClass();
	}
	
	public TypeDeclaration getTypeDeclaration() {
		return typeDeclaration;
	}
	
	void setTypeInfo() {
		int modifier = typeDeclaration.getModifiers();
		if (Modifier.isAbstract(modifier))
			isAbstract = true;
		if (typeDeclaration.isInterface())
			isInterface = true;
	}
	
	public boolean isAbstract() {
		return isAbstract;
	}
	
	public boolean isInterface() {
		return isInterface;
	}
	
	public void setNestedClass(TypeDeclaration referredClass) {
		nestedClass = true;
		this.referredClass = referredClass;	
	}
	
	public boolean isNestedClass() {
		return nestedClass;
	}
	
	//not implemented yet
	void setSuperClass() {
		superclass = typeDeclaration.getSuperclassType();
	}
	
	public List<SM_Method> getMethodList() {
		return methodList;
	}

	public int countMethods() {
		return methodList.size();
	}

	public int countFields() {
		return fieldList.size();
	}

	public int getPublicMethods() {
		return publicMethods;
	}

	public int getPublicFields() {
		return publicFields;
	}

	void computeMetrics(MethodVisitor visitor) {
		for (int i = 0; i < countMethods(); i++) {
			if (accessModifier.equals("PUBLIC"))
				publicMethods++;
		}
	}

	void computeMetrics(FieldVisitor visitor) {
		for (int i = 0; i < countFields(); i++) {
			if (accessModifier.equals("PUBLIC"))
				publicFields++;
		}
	}

	// This has to be changed.
	void parse() {		
		MethodVisitor methodVisitor = new MethodVisitor(typeDeclaration);
		typeDeclaration.accept(methodVisitor);
		List<SM_Method> mList = methodVisitor.getMethods();
		if (mList.size() > 0)
			methodList.addAll(mList);
		parseMethods();

		// computeMetrics(methodVisitor);

		FieldVisitor fieldVisitor = new FieldVisitor(typeDeclaration);
		typeDeclaration.accept(fieldVisitor);
		List<SM_Field> fList = fieldVisitor.getFields();
		if (fList.size() > 0)
			fieldList.addAll(fList);

		// computeMetrics(fieldVisitor);

	}

	private void parseMethods() {
		for (SM_Method method : methodList)
			method.parse();
	}

	@Override
	public void print() {
		System.out.println();
		System.out.println("Type: " + name);
		System.out.println("	Access: " + accessModifier);
		System.out.println("	Interface: " + isInterface);
		System.out.println("	Abstract: " + isAbstract);
		System.out.println("	Superclass: " + superclass);
		System.out.println("	Nested class: " + nestedClass);
		if (nestedClass) 
			System.out.println("	Referred class: " + referredClass.getName());
		for (SM_Field field : fieldList)
			field.print();
		for (SM_Method method : methodList)
			method.print();
	}

}
