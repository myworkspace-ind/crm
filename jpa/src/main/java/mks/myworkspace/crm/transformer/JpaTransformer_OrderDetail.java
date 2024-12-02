package mks.myworkspace.crm.transformer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.GoodsCategory;
import mks.myworkspace.crm.entity.Order;
import mks.myworkspace.crm.entity.OrderCategory;
import mks.myworkspace.crm.entity.OrderStatus;

@Slf4j
public class JpaTransformer_OrderDetail {

	public static Object[] convert2D(Order order, List<OrderStatus> allOrderStatuses,
			List<GoodsCategory> allGoodsCategory, List<Customer> allSenders, List<Customer> allReceivers, List<OrderCategory> allOrderCategories) {
		if (order == null) {
			return null;
		}

		Object[] rowData = new Object[17];
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

		// Sender
		if (allSenders != null && !allSenders.isEmpty()) {
			Object[][] senderData = convert2D_Customer(allSenders);
			rowData[10] = senderData;
		}
		rowData[11] = order.getSender() != null ? order.getSender().getId() : null;

		// Receiver
		if (allReceivers != null && !allReceivers.isEmpty()) {
			Object[][] receiverData = convert2D_Customer(allReceivers);
			rowData[12] = receiverData;
		}
		rowData[13] = order.getReceiver() != null ? order.getReceiver().getId() : null;

		rowData[14] = order.getAddress();

		// Order Category
		if (allOrderCategories != null && !allOrderCategories.isEmpty()) {
			Object[][] orderCategoryData = convert2D_OrderCategory(allOrderCategories);
			rowData[15] = orderCategoryData;
		} else {
			rowData[15] = null;
		}
		rowData[16] = order.getOrderCategory() != null ? order.getOrderCategory().getId() : null;

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

		Object[][] rowData = new Object[allOrderStatuses.size()][2]; // chứa ID và Name của OrderStatus

		for (int i = 0; i < allOrderStatuses.size(); i++) {
			OrderStatus status = allOrderStatuses.get(i);
			rowData[i][0] = status.getId();
			rowData[i][1] = status.getName();
		}

		return rowData;
	}

	public static Object[][] convert2D_GoodsCategory(List<GoodsCategory> allGoodsCategory) {
		if (allGoodsCategory == null || allGoodsCategory.isEmpty()) {
			return new Object[0][];
		}

		Object[][] rowData = new Object[allGoodsCategory.size()][2];

		for (int i = 0; i < allGoodsCategory.size(); i++) {
			GoodsCategory goodsCategory = allGoodsCategory.get(i);
			rowData[i][0] = goodsCategory.getId();
			rowData[i][1] = goodsCategory.getName();
		}

		return rowData;
	}

	public static Object[][] convert2D_OrderCategory(List<OrderCategory> allOrderCategory) {
		if (allOrderCategory == null || allOrderCategory.isEmpty()) {
			return new Object[0][];
		}

		Object[][] rowData = new Object[allOrderCategory.size()][2];

		for (int i = 0; i < allOrderCategory.size(); i++) {
			OrderCategory orderCategory = allOrderCategory.get(i);
			rowData[i][0] = orderCategory.getId();
			rowData[i][1] = orderCategory.getName();
		}

		return rowData;
	}

	public static Object[][] convert2D_Customer(List<Customer> allCustomer) {
		if (allCustomer == null || allCustomer.isEmpty()) {
			return new Object[0][];
		}

		Object[][] rowData = new Object[allCustomer.size()][2];

		for (int i = 0; i < allCustomer.size(); i++) {
			Customer customer = allCustomer.get(i);
			rowData[i][0] = customer.getId();
			rowData[i][1] = customer.getContactPerson();
		}

		return rowData;
	}
}