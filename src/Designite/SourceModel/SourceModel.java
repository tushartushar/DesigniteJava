package Designite.SourceModel;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.jdt.core.dom.CompilationUnit;

public class SourceModel {

	public void create(String sourcePath) {
		/*FileReader files = new FileReader(sourcePath);
		
		for(String pathToAnalyze: files.getPathList()){
			System.out.println(pathToAnalyze);
			
			ASTCreator ast = new ASTCreator(pathToAnalyze);
			CompilationUnit unit = ast.createAST();
			
			ASTVisitor visitor = new ASTVisitor(unit);
			visitor.visitAST();*/
		ClassMetrics classMetrics = new ClassMetrics();
		String fileToString = readFileToString(sourcePath);
		CompilationUnit unit = classMetrics.createAST(fileToString);
		classMetrics.visitAST(unit);
	}
	
	public String readFileToString(String sourcePath) {
		try {
		 			return new String(Files.readAllBytes(Paths.get(sourcePath)));
		} catch (IOException e) {
		 			e.printStackTrace();
		 			return new String();
		}
	}
}
