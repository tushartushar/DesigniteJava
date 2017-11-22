package Designite.SourceModel;

import java.io.PrintWriter;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class SM_LocalVar extends SM_SourceItem {
	private VariableDeclarationFragment localVarFragment;
	private SM_Method parentMethod;
	private VariableDeclarationStatement localVarDecl;
	TypeInfo typeinfo;

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
	public void printDebugLog(PrintWriter writer) {
		print(writer, "\t\t\tLocalVar: " + getName());
		print(writer, "\t\t\tParent method: " + this.parentMethod.getName());

		if (typeinfo.IsPrimitiveType == false && typeinfo.TypeObj != null)
			print(writer, "\t\t\tVariable type: " + typeinfo.TypeObj.getName());
		else
			if (typeinfo.IsPrimitiveType)
				print(writer, "\t\t\tPrimitive variable type: " + typeinfo.PrimitiveType);
		print(writer, "\t\t\t----");
	}

	@Override
	public void parse() {
		//There is nothing to be done further.
	}

	@Override
	public void resolve() {
		Resolver resolver = new Resolver();
		typeinfo = resolver.resolveVariableType(localVarDecl.getType(), parentMethod.getParentType().getParentPkg().getParentProject());
	}
	public boolean isPrimitive() {
		return typeinfo.IsPrimitiveType;
	}

	public SM_Type getType() {
		return typeinfo.TypeObj;
	}
	public String getPrimitiveType(){
		return typeinfo.PrimitiveType;
	}
}
