package Designite.visitors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.WhileStatement;

public class MethodControlFlowVisitor extends ASTVisitor {

	private List<IfStatement> ifStatements = new ArrayList<>();
	private List<SwitchCase> switchCaseStatements = new ArrayList<>();
	private List<SwitchCase> switchCaseStatementsWitoutDefaults = new ArrayList<>();
	private List<ForStatement> forStatements = new ArrayList<>();
	private List<WhileStatement> whileStatements = new ArrayList<>();
	private List<DoStatement> doStatements = new ArrayList<>();
	private List<EnhancedForStatement> foreachStatements = new ArrayList<>();
	
	public boolean visit(IfStatement node) {
		ifStatements.add(node);
		return true;
	}
	
	public boolean visit(SwitchCase node) {
		switchCaseStatements.add(node);
		if (!node.isDefault()) {
			switchCaseStatementsWitoutDefaults.add(node);
		}
		return true;
	}
	
	public boolean visit(ForStatement node) {
		forStatements.add(node);
		return true;
	}
	
	public boolean visit(WhileStatement node) {
		whileStatements.add(node);
		return true;
	}
	
	public boolean visit(DoStatement node) {
		doStatements.add(node);
		return true;
	}
	
	public boolean visit(EnhancedForStatement node) {
		foreachStatements.add(node);
		return true;
	}
	
	public List<IfStatement> getIfStatements() {
		return ifStatements;
	}
	
	public List<ForStatement> getForStatements() {
		return forStatements;
	}
	
	public List<WhileStatement> getWhileStatements() {
		return whileStatements;
	}
	
	public List<DoStatement> getDoStatements() {
		return doStatements;
	}
	
	public int getNumOfIfStatements() {
		return ifStatements.size();
	}

	public int getNumOfSwitchCaseStatementsWitoutDefault() {
		return switchCaseStatementsWitoutDefaults.size();
	}

	public int getNumOfForStatements() {
		return forStatements.size();
	}

	public int getNumOfWhileStatements() {
		return whileStatements.size();
	}

	public int getNumOfDoStatements() {
		return doStatements.size();
	}

	public int getNumOfForeachStatements() {
		return foreachStatements.size();
	}
	
}
