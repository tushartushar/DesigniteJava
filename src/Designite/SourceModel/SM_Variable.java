package Designite.SourceModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.Type;

public abstract class SM_Variable extends SM_SourceItem {

	// protected SM_Project parentProject;
	protected SM_Type variableType;
	protected String primitiveVariableType;

	protected Type type;
	protected Type arrayType;
	protected List<Type> typeList = new ArrayList<>();

	protected boolean isPrimitive = false;
	protected boolean isParameterized = false;
	protected boolean isArray = false;

	public void setName(String name) {
		this.name = name;
	}

	public boolean isParameterized() {
		return isParameterized;
	}

	public boolean isPrimitive() {
		return isPrimitive;
	}

	void setType(Type type) {
		this.type = type;
		setCategoryOfType();
	}

	public Type getTypeBinding() {
		return type;
	}

	void setCategoryOfType() {
		if (type.isPrimitiveType()) {
			isPrimitive = true;
		} else if (type.isParameterizedType()) {
			isParameterized = true;
		} else if (type.isArrayType()) {
			isArray = true;
		}
	}

	void specifyTypes(Type type) {
		if (type.isParameterizedType()) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			List<Type> typeArgs = parameterizedType.typeArguments();

			for (int i = 0; i < typeArgs.size(); i++)
				setTypeList(typeArgs.get(i));

		} else if (type.isArrayType()) {
			Type arrayType = ((ArrayType) type).getElementType();
			setArrayType(arrayType);
		}
	}

	void setTypeList(Type newType) {
		if (newType.isAnnotatable())
			typeList.add(newType);
		else {
			specifyTypes(newType);
		}
	}

	public List<Type> getTypeList() {
		return typeList;
	}

	void setArrayType(Type type) {
		arrayType = type;
	}

	public Type getArrayType() {
		return arrayType;
	}

	public SM_Type getVariableType() {
		return variableType;
	}

	public void resolveVariableType(SM_Project parentProject) {
		specifyTypes(type);

		if (isParameterized) {
			for (Type typeOfVar : getTypeList()) {
				ITypeBinding itype = typeOfVar.resolveBinding();
				if (itype.isFromSource()) {
					SM_Type inferredType = findType(parentProject, itype.getName(), itype.getPackage().getName());
					variableType = inferredType;
				} else {
					isPrimitive = true;
					primitiveVariableType = itype.getName();
				}
			}
		} else if (isArray) {
			ITypeBinding itype = getArrayType().resolveBinding();
			if (itype.isFromSource()) {
				SM_Type inferredType = findType(parentProject, itype.getName(), itype.getPackage().getName());
				variableType = inferredType;
			} else {
				isPrimitive = true;
				primitiveVariableType = itype.getName();
			}
		} else if (!isPrimitive) {
			ITypeBinding itype = type.resolveBinding();
			if (itype.isFromSource()) {
				SM_Type inferredType = findType(parentProject, itype.getName(), itype.getPackage().getName());
				variableType = inferredType;
			} else {
				isPrimitive = true;
				primitiveVariableType = itype.getName();
			}
		} else if (isPrimitive) {
			ITypeBinding itype = type.resolveBinding();
			primitiveVariableType = itype.getName();
		}
	}

	/*
	 * public SM_Project getParentProject() { return parentProject; }
	 */
	public String getVarPrimitiveType() {
		return primitiveVariableType;
	}
}
