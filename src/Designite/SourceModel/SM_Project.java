package Designite.SourceModel;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import Designite.InputArgs;

public class SM_Project extends SM_SourceItem{

	private InputArgs inputArgs;
	private List<String> sourceFileList;
	private List<CompilationUnit> compilationUnitList;
	private List<SM_Package> packageList;
	
	public SM_Project(InputArgs argsObj) {
		this.inputArgs = argsObj;
		sourceFileList = new ArrayList<String>();
		compilationUnitList = new ArrayList<CompilationUnit>();
		packageList = new ArrayList<SM_Package>();
	}

	public void parse() {
		createCompilationUnits();
		createPackageObjects();
	}

	private void createPackageObjects() {
		for(CompilationUnit unit:compilationUnitList){
			String packageName = unit.getPackage().getName().toString();
			SM_Package pkgObj = searchPackage(packageName);
			//If pkgObj is null, package has not yet created
			if(pkgObj == null){
				pkgObj = new SM_Package(packageName);
				packageList.add(pkgObj);
			}
			pkgObj.addCompilationUnit(unit);
		}
	}

	private SM_Package searchPackage(String packageName) {
		for(SM_Package pkg:packageList){
			if (pkg.getName().equals(packageName))
				return pkg;
		}
		return null;
	}

	private void createCompilationUnits() {
		getFileList(inputArgs.getSourceFolder());
		for(String file: sourceFileList) {
			SM_Type classMetrics = new SM_Type();
			String fileToString = readFileToString(file);
			CompilationUnit unit = classMetrics.createAST(fileToString);
			classMetrics.visitAST(unit);
			compilationUnitList.add(unit);
		}
	}
	
	private void getFileList(String path) {
		File root = new File(path);
        File[] list = root.listFiles();

        if (list == null) return;
        for ( File f : list ) {
            if ( f.isDirectory() ) {
                getFileList( f.getAbsolutePath() );
            }
            else {
            	if(f.getName().endsWith(".java"))
            		sourceFileList.add(f.getAbsolutePath());
            }
        }
	}

	private String readFileToString(String sourcePath) {
		try {
		 	return new String(Files.readAllBytes(Paths.get(sourcePath)));
		} catch (IOException e) {
		 	e.printStackTrace();
		 	return new String();
		}
	}
}
