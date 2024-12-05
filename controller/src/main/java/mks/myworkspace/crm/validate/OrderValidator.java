package mks.myworkspace.crm.validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.Order;
import mksgroup.java.common.CommonUtil;

public class OrderValidator {
	public static List<Object[]> convertOrdersToTableData(List<Order> orders) {
	    List<Object[]> tableData = new ArrayList<>();
	    
	    // Định dạng ngày
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	    for (Order order : orders) {
	        if (order != null) {
	            String formattedDate = order.getDeliveryDate() != null 
	                ? sdf.format(order.getDeliveryDate()) 
	                : "";

	            Object[] rowData = new Object[]{
		            order.getId(),  // Lay id
	                formattedDate,        // Ngay van chuyen                        
	                order.getGoodsCategory().getName(),                      
	                order.getName(),                         
	                order.getTransportationMethod(),                            
	            };

	            tableData.add(rowData);
	        }
	    }
	    return tableData;
	}
}
