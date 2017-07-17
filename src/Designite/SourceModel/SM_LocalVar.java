package Designite.SourceModel;

import java.io.PrintWriter;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class SM_LocalVar extends SM_Variable {
	private VariableDeclarationFragment localVarFragment;
	private SM_Method parentMethod;

	public SM_LocalVar(VariableDeclarationFragment localVar, SM_Method method) {
		this.localVarFragment = localVar;
		parentMethod = method;
		setName(localVarFragment.getName().toString());
	}

	/*
	 * public MethodDeclaration getMethodDeclaration() { return
	 * methodDeclaration; }
	 */

	public SM_Method getParentMethod() {
		return parentMethod;
	}

	@Override
	public void printDebugLog(PrintWriter writer) {
		print(writer, "LocalVar: " + getName());
		print(writer, "	Parent: " + this.parentMethod.getName());
		print(writer, "	Type: " + getType());
		/*findRefObject();
		if (getRefType() != null)
			print(writer, "	Refers to: " + getRefType().getName());*/
		/*
		 * for (SM_Variable var: parentType.getVariableList()) print(writer,
		 * var.getName());
		 */
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
