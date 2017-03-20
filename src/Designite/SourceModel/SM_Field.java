package Designite.SourceModel;

import java.lang.reflect.Modifier;
import java.util.List;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class SM_Field extends SM_SourceItem {
	private TypeDeclaration typeDeclaration;
	private FieldDeclaration fieldDeclaration;
	private VariableDeclarationFragment fieldVariable;
	private Type type;
	private SM_Type parentType;
	private boolean finalField = false;
	private boolean staticField = false;
	
	public SM_Field(FieldDeclaration fieldDeclaration, TypeDeclaration typeDeclaration) {
		this.typeDeclaration = typeDeclaration;
		this.fieldDeclaration = fieldDeclaration;
		type = fieldDeclaration.getType();
		setAccessModifier(fieldDeclaration.getModifiers());
		setFieldInfo(fieldDeclaration);
	}
	
	public void setName(String name) {
		this.name = name;
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
	public Type getType() {
		return type;
	}
	public boolean isFinal() {
		return finalField;
	}
	public boolean isStatic() {
		return staticField;
	}
	
	void setParent(SM_Type parentType) {
		this.parentType = parentType;
	}
	
	public SM_Type getParent() {
		return parentType;
	}
	
	@Override
	public void print() {
		System.out.println("Field: " + getName());
		System.out.println("	Parent: " + this.getParent().getName());
		System.out.println("	Access: " + getAccessModifier());
		System.out.println("	Type: " + getType());
		System.out.println("	Final: " + isFinal());
		System.out.println("	Static: " + isStatic());
	}
	
	void parse() {
	
	}
}
