package mks.myworkspace.crm.transformer;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.GoodsCategory;
import mks.myworkspace.crm.entity.Order;
import mks.myworkspace.crm.entity.OrderCategory;
import mks.myworkspace.crm.entity.OrderStatus;

@Slf4j
public class OrderConverter {
	public static Order convertJsonToOrder_Create(String json) {
		JSONObject jsonObject = new JSONObject(json);
		Order order = new Order();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		// order.setId(jsonObject.getLong("id"));
		order.setCode(jsonObject.getString("orderCode"));
		try {
			order.setCreateDate(dateFormat.parse(jsonObject.getString("createDate")));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			order.setDeliveryDate(dateFormat.parse(jsonObject.getString("deliveryDate")));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		order.setCustomerRequirement(jsonObject.getString("requirement"));
		order.setTransportationMethod(jsonObject.getString("transportMethod"));
		order.setAddress(jsonObject.getString("address"));

		// Sender
		log.debug("Fetching sender information...");
		Customer sender = new Customer();
		String senderId = jsonObject.getString("sender");
		log.debug("Sender ID from JSON: {}", senderId);
		sender.setId(Long.parseLong(senderId));
		log.debug("Sender object created: {}", sender);
		order.setSender(sender);

		// Receiver
		log.debug("Fetching receiver information...");
		Customer receiver = new Customer();
		String receiverId = jsonObject.getString("receiver");
		log.debug("Receiver ID from JSON: {}", receiverId);
		receiver.setId(Long.parseLong(receiverId));
		log.debug("Receiver object created: {}", receiver);
		order.setReceiver(receiver);

		// Goods Category
		log.debug("Fetching goods category...");
		GoodsCategory goodsCategory = new GoodsCategory();
		String goodsCategoryId = jsonObject.getString("goodsCategory");
		log.debug("Goods Category ID from JSON: {}", goodsCategoryId);
		goodsCategory.setId(Long.parseLong(goodsCategoryId));
		log.debug("Goods Category object created: {}", goodsCategory);
		order.setGoodsCategory(goodsCategory);

		// Order Status
		log.debug("Fetching order status...");
		OrderStatus orderStatus = new OrderStatus();
		String orderStatusId = jsonObject.getString("orderStatus");
		log.debug("Order Status ID from JSON: {}", orderStatusId);
		orderStatus.setId(Long.parseLong(orderStatusId));
		log.debug("Order Status object created: {}", orderStatus);
		order.setOrderStatus(orderStatus);

		// Order Category
		log.debug("Fetching order category...");
		OrderCategory orderCategory = new OrderCategory();
		String orderCategoryId = jsonObject.getString("orderCategory");
		log.debug("Order Category ID from JSON: {}", orderCategoryId);
		orderCategory.setId(Long.parseLong(orderCategoryId));
		log.debug("Order Category object created: {}", orderCategory);
		order.setOrderCategory(orderCategory);

		log.debug("Order object created: {}", order);

		return order;
	}

	public static Order convertJsonToOrder_Update(String json) {
		JSONObject jsonObject = new JSONObject(json);
		Order order = new Order();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		order.setId(jsonObject.getLong("id"));
		order.setCode(jsonObject.getString("orderCode"));
		try {
			order.setCreateDate(dateFormat.parse(jsonObject.getString("createDate")));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			order.setDeliveryDate(dateFormat.parse(jsonObject.getString("deliveryDate")));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		order.setCustomerRequirement(jsonObject.getString("requirement"));
		order.setTransportationMethod(jsonObject.getString("transport"));
		order.setAddress(jsonObject.getString("address"));

		// Sender
		Customer sender = new Customer();
		sender.setId(jsonObject.getLong("senderName"));
		sender.setEmail(jsonObject.getString("senderEmail"));
		sender.setPhone(jsonObject.getString("senderPhone"));
		order.setSender(sender);

		// Receiver
		Customer receiver = new Customer();
		receiver.setId(jsonObject.getLong("receiverName"));
		receiver.setEmail(jsonObject.getString("receiverEmail"));
		receiver.setPhone(jsonObject.getString("receiverPhone"));
		order.setReceiver(receiver);

		GoodsCategory goodsCategory = new GoodsCategory();
		goodsCategory.setId(jsonObject.getLong("goodsCategory"));
		order.setGoodsCategory(goodsCategory);

		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setId(jsonObject.getLong("status"));
		order.setOrderStatus(orderStatus);
		return order;
	}
	
	public static Long convertJsonToOrder_Delete(String json) {
		JSONObject jsonObject = new JSONObject(json);
		Long orderId = jsonObject.getLong("id");
		return orderId;
	}
	
	public static Order convertJsonToOrder_UpdateOrderStatus(String json) {
		JSONObject jsonObject = new JSONObject(json);
		Order order = new Order();

		order.setId(jsonObject.getLong("id"));

		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setId(jsonObject.getLong("orderStatus"));
		order.setOrderStatus(orderStatus);
		
		return order;
	}
}
