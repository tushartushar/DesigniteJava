package Designite.SourceModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;

public class FieldVisitor extends ASTVisitor {
	List<FieldDeclaration> fields = new ArrayList<FieldDeclaration>();

	@Override
	public boolean visit(FieldDeclaration field) {
		fields.add(field);

		return super.visit(field);
	}

	public List<FieldDeclaration> getFields() {
		return fields;
	}

	public int countFields() {
		return fields.size();
	}

}
