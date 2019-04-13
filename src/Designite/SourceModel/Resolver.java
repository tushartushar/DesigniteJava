package Designite.SourceModel;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;

import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.ImportDeclaration;

class Resolver {
	private List<Type> typeList = new ArrayList<>();
	private boolean isParameterized = false;
	private boolean isArray = false;
	private Type arrayType;

	// FIXME : Duplicated code in the method
	public List<SM_Type> inferStaticAccess(List<Name> staticFieldAccesses, SM_Type type) {
		List<SM_Type> typesOfStaticAccesses = new ArrayList<>();
		for (Name typeName : staticFieldAccesses) {
			if (typeName.resolveBinding() instanceof ITypeBinding) {
				ITypeBinding iType = (ITypeBinding) typeName.resolveBinding();

				if (iType != null && iType.getPackage() != null) {
					SM_Package sm_pkg = findPackage(iType.getPackage().getName().toString(),
							type.getParentPkg().getParentProject());

					if (sm_pkg != null) {
						SM_Type sm_type = findType(iType.getName().toString(), sm_pkg);
						if (sm_type != null) {
							if (!typesOfStaticAccesses.contains(sm_type)) {
								typesOfStaticAccesses.add(sm_type);
							}
						}
					}
				}
			} else {
				String unresolvedTypeName = typeName.toString().replace("[]", ""); // cover the Array case
				SM_Type matchedType = manualLookupForUnresolvedType(type.getParentPkg().getParentProject(),
						unresolvedTypeName, type);
				if (matchedType != null) {
					if (!typesOfStaticAccesses.contains(matchedType)) {
						typesOfStaticAccesses.add(matchedType);
					}
				}
			}
		}

		return typesOfStaticAccesses;
	}

