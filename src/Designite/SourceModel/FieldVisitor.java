package Designite.SourceModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class FieldVisitor extends ASTVisitor {
	List<SM_Field> fields = new ArrayList<SM_Field>();
	private TypeDeclaration typeDeclaration;

	public FieldVisitor(TypeDeclaration typeDeclaration) {
		super();
		this.typeDeclaration = typeDeclaration;
	}
	
	@Override
	public boolean visit(FieldDeclaration field) {
		SM_Field newField = new SM_Field(field, typeDeclaration);
		fields.add(newField);

		return super.visit(field);
	}

	public List<SM_Field> getFields() {
		return fields;
	}

	public int countFields() {
		return fields.size();
	}
}
