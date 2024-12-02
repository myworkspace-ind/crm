package mks.myworkspace.crm.validate;

import java.util.ArrayList;
import java.util.List;

import mks.myworkspace.crm.entity.OrderCategory;
import mksgroup.java.common.CommonUtil;

public class OrderCategoryValidator {
	public static List<OrderCategory> validateAndCleasing(List<Object[]> data) {
		List<OrderCategory> paramList = new ArrayList<>();

		for (Object[] rowData : data) {
			if (CommonUtil.isNNNE(rowData)) {
				Long OrderCateId = parseOrderCategoryId(rowData[0]);

				OrderCategory orderCategory = new OrderCategory(OrderCateId, 
						(String) rowData[1], // name
						(String) rowData[2] // note
				);

				paramList.add(orderCategory);
			}
		}

		return paramList;
	}

	private static Long parseOrderCategoryId(Object id) {
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
