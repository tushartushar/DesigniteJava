package Designite.SourceModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.Document;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import Designite.InputArgs;

public class SM_Project extends SM_SourceItem{

	private InputArgs inputArgs;
	private List<String> sourceFileList;
	private List<CompilationUnit> compilationUnitList;
	private List<SM_Package> packageList;
	private String unitName;
	
	public SM_Project(InputArgs argsObj) {
		this.inputArgs = argsObj;
		sourceFileList = new ArrayList<String>();
		compilationUnitList = new ArrayList<CompilationUnit>();
		packageList = new ArrayList<SM_Package>();
		//setName();
	}
	
	//TODO 
	public void setName() {
		
	}
	
	public void setSourceFileList(List<String> list) {
		sourceFileList = list;
	}
	
	public List<String> getSourceFileList() {
		return sourceFileList;
	}
	
	public void setCompilationUnitList(List<CompilationUnit> list) {
		compilationUnitList = list;
	}
	
	public List<CompilationUnit> getCompilationUnitList() {
		return compilationUnitList;
	}
	
	public List<SM_Package> getPackageList() {
		return packageList;
	}
	
	public int getPackageCounter() {
		return packageList.size();
	}
	
	public void parse() {
		createCompilationUnits();
		createPackageObjects();
		parseAllPackages();
	}
	
	//method used in tests
	public CompilationUnit createCU(String filePath) {
		String fileToString = readFileToString(filePath);
		int startingIndex = filePath.lastIndexOf(File.separatorChar);
		unitName = filePath.substring(startingIndex + 1);
		
		return createAST(fileToString, unitName);
	}

	private void parseAllPackages() {
		for(SM_Package pkg:packageList){
			pkg.parse();
		}
	}
	
	private void createPackageObjects() {
		checkNotNull(compilationUnitList);
		String packageName;
		for(CompilationUnit unit:compilationUnitList){
			if (unit.getPackage() != null) {
				packageName = unit.getPackage().getName().toString();
			} else {
				packageName = "(default package)";
			}
			SM_Package pkgObj = searchPackage(packageName);
			//If pkgObj is null, package has not yet created
			if(pkgObj == null){
				pkgObj = new SM_Package(packageName);
				packageList.add(pkgObj);
			}
			pkgObj.addCompilationUnit(unit);
		}
	}
	
	private void checkNotNull(List<CompilationUnit> list) {
		if (list == null) 
			throw new NullPointerException();
		
		for(CompilationUnit unit:list) {
			if (unit == null){
				list.remove(unit);
			}
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
		try {
			getFileList(inputArgs.getSourceFolder());
		
			for(String file: sourceFileList) {
				String fileToString = readFileToString(file);
				int startingIndex = file.lastIndexOf(File.separatorChar);
				unitName = file.substring(startingIndex + 1);
				CompilationUnit unit = createAST(fileToString, unitName);
				compilationUnitList.add(unit);
			} 
		}catch(NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	//set sources and classpath before execute
	private CompilationUnit createAST(final String content, String unitName) {
 		Document doc = new Document(content);
 		final ASTParser parser = ASTParser.newParser(AST.JLS8);
 		parser.setSource(doc.get().toCharArray());
 		parser.setKind(ASTParser.K_COMPILATION_UNIT); 
 		
		//parser.setUnitName(unitName);
 		
/*		Map<String, String> options = JavaCore.getOptions();
		parser.setCompilerOptions(options);*/
		
		String[] sources = { "C:\\Users\\Alex\\workspace\\DesisgniteJava" }; 
		String[] classpath = {"C:\\Users\\Alex\\workspace\\DesisgniteJava\\lib"};
/*		String[] classpath = {"C:\\Program Files\\Java\\jre1.8.0_121\\lib\\rt.jar" };
		String[] classpath = {"C:\\Users\\Alex\\workspace\\DesisgniteJava\\lib\\org.eclipse.core.contenttype_3.5.100.v20160418-1621.jar",
				"C:\\Users\\Alex\\workspace\\DesisgniteJava\\lib\\org.eclipse.core.jobs_3.8.0.v20160509-0411.jar",
				"C:\\Users\\Alex\\workspace\\DesisgniteJava\\lib\\org.eclipse.core.resources_3.11.1.v20161107-2032.jar",
				"C:\\Users\\Alex\\workspace\\DesisgniteJava\\lib\\org.eclipse.core.runtime_3.12.0.v20160606-1342.jar",
				"C:\\Users\\Alex\\workspace\\DesisgniteJava\\lib\\org.eclipse.equinox.common_3.8.0.v20160509-1230.jar",
				"C:\\Users\\Alex\\workspace\\DesisgniteJava\\lib\\org.eclipse.equinox.preferences_3.6.1.v20160815-1406.jar",
				"C:\\Users\\Alex\\workspace\\DesisgniteJava\\lib\\org.eclipse.jdt.core-3.10.0.jar",
				"C:\\Users\\Alex\\workspace\\DesisgniteJava\\lib\\org.eclipse.osgi_3.11.2.v20161107-1947.jar",
				"C:\\Users\\Alex\\workspace\\DesisgniteJava\\lib\\org.eclipse.text_3.5.0.jar"};*/

 		
 		parser.setEnvironment(classpath, sources, new String[] {"UTF-8"}, true);
 		parser.setResolveBindings(true);
 		parser.setBindingsRecovery(true);
 	    parser.setStatementsRecovery(true);
 		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
 		
 		if (cu.getAST().hasBindingsRecovery()) {
			System.out.println("Binding activated.");
		}
 		return cu;
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

	@Override
	public void print() {
		System.out.println("Project: " + name);
		System.out.println("-------------------");
		for(SM_Package pkg:packageList) {
			pkg.print();
		}
	}

}
