package Designite.SourceModel;

import java.io.PrintWriter;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

public class SM_Parameter extends SM_Variable {
	private SM_Method parentMethod;
	private SingleVariableDeclaration variableDecl;
	
	public SM_Parameter(SingleVariableDeclaration variable, SM_Method methodObj) {
		name = variable.getName().toString();
		this.parentMethod = methodObj;
		variableDecl = variable;
		setType(variableDecl.getType());
	}
	
	void setParent(SM_Method parentMethod) {
		this.parentMethod = parentMethod;
	}
	
	public SM_Method getParent() {
		return parentMethod;
	}
	
	
	@Override
	public void printDebugLog(PrintWriter writer) {
		print(writer, "Parameter: " + name);
		print(writer, "	Parent Method: " + getParent().getName());
		print(writer, "	Type: " + type);
		if (variableType != null)
			print(writer, "	Refers to: " + variableType.getName());
	}


	@Override
	public void parse() {
		//There is nothing to do further. so, chill.
	}

	@Override
	public void resolve() {
		resolveVariableType(parentMethod.getParentType().getParentPkg().getParentProject());
	}

}
