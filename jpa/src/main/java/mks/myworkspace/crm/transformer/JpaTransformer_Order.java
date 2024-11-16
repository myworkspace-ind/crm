package mks.myworkspace.crm.transformer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Order;

@Slf4j
public class JpaTransformer_Order {

	public static List<Object[]> convert2D(List<Order> lstOrders) {
		if (lstOrders == null || lstOrders.isEmpty()) {
			return null;
		}

		List<Object[]> lstObject = new ArrayList<>();

		for (Order order : lstOrders) {
	        Object[] rowData = new Object[6];
	        rowData[0] = order.getId(); // ID
	        rowData[1] = order.getCode();// ma don hang
	        rowData[2] = formatDate(order.getDeliveryDate()); // ngay giao
	        rowData[3] = order.getGoodsCategory().getName(); // loai hang hoa
	        rowData[4] = order.getSender().getContactPerson(); // thong tin nguoi gui
	        rowData[5] = order.getTransportationMethod(); // phuong tien van chuyen

	        // Log dữ liệu của từng hàng trong dataset
	        log.debug("Order row: ID = {}, Delivery Date = {}, Goods = {}, Customer = {}, Transportation Method = {}",
	                rowData[0], rowData[2], rowData[3], rowData[4], rowData[5]);

	        lstObject.add(rowData);
	    }
	    
	    log.debug("Converted dataset: {}", lstObject);
	    return lstObject;
	}

	private static String formatDate(Date date) {
		if (date == null) {
			return null;
		}
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
}