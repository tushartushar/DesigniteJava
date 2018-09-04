package Designite.SourceModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class FieldVisitor extends ASTVisitor {
	List<SM_Field> fields = new ArrayList<SM_Field>();
	private SM_Type parentType;

	public FieldVisitor(SM_Type parentType) {
		super();
		this.parentType = parentType;
	}

	@Override
	public boolean visit(FieldDeclaration fieldDeclaration) {
		List<VariableDeclarationFragment> fieldList = fieldDeclaration.fragments();
		for (VariableDeclarationFragment field : fieldList) {
			SM_Field newField = new SM_Field(fieldDeclaration, field, parentType);
			fields.add(newField);
		}

		return super.visit(fieldDeclaration);
	}

	public List<SM_Field> getFields() {
		return fields;
	}

	/*
	 * public int countFields() { return fields.size(); }
	 */
}
