package Designite.SourceModel;

import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;

import Designite.utils.models.Vertex;
import Designite.visitors.DirectAceessFieldVisitor;
import Designite.visitors.InstanceOfVisitor;
import Designite.visitors.ThrowVisitor;

public class SM_Method extends SM_SourceItem implements Vertex {
		
	private boolean abstractMethod;
	private boolean finalMethod;
	private boolean staticMethod;
	private boolean isConstructor;
	private boolean throwsException;
	private SM_Type parentType;

	private MethodDeclaration methodDeclaration;

	private List<SM_Method> calledMethodsList = new ArrayList<SM_Method>();
	private List<SM_Parameter> parameterList = new ArrayList<SM_Parameter>();
	private List<SM_LocalVar> localVarList = new ArrayList<SM_LocalVar>();
	private List<MethodInvocation> calledMethods = new ArrayList<MethodInvocation>();
	private List<SM_Type> referencedTypeList = new ArrayList<SM_Type>();
	private List<SimpleName> namesInMethod = new ArrayList<>();
	private List<FieldAccess> thisAccessesInMethod = new ArrayList<>();
	private List<SM_Field> directFieldAccesses = new ArrayList<>();
	private List<Type> typesInInstanceOf = new ArrayList<>();
	private List<SM_Type> smTypesInInstanceOf = new ArrayList<>();

	public SM_Method(MethodDeclaration methodDeclaration, SM_Type typeObj) {
		name = methodDeclaration.getName().toString();
		this.parentType = typeObj;
		this.methodDeclaration = methodDeclaration;
		setMethodInfo(methodDeclaration);
		setAccessModifier(methodDeclaration.getModifiers());
	}

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
	
	public boolean throwsException() {
		return throwsException;
	}
	
	public boolean hasBody() {
		return this.methodDeclaration.getBody() != null;
	}

	public List<SM_Parameter> getParameterList() {
		return parameterList;
	}

	public List<SM_LocalVar> getLocalVarList() {
		return localVarList;
	}

	public List<SM_Method> getCalledMethods() {
		return calledMethodsList;
	}
	
	public MethodDeclaration getMethodDeclaration() {
		return methodDeclaration;
	}

	private void prepareCalledMethodsList() {
		MethodInvVisitor invVisitor = new MethodInvVisitor(methodDeclaration);
		methodDeclaration.accept(invVisitor);
		List<MethodInvocation> invList = invVisitor.getCalledMethods();
		if (invList.size() > 0) {
			calledMethods.addAll(invList);
		}
	}

	private void prepareInstanceOfVisitorList() {
		InstanceOfVisitor instanceOfVisitor = new InstanceOfVisitor();
		methodDeclaration.accept(instanceOfVisitor);
		List<Type> instanceOfTypes = instanceOfVisitor.getTypesInInstanceOf();
		if (instanceOfTypes.size() > 0) {
			typesInInstanceOf.addAll(instanceOfTypes);
		}
	}

	private void prepareParametersList(SingleVariableDeclaration var) {
		VariableVisitor parameterVisitor = new VariableVisitor(this);
		// methodDeclaration.accept(parameterVisitor);
		var.accept(parameterVisitor);
		List<SM_Parameter> pList = parameterVisitor.getParameterList();
		if (pList.size() > 0) {
			parameterList.addAll(pList);
		}
	}

	private void parseParameters() {
		for (SM_Parameter param : parameterList) {
			param.parse();
		}
	}

	private void prepareLocalVarList() {
		LocalVarVisitor localVarVisitor = new LocalVarVisitor(this);
		methodDeclaration.accept(localVarVisitor);
		List<SM_LocalVar> lList = localVarVisitor.getLocalVarList();
		if (lList.size() > 0) {
			localVarList.addAll(lList);
		}
	}

	private void parseLocalVar() {
		for (SM_LocalVar var : localVarList) {
			var.parse();
		}
	}
	
	public String getMethodBody() {
		if (this.hasBody())
			return this.getMethodDeclaration().getBody().toString();
		else 
			return "";
	}
	
