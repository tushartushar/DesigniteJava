package Designite.SourceModel;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.io.PrintWriter;

public abstract class SM_SourceItem {
	protected String name;
	protected AccessStates accessModifier;
	// protected SM_SourceItem getParentInfo();

	/**
	 * This method prints the whole source code model For debugging purposes
	 * only
	 */
	public abstract void printDebugLog(PrintWriter writer);

	/**
	 * This is the first pass of parsing a source code entity.
	 */
	public abstract void parse();

	/**
	 * This method establishes relationships among source-code entities. Such
	 * relationships include variable types, super/sub types, etc.
	 */
	public abstract void resolve();

	public String getName() {
		return name;
	}

	public AccessStates getAccessModifier() {
		return accessModifier;
	}

	// TODO check default case
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

	protected SM_Type findType(SM_Project parentProject, String typeName, String pkgName) {
		for (SM_Package pkg:parentProject.getPackageList())
			if (pkg.getName().equals(pkgName))
			{
				for(SM_Type type:pkg.getTypeList())
					if(type.getName().equals(typeName))
						return type;
			}
		return null;
	}
	List<SM_Type> getTypesOfProject(SM_Project project) {
		List<SM_Package> pkgList = new ArrayList<>();
		List<SM_Type> typeList = new ArrayList<>();

		pkgList.addAll(project.getPackageList());
		for (SM_Package pkg : pkgList)
			typeList.addAll(pkg.getTypeList());

		return typeList;
	}

	List<SM_Method> getMethodsOfProject(SM_Project project) {
		List<SM_Type> typeList = new ArrayList<>();
		List<SM_Method> methodList = new ArrayList<>();

		typeList.addAll(getTypesOfProject(project));
		for (SM_Type type : typeList)
			methodList.addAll(type.getMethodList());

		return methodList;
	}

	List<SM_Method> getMethodsOfPkg(SM_Package pkg) {
		List<SM_Type> typeList = new ArrayList<>();
		List<SM_Method> methodList = new ArrayList<>();

		typeList.addAll(pkg.getTypeList());
		for (SM_Type type : typeList)
			methodList.addAll(type.getMethodList());

		return methodList;
	}

	void print(PrintWriter writer, String str) {
		if (writer != null)
		{
			writer.println(str);
			writer.flush();
		}
		else
			System.out.println(str);
	}
	
	protected String convertListToString(List<SM_Type> typeList) {
		String result = "";
		for (SM_Type type : typeList)
			if(result.equals(""))
				result = type.getName();
			else
				result += ", " + type.getName();
		return null;
	}
}
