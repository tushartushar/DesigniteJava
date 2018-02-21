package Designite.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ThrowStatement;

public class ThrowVisitor extends ASTVisitor {

	private boolean throwsException;
	
	public boolean visit(ThrowStatement node) {
		throwsException = true;
		return true;
	}
	
	public boolean throwsException() {
		return throwsException;
	}

}
