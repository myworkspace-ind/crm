package mks.myworkspace.crm.validate;

import java.util.ArrayList;
import java.util.List;

import mks.myworkspace.crm.common.AppConst;
import mks.myworkspace.crm.common.CommonUtil;
import mks.myworkspace.crm.entity.Customer;

public class CustomerValidator {

	public static List<Customer> validateAndCleasing(List<Object[]> data) {
		List<Customer> paramList = new ArrayList<>();

	    for (Object[] rowData : data) {
	        if (CommonUtil.isNNNE(rowData)) {
	            Long taskId = parseTaskId(rowData[0]);

	            Integer statusIndex = AppConst.STATUS_MAP.get((String) rowData[7]);

	            Customer customer = new Customer(
	            	    taskId,               // id
	            	    (String) rowData[1],  // siteId
	            	    (String) rowData[2],  // name
	            	    (String) rowData[3],  // address
	            	    (String) rowData[4]   // phone
	            	);
	            
	            paramList.add(customer);
	        }
	    }

	    return paramList;
	}

	private static Long parseTaskId(Object id) {
		if (id == null) {
			return null;
		} else if (id instanceof Double) {
			return ((Double) id).longValue();
		} else if (id instanceof Long) {
			return (Long) id;
		} else if (id instanceof Integer) {
			return Long.valueOf((Integer) id);
		} else if (id instanceof String) {
			String strId = (String) id;
			return (strId.length() > 0) ? Long.valueOf(strId) : null;
		} else {
			throw new RuntimeException("Unknown data " + id.getClass());
		}
	}
}