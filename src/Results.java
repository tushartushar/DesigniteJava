package project.metrics;

import java.io.IOException;

import org.eclipse.jdt.core.dom.CompilationUnit;

public class Results {
	public static void main(String[] args) throws IOException{
		ClassMetrics test = new ClassMetrics();
		
		//args[0]: filePath
		if (args.length != 1) {
			System.err.println("First argument needs to be the path.");
			throw new IllegalArgumentException();
		}
		String fileToString = ClassMetrics.readFileToString(args[0]);
		CompilationUnit unit = test.createAST(fileToString);
		test.visitAST(unit);
		
	}
}
