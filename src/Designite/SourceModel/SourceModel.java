package Designite.SourceModel;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import Designite.InputArgs;

public class SourceModel {

	private InputArgs inputArgs;
	private List<String> sourceFileList;
	public SourceModel(InputArgs argsObj) {
		this.inputArgs = argsObj;
		sourceFileList = new ArrayList<String>();
	}

	public void create() {
		/*FileReader files = new FileReader(sourcePath);
		
		for(String pathToAnalyze: files.getPathList()){
			System.out.println(pathToAnalyze);
			
			ASTCreator ast = new ASTCreator(pathToAnalyze);
			CompilationUnit unit = ast.createAST();
			
			ASTVisitor visitor = new ASTVisitor(unit);
			visitor.visitAST();*/
		getFileList(inputArgs.getSourceFolder());
		for(String file: sourceFileList) {
			ClassMetrics classMetrics = new ClassMetrics();
			String fileToString = readFileToString(file);
			CompilationUnit unit = classMetrics.createAST(fileToString);
			classMetrics.visitAST(unit);
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

	public String readFileToString(String sourcePath) {
		try {
		 			return new String(Files.readAllBytes(Paths.get(sourcePath)));
		} catch (IOException e) {
		 			e.printStackTrace();
		 			return new String();
		}
	}
}
