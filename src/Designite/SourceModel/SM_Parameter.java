package Designite.SourceModel;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;

public class SM_Parameter extends SM_SourceItem {
	private SingleVariableDeclaration variable;
	private MethodDeclaration methodDeclaration;
	private SM_Method parentMethod;
	private Type type;
	
	public SM_Parameter(SingleVariableDeclaration variable, MethodDeclaration methodDeclaration) {
		name = variable.getName().toString();
		this.variable = variable;
		this.methodDeclaration = methodDeclaration;
		setType();
	}
	
	void setType() {
		this.type = variable.getType();
	}
	
	public Type getType() {
		return type;
	}
	
	void setParent(SM_Method parentMethod) {
		this.parentMethod = parentMethod;
	}
	
	public SM_Method getParent() {
		return parentMethod;
	}
	
	@Override
	public void print() {
		System.out.println("Parameter: " + name);
		System.out.println("	Parent: " + this.getParent().getName());
		System.out.println("	Type: " + type);
	}

}
