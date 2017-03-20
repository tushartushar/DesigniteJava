package Designite.SourceModel;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class SM_Method extends SM_SourceItem {
	//private boolean publicMethod = false;
	private boolean abstractMethod = false;
	private boolean finalMethod = false;
	private boolean staticMethod = false;
	private boolean isConstructor = false;
	private SM_Type parentType;
	private Block methodBody;
	
	private TypeDeclaration typeDeclaration;
	private MethodDeclaration methodDeclaration;
	private IMethodBinding IMethod;
	
	private List<SM_Parameter> parameterList = new ArrayList<SM_Parameter>();
	private List<SM_LocalVar> localVarList = new ArrayList<SM_LocalVar>();
	
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
	}
	
	public SM_Type getParent() {
		return parentType;
	}
	
	void parse() {
		List<SingleVariableDeclaration> variableList = methodDeclaration.parameters();
		for(SingleVariableDeclaration var: variableList) {
			VariableVisitor parameterVisitor = new VariableVisitor(methodDeclaration, typeDeclaration);
			methodDeclaration.accept(parameterVisitor);
			List<SM_Parameter> pList = parameterVisitor.getParameters();
			if (pList.size() > 0)
				parameterList.addAll(pList);
			parseParameters(this);		
		}
	}

	private void parseParameters(SM_Method parentMethod) {
		for(SM_Parameter param: parameterList) {
//			param.parse();
			param.setParent(parentMethod);
		}
	}
	
	@Override
	public void print() {
		System.out.println("Method: " + name);
		System.out.println("	Parent: " + this.getParent().getName());
		System.out.println("	Constructor: " + isConstructor);
		System.out.println("	Returns: " +  methodDeclaration.getReturnType2());
		System.out.println("	Access: " + accessModifier);
		System.out.println("	Abstract: " + abstractMethod);
		System.out.println("	Final: " + finalMethod);
		System.out.println("	Static: " + staticMethod);
//		System.out.println("	Binding: " + methodDeclaration.resolveBinding());
		for(SM_Parameter param:parameterList)
			param.print();
		for(SM_LocalVar var:localVarList)
			var.print();
	}
}
