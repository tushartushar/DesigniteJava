package Designite;

import java.io.IOException;
import Designite.SourceModel.*;

public class Designite {
	public static void main(String[] args) throws IOException{

		//args[0]: filePath
		if (args.length != 1) {
			usage();
			throw new IllegalArgumentException();
		}
		InputArgs argsObj = new InputArgs(args[0]);
		SM_Project sourceModel = new SM_Project(argsObj);
		sourceModel.parse();
	}

	private static void usage() {
		System.err.println("First argument needs to be the path to a batch input file.");
		System.out.println("Usage instructions:");
		System.out.println("java Designite <Path to a batch input file>");
	}
}
