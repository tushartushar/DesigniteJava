package Designite.SourceModel;

import java.io.PrintWriter;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;

public class SM_Parameter extends SM_SourceItem {
	private SM_Method parentMethod;
	private SingleVariableDeclaration variableDecl;
	private TypeInfo typeinfo;
	
	public SM_Parameter(SingleVariableDeclaration variable, SM_Method methodObj) {
		name = variable.getName().toString();
		this.parentMethod = methodObj;
		variableDecl = variable;
		
	}
	
	void setParent(SM_Method parentMethod) {
		this.parentMethod = parentMethod;
	}
	
	public SM_Method getParent() {
		return parentMethod;
	}
	
	
	@Override
	public void printDebugLog(PrintWriter writer) {
		print(writer, "\t\t\tParameter: " + name);
		print(writer, "\t\t\tParent Method: " + getParent().getName());
		if (!isPrimitiveType() && getType() != null)
			print(writer, "\t\t\tParameter type: " + getType().getName());
		else
			if (isPrimitiveType())
				print(writer, "\t\t\tPrimitive parameter type: " + getPrimitiveType());
		print(writer, "\t\t\t----");
	}


	@Override
	public void parse() {
		//There is nothing to do further. so, chill.
	}

	@Override
	public void resolve() {
		Resolver resolver = new Resolver();
		typeinfo = resolver.resolveVariableType(variableDecl.getType(), parentMethod.getParentType().getParentPkg().getParentProject());
	}

	public Type getTypeBinding() {
		return variableDecl.getType();
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
