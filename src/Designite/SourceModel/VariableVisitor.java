package Designite.SourceModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

public class VariableVisitor extends ASTVisitor {
	List<SM_Parameter> parameters = new ArrayList<SM_Parameter>();
	private SM_Method parentMethod;
	
	public VariableVisitor(SM_Method methodObj) {
		super();
		this.parentMethod = methodObj;
	}

	@Override
	public boolean visit(SingleVariableDeclaration variable) {
		SM_Parameter newParameter = new SM_Parameter(variable, parentMethod);
		parameters.add(newParameter);

		return super.visit(variable);
	}

	public List<SM_Parameter> getParameterList() {
		return parameters;
	}

	/*public int countParameters() {
		return parameters.size();
	}*/
}
