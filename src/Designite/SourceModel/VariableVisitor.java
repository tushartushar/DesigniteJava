package Designite.SourceModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class VariableVisitor extends ASTVisitor {
	List<SM_Parameter> parameters = new ArrayList<SM_Parameter>();
	private MethodDeclaration methodDeclaration;
	private TypeDeclaration typeDeclaration;

	public VariableVisitor(MethodDeclaration methodDeclaration, TypeDeclaration typeDeclaration) {
		super();
		this.methodDeclaration = methodDeclaration;
		this.typeDeclaration = typeDeclaration;
	}

	@Override
	public boolean visit(SingleVariableDeclaration variable) {
		SM_Parameter newParameter = new SM_Parameter(variable, methodDeclaration);
		parameters.add(newParameter);

		return super.visit(variable);
	}

	public List<SM_Parameter> getParameters() {
		return parameters;
	}

	public int countParameters() {
		return parameters.size();
	}
}
