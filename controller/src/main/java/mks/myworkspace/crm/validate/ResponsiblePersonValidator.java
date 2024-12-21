package mks.myworkspace.crm.validate;

import java.util.ArrayList;
import java.util.List;

import mks.myworkspace.crm.entity.ResponsiblePerson;
import mksgroup.java.common.CommonUtil;

public class ResponsiblePersonValidator {
	public static List<ResponsiblePerson> validateAndCleasing(List<Object[]> data) {
		List<ResponsiblePerson> paramList = new ArrayList<>();

		for (Object[] rowData : data) {
			if (CommonUtil.isNNNE(rowData)) {
				Long responsiblePersonId = parseResponsiblePersonId(rowData[0]);
				Long seqno = parseResponsiblePersonId(rowData[3]);
				ResponsiblePerson responsiblePerson = new ResponsiblePerson(responsiblePersonId, 
						(String) rowData[1], // name
						(String) rowData[2], // note
						seqno
				);
				paramList.add(responsiblePerson);
			}
		}

		return paramList;
	}

	private static Long parseResponsiblePersonId(Object id) {
		if (id == null) {
			return null;
		} else if (id instanceof Double) {
			return ((Double) id).longValue();
		} else if (id instanceof Long) {
			return (Long) id;
		} else if (id instanceof Integer) {
			return Long.valueOf((Integer) id);
		} else if (id instanceof String) {
			String strId = (String) id;
			return (strId.length() > 0) ? Long.valueOf(strId) : null;
		} else {
			throw new RuntimeException("Unknown data " + id.getClass());
		}
	}
}
