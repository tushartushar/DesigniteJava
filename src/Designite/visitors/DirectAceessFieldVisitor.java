package Designite.visitors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.ThisExpression;

public class DirectAceessFieldVisitor extends ASTVisitor {
	
	private List<SimpleName> names = new ArrayList<>();
	private List<FieldAccess> thisAccesses = new ArrayList<>();
	
	public boolean visit(SimpleName node) {
		if (!names.contains(node)) {
			names.add(node);
		}
		return true;
	}
	
	public boolean visit(FieldAccess node) {
		if (node.getExpression() instanceof ThisExpression) {
			if (!this.thisAccesses.contains(node)) {
				thisAccesses.add(node);
			}
		}
		return true;
	}
	
	public List<SimpleName> getNames() {
		return names;
	}
	
	public List<FieldAccess> getThisAccesses() {
		return thisAccesses;
	}
}
