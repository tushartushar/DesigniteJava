package Designite.SourceModel;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;

public class SM_Method extends SM_SourceItem{
	private SimpleName name;
	private boolean publicMethod;
	private boolean abstractMethod;
	private List<SM_Parameter> parameterList = new ArrayList<SM_Parameter>();
	private List<SM_LocalVar> localVarList = new ArrayList<SM_LocalVar>();
	
	SM_Method(MethodDeclaration method) {
		name = method.getName();
		setPublicMethod(method);
		setAbstractMethod(method);
		setParameters(method);
	}
	
	public SimpleName getName() {
		return name;
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
	
	public void setParameters(MethodDeclaration method) {
		//TODO: Need to change; create appropriate parameter object with type identified
		//parameters = method.parameters();
	}
	
	public List<SM_Parameter> getParameters() {
		return parameterList;
	}

	@Override
	public void print() {
		System.out.println("Method: " + name);
		for(SM_Parameter param:parameterList)
			param.print();
		for(SM_LocalVar var:localVarList)
			var.print();
	}

}
