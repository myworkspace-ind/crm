package mks.myworkspace.crm.transformer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mks.myworkspace.crm.entity.Order;

public class JpaTransformer_Order {

	public static List<Object[]> convert2D(List<Order> lstOrders) {
		if (lstOrders == null) {
			return null;
		}

		List<Object[]> lstObject = new ArrayList<>();

		for (Order order : lstOrders) {
			Object[] rowData = new Object[5];
			rowData[0] = order.getId();// ma don hang
			rowData[1] = formatDate(order.getDeliveryDate());// ngay giao
			//rowData[2] = order.getGoodsCategory();// loai hang hoa
			rowData[3] = order.getCustomer();// thong tin nguoi gui
			rowData[4] = order.getTransportationMethod();// phuong tien van chuyen
			lstObject.add(rowData);
		}
		return lstObject;
	}

	private static String formatDate(Date date) {
		if (date == null) {
			return null;
		}
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
}