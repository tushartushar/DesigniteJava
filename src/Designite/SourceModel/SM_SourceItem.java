package Designite.SourceModel;

import java.lang.reflect.Modifier;

public abstract class SM_SourceItem {
	protected String name;
	protected AccessStates accessModifier;
	//protected SM_SourceItem getParentInfo();
	
	//This method prints the whole source code model
	public abstract void print();
	
	public String getName() {
		return name;
	}
	
	public AccessStates getAccessModifier() {
		return accessModifier;
	}
	
	public enum AccessStates {
		PUBLIC,
		PROTECTED,
		DEFAULT,
		PRIVATE
	}
	
	//TODO check default case
	void setAccessModifier(int modifier) {
		if (Modifier.isPublic(modifier))
			accessModifier = AccessStates.PUBLIC;
		else if (Modifier.isProtected(modifier))
			accessModifier = AccessStates.PROTECTED;
		else if (Modifier.isPrivate(modifier))
			accessModifier = AccessStates.PRIVATE;
		else
			accessModifier = AccessStates.DEFAULT;
	}
	
//	abstract SM_SourceItem getParent();
	
}
