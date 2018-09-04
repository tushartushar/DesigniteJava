package Designite.SourceModel;

import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import Designite.utils.models.Vertex;

public class SM_Field extends SM_EntitiesWithType implements Vertex {
	private TypeDeclaration typeDeclaration;
	private FieldDeclaration fieldDeclaration;
	private SM_Type parentType;
	private SM_Type nestedParentType = null;
	private boolean finalField = false;
	private boolean staticField = false;
	//private VariableDeclarationFragment variableDeclaration;
	
	public SM_Field(FieldDeclaration fieldDeclaration, VariableDeclarationFragment varDecl, SM_Type parentType) {
		this.fieldDeclaration = fieldDeclaration;
		//this.variableDeclaration = varDecl;
		this.parentType = parentType;
		setAccessModifier(fieldDeclaration.getModifiers());
		setFieldInfo(fieldDeclaration);
		name = varDecl.getName().toString();
		assignToNestedTypeIfNecessary();
	}
	
	void setFieldInfo(FieldDeclaration field){
		int modifiers = field.getModifiers();
		if (Modifier.isFinal(modifiers)) 
			finalField =  true;
		if (Modifier.isStatic(modifiers)) 
			staticField =  true;
	}
	
	private void assignToNestedTypeIfNecessary() {
		if (parentType.getNestedTypes().size() < 1) {
			return;			
		} else {
			String typeName = getNestedParentName();
			if(typeName != null) {
				typeName = typeName.trim();
				this.nestedParentType = parentType.getNestedTypeFromName(typeName);
				if(this.nestedParentType != null) {
				}
			}
		}
	}
	
	private String getNestedParentName() {
		final String regex = "public|private[ ]{1,}class[ ]{1,}([^\\{]*)[\\{]{1}";
		final String inputString = this.fieldDeclaration.getParent().toString();
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(inputString);
		
		String typeName = "";
		while (matcher.find()) {
			typeName = matcher.group(1);
			return typeName;
		}
		return "";
	}
	
	public SM_Type getNestedParent() {
		return this.nestedParentType;
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
	
	@Override
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
		if (!isPrimitiveType()) {
			if (getType() != null) {
				print(writer, "\t\tField type: " + getType().getName());
			} else {
				print(writer, "\t\tField type: " + typeInfo.getObjPrimitiveType());
			}
		}
		else
			if (isPrimitiveType())
				print(writer, "\t\tPrimitive field type: " + getPrimitiveType());
		if (isParametrizedType()) {
			print(writer, "\t\tList of parameters: " + typeInfo.getStringOfNonPrimitiveParameters());
		}
		print(writer, "\t\t----");
	}

	@Override
	public void resolve() {
		Resolver resolver = new Resolver();
		typeInfo = resolver.resolveVariableType(fieldDeclaration.getType(), getParentType().getParentPkg().getParentProject(), getParentType());
	}
	
}
