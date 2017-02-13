package Designite.SourceModel;

import java.lang.reflect.Modifier;
import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;

public class Method {
	private SimpleName name;
	private boolean publicMethod;
	private boolean abstractMethod;
	private List parameters;
	
	Method(MethodDeclaration method) {
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
		parameters = method.parameters();
	}
	
	public List getParameters() {
		return parameters;
	}
	
	public int countParameters(List parameters) {
		return parameters.size();
	}
	
	public String toString() {
		return "Method: " + getName() + "\n" + "Parameters: " + parameters + " (" + countParameters(parameters) + ")\n";
	}

}
