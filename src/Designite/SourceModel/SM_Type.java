package Designite.SourceModel;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jface.text.Document;

public class SM_Type extends SM_SourceItem{

	private int countMethods = 0;
	private int publicMethods = 0;
	private int countFields = 0;
	private int publicFields = 0;
	

	public int getNoMethods() {
		return countMethods;
	}

	public int getPublicMethods() {
		return publicMethods;
	}

	public int getNoFields() {
		return countFields;
	}

	public int getPublicFields() {
		return publicFields;
	}

	void computeMetrics(MethodVisitor visitor) {
		countMethods = visitor.countMethods();
		for (int i = 0; i < countMethods; i++) {
			if (visitor.methods.get(i).isPublic())
				publicMethods++;
		}
	}

	void computeMetrics(FieldVisitor visitor) {
		countFields = visitor.countFields();
		for (int i = 0; i < countFields; i++) {
			int fieldModifier = visitor.fields.get(i).getModifiers();
			if (Modifier.isPublic(fieldModifier))
				publicFields++;
		}
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
 		MethodVisitor methodVisitor = new MethodVisitor();
 		cu.accept(methodVisitor);
 		computeMetrics(methodVisitor);
 
 		FieldVisitor fieldVisitor = new FieldVisitor();
 		cu.accept(fieldVisitor);
 		computeMetrics(fieldVisitor);
 		
 		printMetrics();
 	}
	public void printMetrics() {
		System.out.println("NOM: " + getNoMethods());
		System.out.println("NOPM: " + getPublicMethods());
		System.out.println("NOF: " + getNoFields());
		System.out.println("NOPF: " + getPublicFields());
	}

}
