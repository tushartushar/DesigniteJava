package Designite.SourceModel;

public abstract class SM_EntitiesWithType extends SM_SourceItem {
	
	protected TypeInfo typeinfo;

	public boolean isPrimitiveType() {
		return typeinfo.isPrimitiveType();
	}

	public SM_Type getType() {
		return typeinfo.getTypeObj();
	}
	
	public String getPrimitiveType() {
		return typeinfo.getObjType();
	}
	
	@Override
	public void parse() {
		
	}
}