	public List<SM_Method> inferCalledMethods(List<MethodInvocation> calledMethods, SM_Type parentType) {
		List<SM_Method> calledMethodsList = new ArrayList<>();
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
			/*
			 * Manual resolving of methods that failed to automatically resolve
			 */
			else {
				Expression exp = method.getExpression();
				// System.out.println("## Uresolved method :: " + method.toString());
				if (exp != null) {
					String typeName = exp.toString();
					SM_Type matchedType = manualLookupForUnresolvedType(parentType.getParentPkg().getParentProject(),
							typeName, parentType);
					if (matchedType != null) {
						parentType.addStaticMethodInvocation(matchedType);
					}
					// }
				}
				// scan (only the simple cases) the method parameters.
				List<Expression> arguments = new ArrayList<Expression>(method.arguments());
				ListIterator<Expression> itr = arguments.listIterator();

				while (itr.hasNext()) {
					Expression temp = null;
					exp = itr.next();
					String typeName = exp.toString();
					SM_Type matchedType = manualLookupForUnresolvedType(parentType.getParentPkg().getParentProject(),
							typeName, parentType);
					if (matchedType != null) {
						parentType.addStaticMethodInvocation(matchedType);
					}
					if (exp instanceof MethodInvocation) {
						temp = ((MethodInvocation) exp).getExpression();
						// add all arguments to the list
						addExpressionArguments(((MethodInvocation) exp).arguments(), itr);
						if (temp != null) {
							itr.add(temp);
						}
					} else if (exp instanceof ClassInstanceCreation) {
						// Type type = ((ClassInstanceCreation)exp).getType();
						// add all arguments to the list
						addExpressionArguments(((ClassInstanceCreation) exp).arguments(), itr);
					}
					// FIXME : Cover more cases in the future
				}

			}
		}
		return calledMethodsList;
	}

	public void addExpressionArguments(List<Expression> newArgumentList,
			ListIterator<Expression> existingArgumentList) {
		for (Expression newArgument : newArgumentList)
			existingArgumentList.add(newArgument);
	}

	private SM_Package findPackage(String packageName, SM_Project project) {
		for (SM_Package sm_pkg : project.getPackageList()) {
			if (sm_pkg.getName().equals(packageName)) {
				return sm_pkg;
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
					if (parameterCount == 0) {
						return sm_method;
					}
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

	public SM_Type resolveType(Type type, SM_Project project) {
		ITypeBinding binding = type.resolveBinding();
		if (binding == null || binding.getPackage() == null) // instanceof String[] returns null package
			return null;
		SM_Package pkg = findPackage(binding.getPackage().getName(), project);
		if (pkg != null) {
			return findType(binding.getName(), pkg);
		}
		return null;
	}

	public TypeInfo resolveVariableType(Type typeNode, SM_Project parentProject, SM_Type callerType) {
		TypeInfo typeInfo = new TypeInfo();
		specifyTypes(typeNode);

		if (isParameterized) {
			for (Type typeOfVar : getTypeList()) {
				inferTypeInfo(parentProject, typeInfo, typeOfVar, callerType);
			}
		} else if (isArray) {
			inferTypeInfo(parentProject, typeInfo, getArrayType(), callerType);
		} else {
			inferTypeInfo(parentProject, typeInfo, typeNode, callerType);
		}
		return typeInfo;
	}

	private void inferTypeInfo(SM_Project parentProject, TypeInfo typeInfo, Type typeOfVar, SM_Type callerType) {
		ITypeBinding iType = typeOfVar.resolveBinding();
		/*
		 * In some cases, the above statement doesnt resolve the binding even if the
		 * type is present in the same project (and we dont know the reason). We need to
		 * handle this situation explicitly by checking the 'binding' property of iType.
		 * if it is of type MissingTypeBinding, we need to search the type in the
		 * present project. We may have to use import statements to identify the package
		 * in which this (to be resolved) type exists.
		 */

		// The case that the iType is RecoveredTypeBinding which leads to
		// ProblemReferenceBinding and consequently to MissingTypeBinding
		if (iType == null) {
			inferPrimitiveType(parentProject, typeInfo, iType);
			infereParametrized(parentProject, typeInfo, iType);
		} else if (iType.isRecovered()) {
			// Search in the ast explicitly and assign
			String unresolvedTypeName = typeOfVar.toString().replace("[]", ""); // cover the Array case
			SM_Type matchedType = manualLookupForUnresolvedType(parentProject, unresolvedTypeName, callerType);
			if (matchedType != null) {
				manualInferUnresolvedTypeType(typeInfo, matchedType);
			}
		} else {
			inferPrimitiveType(parentProject, typeInfo, iType);
			infereParametrized(parentProject, typeInfo, iType);
		}
	}

	private SM_Type manualLookupForUnresolvedType(SM_Project parentProject, String unresolvedTypeName,
			SM_Type callerType) {
		SM_Type matchedType = null;

		int numberOfDots = new StringTokenizer(" " + unresolvedTypeName + " ", ".").countTokens() - 1;

		// Case of static call Type.field
		if (numberOfDots == 1) {
			unresolvedTypeName = unresolvedTypeName.substring(0, unresolvedTypeName.indexOf("."));
		}
		// Case of static call with full class name :: package.package.Type.field
		else if (numberOfDots > 1) {
			String packageName = getPackageName(unresolvedTypeName);
			String typeName = getTypeName(unresolvedTypeName);
			matchedType = findType(typeName, packageName, parentProject);
			if (matchedType != null) {
				return matchedType;
			}
		}

		if ((matchedType = findType(unresolvedTypeName, callerType.getParentPkg())) != null) {
			return matchedType;
		}
		// TODO break it to different lookups for * and simple.
		else {
			List<ImportDeclaration> importList = callerType.getImportList();
			for (ImportDeclaration importEntry : importList) {
				matchedType = findType(unresolvedTypeName, getPackageName(importEntry.getName().toString()),
						parentProject);
				if (matchedType != null) {
					return matchedType;
				}
			}
		}
		return null;
	}

	private String getTypeName(String fullTypePath) {
		return fullTypePath.substring(fullTypePath.lastIndexOf(".") + 1);
	}

	private String getPackageName(String fullTypePath) {
		int index = fullTypePath.lastIndexOf('.');
		if (index >= 0)
			return fullTypePath.substring(0, fullTypePath.lastIndexOf('.'));
		else
			return "default";
	}

	private void manualInferUnresolvedTypeType(TypeInfo typeInfo, SM_Type type) {
		typeInfo.setTypeObj(type);
		typeInfo.setPrimitiveType(false);
	}

	private void inferPrimitiveType(SM_Project parentProject, TypeInfo typeInfo, ITypeBinding iType) {
		if (iType != null && iType.isFromSource() && iType.getModifiers() != 0 && !iType.isWildcardType()) {
			SM_Type inferredType = findType(iType.getName(), iType.getPackage().getName(), parentProject);
			if (inferredType != null) {
				typeInfo.setTypeObj(inferredType);
				typeInfo.setPrimitiveType(false);
			} else {
				typeInfo.setObjPrimitiveType(iType.getName());
				typeInfo.setPrimitiveType(true);
			}
		} else {
			if (iType == null)
				typeInfo.setObjPrimitiveType("wildcard");
			else
				typeInfo.setObjPrimitiveType(iType.getName());
			typeInfo.setPrimitiveType(true);
		}
	}

	private void infereParametrized(SM_Project parentProject, TypeInfo typeInfo, ITypeBinding iType) {
		if (iType != null && iType.isParameterizedType()) {
			typeInfo.setParametrizedType(true);
			addNonPrimitiveParameters(parentProject, typeInfo, iType);
			if (hasNonPrimitivePArameters(typeInfo)) {
				typeInfo.setPrimitiveType(false);
			}
		}
	}

	private void addNonPrimitiveParameters(SM_Project parentProject, TypeInfo typeInfo, ITypeBinding iType) {
		if (iType.isFromSource() && iType.getModifiers() != 0) {
			SM_Type inferredBasicType = findType(iType.getName(), iType.getPackage().getName(), parentProject);
			addParameterIfNotAlreadyExists(typeInfo, inferredBasicType);
		}
		for (ITypeBinding typeParameter : iType.getTypeArguments()) {
			if (typeParameter.isParameterizedType()) {
				addNonPrimitiveParameters(parentProject, typeInfo, typeParameter);
			} else {
				if (typeParameter.isFromSource() && typeParameter.getModifiers() != 0) {
					SM_Type inferredType = findType(typeParameter.getName(), typeParameter.getPackage().getName(),
							parentProject);
					if (inferredType != null) {
						addParameterIfNotAlreadyExists(typeInfo, inferredType);
					}
				}
			}
		}
	}

	private void addParameterIfNotAlreadyExists(TypeInfo typeInfo, SM_Type inferredType) {
		if (!typeInfo.getNonPrimitiveTypeParameters().contains(inferredType)) {
			typeInfo.addNonPrimitiveTypeParameter(inferredType);
		}
	}

	private boolean hasNonPrimitivePArameters(TypeInfo typeInfo) {
		return typeInfo.getNumOfNonPrimitiveParameters() > 0;
	}

	private SM_Type findType(String typeName, String packageName, SM_Project project) {
		SM_Package pkg = findPackage(packageName, project);
		if (pkg != null) {
			return findType(typeName, pkg);
		}
		return null;
	}

	private SM_Type findType(String className, SM_Package pkg) {
		for (SM_Type sm_type : pkg.getTypeList()) {
			if (sm_type.getName().equals(trimParametersIfExist(className))) {
				return sm_type;
			}
		}

		return null;
	}

	private String trimParametersIfExist(String objName) {
		int index = objName.indexOf('<');
		if (index >= 0) {
			return objName.substring(0, index);
		}
		return objName;
	}

	private void specifyTypes(Type type) {
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

	private void setTypeList(Type newType) {
		if (newType.isAnnotatable())
			typeList.add(newType);
		else {
			specifyTypes(newType);
		}
	}

	private List<Type> getTypeList() {
		return typeList;
	}

	private Type getArrayType() {
		return arrayType;
	}

	private void setArrayType(Type type) {
		arrayType = type;
	}
}
