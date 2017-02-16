package Designite.SourceModel;

import org.eclipse.jdt.core.dom.CompilationUnit;

public class SourceModel {

	public void create(String sourcePath) {
		FileReader files = new FileReader(sourcePath);
		
		for(String pathToAnalyze: files.getPathList()){
			System.out.println(pathToAnalyze);
			
			ASTCreator ast = new ASTCreator(pathToAnalyze);
			CompilationUnit unit = ast.createAST();
			
			ASTVisitor visitor = new ASTVisitor(unit);
			visitor.visitAST();
		}
		
	}
}
