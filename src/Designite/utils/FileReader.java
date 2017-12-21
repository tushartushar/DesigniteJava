package Designite.utils;

import java.io.File;
import java.util.ArrayList;

public class FileReader {
	private ArrayList<String> pathList = new ArrayList();
	private String sourcePath;

	public FileReader(String sourcePath) {
		this.sourcePath = sourcePath;
		listFiles(sourcePath);
	}

	public ArrayList<String> getPathList() {
		return pathList;
	}

	// keeping all java files from the given path in a list
	public void listFiles(String sourcePath) {
		File f = null;

		try {
			f = new File(sourcePath);

			if (f.isFile() && f.getAbsolutePath().endsWith(".java")) {
				pathList.add(f.getAbsolutePath());
			} else if (f.isDirectory()) {
				getFilesFromFolder(f.getAbsolutePath());
			} else {
				// help menu
				System.out.println("No file found to be analyzed.");
				System.out.println("Usage instructions: ");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// adding java files of a folder in the List
	public void getFilesFromFolder(String folderPath) {
		File f = new File(folderPath);
		File[] paths;

		paths = f.listFiles();

		for (File path : paths) {
			if (path.isFile() && path.getAbsolutePath().endsWith(".java")) {
				pathList.add(path.getAbsolutePath());
			} else if (path.isDirectory()) {
				getFilesFromFolder(path.getAbsolutePath());
			}
		}
	}
}
