package Designite.SourceModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodVisitor extends ASTVisitor {
	List<Method> methods = new ArrayList<Method>();
	
	@Override
	public boolean visit(MethodDeclaration method) {
		Method newMethod = new Method(method);
		methods.add(newMethod);
		
		return super.visit(method);
	}
	
	public List<Method> getMethods(){
		return methods;
	}
	
	public int countMethods(List<Method> methods) {
		return methods.size();
	}
	
}
