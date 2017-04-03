package Designite.SourceModel;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class SM_LocalVar extends SM_Variable {
	
	private MethodDeclaration methodDeclaration;
	private VariableDeclarationFragment localVarFragment;
	private SM_Method parentMethod;
	private SM_Type parentType;
	
	public SM_LocalVar(VariableDeclarationFragment localVar) {
		this.localVarFragment = localVar;
//		this.methodDeclaration = methodDeclaration;
		setName(localVarFragment.getName().toString());
	}
	
	public MethodDeclaration getMethodDeclaration() {
		return methodDeclaration;
	}
	
	void setParent(SM_Method parentMethod) {
		this.parentMethod = parentMethod;
		this.parentType = parentMethod.getParentType();
		this.parentProject = parentMethod.getParentProject();
	}
	
	public SM_Method getParent() {
		return parentMethod;
	}
	
	public SM_Type getParentType() {
		return parentType;
	}
	
	void parse(SM_Method parentMethod) {
		setParent(parentMethod);
		parentType.addToVariableList(this);
	}
	
	@Override
	public void print() {
		System.out.println("LocalVar: " + getName());
		System.out.println("	Parent: " + this.getParent().getName());
		System.out.println("	Type: " + getType());
		findRefObject();
		if (getRefType() != null)
			System.out.println("	Refers to: " + getRefType().getName());
/*		for (SM_Variable var: parentType.getVariableList()) 
			System.out.println(var.getName());*/
	}
}
