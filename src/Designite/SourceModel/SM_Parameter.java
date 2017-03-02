package Designite.SourceModel;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

public class SM_Parameter extends SM_SourceItem {
	private SingleVariableDeclaration variable;
	private MethodDeclaration methodDeclaration;
	
	SM_Parameter(SingleVariableDeclaration variable, MethodDeclaration methodDeclaration) {
		name = variable.getName().toString();
		this.variable = variable;
		this.methodDeclaration = methodDeclaration;
	}
	
	@Override
	public void print() {
		System.out.println("Parameter: " + name);
	}

}
