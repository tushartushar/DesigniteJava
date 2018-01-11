package Designite.smells.models;

public class DesignCodeSmell extends CodeSmell {
	
	private String projectName;
	private String packageName;
	private String typeName;
	private String smellName;
	
	public DesignCodeSmell(String projectName
			, String packageName
			, String typeName
			, String smellName) {
		this.projectName = projectName;
		this.packageName = packageName;
		this.typeName = typeName;
		this.smellName = smellName;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getTypeName() {
		return typeName;
	}

	public String getSmellName() {
		return smellName;
	}
	
	@Override
	public String toString() {
		return projectName
				+ "," + packageName
				+ "," + typeName
				+ "," + smellName
				+ "\n";
	}

}
