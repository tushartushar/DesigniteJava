package Designite.SourceModel;

import java.io.PrintWriter;
import java.lang.reflect.Modifier;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class SM_Field extends SM_SourceItem {
	private TypeDeclaration typeDeclaration;
	private FieldDeclaration fieldDeclaration;
	private SM_Type parentType;
	private boolean finalField = false;
	private boolean staticField = false;
	private VariableDeclarationFragment variableDeclaration;
	private TypeInfo typeinfo;
	
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
		print(writer, "\t\tField name: " + getName());
		print(writer, "\t\tParent class: " + this.parentType.getName());
		print(writer, "\t\tAccess: " + getAccessModifier());
		print(writer, "\t\tFinal: " + isFinal());
		print(writer, "\t\tStatic: " + isStatic());
		if (!isPrimitiveType() && getType() != null)
			print(writer, "\t\tField type: " + getType().getName());
		else
			if (isPrimitiveType())
				print(writer, "\t\tPrimitive field type: " + getPrimitiveType());
		print(writer, "\t\t----");
	}


	@Override
	public void parse() {
	}

	@Override
	public void resolve() {
		Resolver resolver = new Resolver();
		typeinfo = resolver.resolveVariableType(fieldDeclaration.getType(), getParentType().getParentPkg().getParentProject());
	}
	
	public boolean isPrimitiveType() {
		return typeinfo.isPrimitiveType();
	}

	public SM_Type getType() {
		return typeinfo.getTypeObj();
	}
	
	public String getPrimitiveType() {
		return typeinfo.getPrimitiveObj();
	}
}
