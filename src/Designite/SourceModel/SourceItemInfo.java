package Designite.SourceModel;

public class SourceItemInfo {
	
	private String projectName;
	private String packageName;
	private String typeName;
	private String methodName;
	
	public SourceItemInfo(String projectName
			, String packageName) {
		this.projectName = projectName;
		this.packageName = packageName;
	}
	
	public SourceItemInfo(String projectName
			, String packageName
			, String typeName) {
		this(projectName, packageName);
		this.typeName = typeName;
	}
	
	public SourceItemInfo(String projectName
			, String packageName
			, String typeName
			, String methodName) {
		this(projectName, packageName, typeName);
		this.methodName = methodName;
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

	public String getMethodName() {
		return methodName;
	}
	
	
}
