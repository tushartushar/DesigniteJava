package Designite.SourceModel;

import java.io.PrintWriter;
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
	private VariableDeclarationFragment variableDeclaration;
	
	public SM_Field(FieldDeclaration fieldDeclaration, VariableDeclarationFragment varDecl, SM_Type parentType) {
		this.fieldDeclaration = fieldDeclaration;
		this.variableDeclaration = varDecl;
		this.parentType = parentType;
		setAccessModifier(fieldDeclaration.getModifiers());
		setFieldInfo(fieldDeclaration);
		name = varDecl.getName().toString();
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
	
	
	public SM_Type getParentType() {
		return parentType;
	}
	
	
	@Override
	public void printDebugLog(PrintWriter writer) {
		print(writer, "Field: " + getName());
		print(writer, "	Parent: " + this.parentType.getName());
		print(writer, "	Access: " + getAccessModifier());
		print(writer, "	Type: " + getType());
		print(writer, "	Final: " + isFinal());
		print(writer, "	Static: " + isStatic());
		/*findRefObject();
		if (getRefType() != null)
			print(writer, "	Refers to: " + getRefType().getName());*/
	}

	@Override
	public void printDebugLog() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parse() {
	}

	@Override
	public void resolve() {
		// TODO Auto-generated method stub
		
	}
}
