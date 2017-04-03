package Designite.SourceModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.Type;

public abstract class SM_Variable extends SM_SourceItem {

	protected SM_Project parentProject;
	protected SM_Type refType;
	
	protected Type type;
	protected Type arrayType;
	protected List<Type> typeList = new ArrayList<>();
	
	protected boolean isPrimitive = false;
	protected boolean isParameterized = false;
	protected boolean isArray = false;
	
	public void setName(String name) {
		this.name = name;
	}
	
	void setType(Type type) {
		this.type = type;
		setCategoryOfType();
	}
	
	public Type getType() {
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
			
			for (int i= 0; i < typeArgs.size(); i++) 
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
	
	void setReferringType(SM_Type type) {
		refType = type;
	}
	
	//It might be Enum
	public SM_Type getRefType() {
		return refType;
	}
	
	public void findRefObject() {
		specifyTypes(getType());
		
		if (isParameterized) {
			for (Type typeOfVar: getTypeList()) {
				ITypeBinding itype = typeOfVar.resolveBinding();
				for (SM_Type type: getTypesOfProject(parentProject)) {
					if (type.getName().equals(itype.getName())) 
						setReferringType(type);
				}
			}
		} else if (isArray) {
			ITypeBinding itype = getArrayType().resolveBinding();
			for (SM_Type type: getTypesOfProject(parentProject)) {
				if (type.getName().equals(itype.getName())) 
					setReferringType(type);
			}
		} else if (!isPrimitive) {
			ITypeBinding itype = getType().resolveBinding();
			for (SM_Type type: getTypesOfProject(parentProject)) {
				if (type.getName().equals(itype.getName())) 
					setReferringType(type);
			}
		} else if (isPrimitive) {
			ITypeBinding itype = getType().resolveBinding();
			for (SM_Type type: getTypesOfProject(parentProject)) {
				if (type.getName().equals(itype.getName())) 
					setReferringType(type);
			}
		} 
	}
	
	public SM_Project getParentProject() {
		return parentProject;
	}
	
	public abstract void print();
	
}
