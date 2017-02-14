package Designite.SourceModel;

import org.eclipse.jdt.core.dom.CompilationUnit;

public class SourceModel {

	public void create(String sourcePath) {
		ClassMetrics classMetrics = new ClassMetrics(sourcePath);
		String fileToString = classMetrics.readFileToString();
		CompilationUnit unit = classMetrics.createAST(fileToString);
		classMetrics.visitAST(unit);
		
	}

}
