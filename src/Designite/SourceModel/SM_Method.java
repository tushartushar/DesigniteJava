package Designite.SourceModel;

import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class SM_Method extends SM_SourceItem {
	private boolean abstractMethod = false;
	private boolean finalMethod = false;
	private boolean staticMethod = false;
	private boolean isConstructor = false;
	private SM_Type parentType;

	private MethodDeclaration methodDeclaration;
	private IMethodBinding methodBinding;

	private List<SM_Method> calledMethodsList = new ArrayList<SM_Method>();
	private List<SM_Parameter> parameterList = new ArrayList<SM_Parameter>();
	private List<SM_LocalVar> localVarList = new ArrayList<SM_LocalVar>();
	private List<MethodInvocation> calledMethods = new ArrayList<MethodInvocation>();

	public SM_Method(MethodDeclaration methodDeclaration, SM_Type typeObj) {
		name = methodDeclaration.getName().toString();
		this.parentType = typeObj;
		this.methodDeclaration = methodDeclaration;
		setIMethod(methodDeclaration);
		setMethodInfo(methodDeclaration);
		setAccessModifier(methodDeclaration.getModifiers());
	}

	/*
	 * public SimpleName getSimpleName() { return methodDeclaration.getName(); }
	 */

	/*
	 * public Block getMethodBody() { return methodBody; }
	 */

	void setIMethod(MethodDeclaration methodDeclaration) {
		this.methodBinding = methodDeclaration.resolveBinding();
	}

	/*
	 * public IMethodBinding getIMethod() { return IMethod; }
	 */

	public void setMethodInfo(MethodDeclaration method) {
		int modifiers = method.getModifiers();
		if (Modifier.isAbstract(modifiers))
			abstractMethod = true;
		if (Modifier.isFinal(modifiers))
			finalMethod = true;
		if (Modifier.isStatic(modifiers))
			staticMethod = true;
		if (method.isConstructor())
			isConstructor = true;
	}

	public boolean isAbstract() {
		return this.abstractMethod;
	}

	public boolean isStatic() {
		return this.staticMethod;
	}

	public boolean isFinal() {
		return this.finalMethod;
	}

	public boolean isConstructor() {
		return this.isConstructor;
	}

	public SM_Type getParentType() {
		return parentType;
	}

	/*
	 * public SM_Package getParentPkg() { return parentPkg; }
	 * 
	 * public SM_Project getParentProject() { return parentProject; }
	 */

	public List<SM_Parameter> getParameterList() {
		return parameterList;
	}

	public List<SM_LocalVar> getLocalVarList() {
		return localVarList;
	}

	// TODO: should return a list of SM_Method
	public List<MethodInvocation> getCalledMethods() {
		return calledMethods;
	}

	private void parseParameters() {
		for (SM_Parameter param : parameterList) {
			param.parse();
		}
	}

	private void parseLocalVar() {
		for (SM_LocalVar var : localVarList) {
			var.parse();
		}
	}

	public void inferCalledMethods() {
		for (MethodInvocation method : calledMethods) {
			IMethodBinding imethod = method.resolveMethodBinding();

			// binding is resolved without returning null
			if (imethod != null) {
				SM_Package sm_pkg = findPkg(imethod, parentType.getParentPkg().getParentProject());
				if (sm_pkg != null) {
					SM_Type sm_type = findType(imethod, sm_pkg);
					if (sm_type != null) {
						SM_Method sm_method = findMethod(imethod, sm_type);
						if (sm_method != null)
							calledMethodsList.add(sm_method);
					}
				}
				// binding is not resolved
			} /*
				 * else { if (method.getExpression() != null) { String
				 * objectName = method.getExpression().toString(); for
				 * (SM_Variable var: parentType.getVariableList()) { if
				 * (var.getName().equals(objectName)){ SM_Type typeToCheck =
				 * var.getRefType(); if (typeToCheck != null) { SM_Method
				 * sm_method = findMethod(method, typeToCheck);
				 * calledMethodsList.add(sm_method); } } } }
				 * 
				 * /* List<SM_Package> tempPkg = new ArrayList<>();
				 * List<ImportDeclaration> importList =
				 * parentType.getImportList(); for (SM_Package sm_package:
				 * getPkgOfProject(parentProject)){ for (ImportDeclaration imp:
				 * importList) { if
				 * (imp.getName().toString().contains(sm_package.getName())) {
				 * if (!(tempPkg.contains(sm_package))) {
				 * tempPkg.add(sm_package); } } } }
				 * if(!(tempPkg.contains(parentPkg))) tempPkg.add(parentPkg);* }
				 */

		}
	}

	private SM_Package findPkg(IMethodBinding method, SM_Project project) {
		ITypeBinding type = method.getDeclaringClass();

		if (type != null) {
			String pkgName = method.getDeclaringClass().getPackage().getName().toString();
			for (SM_Package sm_pkg : getPkgOfProject(project)) {
				if (sm_pkg.getName().equals(pkgName)) {
					return sm_pkg;
				}
			}
		}

		return null;
	}

	private SM_Type findType(IMethodBinding method, SM_Package pkg) {
		String className = method.getDeclaringClass().getName().toString();
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

	private SM_Method findMethod(MethodInvocation method, SM_Type type) {
		int parameterCount = method.arguments().size();

		for (SM_Method sm_method : type.getMethodList()) {
			if (method.getName().toString().equals(sm_method.getName())) {
				if (sm_method.getParameterList().size() == parameterCount) {
					if (parameterCount == 0) {
						return sm_method;
					} else {
						for (int i = 0; i < parameterCount; i++) {
							// not implemented yet
						}
					}
				}
			}
		}

		return null;
	}

	@Override
	public void printDebugLog(PrintWriter writer) {
		print(writer, "Method: " + name);
		print(writer, "	Parent type: " + this.getParentType().getName());
		print(writer, "	Constructor: " + isConstructor);
		print(writer, "	Returns: " + methodDeclaration.getReturnType2());
		print(writer, "	Access: " + accessModifier);
		print(writer, "	Abstract: " + abstractMethod);
		print(writer, "	Final: " + finalMethod);
		print(writer, "	Static: " + staticMethod);
		print(writer, "	Called methods: " + getCalledMethods());
		print(writer, "	Binding: " + methodBinding.getMethodDeclaration());
		for (SM_Parameter param : parameterList)
			param.printDebugLog(writer);
		for (SM_LocalVar var : localVarList)
			var.printDebugLog(writer);

		for (SM_Method calledMethod : calledMethodsList)
			System.out.println("	Called method: " + calledMethod.getName());

	}

	@Override
	public void parse() {
		MethodInvVisitor invVisitor = new MethodInvVisitor(methodDeclaration);
		methodDeclaration.accept(invVisitor);
		List<MethodInvocation> invList = invVisitor.getCalledMethods();
		if (invList.size() > 0)
			calledMethods.addAll(invList);

		List<SingleVariableDeclaration> variableList = methodDeclaration.parameters();
		for (SingleVariableDeclaration var : variableList) {
			VariableVisitor parameterVisitor = new VariableVisitor(this);
			// methodDeclaration.accept(parameterVisitor);
			var.accept(parameterVisitor);
			List<SM_Parameter> pList = parameterVisitor.getParameterList();
			if (pList.size() > 0)
				parameterList.addAll(pList);
			parseParameters();
		}

		LocalVarVisitor localVarVisitor = new LocalVarVisitor(this);
		methodDeclaration.accept(localVarVisitor);
		List<SM_LocalVar> lList = localVarVisitor.getLocalVarList();
		if (lList.size() > 0)
			localVarList.addAll(lList);
		parseLocalVar();

	}

	@Override
	public void resolve() {
		for (SM_Parameter param : parameterList)
			param.resolve();
		for (SM_LocalVar localVar : localVarList)
			localVar.resolve();
		inferCalledMethods();
		//setReferencedTypes();
	}
}
