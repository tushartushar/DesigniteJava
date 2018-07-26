package Designite.SourceModel;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import Designite.metrics.MethodMetrics;
import Designite.smells.implementationSmells.ImplementationSmellDetector;
import Designite.smells.models.ImplementationCodeSmell;
import Designite.utils.CSVUtils;
import Designite.utils.Constants;
import Designite.utils.models.Edge;
import Designite.utils.models.Vertex;
import Designite.visitors.StaticFieldAccessVisitor;

//TODO check EnumDeclaration, AnnotationTypeDeclaration and nested classes
public class SM_Type extends SM_SourceItem implements Vertex {
	
	
	private boolean isAbstract = false;
	private boolean isInterface = false;
	private SM_Package parentPkg;

	private CompilationUnit compilationUnit;
	private TypeDeclaration typeDeclaration;
	private ITypeBinding IType;

	private TypeDeclaration containerClass;
	private boolean nestedClass;
	
	private List<SM_Type> superTypes = new ArrayList<>();
	private List<SM_Type> subTypes = new ArrayList<>();
	private List<SM_Type> referencedTypeList = new ArrayList<>();
	private List<SM_Type> typesThatReferenceThisList = new ArrayList<>();
	private List<ImportDeclaration> importList = new ArrayList<>();
	private List<SM_Method> methodList = new ArrayList<>();
	private List<SM_Field> fieldList = new ArrayList<>();
	private List<Name> staticFieldAccesses = new ArrayList<>();
	private List<SM_Type> staticFieldAccessList = new ArrayList<>();
	private List<SM_Type> staticMethodInvocations = new ArrayList<>();
	private Map<SM_Method, MethodMetrics> metricsMapping = new HashMap<>();
	private Map<SM_Method, List<ImplementationCodeSmell>> smellMapping = new HashMap<>();

	public SM_Type(TypeDeclaration typeDeclaration, CompilationUnit compilationUnit, SM_Package pkg) {
		parentPkg = pkg;
		if (typeDeclaration == null || compilationUnit == null)
			throw new NullPointerException();

		name = typeDeclaration.getName().toString();
		this.typeDeclaration = typeDeclaration;
		this.compilationUnit = compilationUnit;
		setTypeInfo();
		setAccessModifier(typeDeclaration.getModifiers());
		setImportList(compilationUnit);
	}
	
	public List<SM_Type> getSuperTypes() {
		return superTypes;
	}
	
	public List<SM_Type> getSubTypes() {
		return subTypes;
	}
	
	public List<SM_Type> getReferencedTypeList() {
		return referencedTypeList;
	}
	
	public List<SM_Type> getTypesThatReferenceThis() {
		return typesThatReferenceThisList;
	}

	public TypeDeclaration getTypeDeclaration() {
		return typeDeclaration;
	}
	
	public void addReferencedTypeList(SM_Type type) {
		referencedTypeList.add(type);
	}
	
	public void addStaticMethodInvocation(SM_Type type) {
		if (!this.staticMethodInvocations.contains(type)){
			System.out.println("Adding static method invocation dependency");
			this.staticMethodInvocations.add(type);
		}
	}
	
	public boolean containsTypeInReferencedTypeList(SM_Type type) {
		return referencedTypeList.contains(type);
	}
	
	public void addTypesThatReferenceThisList(SM_Type type) {
		typesThatReferenceThisList.add(type);
	}
	
	public boolean containsTypeInTypesThatReferenceThisList(SM_Type type) {
		return typesThatReferenceThisList.contains(type);
	}

