package Designite.SourceModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class FieldVisitor extends ASTVisitor {
	List<SM_Field> fields = new ArrayList<SM_Field>();
	private TypeDeclaration typeDeclaration;

	public FieldVisitor(TypeDeclaration typeDeclaration) {
		super();
		this.typeDeclaration = typeDeclaration;
	}
	
	@Override
	public boolean visit(FieldDeclaration fieldDeclaration) {
		List<VariableDeclarationFragment> fieldList = fieldDeclaration.fragments();
		for(VariableDeclarationFragment field: fieldList) {
			SM_Field newField = new SM_Field(fieldDeclaration, typeDeclaration);
			fields.add(newField);
			
			newField.setName(field.getName().toString());
			newField.setFieldVar(field);
		}

		return super.visit(fieldDeclaration);
	}

	public List<SM_Field> getFields() {
		return fields;
	}

	public int countFields() {
		return fields.size();
	}
}
