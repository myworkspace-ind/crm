package mks.myworkspace.crm.transformer;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.GoodsCategory;
import mks.myworkspace.crm.entity.Order;
import mks.myworkspace.crm.entity.OrderCategory;
import mks.myworkspace.crm.entity.OrderStatus;

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
		Customer sender = new Customer();
		sender.setId(Long.parseLong(jsonObject.getString("sender")));
		order.setSender(sender);

		// Receiver
		Customer receiver = new Customer();
		receiver.setId(Long.parseLong(jsonObject.getString("receiver")));
		order.setReceiver(receiver);

		GoodsCategory goodsCategory = new GoodsCategory();
		goodsCategory.setId(Long.parseLong(jsonObject.getString("goodsCategory")));
		order.setGoodsCategory(goodsCategory);

		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setId(Long.parseLong(jsonObject.getString("orderStatus")));
		order.setOrderStatus(orderStatus);

		OrderCategory orderCategory = new OrderCategory();
		orderCategory.setId(Long.parseLong(jsonObject.getString("orderCategory")));
		order.setOrderCategory(orderCategory);

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

		// Sender
		Customer sender = new Customer();
		sender.setId(jsonObject.getLong("senderName"));
		sender.setEmail(jsonObject.getString("senderEmail"));
		sender.setPhone(jsonObject.getString("senderPhone"));
		order.setSender(sender);

		GoodsCategory goodsCategory = new GoodsCategory();
		goodsCategory.setId(jsonObject.getLong("goodsCategory"));
		order.setGoodsCategory(goodsCategory);

		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setId(jsonObject.getLong("status"));
		order.setOrderStatus(orderStatus);
		return order;
	}

}
