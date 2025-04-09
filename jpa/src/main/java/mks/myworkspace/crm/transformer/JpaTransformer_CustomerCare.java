package mks.myworkspace.crm.transformer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.CustomerCare;
import mks.myworkspace.crm.entity.Interaction;

@Slf4j
public class JpaTransformer_CustomerCare {
	
	public static List<Object[]> convert2D_Customers(List<Customer> lstCustomers, int reminderDays, int reminderDaysCase2) {
		if (lstCustomers == null || lstCustomers.isEmpty()) {
			return null;
		}

		List<Object[]> lstObject = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");

		for (Customer customerCare : lstCustomers) {
	        Object[] rowData = new Object[10];//SL cột 
	        
	        rowData[0] = customerCare.getId(); // ID
	        rowData[1] = customerCare.getId(); // ID
	        rowData[2] = customerCare.getCompanyName();
	        rowData[3] = customerCare.getContactPerson();
	        rowData[4] = customerCare.getMainStatus().getName();
	        
	        //Tính toán ngày nhắc nhở (Ngày nhắc nhở = Ngày tạo mới KH + reminderDays)
//	        if (customerCare.getCreatedAt() != null) {
//                LocalDateTime reminderDate = customerCare.getCreatedAt().plusDays(reminderDays);
//                rowData[9] = reminderDate.format(formatter);
//            } else {
//                rowData[9] = "Không xác định"; // Nếu không có ngày tạo
//            }
	        
	        String mainStatus = rowData[4].toString();

			if ("Mới".equals(mainStatus)) {
				// Nếu là khách hàng mới và chưa có interaction nào
				if (customerCare.getInteractions() == null || customerCare.getInteractions().isEmpty()) {
					if (customerCare.getCreatedAt() != null) {
						LocalDateTime reminderDate = customerCare.getCreatedAt().plusDays(reminderDays);
						rowData[9] = reminderDate.format(formatter);
					} else {
						rowData[9] = "Không xác định";
					}
				} else {
					rowData[9] = "Đã có interaction";
				}
			} else if ("Tiềm năng".equals(mainStatus)) {
				// Nếu là Tiềm năng và có interaction, lấy interaction mới nhất
				if (customerCare.getInteractions() != null && !customerCare.getInteractions().isEmpty()) {
					Optional<Interaction> latestInteraction = customerCare.getInteractions().stream()
							.filter(i -> i.getCreatedAt() != null).max(Comparator.comparing(Interaction::getCreatedAt));

					if (latestInteraction.isPresent()) {
						LocalDateTime reminderDate = latestInteraction.get().getCreatedAt().plusDays(reminderDaysCase2);
						rowData[9] = reminderDate.format(formatter);
					} else {
						rowData[9] = "Không xác định";
					}
				} else {
					rowData[9] = "Không có interaction";
				}
			} else {
				rowData[9] = "Không xác định";
			}
	        
	        lstObject.add(rowData);
	    }
	    
	    log.debug("Converted dataset: {}", lstObject);
	    return lstObject;
	}
	
	public static List<Object[]> convert2D_CustomerCares(List<CustomerCare> lstCustomerCares, List<Customer> allCustomers) {
		if (lstCustomerCares == null || lstCustomerCares.isEmpty()) {
			return null;
		}

		List<Object[]> lstObject = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

		for (CustomerCare customerCare : lstCustomerCares) {
	        Object[] rowData = new Object[10];//SL cột 
	        
	        rowData[0] = customerCare.getId(); // ID

	        if (customerCare.getCustomer() != null) {
	            Long customerId = customerCare.getCustomer().getId();
	            rowData[1] = customerId;
	            
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
	        rowData[6] = customerCare.getPriority();
	        rowData[7] = customerCare.getCareStatus(); 
	        
	        if (customerCare.getRemindDate() != null) {
                LocalDateTime reminderDate = customerCare.getRemindDate();
                rowData[9] = reminderDate.format(formatter);
            } else {
                rowData[9] = "Không xác định"; // Nếu không có ngày tạo
            }

	        
	        lstObject.add(rowData);
	    }
	    
	    log.debug("Converted dataset: {}", lstObject);
	    return lstObject;
	}
	
}


