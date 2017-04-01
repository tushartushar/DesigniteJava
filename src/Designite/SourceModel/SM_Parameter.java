package Designite.SourceModel;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

public class SM_Parameter extends SM_Variable {
	private SingleVariableDeclaration variable;
	private MethodDeclaration methodDeclaration;
	private SM_Method parentMethod;
	
	public SM_Parameter(SingleVariableDeclaration variable, MethodDeclaration methodDeclaration) {
		name = variable.getName().toString();
		this.variable = variable;
		this.methodDeclaration = methodDeclaration;
		setType(variable.getType());
	}
	
	void setParent(SM_Method parentMethod) {
		this.parentMethod = parentMethod;
		parentProject = parentMethod.getParentProject();
	}
	
	public SM_Method getParent() {
		return parentMethod;
	}
	
	void parse(SM_Method parentMethod) {
		setParent(parentMethod);
	}
	
	@Override
	public void print() {
		System.out.println("Parameter: " + name);
		System.out.println("	Parent: " + getParent().getName());
		System.out.println("	Type: " + type);
		findRefObject();
		if (getRefType() != null)
			System.out.println("	Refers to: " + getRefType().getName());
	}
}
