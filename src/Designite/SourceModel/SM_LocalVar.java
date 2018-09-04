package Designite.SourceModel;

import java.io.PrintWriter;

import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class SM_LocalVar extends SM_EntitiesWithType {
	private VariableDeclarationFragment localVarFragment;
	private SM_Method parentMethod;
	private VariableDeclarationStatement localVarDecl;

	public SM_LocalVar(VariableDeclarationStatement varDecl, VariableDeclarationFragment localVar, SM_Method method) {
		this.localVarFragment = localVar;
		parentMethod = method;
		name = localVarFragment.getName().toString();
		localVarDecl = varDecl;
	}

	public SM_Method getParentMethod() {
		return parentMethod;
	}
	
	@Override
	public SM_Type getParentType() {
		return this.parentMethod.getParentType();
	}

	@Override
	public void printDebugLog(PrintWriter writer) {
		print(writer, "\t\t\tLocalVar: " + getName());
		print(writer, "\t\t\tParent method: " + this.parentMethod.getName());

		if (!isPrimitiveType()) {
//		if (!isPrimitiveType() && !isTypeVariable()) {
			if (typeInfo.isParametrizedType()) {
				print(writer, "\t\t\tParameter types: " + typeInfo.getStringOfNonPrimitiveParameters());
			}
			else {
//				print(writer, "\t\t\tVariable type: " + getType().getName());
				print(writer, "\t\t\tVariable type: " + getType());
			}
		} /*else if (isTypeVariable()) {
			print(writer, "\t\t\tType Variable :: " + getName());
		}*/
		else
			print(writer, "\t\t\tPrimitive variable type: " + getPrimitiveType());
		print(writer, "\t\t\t----");
	}

	@Override
	public void resolve() {
		Resolver resolver = new Resolver();
		typeInfo = resolver.resolveVariableType(localVarDecl.getType(), parentMethod.getParentType().getParentPkg().getParentProject(), getParentType());
	}
	
	@Override
	public String toString() {
		return "Local variable name=" + name
		+ ", type=" + localVarDecl.getType()
		+ ", isParameterizedType=" + localVarDecl.getType().isParameterizedType();
	}
	
}
