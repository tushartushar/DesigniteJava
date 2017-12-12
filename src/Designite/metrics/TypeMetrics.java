package Designite.metrics;

import java.util.List;

import Designite.SourceModel.AccessStates;
import Designite.SourceModel.SM_Field;

public class TypeMetrics {
	
	private int numOfFields;
	private int numOfPublicFields;
	
	private List<SM_Field> fields;
	
	public TypeMetrics(List<SM_Field> fields) {
		this.fields = fields;
		
		numOfFields = 0;
		numOfPublicFields = 0;
	}
	
	public void extractMetrics() {
		extractFieldMetrics();
	}
	
	private void extractFieldMetrics() {
		for (SM_Field field : fields) {
			numOfFields++;
			if (field.getAccessModifier() == AccessStates.PUBLIC) {
				numOfPublicFields++;
			}	
		}
	}

	public int getNumOfFields() {
		return numOfFields;
	}

	public int getNumOfPublicFields() {
		return numOfPublicFields;
	}
	
}
