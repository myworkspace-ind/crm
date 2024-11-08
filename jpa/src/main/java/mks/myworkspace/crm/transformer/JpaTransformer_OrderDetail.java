package mks.myworkspace.crm.transformer;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Order;

@Slf4j
public class JpaTransformer_OrderDetail {

	public static Object[] convert2D(Order order) {
		if (order == null) {
			return null;
		}

		Object[] rowData = new Object[10];
		rowData[0] = order.getId();
		rowData[1] = order.getCode();
		rowData[2] = formatDate(order.getDeliveryDate());
		rowData[3] = formatDate(order.getCreateDate());
		rowData[4] = order.getOrderStatus().getName();
		rowData[5] = order.getGoodsCategory() != null ? order.getGoodsCategory().getName() : null;
		rowData[6] = order.getCustomer() != null ? order.getCustomer().getName() : null;
		rowData[7] = order.getCustomer() != null ? order.getCustomer().getPhone() : null;
		rowData[8] = order.getTransportationMethod();
		rowData[9] = order.getCustomerRequirement();

		log.debug(
				"Order detail row: ID = {}, Code = {}, Delivery Date = {}, Category = {}, Customer = {}, Transportation Method = {}",
				rowData[0], rowData[1], rowData[2], rowData[3], rowData[4], rowData[5]);

		return rowData;
	}

	private static String formatDate(Date date) {
		if (date == null) {
			return null;
		}
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
}