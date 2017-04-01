package Designite.SourceModel;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class SM_Method extends SM_SourceItem {
	private boolean abstractMethod = false;
	private boolean finalMethod = false;
	private boolean staticMethod = false;
	private boolean isConstructor = false;
	private SM_Type parentType;
	private SM_Package parentPkg;
	private SM_Project parentProject;
	private Block methodBody;
	
	private TypeDeclaration typeDeclaration;
	private MethodDeclaration methodDeclaration;
	private IMethodBinding IMethod;
	
	private List<SM_Parameter> parameterList = new ArrayList<SM_Parameter>();
	private List<SM_LocalVar> localVarList = new ArrayList<SM_LocalVar>();
	private List<MethodInvocation> calledMethods = new ArrayList<MethodInvocation>();
	
	public SM_Method(MethodDeclaration methodDeclaration, TypeDeclaration typeDeclaration) {
		name = methodDeclaration.getName().toString();
		this.typeDeclaration = typeDeclaration; 
		this.methodDeclaration = methodDeclaration;
		setIMethod(methodDeclaration);
		setMethodInfo(methodDeclaration);
		setAccessModifier(methodDeclaration.getModifiers());
		setMethodBody();
	}
	
	void setMethodBody() {
		methodBody = methodDeclaration.getBody();
	}
	
	public Block getMethodBody() {
		return methodBody;
	}
	
	void setIMethod(MethodDeclaration methodDeclaration) {
		this.IMethod = methodDeclaration.resolveBinding();
	}
	
	public IMethodBinding getIMethod() {
		return IMethod;
	}
	
	public void setMethodInfo(MethodDeclaration method){
		int modifiers = method.getModifiers();
		if (Modifier.isAbstract(modifiers)) 
			abstractMethod =  true;
		if (Modifier.isFinal(modifiers)) 
			finalMethod =  true;
		if (Modifier.isStatic(modifiers)) 
			staticMethod =  true;
		if (method.isConstructor()) 
			isConstructor =  true;
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
	
	void setParent(SM_Type parentType) {
		this.parentType = parentType;
		this.parentPkg = parentType.getParentPkg();
		this.parentProject = parentPkg.getParent();
	}
	
	public SM_Type getParentType() {
		return parentType;
	}
	
	public SM_Package getParentPkg() {
		return parentPkg;
	}
	
	public SM_Project getParentProject() {
		return parentProject;
	}
	
	public List<MethodInvocation> getCalledMethods() {
		return calledMethods;
	}
	
	void parse(SM_Type parentType) {
		setParent(parentType);
		
		MethodInvVisitor invVisitor = new MethodInvVisitor(methodDeclaration);
		methodDeclaration.accept(invVisitor);
		List<MethodInvocation> invList = invVisitor.getCalledMethods();
		if (invList.size() > 0)
			calledMethods.addAll(invList);
		
		List<SingleVariableDeclaration> variableList = methodDeclaration.parameters();
		for(SingleVariableDeclaration var: variableList) {
			VariableVisitor parameterVisitor = new VariableVisitor(methodDeclaration, typeDeclaration);
//			methodDeclaration.accept(parameterVisitor);
			var.accept(parameterVisitor);
			List<SM_Parameter> pList = parameterVisitor.getParameters();
			if (pList.size() > 0)
				parameterList.addAll(pList);
			parseParameters(this);		
		}
		
		LocalVarVisitor localVarVisitor = new LocalVarVisitor();
		methodDeclaration.accept(localVarVisitor);
		List<SM_LocalVar> lList = localVarVisitor.getLocalVar();
		if (lList.size() > 0)
			localVarList.addAll(lList);
		parseLocalVar(this);
		
	}

	private void parseParameters(SM_Method parentMethod) {
		for(SM_Parameter param: parameterList) {
			param.parse(parentMethod);
		}
	}
	
	private void parseLocalVar(SM_Method parentMethod) {
		for(SM_LocalVar var: localVarList) {
			var.parse(parentMethod);
		}
	}
	
	public void analyzeCalledMethod() {
		for (MethodInvocation method: calledMethods) {
//			System.out.println(method.arguments());
//			System.out.println(method.getExpression());
			System.out.println(method.getName().getParent());
			IMethodBinding imethod = method.resolveMethodBinding();
/*			if (imethod != null)
				System.out.println(imethod.getDeclaringClass().getPackage().getName());
			else
				System.out.println("null");*/
/*			String pckName = imethod.getDeclaringClass().getPackage().getName().toString();
			List<SM_Package> pkgList = new ArrayList<>();
			pkgList.addAll(getParentProject().getPackageList());
			for (SM_Package pkg: pkgList) {
				System.out.println(pkg.getName());
			}
			
			if (pkgList != null) {
				for (SM_Package pkg: pkgList) {
					if (pkg.getName().equals(pkgList))
						System.out.println(pkg.getName());
				}
			}*/
		}
	}
	
	private void findPkg(IMethodBinding method, SM_Project project) {
		ITypeBinding type = method.getDeclaringClass();
/*		if (type != null)
			System.out.println(type);*/
/*		if (type != null) {
			String pkgName = type.getPackage().getName().toString();
			if (pkgName != null) {
				List<SM_Package> pkgList = new ArrayList<SM_Package>();
				pkgList.addAll(getParentProject().getPackageList());
				System.out.println(pkgList);
			}
		}*/
		
//		return pkg;
	}
	
	@Override
	public void print() {
		System.out.println("Method: " + name);
		System.out.println("	Parent: " + this.getParentPkg().getName());
		System.out.println("	Constructor: " + isConstructor);
		System.out.println("	Returns: " +  methodDeclaration.getReturnType2());
		System.out.println("	Access: " + accessModifier);
		System.out.println("	Abstract: " + abstractMethod);
		System.out.println("	Final: " + finalMethod);
		System.out.println("	Static: " + staticMethod);
		System.out.println("	Called methods: " + getCalledMethods());
		System.out.println("	Binding: " + getIMethod().getMethodDeclaration());
		for(SM_Parameter param:parameterList)
			param.print();
		for(SM_LocalVar var:localVarList) 
			var.print();
		analyzeCalledMethod();
	}
}
