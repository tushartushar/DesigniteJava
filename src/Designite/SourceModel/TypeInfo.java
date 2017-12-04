package Designite.SourceModel;

import java.util.ArrayList;
import java.util.List;

public class TypeInfo {

	private SM_Type typeObj;
	private boolean primitiveType;
	private String objType;
	private boolean parametrizedType;
	private List<String> nonPrimitiveTypeParameters = new ArrayList<>();
	
	public SM_Type getTypeObj() {
		return typeObj;
	}
	
	public void setTypeObj(SM_Type typeObj) {
		this.typeObj = typeObj;
	}
	
	public boolean isPrimitiveType() {
		return primitiveType;
	}
	
	public void setPrimitiveType(boolean primitiveType) {
		this.primitiveType = primitiveType;
	}

	public String getObjType() {
		return objType;
	}

	public void setObjType(String objType) {
		this.objType = objType;
	}

	public boolean isParametrizedType() {
		return parametrizedType;
	}

	public void setParametrizedType(boolean parametrizedType) {
		this.parametrizedType = parametrizedType;
	}
	
	public List<String> getNonPrimitiveTypeParameters() {
		return nonPrimitiveTypeParameters;
	}
	
	public void addNonPrimitiveTypeParameter(String element) {
		nonPrimitiveTypeParameters.add(element);
	}

	@Override
	public String toString() {
		return "TypeInfo [typeObj=" + typeObj + ", primitiveType=" + primitiveType + ", objType=" + objType
				+ ", parametrizedType=" + parametrizedType + ", nonPrimitiveTypeParameters="
				+ nonPrimitiveTypeParameters + "]";
	}

}
