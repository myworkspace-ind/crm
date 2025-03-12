package mks.myworkspace.crm.transformer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.CustomerCare;

@Slf4j
public class JpaTransformer_CustomerCare {
	public static List<Object[]> convert2D(List<CustomerCare> lstCustomerCares, List<Customer> allCustomers) {
		if (lstCustomerCares == null || lstCustomerCares.isEmpty()) {
			return null;
		}

		List<Object[]> lstObject = new ArrayList<>();

		for (CustomerCare customerCare : lstCustomerCares) {
	        Object[] rowData = new Object[10];//SL cột 
	        
	        rowData[0] = customerCare.getId(); // ID
	        
	        if (customerCare.getCustomer() != null) {
	            Long customerId = customerCare.getCustomer().getId();
	            Customer matchingCustomer = allCustomers.stream()
	                .filter(gc -> gc.getId().equals(customerId))
	                .findFirst()
	                .orElse(null);

	            rowData[2] = matchingCustomer != null ? matchingCustomer.getCompanyName() : "Không xác định";
	        } else {
	            rowData[2] = "Không xác định";
	        }
	        
	        if (customerCare.getCustomer() != null) {
	        	Long customerId = customerCare.getCustomer().getId();
	            Customer matchingCustomer = allCustomers.stream()
	                .filter(gc -> gc.getId().equals(customerId))
	                .findFirst()
	                .orElse(null);

	            rowData[3] = matchingCustomer != null ? matchingCustomer.getContactPerson() : "Không xác định";
	        } else {
	            rowData[3] = "Không xác định";
	        }
	        
	        if (customerCare.getCustomer() != null) {
	        	Long customerId = customerCare.getCustomer().getId();
	            Customer matchingCustomer = allCustomers.stream()
	                .filter(gc -> gc.getId().equals(customerId))
	                .findFirst()
	                .orElse(null);

	            rowData[4] = matchingCustomer != null ? matchingCustomer.getMainStatus().getName() : "Không xác định";
	        } else {
	            rowData[4] = "Không xác định";
	        }
	        
	        rowData[7] = customerCare.getCareStatus(); 
	        rowData[9] = formatDate(customerCare.getRemindDate()); 

	        
	        lstObject.add(rowData);
	    }
	    
	    log.debug("Converted dataset: {}", lstObject);
	    return lstObject;
	}
	
	private static String formatDate(Date date) {
		if (date == null) {
			return null;
		}
		return new SimpleDateFormat("dd/MM/yyyy").format(date);
	}
}
