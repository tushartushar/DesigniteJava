package Designite.SourceModel;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class SM_Method extends SM_SourceItem {
	private boolean publicMethod;
	private boolean abstractMethod;
	private TypeDeclaration typeDeclaration;
	private MethodDeclaration methodDeclaration;
	private List<SM_Parameter> parameterList = new ArrayList<SM_Parameter>();
	private List<SM_LocalVar> localVarList = new ArrayList<SM_LocalVar>();
	
	SM_Method(MethodDeclaration methodDeclaration, TypeDeclaration typeDeclaration) {
		name = methodDeclaration.getName().toString();
		this.typeDeclaration = typeDeclaration; 
		this.methodDeclaration = methodDeclaration;
		setPublicMethod(methodDeclaration );
		setAbstractMethod(methodDeclaration );
	}
	
	public void setPublicMethod(MethodDeclaration method){
		int modifiers = method.getModifiers();
		if (Modifier.isPublic(modifiers)) {
			publicMethod = true;
		} else 
			publicMethod = false;
	}
	
	public boolean isPublic() {
		return this.publicMethod;
	}
	
	public void setAbstractMethod(MethodDeclaration method){
		int modifiers = method.getModifiers();
		if (Modifier.isAbstract(modifiers)) {
			abstractMethod =  true;
		} else 
			abstractMethod =  false;
	}
	
	public boolean isAbstract() {
		return this.abstractMethod;
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
		for(SM_Parameter param:parameterList)
			param.print();
		for(SM_LocalVar var:localVarList)
			var.print();
	}

}
