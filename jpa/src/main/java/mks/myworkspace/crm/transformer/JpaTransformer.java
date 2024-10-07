package mks.myworkspace.crm.transformer;

import java.util.ArrayList;
import java.util.List;

import mks.myworkspace.crm.entity.Customer;

public class JpaTransformer {
	public static List<Object[]> convert2D(List<Customer> lstCustomers) {
        if (lstCustomers == null) {
            return null;
        }

        List<Object[]> lstObject = new ArrayList<>();

        for (Customer customer : lstCustomers) {
            Object[] rowData = new Object[4];
            rowData[0] = customer.getId();
            rowData[1] = customer.getName();
            rowData[2] = customer.getAddress();
            rowData[3] = customer.getPhone();
            
            lstObject.add(rowData);
        }

        return lstObject;
    }

	/*
	 * private static String formatDate(Date date) { if (date == null) { return
	 * null; } return new SimpleDateFormat("yyyy-MM-dd").format(date); }
	 */
}
