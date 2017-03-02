package Designite.SourceModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

public class SM_Package extends SM_SourceItem{
	private List<CompilationUnit> compilationUnitList;
	private List<SM_Type> typeList = new ArrayList<SM_Type>();
	
	public SM_Package(String packageName) {
		name = packageName;
		compilationUnitList = new ArrayList<CompilationUnit>();
	}

	public int countTypes() {
		return typeList.size();
	}
	
	void addCompilationUnit(CompilationUnit unit) {
		compilationUnitList.add(unit);
	}

	void parse() {
		for (CompilationUnit unit:compilationUnitList){
			TypeVisitor visitor = new TypeVisitor(unit);
			unit.accept(visitor);
			List<SM_Type> list = visitor.getTypeList();
			if (list.size()>0)
				typeList.addAll(list);
		}
		parseTypes();
	}

	private void parseTypes() {
		for (SM_Type type:typeList){
			type.parse();
		}
	}

	@Override
	public void print() {
		System.out.println();
		System.out.println("Package: " + name);
		for(SM_Type type:typeList)
			type.print();
	}

}
