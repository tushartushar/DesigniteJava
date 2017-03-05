package DesigniteTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Designite.InputArgs;
import Designite.SourceModel.SM_Method;
import Designite.SourceModel.SM_Package;
import Designite.SourceModel.SM_Project;
import Designite.SourceModel.SM_Type;

public class SM_MethodTest{
	//Set this path before executing tests
	private static String TESTS_PATH = "C:\\Users\\Alex\\workspace\\DesigniteJava\\tests\\TestFiles\\testBatchFile.txt";
	private List<SM_Method> methodList; 
	
	@Before
	public void setUp() throws IOException {
		SM_Project project = new SM_Project(new InputArgs(TESTS_PATH));
		project.parse();
		List<SM_Package> packageList = project.getPackageList();
		List<SM_Type> typeList = null; 
		for (SM_Package pkg: packageList) {
			typeList = pkg.getTypeList();
		}
		for (SM_Type type: typeList) {
			methodList = type.getMethodList();
		}
	}
	
	@Test
	public void checkMethodName() {
		for (SM_Method method:  methodList)
			assertEquals(method.getName(), "method");
	}
}
