package Designite.visitors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class NumberLiteralVisitor extends ASTVisitor {
	private List<NumberLiteral> numberLiteralsExpressions = new ArrayList<>();
	
	public boolean visit(NumberLiteral node)
	{
		numberLiteralsExpressions.add(node);
		return true;
	}
	
	public List<NumberLiteral> getNumberLiteralsExpressions() {
		return numberLiteralsExpressions;
	}
	
	
}
