package Designite.SourceModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ImportDeclaration;

public class ImportVisitor extends ASTVisitor {
	List<ImportDeclaration> imports = new ArrayList<>();
	
	@Override
	public boolean visit(ImportDeclaration newImport) {
		imports.add(newImport);
		
		return super.visit(newImport);
	}
	
	public List<ImportDeclaration> getImports(){
		return imports;
	}
}
