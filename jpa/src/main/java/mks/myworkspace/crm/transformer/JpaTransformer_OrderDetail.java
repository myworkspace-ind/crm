package mks.myworkspace.crm.transformer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.GoodsCategory;
import mks.myworkspace.crm.entity.Order;
import mks.myworkspace.crm.entity.OrderStatus;

@Slf4j
public class JpaTransformer_OrderDetail {

	public static Object[] convert2D(Order order, List<OrderStatus> allOrderStatuses, List<GoodsCategory> allGoodsCategory) {
		if (order == null) {
			return null;
		}

		Object[] rowData = new Object[14];
		rowData[0] = order.getId();
		rowData[1] = order.getCode();
		rowData[2] = formatDate(order.getDeliveryDate());
		rowData[3] = formatDate(order.getCreateDate());
		rowData[4] = order.getTransportationMethod();
		rowData[5] = order.getCustomerRequirement();

		// Include both ID and name of reference tables
		// Order Status
		if (allOrderStatuses != null && !allOrderStatuses.isEmpty()) {
			Object[][] orderStatusData = convert2D_OrderStatus(allOrderStatuses);
			rowData[6] = orderStatusData; 
		} else {
			rowData[6] = null;
		}
		
		rowData[7] = order.getOrderStatus() != null ? order.getOrderStatus().getId() : null;
		
		// GoodsCategory
		if (allGoodsCategory != null && !allGoodsCategory.isEmpty()) {
			Object[][] goodsCategoryData = convert2D_GoodsCategory(allGoodsCategory);
			rowData[8] = goodsCategoryData; 
		} else {
			rowData[8] = null;
		}
		
		rowData[9] = order.getGoodsCategory() != null ? order.getGoodsCategory().getId() : null;
		
		// Customer
		rowData[10] = order.getCustomer() != null ? order.getCustomer().getId() : null;
		rowData[11] = order.getCustomer() != null ? order.getCustomer().getContactPerson() : null;
		rowData[12] = order.getCustomer() != null ? order.getCustomer().getPhone() : null;
		rowData[13] = order.getCustomer() != null ? order.getCustomer().getEmail() : null;

//		log.debug(
//				"Order detail row: ID = {}, Code = {}, Delivery Date = {}, Category = {}, Customer = {}, Transportation Method = {}",
//				rowData[0], rowData[1], rowData[2], rowData[3], rowData[4], rowData[5]);

		return rowData;
	}

	private static String formatDate(Date date) {
		if (date == null) {
			return null;
		}
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	public static Object[][] convert2D_OrderStatus(List<OrderStatus> allOrderStatuses) {
		if (allOrderStatuses == null || allOrderStatuses.isEmpty()) {
			return new Object[0][];
		}

		Object[][] rowData = new Object[allOrderStatuses.size()][2]; //chứa ID và Name của OrderStatus

		for (int i = 0; i < allOrderStatuses.size(); i++) {
			OrderStatus status = allOrderStatuses.get(i);
			rowData[i][0] = status.getId();
			rowData[i][1] = status.getName();
		}

		return rowData;
	}
	
	public static Object[][] convert2D_GoodsCategory(List<GoodsCategory> allGoodsCategory){
		if(allGoodsCategory == null || allGoodsCategory.isEmpty()) {
			return new Object[0][];
		}
		
		Object[][] rowData = new Object[allGoodsCategory.size()][2];
		
		for(int i = 0; i < allGoodsCategory.size(); i++) {
			GoodsCategory goodsCategory = allGoodsCategory.get(i);
			rowData[i][0] = goodsCategory.getId();
			rowData[i][1] = goodsCategory.getName();
		}
		
		return rowData;
	}
}