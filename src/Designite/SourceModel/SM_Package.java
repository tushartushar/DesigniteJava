package Designite.SourceModel;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Designite.metrics.TypeMetricsExtractor;
import org.eclipse.jdt.core.dom.CompilationUnit;

import Designite.InputArgs;
import Designite.metrics.TypeMetrics;
import Designite.smells.designSmells.DesignSmellFacade;
import Designite.smells.models.DesignCodeSmell;
import Designite.utils.CSVUtils;
import Designite.utils.Constants;
import Designite.utils.models.Edge;

public class SM_Package extends SM_SourceItem implements Parsable{
	private List<CompilationUnit> compilationUnitList;
	private List<SM_Type> typeList = new ArrayList<>();
	private SM_Project parentProject;
	private Map<SM_Type, TypeMetrics> metricsMapping = new HashMap<>();
	private Map<SM_Type, List<DesignCodeSmell>> smellMapping = new HashMap<>();
	private InputArgs inputArgs;

	public SM_Package(String packageName, SM_Project parentObj, InputArgs inputArgs) {
		name = packageName;
		compilationUnitList = new ArrayList<CompilationUnit>();
		parentProject = parentObj;
		this.inputArgs = inputArgs;
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
				//SM_Type nested = list.get(i);
				//SM_Type outer = list.get(0);
				typeList.add(list.get(i));
				list.get(0).addNestedClass(list.get(i));
				list.get(i).setNestedClass(list.get(0).getTypeDeclaration());
			}
		}
	}

	private void parseTypes(SM_Package parentPkg) {
		for (SM_Type type : typeList) {
			type.parse();
//			System.out.println("Type : " + type.name + ", nested:: " + type.getNestedTypes());
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

			TypeVisitor visitor = new TypeVisitor(unit, this, inputArgs);
			unit.accept(visitor);
			List<SM_Type> list = visitor.getTypeList();
			if (list.size() > 0) {
				if (list.size() == 1) {
					typeList.addAll(list); // if the compilation unit contains
											// only one class; simpler case,
											// there is no nested classes
				} else {
					typeList.add(list.get(0));
//					System.out.println("TypeList :: " + list);
					addNestedClass(list);
				}
			}
		}
		parseTypes(this);
	}

	@Override
	public void resolve() {
		for (SM_Type type : typeList) { 
			type.resolve();
		}
	}

	public void extractTypeMetrics() {
		for (SM_Type type : typeList) {
			type.extractMethodMetrics();
			TypeMetrics metrics = new TypeMetricsExtractor(type).extractMetrics();
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
		String path = inputArgs.getOutputFolder()
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
		CSVUtils.addAllToCSVFile(inputArgs.getOutputFolder()
				+ File.separator + Constants.DESIGN_CODE_SMELLS_PATH_SUFFIX
				, smellMapping.get(type));
	}

}
