package mks.myworkspace.crm.transformer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.GoodsCategory;
import mks.myworkspace.crm.entity.Order;

@Slf4j
public class JpaTransformer_OrderSearch {

	public static List<Object[]> convert2D(List<Order> lstOrders, List<GoodsCategory> allGoodsCategory, List<Customer> allSenders) {
		if (lstOrders == null || lstOrders.isEmpty()) {
			return null;
		}

		List<Object[]> lstObject = new ArrayList<>();

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		for (Order order : lstOrders) {
			Object[] rowData = new Object[6];
			rowData[0] = order.getId();
			rowData[1] = order.getCode();
			rowData[2] = order.getDeliveryDate() != null ? dateFormat.format(order.getDeliveryDate()) : null;
			if (order.getGoodsCategory() != null) {
				Long goodsCategoryId = order.getGoodsCategory().getId();
				GoodsCategory matchingCategory = allGoodsCategory.stream()
						.filter(gc -> gc.getId().equals(goodsCategoryId)).findFirst().orElse(null);

				rowData[3] = matchingCategory != null ? matchingCategory.getName() : "Không xác định";
			} else {
				rowData[3] = "Không xác định";
			}
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
			rowData[5] = order.getTransportationMethod();
			
			//rowData[0] = order.getSender() != null ? order.getSender().getId() : null;// id của sender
			//rowData[1] = order.getReceiver() != null ? order.getReceiver().getId() : null; // id của receiver

			log.debug("Order Sender ID: {}, Receiver ID: {}", rowData[0], rowData[1]);

			lstObject.add(rowData);
		}

		log.debug("Converted dataset: {}", lstObject);
		return lstObject;
	}
}
