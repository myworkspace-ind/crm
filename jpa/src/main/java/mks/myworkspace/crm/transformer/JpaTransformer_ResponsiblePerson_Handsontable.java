package mks.myworkspace.crm.transformer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import mks.myworkspace.crm.entity.ResponsiblePerson;

public class JpaTransformer_ResponsiblePerson_Handsontable {
	public static List<Object[]> convert2D(List<ResponsiblePerson> lstResponPerson) {
		if (lstResponPerson == null) {
			return null;
		}

		List<Object[]> lstObject = new ArrayList<>();

		for (ResponsiblePerson responsiblePerson : lstResponPerson) {
			Object[] rowData = new Object[3];
			rowData[0] = responsiblePerson.getId();
			rowData[1] = responsiblePerson.getName();
			rowData[2] = responsiblePerson.getNote();

			lstObject.add(rowData);
		}

		return lstObject;
		
	}
	public static <T> List<Object[]> convert2DGeneric(List<T> lst, List<String> fields) {
		if (lst == null || lst.isEmpty()) {
	        return null;
	    }

	    List<Object[]> lstObject = new ArrayList<>();
	    Class<?> clazz = lst.get(0).getClass();

	    // Tạo danh sách các trường theo thứ tự chỉ định
	    List<Field> orderedFields = new ArrayList<>();
	    for (String fieldName : fields) {
	        try {
	            Field field = clazz.getDeclaredField(fieldName);
	            field.setAccessible(true); // Cho phép truy cập vào các trường private
	            orderedFields.add(field);
	        } catch (NoSuchFieldException e) {
	            throw new IllegalArgumentException("Field not found: " + fieldName, e);
	        }
	    }

	    // Duyệt qua từng đối tượng và lấy giá trị theo thứ tự các trường
	    for (T item : lst) {
	        Object[] rowData = new Object[orderedFields.size()];
	        int index = 0;
	        for (Field field : orderedFields) {
	            try {
	                rowData[index++] = field.get(item);
	            } catch (IllegalAccessException e) {
	                e.printStackTrace();
	            }
	        }
	        lstObject.add(rowData);
	    }

	    return lstObject;
	}

	
}
