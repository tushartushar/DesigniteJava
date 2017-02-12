package project.metrics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;

public class ClassMetrics {

	private int countMethods = 0;
	private int publicMethods = 0;

	public int getNoMethods() {
		return countMethods;
	}
	
	public int getPublicMethods() {
		return publicMethods;
	}
	
	//temporary solution
	public static String readFileToString(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));

		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			//System.out.println(numRead);
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}

		reader.close();

		return fileData.toString();
	}
	
	public CompilationUnit createAST(final String content) {
		Document doc = new Document(content);
		final ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(doc.get().toCharArray());
		parser.setResolveBindings(true);
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		return cu;
	}

	public void visitAST(CompilationUnit cu) {
		MethodVisitor visitor = new MethodVisitor();
		cu.accept(visitor);
		
		computeMetrics(visitor);
		printMetrics();
		
	}

	public void computeMetrics(MethodVisitor visitor) {
		countMethods = visitor.countMethods(visitor.methods);
		for(int i = 0; i < countMethods; i++){
			if (visitor.methods.get(i).isPublic())
				publicMethods++;
		}
	}
	
	public void printMetrics(){
		System.out.println("NOM: " + getNoMethods());
		System.out.println("NOPM: " + getPublicMethods());
	}

}
