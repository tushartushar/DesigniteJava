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
		print(writer, "	Parent method: " + this.parentMethod.getName());

		if (variableType != null)
			print(writer, "	Variable type: " + variableType.getName());
		else
			if (isPrimitive())
				print(writer, "Primitive variable type: " + primitiveVariableType);
	}

	@Override
	public void parse() {
		//There is nothing to be done further.
	}

	@Override
	public void resolve() {
		resolveVariableType(parentMethod.getParentType().getParentPkg().getParentProject());
	}
}
