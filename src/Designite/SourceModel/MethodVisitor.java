package Designite.SourceModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodVisitor extends ASTVisitor {
	List<SM_Method> methods = new ArrayList<SM_Method>();
	
	@Override
	public boolean visit(MethodDeclaration method) {
		SM_Method newMethod = new SM_Method(method);
		methods.add(newMethod);
		
		return super.visit(method);
	}
	
	public List<SM_Method> getMethods(){
		return methods;
	}
	
	public int countMethods() {
		return methods.size();
	}
	
}
