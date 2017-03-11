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
	private Type type;
	private boolean finalField = false;
	private boolean staticField = false;
	
	public SM_Field(FieldDeclaration fieldDeclaration, TypeDeclaration typeDeclaration) {
		this.typeDeclaration = typeDeclaration;
		this.fieldDeclaration = fieldDeclaration;
		name = findName();
		type = fieldDeclaration.getType();
		setAccessModifier(fieldDeclaration.getModifiers());
		setFieldInfo(fieldDeclaration);
	}
	
	String findName() {
		List<VariableDeclarationFragment> fields = fieldDeclaration.fragments();
		String nameField;
		for(VariableDeclarationFragment field: fields) {
			nameField = field.getName().toString();
			return nameField;
		}
		return null;
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
	
	@Override
	public void print() {
		System.out.println("Field: " + getName());
		System.out.println("	Access: " + getAccessModifier());
		System.out.println("	Type: " + getType());
		System.out.println("	Final: " + isFinal());
		System.out.println("	Static: " + isStatic());
	}
	
	void parse() {
	
	}
}
