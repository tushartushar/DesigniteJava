package Designite.SourceModel;

import java.util.ArrayList;
import java.util.List;

public class TypeInfo {

	private SM_Type typeObj;
	private boolean primitiveType;
	private String objPrimitiveType;
	private boolean parametrizedType;
	private List<SM_Type> nonPrimitiveTypeParameters = new ArrayList<>();

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

	public String getObjPrimitiveType() {
		return objPrimitiveType;
	}

	public void setObjPrimitiveType(String objType) {
		this.objPrimitiveType = objType;
	}

	public boolean isParametrizedType() {
		return parametrizedType;
	}

	public void setParametrizedType(boolean parametrizedType) {
		this.parametrizedType = parametrizedType;
	}
	
	public List<SM_Type> getNonPrimitiveTypeParameters() {
		return nonPrimitiveTypeParameters;
	}
	
	public String getStringOfNonPrimitiveParameters() {
		String output = "[";
		for (SM_Type type : nonPrimitiveTypeParameters) {
			output += type.getName() + ", "; 
		}
		return removeLastComma(output) + "]";
	}
	
	private String removeLastComma(String str) {
		return (str.length() > 2) ? str.substring(0, str.length() - 2) : str;
	}
	 
	public int getNumOfNonPrimitiveParameters() {
		return getNonPrimitiveTypeParameters().size();
	}
	
	public void addNonPrimitiveTypeParameter(SM_Type element) {
		nonPrimitiveTypeParameters.add(element);
	}

	@Override
	public String toString() {
		return "TypeInfo [typeObj=" + typeObj + ", primitiveType=" + primitiveType + ", objPrimitiveType=" + objPrimitiveType
						+ ", parametrizedType=" + parametrizedType + ", nonPrimitiveTypeParameters="
						+ getStringOfNonPrimitiveParameters() + "]";
	}

}
