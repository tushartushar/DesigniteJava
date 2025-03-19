package Designite.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static FileManager fileManager;

    /**
     * The Singleton Design principle has been applied to manage one object only throughout the program execution.
     * @return {@code Instance of} {@link FileManager}
     */
    public static FileManager getInstance() {
        if (fileManager == null) {
            fileManager = new FileManager();
        }
        return fileManager;
    }

    private FileManager() {
    }

    public FileManager(String sourcePath) {
        listFiles(sourcePath);
    }

    /**
     * Returns list of all files from a nested folder, recursively traversing from
     * the deepest level of folder tree.
     * @param sourcePath
     * @return
     */
    public List<String> listFiles(String sourcePath) {
        List<String> sourceFileList = new ArrayList<String>();
        this.listFiles(sourcePath, sourceFileList);
        return sourceFileList;
    }

    /**
     * Returns list of all files from a nested folder, recursively traversing tree from
     * the deepest level of folder tree.
     * @param sourcePath
     * @param sourceFileList
     * @return List
     */
    private List<String> listFiles(String sourcePath, List<String> sourceFileList) {
        File file;
        try {
            file = new File(sourcePath);
            if (file.isFile() && file.getName().endsWith(".java")) {
                sourceFileList.add(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                sourceFileList.addAll(listFilesFromFolder(file.getAbsolutePath()));
            } else {
                // help menu
                // This block is a dead code. Moreover, It does not provide a resolution if the files are not found.
                System.out.println("No file found to be analyzed.");
                System.out.println("Usage instructions: ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sourceFileList;
    }

    /**
     * Returns List of files in the specified {@code folderPath}
     * @param folderPath
     * @return List
     */
    public List<String> listFilesFromFolder(String folderPath) {
        File file = new File(folderPath);
        File[] paths;
        List<String> fileList = new ArrayList<String>();
        paths = file.listFiles();
        for (File path : paths) {
            if (path.isFile() && path.getAbsolutePath().endsWith(".java")) {
                fileList.add(path.getAbsolutePath());
            } else if (path.isDirectory()) {
                fileList.addAll(listFilesFromFolder(path.getAbsolutePath()));
            }
        }
        return fileList;
    }


    /**
     * Reads all lines from the file specified in {@code sourcePath}.
     * @param sourcePath
     * @return
     */
    public String readFileToString(String sourcePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(sourcePath)));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * Creates a new file at the specified path if it doesn't exist.
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */
    public PrintWriter createFileIfNotExists(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent.isDirectory() && !parent.exists()) {
            parent.mkdirs();
        }
        return new PrintWriter(file);
    }


}