	private void setTypeInfo() {
		int modifier = typeDeclaration.getModifiers();
		if (Modifier.isAbstract(modifier)) {
			isAbstract = true;
		}
		if (typeDeclaration.isInterface()) {
			isInterface = true;
		}
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public boolean isInterface() {
		return isInterface;
	}

	public void setNestedClass(TypeDeclaration referredClass) {
		nestedClass = true;
		this.containerClass = referredClass;
	}

	public boolean isNestedClass() {
		return nestedClass;
	}

	private void setImportList(CompilationUnit unit) {
		ImportVisitor importVisitor = new ImportVisitor();
		unit.accept(importVisitor);
		List<ImportDeclaration> imports = importVisitor.getImports();
		if (imports.size() > 0)
			importList.addAll(imports);
	}

	public List<ImportDeclaration> getImportList() {
		return importList;
	}
	
	private void setSuperTypes() {
		setSuperClass();
		setSuperInterface();
	}
	
	private void setSuperClass() {
		Type superclass = typeDeclaration.getSuperclassType();
		if (superclass != null)
		{
			SM_Type inferredType = (new Resolver()).resolveType(superclass, parentPkg.getParentProject());
			if(inferredType != null) {
				superTypes.add(inferredType);
				inferredType.addThisAsChildToSuperType(this);
			}
		}
			
	}
	
	private void setSuperInterface() {
		List<Type> superInterfaces = typeDeclaration.superInterfaceTypes();
		if (superInterfaces != null)
		{
			for (Type superInterface : superInterfaces)  {
				SM_Type inferredType = (new Resolver()).resolveType(superInterface, parentPkg.getParentProject());
				if(inferredType != null) {
					superTypes.add(inferredType);
					inferredType.addThisAsChildToSuperType(this);
				}
			}
		}
			
	}
	
	private void addThisAsChildToSuperType(SM_Type child) {
		if (!subTypes.contains(child)) {
			subTypes.add(child);
		}
	}

	public List<SM_Method> getMethodList() {
		return methodList;
	}

	public List<SM_Field> getFieldList() {
		return fieldList;
	}

	public SM_Package getParentPkg() {
		return parentPkg;
	}

	private void parseMethods() {
		for (SM_Method method : methodList) {
			method.parse();
		}
	}

	private void parseFields() {
		for (SM_Field field : fieldList) {
			field.parse();
		}
	}

	@Override
	public void printDebugLog(PrintWriter writer) {
		print(writer, "\tType: " + name);
		print(writer, "\tPackage: " + this.getParentPkg().getName());
		print(writer, "\tAccess: " + accessModifier);
		print(writer, "\tInterface: " + isInterface);
		print(writer, "\tAbstract: " + isAbstract);
		print(writer, "\tSupertypes: " + ((getSuperTypes().size() != 0) ? getSuperTypes().get(0).getName() : "Object"));
		print(writer, "\tNested class: " + nestedClass);
		if (nestedClass)
			print(writer, "\tContainer class: " + containerClass.getName());
		print(writer, "\tReferenced types: ");
		for (SM_Type type:referencedTypeList)
			print(writer, "\t\t" + type.getName());
		for (SM_Field field : fieldList)
			field.printDebugLog(writer);
		for (SM_Method method : methodList)
			method.printDebugLog(writer);
		print(writer, "\t----");
	}


	@Override
	public void parse() {
		MethodVisitor methodVisitor = new MethodVisitor(typeDeclaration, this);
		typeDeclaration.accept(methodVisitor);
		List<SM_Method> mList = methodVisitor.getMethods();
		if (mList.size() > 0)
			methodList.addAll(mList);
		parseMethods();

		FieldVisitor fieldVisitor = new FieldVisitor(this);
		typeDeclaration.accept(fieldVisitor);
		List<SM_Field> fList = fieldVisitor.getFields();
		if (fList.size() > 0)
			fieldList.addAll(fList);
		parseFields();
		
		StaticFieldAccessVisitor fieldAccessVisitor = new StaticFieldAccessVisitor();
		typeDeclaration.accept(fieldAccessVisitor);
		staticFieldAccesses = fieldAccessVisitor.getStaticFieldAccesses();
	}

	@Override
	public void resolve() {
		for (SM_Method method : methodList)
			method.resolve();
		for (SM_Field field : fieldList)
			field.resolve();
		setStaticAccessList();
		setReferencedTypes();
		setTypesThatReferenceThis();
		setSuperTypes();
		updateHierarchyGraph();
		updateDependencyGraph();
	}
	
	private void setStaticAccessList() {
		staticFieldAccessList = (new Resolver()).inferStaticAccess(staticFieldAccesses, this);
	}
	
	private void setReferencedTypes() {
		for (SM_Field field:fieldList)
			if(!field.isPrimitiveType()) {
				addUniqueReference(this, field.getType(), false);
			}	
		for (SM_Method method:methodList) {
			for (SM_Type refType:method.getReferencedTypeList()) {
				addUniqueReference(this, refType, false);
			}
		}
		for (SM_Type staticAccessType : staticFieldAccessList) {
			addUniqueReference(this, staticAccessType, false);
		}
		for (SM_Type methodInvocation : staticMethodInvocations){
			addUniqueReference(this, methodInvocation, false);
			
		}
	}
	
	private void setTypesThatReferenceThis() {
		for (SM_Type refType : referencedTypeList) {
			addUniqueReference(refType, this, true);
		}
	}
	
	private void updateHierarchyGraph() {
		if (superTypes.size() > 0) {
			for (SM_Type superType : superTypes) {
				getParentPkg().getParentProject().getHierarchyGraph().addEdge(
						new Edge(this, superType));
			}
		}
		getParentPkg().getParentProject().getHierarchyGraph().addVertex(this);		
	}
	
	private void updateDependencyGraph() {
		if (getReferencedTypeList().size() > 0) {
			for (SM_Type dependency : getReferencedTypeList()) {
				getParentPkg().getParentProject().getDependencyGraph().addEdge(
						new Edge(this, dependency));
			}
		}
		getParentPkg().getParentProject().getDependencyGraph().addVertex(this);
	}
	
	private void addUniqueReference(SM_Type type, SM_Type typeToAdd, boolean invardReference) {
		if(typeToAdd == null)
			return;
		if (invardReference) {
			if (!type.containsTypeInTypesThatReferenceThisList(typeToAdd)) {
				type.addTypesThatReferenceThisList(typeToAdd);//FAN-IN?
			}
		} else {
			if (!type.containsTypeInReferencedTypeList(typeToAdd)) {
				type.addReferencedTypeList(typeToAdd);//FAN-OUT?
			}
		}
	}

	public void extractMethodMetrics() {
		for (SM_Method method : methodList) {
			MethodMetrics metrics = new MethodMetrics(method);
			metrics.extractMetrics();
			metricsMapping.put(method, metrics);
			exportMethodMetricsToCSV(metrics, method.getName());
		}
	}
	
	public MethodMetrics getMetricsFromMethod(SM_Method method) {
		return metricsMapping.get(method);
	}
	
	public void exportMethodMetricsToCSV(MethodMetrics metrics, String methodName) {
		String path = Constants.CSV_DIRECTORY_PATH
				+ File.separator + Constants.METHOD_METRICS_PATH_SUFFIX;
		CSVUtils.addToCSVFile(path, getMetricsAsARow(metrics, methodName));
	}
	
	private String getMetricsAsARow(MethodMetrics metrics, String methodName) {
		return getParentPkg().getParentProject().getName()
				+ "," + getParentPkg().getName()
				+ "," + getName()
				+ "," + name
				+ "," + metrics.getNumOfLines()
				+ "," + metrics.getCyclomaticComplexity()
				+ "," + metrics.getNumOfParameters()
				+ "\n";
	}
	
	public void extractCodeSmells() {
		for (SM_Method method : methodList) {
			ImplementationSmellDetector detector = new ImplementationSmellDetector(metricsMapping.get(method)
					, new SourceItemInfo(getParentPkg().getParentProject().getName()
							, getParentPkg().getName()
							, getName()
							, method.getName()));
			smellMapping.put(method, detector.detectCodeSmells());
			exportDesignSmellsToCSV(method);
			
		}
	}
	
	private void exportDesignSmellsToCSV(SM_Method method) {
		CSVUtils.addAllToCSVFile(Constants.CSV_DIRECTORY_PATH
				+ File.separator + Constants.IMPLEMENTATION_CODE_SMELLS_PATH_SUFFIX
				, smellMapping.get(method));
	}
	
	@Override
	public String toString() {
		return "Type="+name;
	}

}
