package Designite.SourceModel;

import java.lang.reflect.Modifier;
import java.util.List;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class SM_Field extends SM_SourceItem {
	private TypeDeclaration typeDeclaration;
	private FieldDeclaration fieldDeclaration;
	private Type type;
	private boolean finalField = false;
	private boolean staticField = false;
	
	SM_Field(FieldDeclaration fieldDeclaration, TypeDeclaration typeDeclaration) {
		this.typeDeclaration = typeDeclaration;
		this.fieldDeclaration = fieldDeclaration;
		setAccessModifier(fieldDeclaration.getModifiers());
		name = findName();
		type = fieldDeclaration.getType();
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
	
	public void setFieldInfo(FieldDeclaration field){
		int modifiers = field.getModifiers();
		if (Modifier.isFinal(modifiers)) 
			finalField =  true;
		if (Modifier.isStatic(modifiers)) 
			staticField =  true;
	}
	
	@Override
	public void print() {
		System.out.println("Field: " + name);
		System.out.println("	Access: " + accessModifier);
		System.out.println("	Type: " + type);
		System.out.println("	Final: " + finalField);
		System.out.println("	Static: " + staticField);
	}
	
	void parse() {
	
	}
}
