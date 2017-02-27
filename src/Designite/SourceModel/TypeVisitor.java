package Designite.SourceModel;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import java.util.ArrayList;
import java.util.List;

public class TypeVisitor extends ASTVisitor{
	private List<SM_Type> types = new ArrayList<SM_Type>();
	private CompilationUnit compilationUnit;
	
	public TypeVisitor(CompilationUnit cu) {
		super();
		this.compilationUnit = cu;
	}
	
	@Override
	public boolean visit(TypeDeclaration typeDeclaration){
		SM_Type newType = new SM_Type(typeDeclaration, compilationUnit);
		types.add(newType);
		
		return super.visit(typeDeclaration);
	}
	
	List<SM_Type> getTypeList() {
		return types;
	}
}
