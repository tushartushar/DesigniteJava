package Designite.SourceModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

public class SM_Package extends SM_SourceItem{
	private List<CompilationUnit> compilationUnitList;
	
	public SM_Package(String packageName) {
		name = packageName;
		compilationUnitList = new ArrayList<CompilationUnit>();
	}

	public String getName() {
		return name;
	}

	void addCompilationUnit(CompilationUnit unit) {
		compilationUnitList.add(unit);
	}

}
