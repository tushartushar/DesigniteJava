package Designite.SourceModel;

import java.util.List;

public abstract class SM_EntitiesWithType extends SM_SourceItem {
	
	protected TypeInfo typeInfo;

	public boolean isPrimitiveType() {
		return typeInfo.isPrimitiveType();
	}
	
	public SM_Type getParentType() {
		// Always returns null.
		// Should be overridden by subclasses
		return null;
	}
	
//	public boolean isTypeVariable() {
//		return typeInfo.isTypeVariable();
//	}

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
			return getType() != null 
					? getType().getName() 
					: "UnresolvedType"; // in case of unresolved types
		}
//		if (typeInfo != null) {
//			if (isPrimitiveType()) {
//				return getPrimitiveType();
//			} else {
//				return getType().getName();
//			}
//		} else
//			return "generic";
	}
	
	@Override
	public void parse() {
		
	}
}
