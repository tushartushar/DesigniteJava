package Designite;

import java.io.IOException;
import Designite.SourceModel.*;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class Application {
	public static void main(String[] args) throws IOException{

		//args[0]: filePath
		if (args.length != 1) {
			usage();
			throw new IllegalArgumentException();
		}
		SourceModel sourceModel = new SourceModel();
		sourceModel.create(args[0]);
	}

	private static void usage() {
		System.err.println("First argument needs to be the path.");
		System.out.println("Usage instructions:");
		System.out.println("java Designite <file/folder of Java source code>");
	}
}
