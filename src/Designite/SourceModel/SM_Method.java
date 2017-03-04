package Designite.SourceModel;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;


public class SM_Method extends SM_SourceItem {
	//private boolean publicMethod = false;
	private boolean abstractMethod = false;
	private boolean finalMethod = false;
	private boolean staticMethod = false;
	private boolean isConstructor = false;
	private TypeDeclaration typeDeclaration;
	private MethodDeclaration methodDeclaration;
	private List<SM_Parameter> parameterList = new ArrayList<SM_Parameter>();
	private List<SM_LocalVar> localVarList = new ArrayList<SM_LocalVar>();
	
	SM_Method(MethodDeclaration methodDeclaration, TypeDeclaration typeDeclaration) {
		name = methodDeclaration.getName().toString();
		this.typeDeclaration = typeDeclaration; 
		this.methodDeclaration = methodDeclaration;
		setMethodInfo(methodDeclaration );
		setAccessModifier(methodDeclaration.getModifiers());
	}
	
	public Block methodBody() {
		return methodDeclaration.getBody();
	}
	
/*	public void setPublicMethod(MethodDeclaration method){
		int modifiers = method.getModifiers();
		if (Modifier.isPublic(modifiers)) 
			publicMethod = true;
	}
	
	public boolean isPublic() {
		return this.publicMethod;
	}*/
	
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
	
	void parse() {
		List<SingleVariableDeclaration> variableList = methodDeclaration.parameters();
		for(SingleVariableDeclaration var: variableList) {
			VariableVisitor parameterVisitor = new VariableVisitor(methodDeclaration, typeDeclaration);
			methodDeclaration.accept(parameterVisitor);
			List<SM_Parameter> pList = parameterVisitor.getParameters();
			if (pList.size()>0)
				parameterList.addAll(pList);
	 		//parseParameters();
		}
	}

/*	private void parseParameters() {
		for(SM_Parameter param: parameterList)
			param.parse();
	}*/
	
	@Override
	public void print() {
		System.out.println("Method: " + name);
		System.out.println("	Constructor: " + isConstructor);
		System.out.println("	Returns: " +  methodDeclaration.getReturnType2());
		System.out.println("	Access: " + accessModifier);
		System.out.println("	Abstract: " + abstractMethod);
		System.out.println("	Final: " + finalMethod);
		System.out.println("	Static: " + staticMethod);
	
		for(SM_Parameter param:parameterList)
			param.print();
		for(SM_LocalVar var:localVarList)
			var.print();
	}

}