	@Override
	public void printDebugLog(PrintWriter writer) {
		print(writer, "\t\tMethod: " + name);
		print(writer, "\t\tParent type: " + this.getParentType().getName());
		print(writer, "\t\tConstructor: " + isConstructor);
		print(writer, "\t\tReturns: " + methodDeclaration.getReturnType2());
		print(writer, "\t\tAccess: " + accessModifier);
		print(writer, "\t\tAbstract: " + abstractMethod);
		print(writer, "\t\tFinal: " + finalMethod);
		print(writer, "\t\tStatic: " + staticMethod);
		print(writer, "\t\tCalled methods: ");
		for(SM_Method method:getCalledMethods())
			print(writer, "\t\t\t" + method.getName());
		for (SM_Parameter param : parameterList)
			param.printDebugLog(writer);
		for (SM_LocalVar var : localVarList)
			var.printDebugLog(writer);
		print(writer, "\t\t----");
	}

	//TODO: Modularize parser with private functions
	@Override
	public void parse() {
		prepareCalledMethodsList();

		List<SingleVariableDeclaration> variableList = methodDeclaration.parameters();
		for (SingleVariableDeclaration var : variableList) {
			prepareParametersList(var);
			parseParameters();
		}

		prepareLocalVarList();
		parseLocalVar();
		
		DirectAceessFieldVisitor directAceessFieldVisitor = new DirectAceessFieldVisitor();
		methodDeclaration.accept(directAceessFieldVisitor);
		List<SimpleName> names = directAceessFieldVisitor.getNames();
		List<FieldAccess> thisAccesses = directAceessFieldVisitor.getThisAccesses();
		if (names.size() > 0) {
			namesInMethod.addAll(names);
		}
		if (thisAccesses.size() > 0) {
			thisAccessesInMethod.addAll(thisAccesses);
		}
		prepareInstanceOfVisitorList();

		ThrowVisitor throwVisithor = new ThrowVisitor();
		methodDeclaration.accept(throwVisithor);
		throwsException = throwVisithor.throwsException();
	}

	@Override
	public void resolve() {
		for (SM_Parameter param : parameterList) {
				param.resolve();
		}
		for (SM_LocalVar localVar : localVarList) {
			localVar.resolve();
		}
		calledMethodsList = (new Resolver()).inferCalledMethods(calledMethods, parentType);
		setReferencedTypes();
		setDirectFieldAccesses();
		setSMTypesInInstanceOf();
	}
	
	private void setReferencedTypes() {
		for (SM_Parameter param : parameterList) {
			if (!param.isPrimitiveType()) {
				addunique(param.getType());
			}
		}
		for (SM_LocalVar localVar : localVarList) {
			if (!localVar.isPrimitiveType()) {
				addunique(localVar.getType());
			}
		}
		for (SM_Method methodCall : calledMethodsList) {
			if (methodCall.isStatic()) {
				addunique(methodCall.getParentType());
			}
		}
	}
	
	private void setDirectFieldAccesses() {
		for (FieldAccess thisAccess : thisAccessesInMethod) {
			SM_Field sameField = getFieldWithSameName(thisAccess.getName().toString());
			if (sameField != null && !directFieldAccesses.contains(sameField)) {
				directFieldAccesses.add(sameField);
			}
		}
		for (SimpleName name : namesInMethod) {
			if (!existsAsNameInLocalVars(name.toString())) {
				SM_Field sameField = getFieldWithSameName(name.toString());
				if (sameField != null && !directFieldAccesses.contains(sameField)) {
					directFieldAccesses.add(sameField);
				}
			}
		}
	}
	
	private boolean existsAsNameInLocalVars(String name) {
		for (SM_LocalVar localVar : localVarList) {
			if (name.equals(localVar.getName())) {
				return true;
			}
		}
		return false;
	}
	
	private SM_Field getFieldWithSameName(String name) {
		for (SM_Field field : parentType.getFieldList()) {
			if (name.equals(field.getName())) {
				return field;
			}
		}
		return null;
	}
	
	private void setSMTypesInInstanceOf() {
		Resolver resolver = new Resolver();
		for (Type type : typesInInstanceOf) {
			SM_Type smType = resolver.resolveType(type, parentType.getParentPkg().getParentProject());
			if (smType != null && !smTypesInInstanceOf.contains(smType)) {
				smTypesInInstanceOf.add(smType);
			}
		}
	}
	
	private void addunique(SM_Type variableType) {
		if (!referencedTypeList.contains(variableType))
			referencedTypeList.add(variableType);
	}

	public List<SM_Type> getReferencedTypeList() {
		return referencedTypeList;
	}
	
	public List<SM_Field> getDirectFieldAccesses() {
		return directFieldAccesses;
	}
	
	public List<SM_Type> getSMTypesInInstanceOf() {
		return smTypesInInstanceOf;
	}
	
}
