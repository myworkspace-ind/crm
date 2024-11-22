package mks.myworkspace.crm.transformer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.GoodsCategory;
import mks.myworkspace.crm.entity.Order;

@Slf4j
public class JpaTransformer_Order {

	public static List<Object[]> convert2D(List<Order> lstOrders, List<GoodsCategory> allGoodsCategory, List<Customer> allSenders) {
		if (lstOrders == null || lstOrders.isEmpty()) {
			return null;
		}

		List<Object[]> lstObject = new ArrayList<>();

		for (Order order : lstOrders) {
	        Object[] rowData = new Object[6];
	        rowData[0] = order.getId(); // ID
	        rowData[1] = order.getCode();// ma don hang
	        rowData[2] = formatDate(order.getDeliveryDate()); // ngay giao
	        
	        //rowData[3] = order.getGoodsCategory() != null ? order.getGoodsCategory().getId() : null; // loai hang hoa
	        if (order.getGoodsCategory() != null) {
	            Long goodsCategoryId = order.getGoodsCategory().getId();
	            GoodsCategory matchingCategory = allGoodsCategory.stream()
	                .filter(gc -> gc.getId().equals(goodsCategoryId))
	                .findFirst()
	                .orElse(null);

	            rowData[3] = matchingCategory != null ? matchingCategory.getName() : "Không xác định";
	        } else {
	            rowData[3] = "Không xác định";
	        }
	        
	        //rowData[4] = order.getSender() != null ? order.getSender().getId() : null; // thong tin nguoi gui
	        if (order.getSender() != null) {
	            Long senderId = order.getSender().getId();
	            Customer matchingSender = allSenders.stream()
	                .filter(gc -> gc.getId().equals(senderId))
	                .findFirst()
	                .orElse(null);

	            rowData[4] = matchingSender != null ? matchingSender.getContactPerson() : "Không xác định";
	        } else {
	            rowData[4] = "Không xác định";
	        }
	        
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