package Designite.SourceModel;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;

import Designite.metrics.TypeMetrics;
import Designite.smells.designSmells.DesignSmellFacade;
import Designite.smells.models.DesignCodeSmell;
import Designite.utils.CSVUtils;
import Designite.utils.Constants;
import Designite.utils.models.Edge;

public class SM_Package extends SM_SourceItem {
	private List<CompilationUnit> compilationUnitList;
	// private List<ImportDeclaration> imports = new ArrayList<>();
	private List<SM_Type> typeList = new ArrayList<>();
	// private List<SM_Type> nestedClassList;
	private SM_Project parentProject;
	private Map<SM_Type, TypeMetrics> metricsMapping = new HashMap<>();
	private Map<SM_Type, List<DesignCodeSmell>> smellMapping = new HashMap<>();

	public SM_Package(String packageName, SM_Project parentObj) {
		name = packageName;
		compilationUnitList = new ArrayList<CompilationUnit>();
		parentProject = parentObj;
	}

	public SM_Project getParentProject() {
		return parentProject;
	}

	
	 public List<CompilationUnit> getCompilationUnitList() { 
		 return compilationUnitList; 
     }
	 

	public List<SM_Type> getTypeList() {
		return typeList;
	}

	void addCompilationUnit(CompilationUnit unit) {
		compilationUnitList.add(unit);
	}

	private void addNestedClass(List<SM_Type> list) {
		if (list.size() > 1) {
			for (int i = 1; i < list.size(); i++) {
				typeList.add(list.get(i));
				list.get(i).setNestedClass(list.get(0).getTypeDeclaration());
			}
		}
	}

	private void parseTypes(SM_Package parentPkg) {
		for (SM_Type type : typeList) {
			type.parse();
		}
	}

	@Override
	public void printDebugLog(PrintWriter writer) {
		print(writer, "Package: " + name);
		for (SM_Type type : typeList) {
			type.printDebugLog(writer);
		}
		print(writer, "----");
	}
	
	@Override
	public void parse() {

		for (CompilationUnit unit : compilationUnitList) {
			/*
			 * ImportVisitor importVisitor = new ImportVisitor();
			 * unit.accept(importVisitor); List<ImportDeclaration> importList =
			 * importVisitor.getImports(); if (importList.size() > 0)
			 * imports.addAll(importList);
			 */

			TypeVisitor visitor = new TypeVisitor(unit, this);
			unit.accept(visitor);
			List<SM_Type> list = visitor.getTypeList();
			if (list.size() > 0) {
				if (list.size() == 1) {
					typeList.addAll(list); // if the compilation unit contains
											// only one class; simpler case,
											// there is no nested classes
				} else {
					typeList.add(list.get(0));
					addNestedClass(list);
				}
			}
		}
		parseTypes(this);
	}

	@Override
	public void resolve() {
		System.out.println("Resolving Package :: " + name);
		for (SM_Type type : typeList) { 
			type.resolve();
		}
	}

	public void extractTypeMetrics() {
		for (SM_Type type : typeList) {
			type.extractMethodMetrics();
			TypeMetrics metrics = new TypeMetrics(type);
			metrics.extractMetrics();
			metricsMapping.put(type, metrics);
			exportMetricsToCSV(metrics, type.getName());
			updateDependencyGraph(type);
		}
	}
	
	private void updateDependencyGraph(SM_Type type) {
		if (type.getReferencedTypeList().size() > 0) {
			for (SM_Type dependency : type.getReferencedTypeList()) {
				getParentProject().getHierarchyGraph().addEdge(new Edge(type, dependency));
			}
		}
		getParentProject().getHierarchyGraph().addVertex(type);
	}
	
	public TypeMetrics getMetricsFromType(SM_Type type) {
		return metricsMapping.get(type);
	}
	
	private void exportMetricsToCSV(TypeMetrics metrics, String typeName) {
		String path = Constants.CSV_DIRECTORY_PATH
				+ File.separator + Constants.TYPE_METRICS_PATH_SUFFIX;
		CSVUtils.addToCSVFile(path, getMetricsAsARow(metrics, typeName));
	}
	
	private String getMetricsAsARow(TypeMetrics metrics, String typeName) {
		return getParentProject().getName()
				+ "," + getName()
				+ "," + typeName
				+ "," + metrics.getNumOfFields()
				+ "," + metrics.getNumOfPublicFields()
				+ "," + metrics.getNumOfMethods()
				+ "," + metrics.getNumOfPublicMethods()
				+ "," + metrics.getNumOfLines()
				+ "," + metrics.getWeightedMethodsPerClass()
				+ "," + metrics.getNumOfChildren()
				+ "," + metrics.getInheritanceDepth()
				+ "," + metrics.getLcom()
				+ "," + metrics.getNumOfFanInTypes()
				+ "," + metrics.getNumOfFanOutTypes()
				+ "\n";
	}

	public void extractCodeSmells() {
		for (SM_Type type : typeList) { 
			DesignSmellFacade detector = new DesignSmellFacade(metricsMapping.get(type)
					, new SourceItemInfo(getParentProject().getName()
							, getName()
							, type.getName())
					);
			type.extractCodeSmells();
			smellMapping.put(type, detector.detectCodeSmells());
			exportDesignSmellsToCSV(type);
		}
	}

	private void exportDesignSmellsToCSV(SM_Type type) {
		CSVUtils.addAllToCSVFile(Constants.CSV_DIRECTORY_PATH
				+ File.separator + Constants.DESIGN_CODE_SMELLS_PATH_SUFFIX
				, smellMapping.get(type));
	}

}
