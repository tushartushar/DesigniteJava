package Designite.SourceModel;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Designite.utils.DJLogger;
import Designite.utils.FileManager;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;

import Designite.ArgumentParser.InputArgs;
import Designite.utils.CSVUtils;
import Designite.utils.models.Graph;

public class SM_Project extends SM_SourceItem implements Parsable {

	private InputArgs inputArgs;
	private List<String> sourceFileList;
	private List<CompilationUnit> compilationUnitList;
	private List<SM_Package> packageList;
	private Graph hierarchyGraph;
	private Graph dependencyGraph;
	private String unitName;

	public SM_Project(InputArgs argsObj) {
		this.inputArgs = argsObj;
		sourceFileList = new ArrayList<String>();
		compilationUnitList = new ArrayList<CompilationUnit>();
		packageList = new ArrayList<SM_Package>();
		hierarchyGraph = new Graph();
		dependencyGraph = new Graph();
		setName(this.inputArgs.getProjectName());
	}

	public SM_Project() {
		sourceFileList = new ArrayList<String>();
		compilationUnitList = new ArrayList<CompilationUnit>();
		packageList = new ArrayList<SM_Package>();
		hierarchyGraph = new Graph();
		dependencyGraph = new Graph();
		setName(this.inputArgs.getProjectName());
	}




	public void setName(String name) {
		this.name = name;
	}

	public List<String> getSourceFileList() {
		return sourceFileList;
	}

	public void setCompilationUnitList(List<CompilationUnit> list) {
		if (list == null)
			throw new NullPointerException();
		compilationUnitList = list;
	}

	public List<CompilationUnit> getCompilationUnitList() {
		return compilationUnitList;
	}

	public List<SM_Package> getPackageList() {
		return packageList;
	}

	public int getPackageCount() {
		return packageList.size();
	}

	// method used in tests
	public CompilationUnit createCU(String filePath) {
		String fileToString = FileManager.getInstance().readFileToString(filePath);
		int startingIndex = filePath.lastIndexOf(File.separatorChar);
		unitName = filePath.substring(startingIndex + 1);

		return createAST(fileToString, unitName);
	}

	private void parseAllPackages() {
		for (SM_Package pkg : packageList) {
			pkg.parse();
		}
	}

	public Graph getHierarchyGraph() {
		return hierarchyGraph;
	}

	public Graph getDependencyGraph() {
		return dependencyGraph;
	}

	private void createPackageObjects() {
		checkNotNull(compilationUnitList);
		String packageName;
		for (CompilationUnit unit : compilationUnitList) {
			if (unit.getPackage() != null) {
				packageName = unit.getPackage().getName().toString();
			} else {
				packageName = "(default package)";
			}
			SM_Package pkgObj = searchPackage(packageName);
			// If pkgObj is null, package has not yet created
			if (pkgObj == null) {
				pkgObj = new SM_Package(packageName, this, inputArgs);
				packageList.add(pkgObj);
			}
			pkgObj.addCompilationUnit(unit);
		}
	}

	private void checkNotNull(List<CompilationUnit> list) {
		if (list == null) {
			DJLogger.log("Application couldn't find any source code files in the specified path.");
			System.exit(1);
			DJLogger.log("Quitting..");
		}
	}

	private SM_Package searchPackage(String packageName) {
		for (SM_Package pkg : packageList) {
			if (pkg.getName().equals(packageName))
				return pkg;
		}
		return null;
	}

	private void createCompilationUnits() {
		try {
			sourceFileList = FileManager.getInstance().listFiles(inputArgs.getSourceFolder());
			for (String file : sourceFileList) {
				String fileToString = FileManager.getInstance().readFileToString(file);
				int startingIndex = file.lastIndexOf(File.separatorChar);
				unitName = file.substring(startingIndex + 1);
				CompilationUnit unit = createAST(fileToString, unitName);
				if (unit != null)
					compilationUnitList.add(unit);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	private CompilationUnit createAST(final String content, String unitName) {
		Document doc = new Document(content);
		final ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);
		parser.setUnitName(unitName);
		Map<String, String> options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
		parser.setCompilerOptions(options);
		String[] sources = { inputArgs.getSourceFolder() };
		parser.setEnvironment(null, sources, null, true);
		parser.setSource(doc.get().toCharArray());

		CompilationUnit cu = null;
		try {
			cu = (CompilationUnit) parser.createAST(null);
		} catch (NullPointerException ex) {
			// Consume it.
			// In some rare situations, the above statement in try block results in a
			// NullPointer exception.
			// I tried to figure out but it seems it is coming from the parser library.
			// Hence, leaving it silently.
		}
		if (cu != null)
			if (!cu.getAST().hasBindingsRecovery()) {
				System.out.println("Binding not activated.");
			}

		return cu;
	}

	// TODO : Duplicate code found in FileManager.
	// VIOLATION: Single Responsibility
//	private void getFileList(String path) {
//		File root = new File(path);
//		File[] list = root.listFiles();
//
//		if (list == null)
//			return;
//		for (File f : list) {
//			if (f.isDirectory()) {
//				getFileList(f.getAbsolutePath());
//			} else {
//
//				if (f.getName().endsWith(".java"))
//					sourceFileList.add(f.getAbsolutePath());
//			}
//		}
//		return;
//	}

	// VIOLATION: Single Responsibility
//	private String readFileToString(String sourcePath) {
//		try {
//			return new String(Files.readAllBytes(Paths.get(sourcePath)));
//		} catch (IOException e) {
//			e.printStackTrace();
//			return new String();
//		}
//	}

	@Override
	public void printDebugLog(PrintWriter writer) {
		print(writer, "Project: " + name);
		print(writer, "-------------------");
		for (SM_Package pkg : packageList) {
			pkg.printDebugLog(writer);
		}
	}

	@Override
	public void parse() {
		DJLogger.log("Parsing the source code ...");
		createCompilationUnits();
		createPackageObjects();
		parseAllPackages();
	}

	public void resolve() {
		DJLogger.log("Resolving symbols...");
		for (SM_Package pkg : packageList) {
			pkg.resolve();
		}
		hierarchyGraph.computeConnectedComponents();
		dependencyGraph.computeStronglyConnectedComponents();
	}

	public void computeMetrics() {
		DJLogger.log("Extracting metrics...");
		CSVUtils.initializeCSVDirectory(name, inputArgs.getOutputFolder());
		for (SM_Package pkg : packageList) {
			pkg.extractTypeMetrics();
		}
	}

	public void detectCodeSmells() {
		DJLogger.log("Extracting code smells...");
		for (SM_Package pkg : packageList) {
			pkg.extractCodeSmells();
		}
	}

}
