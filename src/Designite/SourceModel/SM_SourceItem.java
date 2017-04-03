package Designite.SourceModel;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

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
	
	List<SM_Package> getPkgOfProject(SM_Project project) {
		List<SM_Package> pkgList = new ArrayList<>();
		pkgList.addAll(project.getPackageList());
		
		return pkgList; 
	}
	
	List<SM_Type> getTypesOfProject(SM_Project project) {
		List<SM_Package> pkgList = new ArrayList<>();
		List<SM_Type> typeList = new ArrayList<>();
		
		pkgList.addAll(project.getPackageList());
		for (SM_Package pkg: pkgList)
			typeList.addAll(pkg.getTypeList());
		
		return typeList; 
	}
	
	List<SM_Method> getMethodsOfProject(SM_Project project) {
		List<SM_Type> typeList = new ArrayList<>();
		List<SM_Method> methodList = new ArrayList<>();
		
		typeList.addAll(getTypesOfProject(project));
		for (SM_Type type: typeList)
			methodList.addAll(type.getMethodList());
		
		return methodList; 
	}
	
	List<SM_Method> getMethodsOfPkg(SM_Package pkg) {
		List<SM_Type> typeList = new ArrayList<>();
		List<SM_Method> methodList = new ArrayList<>();
		
		typeList.addAll(pkg.getTypeList());
		for (SM_Type type: typeList)
			methodList.addAll(type.getMethodList());
		
		return methodList; 
	}
	
}
