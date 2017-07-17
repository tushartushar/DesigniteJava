package Designite.SourceModel;

import java.io.PrintWriter;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

public class SM_Parameter extends SM_Variable {
	private SingleVariableDeclaration variable;
	private SM_Method parentMethod;
	
	public SM_Parameter(SingleVariableDeclaration variable, SM_Method methodObj) {
		name = variable.getName().toString();
		this.variable = variable;
		this.parentMethod = methodObj;
	}
	
	void setParent(SM_Method parentMethod) {
		this.parentMethod = parentMethod;
	}
	
	public SM_Method getParent() {
		return parentMethod;
	}
	
	
	@Override
	public void printDebugLog(PrintWriter writer) {
		print(writer, "Parameter: " + name);
		print(writer, "	Parent Method: " + getParent().getName());
		print(writer, "	Type: " + type);
		/*findRefObject();
		if (getRefType() != null)
			print(writer, "	Refers to: " + getRefType().getName());*/
	}

	@Override
	public void printDebugLog() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parse() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resolve() {
		// TODO Auto-generated method stub
		
	}
}
