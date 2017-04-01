package Designite.SourceModel;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class SM_LocalVar extends SM_Variable {
	
	private MethodDeclaration methodDeclaration;
	private VariableDeclarationFragment localVarFragment;
	private SM_Method parentMethod;
	
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
		this.parentProject = parentMethod.getParentProject();
	}
	
	public SM_Method getParent() {
		return parentMethod;
	}
	
	void parse(SM_Method parentMethod) {
		setParent(parentMethod);
	}
	
	@Override
	public void print() {
		System.out.println("LocalVar: " + getName());
		System.out.println("	Parent: " + this.getParent().getName());
		System.out.println("	Type: " + getType());
		findRefObject();
		if (getRefType() != null)
			System.out.println("	Refers to: " + getRefType().getName());
	}
}
