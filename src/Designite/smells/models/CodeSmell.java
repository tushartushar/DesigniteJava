package Designite.smells.models;

public class CodeSmell {
	
	private String projectName;
	private String packageName;
	
	public CodeSmell(String projectName, String packageName) {
		this.projectName = projectName;
		this.packageName = packageName;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getPackageName() {
		return packageName;
	}
	
}
