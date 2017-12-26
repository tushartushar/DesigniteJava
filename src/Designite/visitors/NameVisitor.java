package Designite.visitors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.SimpleName;

public class NameVisitor extends ASTVisitor {
	
	private List<SimpleName> names = new ArrayList<>();
	
	public boolean visit(SimpleName node) {
		if (!names.contains(node)) {
			names.add(node);
		}
		return true;
	}
	
	public List<SimpleName> getNames() {
		return names;
	}
}
