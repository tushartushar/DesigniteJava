package Designite.SourceModel;

import java.lang.reflect.Modifier;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class SM_Field extends SM_SourceItem {
	private TypeDeclaration typeDeclaration;
	private boolean publicField;
	
	SM_Field(FieldDeclaration field, TypeDeclaration typeDeclaration) {
		this.typeDeclaration = typeDeclaration;
		setPublicField(field);
	}
	
	public void setPublicField(FieldDeclaration field) {
		int modifiers = field.getModifiers();
		if (Modifier.isPublic(modifiers)) {
			publicField = true;
		} else 
			publicField = false;
	}
	
	public boolean isPublic() {
		return this.publicField;
	}
	
	@Override
	public void print() {
		System.out.println("Field: " + name);
	}
	
	void parse() {
	
	}
}
