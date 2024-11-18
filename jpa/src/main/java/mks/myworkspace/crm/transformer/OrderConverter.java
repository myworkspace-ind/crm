package mks.myworkspace.crm.transformer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.GoodsCategory;
import mks.myworkspace.crm.entity.Order;
import mks.myworkspace.crm.entity.OrderStatus;

public class OrderConverter {
	public static Order convertJsonToOrder(String json) {
		JSONObject jsonObject = new JSONObject(json);
		Order order = new Order();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		order.setId(jsonObject.getLong("id"));
		order.setCode(jsonObject.getString("orderCode"));
		try {
			order.setCreateDate(dateFormat.parse(jsonObject.getString("createDate")));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			order.setDeliveryDate(dateFormat.parse(jsonObject.getString("deliveryDate")));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		order.setCustomerRequirement(jsonObject.getString("requirement"));
		order.setTransportationMethod(jsonObject.getString("transport"));
		
		//Sender
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

	private static String formatDate(Date date) {
		if (date == null) {
			return null;
		}
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

}
