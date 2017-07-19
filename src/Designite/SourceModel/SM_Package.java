package Designite.SourceModel;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;

public class SM_Package extends SM_SourceItem {
	private List<CompilationUnit> compilationUnitList;
	// private List<ImportDeclaration> imports = new ArrayList<>();
	private List<SM_Type> typeList = new ArrayList<SM_Type>();
	// private List<SM_Type> nestedClassList;
	private SM_Project parentProject;

	public SM_Package(String packageName, SM_Project parentObj) {
		name = packageName;
		compilationUnitList = new ArrayList<CompilationUnit>();
		parentProject = parentObj;
	}

	public SM_Project getParentProject() {
		return parentProject;
	}

	
	 public List<CompilationUnit> getCompilationUnitList() { return
	  compilationUnitList; }
	 

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
		print(writer, "\nPackage: " + name);
		for (SM_Type type : typeList) {
			type.printDebugLog(writer);
		}
	}

	@Override
	public void resolve() {
		for (SM_Type type : typeList) 
			type.resolve();
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

}
