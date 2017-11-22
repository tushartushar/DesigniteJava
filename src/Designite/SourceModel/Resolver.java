package Designite.SourceModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.Type;

class Resolver {
	private List<Type> typeList = new ArrayList<>();
	private boolean isParameterized = false;
	private boolean isArray = false;
	private Type arrayType;
	
	public List<SM_Method> inferCalledMethods(List<MethodInvocation> calledMethods, SM_Type parentType) {
		List<SM_Method> calledMethodsList = new ArrayList<SM_Method>();
		for (MethodInvocation method : calledMethods) {
			IMethodBinding imethod = method.resolveMethodBinding();

			// binding is resolved without returning null
			if (imethod != null) {
				SM_Package sm_pkg = findPackage(imethod.getDeclaringClass().getPackage().getName().toString(),
						parentType.getParentPkg().getParentProject());
				if (sm_pkg != null) {
					SM_Type sm_type = findType(imethod.getDeclaringClass().getName().toString(), sm_pkg);
					if (sm_type != null) {
						SM_Method sm_method = findMethod(imethod, sm_type);
						if (sm_method != null)
							calledMethodsList.add(sm_method);
					}
				}
			}
		}
		return calledMethodsList;
	}

	private SM_Package findPackage(String packageName, SM_Project project) {
		for (SM_Package sm_pkg : project.getPackageList()) {
			if (sm_pkg.getName().equals(packageName)) {
				return sm_pkg;
			}
		}

		return null;
	}

	private SM_Type findType(String className, SM_Package pkg) {
		for (SM_Type sm_type : pkg.getTypeList()) {
			if (sm_type.getName().equals(className)) {
				return sm_type;
			}
		}

		return null;
	}

	private SM_Method findMethod(IMethodBinding method, SM_Type type) {
		String methodName = method.getName().toString();
		int parameterCount = method.getParameterTypes().length;
		boolean sameParameters = true;

		for (SM_Method sm_method : type.getMethodList()) {
			if (sm_method.getName().equals(methodName)) {
				if (sm_method.getParameterList().size() == parameterCount) {
					for (int i = 0; i < parameterCount; i++) {
						ITypeBinding parameterType = method.getParameterTypes()[i];
						Type typeToCheck = sm_method.getParameterList().get(i).getTypeBinding();
						if (!(parameterType.getName().contentEquals(typeToCheck.toString()))) {
							sameParameters = false;
							break;
						} else if (i == parameterCount - 1) {
							if (sameParameters)
								return sm_method;
						}
					}
				}
			}
		}

		return null;
	}

	SM_Type resolveType(Type type, SM_Project project) {
		ITypeBinding binding = type.resolveBinding();
		if (binding == null)
			return null;
		SM_Package pkg = findPackage(binding.getPackage().getName(),project);
		if (pkg !=null)
		{
			return findType (binding.getName(), pkg);
		}
		return null;
	}
	
	public TypeInfo resolveVariableType(Type typeNode, SM_Project parentProject) {
		TypeInfo typeinfo = new TypeInfo();
		specifyTypes(typeNode);

		if (isParameterized) {
			for (Type typeOfVar : getTypeList()) {
				inferTypeInfo(parentProject, typeinfo, typeOfVar);
			}
		} else if (isArray) {
			inferTypeInfo(parentProject, typeinfo, getArrayType());
		} else {
			inferTypeInfo(parentProject, typeinfo, typeNode);
		}
		return typeinfo;
	}

	private void inferTypeInfo(SM_Project parentProject, TypeInfo typeinfo, Type typeOfVar) {
		ITypeBinding itype = typeOfVar.resolveBinding();
String qualified = typeOfVar.resolveBinding().getQualifiedName();
		if (itype.isFromSource()) {
			SM_Type inferredType = findType(itype.getName(), itype.getPackage().getName(), parentProject);
			if(inferredType!=null)
			{
				typeinfo.TypeObj = inferredType;
				typeinfo.IsPrimitiveType = false;
			}
			else
			{
				typeinfo.PrimitiveType = itype.getName();
				typeinfo.IsPrimitiveType = true;
			}
		} else {
			typeinfo.PrimitiveType = itype.getName();
			typeinfo.IsPrimitiveType = true;
		}
	}
	
	private SM_Type findType(String typeName, String packageName, SM_Project project) {
		SM_Package pkg = findPackage(packageName, project);
		if (pkg !=null)
		{
			return findType (typeName, pkg);
		}
		return null;
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
	List<Type> getTypeList() {
		return typeList;
	}
	Type getArrayType() {
		return arrayType;
	}
	void setArrayType(Type type) {
		arrayType = type;
	}
}
