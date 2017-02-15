package Designite.SourceModel;

import org.eclipse.jdt.core.dom.CompilationUnit;

public class SourceModel {

	public void create(String sourcePath) {
		FileReader files = new FileReader(sourcePath);
		
		for(String pathToAnalyze: files.getPathList()){
			System.out.println(pathToAnalyze);
			ClassMetrics classMetrics = new ClassMetrics(pathToAnalyze);
			String fileToString = classMetrics.readFileToString();
			CompilationUnit unit = classMetrics.createAST(fileToString);
			classMetrics.visitAST(unit);
		}
		
	}

}
