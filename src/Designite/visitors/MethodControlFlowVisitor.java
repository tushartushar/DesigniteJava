package Designite.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.WhileStatement;

public class MethodControlFlowVisitor extends ASTVisitor {
	
	private int numOfIfStatements;
	private int numOfSwitchCaseStatements;
	private int numOfForStatements;
	private int numOfWhileStatements;
	private int numOfDoStatements;
	private int numOfForeachStatements;
	
	public boolean visit(IfStatement node) {
		numOfIfStatements++;
		return true;
	}
	
	public boolean visit(SwitchCase node) {
		if (!node.isDefault()) {
			numOfSwitchCaseStatements++;
		}
		return true;
	}
	
	public boolean visit(ForStatement node) {
		numOfForStatements++;
		return true;
	}
	
	public boolean visit(WhileStatement node) {
		numOfWhileStatements++;
		return true;
	}
	
	public boolean visit(DoStatement node) {
		numOfDoStatements++;
		return true;
	}
	
	public boolean visit(EnhancedForStatement node) {
		numOfForeachStatements++;
		return true;
	}
	
	public int getNumOfIfStatements() {
		return numOfIfStatements;
	}

	public int getNumOfSwitchCaseStatements() {
		return numOfSwitchCaseStatements;
	}

	public int getNumOfForStatements() {
		return numOfForStatements;
	}

	public int getNumOfWhileStatements() {
		return numOfWhileStatements;
	}

	public int getNumOfDoStatements() {
		return numOfDoStatements;
	}

	public int getNumOfForeachStatements() {
		return numOfForeachStatements;
	}
	
}
