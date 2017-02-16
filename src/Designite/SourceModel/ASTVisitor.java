package Designite.SourceModel;

import org.eclipse.jdt.core.dom.CompilationUnit;

public class ASTVisitor {
	private CompilationUnit cu;
	
	public ASTVisitor(CompilationUnit cu){
		this.cu = cu;
	}
	
	public void visitAST() {
		ClassMetrics classInfo = new ClassMetrics();
		
		MethodVisitor methodVisitor = new MethodVisitor();
		cu.accept(methodVisitor);
		classInfo.computeMetrics(methodVisitor);

		FieldVisitor fieldVisitor = new FieldVisitor();
		cu.accept(fieldVisitor);
		classInfo.computeMetrics(fieldVisitor);
		
		classInfo.printMetrics();
	}
}
