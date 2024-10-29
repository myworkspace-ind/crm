package mks.myworkspace.crm.validate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.GoodsCategory;
import mks.myworkspace.crm.entity.Order;
import mksgroup.java.common.CommonUtil;

public class OrderValidator {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static List<Order> validateAndCleasing(List<Object[]> data) {
	    List<Order> paramList = new ArrayList<>();

	    for (Object[] rowData : data) {
	        if (CommonUtil.isNNNE(rowData)) {
	            String orderId = parseOrderId(rowData[0]); 
	            Date deliveryDate = parseDate(rowData[1]); 
	            
	            GoodsCategory goodsCategory = new GoodsCategory(); 
                goodsCategory.setId((String) rowData[2]);  

                Customer customer = new Customer();
                customer.setId((Long) rowData[3]);
                
                String transportationMethod = (String) rowData[5];
                
	            Order order = new Order(
	                orderId,
	                deliveryDate,
	                goodsCategory,
	                customer,
	                transportationMethod
	            );

	            paramList.add(order);
	        }
	    }

	    return paramList;
	}

	private static String parseOrderId(Object id) {
		return null;
//		if (id == null) {
//			return null;
//		} else if (id instanceof Double) {
//			return ((Double) id).longValue();
//		} else if (id instanceof Long) {
//			return (Long) id;
//		} else if (id instanceof Integer) {
//			return Long.valueOf((Integer) id);
//		} else if (id instanceof String) {
//			String strId = (String) id;
//			return (strId.length() > 0) ? Long.valueOf(strId) : null;
//		} else {
//			throw new RuntimeException("Unknown data " + id.getClass());
//		}
	}
//
	private static Date parseDate(Object dateObj) {
		return null;
//		if (dateObj == null || ((String) dateObj).trim().isEmpty()) {
//			return null;
//		}
//		try {
//			return dateFormat.parse((String) dateObj);
//		} catch (ParseException e) {
//			e.printStackTrace();
//			throw new RuntimeException("Could not parse date: " + e.getMessage());
//		}
	}
}
