package Designite.SourceModel;

import java.lang.reflect.Modifier;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class SM_Field extends SM_Variable {
	private TypeDeclaration typeDeclaration;
	private FieldDeclaration fieldDeclaration;
	private VariableDeclarationFragment fieldVariable;
	private SM_Type parentType;
	private boolean finalField = false;
	private boolean staticField = false;
	
	public SM_Field(FieldDeclaration fieldDeclaration, TypeDeclaration typeDeclaration) {
		this.typeDeclaration = typeDeclaration;
		this.fieldDeclaration = fieldDeclaration;
		setType(fieldDeclaration.getType());
		setAccessModifier(fieldDeclaration.getModifiers());
		setFieldInfo(fieldDeclaration);
	}
	
	void setFieldVar(VariableDeclarationFragment fieldVariable) {
		this.fieldVariable = fieldVariable;
	}
	
	public VariableDeclarationFragment getFieldVar() {
		return fieldVariable;
	}
	
	void setFieldInfo(FieldDeclaration field){
		int modifiers = field.getModifiers();
		if (Modifier.isFinal(modifiers)) 
			finalField =  true;
		if (Modifier.isStatic(modifiers)) 
			staticField =  true;
	}
	
	public TypeDeclaration getTypeDeclaration() {
		return typeDeclaration;
	}
	
	public boolean isFinal() {
		return finalField;
	}
	
	public boolean isStatic() {
		return staticField;
	}
	
	void setParent(SM_Type parentType) {
		this.parentType = parentType;
		this.parentProject = parentType.getParentProject();
	}
	
	public SM_Type getParent() {
		return parentType;
	}
	
	public SM_Project getParentProject() {
		return parentProject;
	}
	
	void parse(SM_Type parentType) {
		setParent(parentType);
		parentType.addToVariableList(this);
	}
	
	@Override
	public void print() {
		System.out.println("Field: " + getName());
		System.out.println("	Parent: " + this.getParent().getName());
		System.out.println("	Access: " + getAccessModifier());
		System.out.println("	Type: " + getType());
		System.out.println("	Final: " + isFinal());
		System.out.println("	Static: " + isStatic());
		findRefObject();
		if (getRefType() != null)
			System.out.println("	Refers to: " + getRefType().getName());
	}
}
