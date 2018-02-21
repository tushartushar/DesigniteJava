package Designite.SourceModel;

import java.util.List;

public abstract class SM_EntitiesWithType extends SM_SourceItem {
	
	protected TypeInfo typeInfo;

	public boolean isPrimitiveType() {
		return typeInfo.isPrimitiveType();
	}

	public SM_Type getType() {
		return typeInfo.getTypeObj();
	}	
	
	public String getPrimitiveType() {
		return typeInfo.getObjPrimitiveType();
	}
	
	public boolean isParametrizedType() {
		return typeInfo.isParametrizedType();
	}
	
	public List<SM_Type> getNonPrimitiveTypeParameters() {
		return typeInfo.getNonPrimitiveTypeParameters();
	}
	
	public String getTypeOverallToString() {
		if (isPrimitiveType()) {
			return getPrimitiveType();
		} else {
			return getType().getName();
		}
	}
	
	@Override
	public void parse() {
		
	}
}
