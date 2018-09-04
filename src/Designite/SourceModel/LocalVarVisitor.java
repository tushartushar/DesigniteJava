package Designite.SourceModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class LocalVarVisitor extends ASTVisitor {
	List<SM_LocalVar> localVariables = new ArrayList<SM_LocalVar>();
	private SM_Method parentMethod;
	
	public LocalVarVisitor(SM_Method methodObj) {
		this.parentMethod = methodObj;
	}

	public boolean visit(VariableDeclarationStatement variable){
		for (Iterator iter = variable.fragments().iterator(); iter.hasNext();) {
			VariableDeclarationFragment fragment = (VariableDeclarationFragment) iter.next();
//			IVariableBinding binding = fragment.resolveBinding();
			
			SM_LocalVar newLocalVar = new SM_LocalVar(variable, fragment, parentMethod);
			//newLocalVar.setType(variable.getType());
			localVariables.add(newLocalVar);
		}
		return super.visit(variable);
	}
	
	public List<SM_LocalVar> getLocalVarList() {
		return localVariables;
	}
	
}
