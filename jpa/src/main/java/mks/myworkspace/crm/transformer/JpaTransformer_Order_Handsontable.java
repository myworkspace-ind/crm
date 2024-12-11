package mks.myworkspace.crm.transformer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import mks.myworkspace.crm.entity.Order;
import mks.myworkspace.crm.entity.OrderCategory;


public class JpaTransformer_Order_Handsontable {
	public static List<Object[]> convert2D(List<Order> orders) {
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
	                order.getSender().getContactPerson(),                         
	                order.getTransportationMethod(),                            
	            };

	            tableData.add(rowData);
	        }
	    }
	    return tableData;
	}
}
