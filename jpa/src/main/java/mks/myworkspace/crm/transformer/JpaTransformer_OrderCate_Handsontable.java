package mks.myworkspace.crm.transformer;

import java.util.ArrayList;
import java.util.List;

import mks.myworkspace.crm.entity.OrderCategory;


public class JpaTransformer_OrderCate_Handsontable {
	public static List<Object[]> convert2D(List<OrderCategory> lstOrderCates) {
		if (lstOrderCates == null) {
			return null;
		}

		List<Object[]> lstObject = new ArrayList<>();

		for (OrderCategory orderCategory : lstOrderCates) {
			Object[] rowData = new Object[3];
			rowData[0] = orderCategory.getId();
			rowData[1] = orderCategory.getName();
			rowData[2] = orderCategory.getNote();

			lstObject.add(rowData);
		}

		return lstObject;
	}
}
