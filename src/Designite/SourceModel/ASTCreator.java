package Designite.SourceModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;

public class ASTCreator {
	private String pathToAnalyze;
	private CompilationUnit cu;
	
	public ASTCreator(String pathToAnalyze){
		this.pathToAnalyze = pathToAnalyze;
	}
	
	public CompilationUnit getCU(){
		return cu;
	}
	
	public String readFileToString() {
		try {
			return new String(Files.readAllBytes(Paths.get(pathToAnalyze)));
		} catch (IOException e) {
			e.printStackTrace();
			return new String();
		}
	}
	
	public CompilationUnit createAST() {
		String content = readFileToString();
		Document doc = new Document(content);
		final ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(doc.get().toCharArray());
		parser.setResolveBindings(true);
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		return cu;
	}
}
