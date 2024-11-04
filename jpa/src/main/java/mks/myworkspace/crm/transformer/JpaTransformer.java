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
			Object[] rowData = new Object[13];

			rowData[0] = customer.getId();
			rowData[1] = customer.getSiteId();
			rowData[2] = customer.getCompanyName();
			rowData[3] = customer.getContactPerson();
			rowData[4] = customer.getEmail();
			rowData[5] = customer.getPhone();
			rowData[6] = customer.getAddress();
			rowData[7] = customer.getProfession();
			rowData[8] = customer.getMainStatus() != null ? customer.getMainStatus().getName() : null;
			rowData[9] = customer.getSubStatus() != null ? customer.getSubStatus().getName() : null;
			rowData[10] = customer.getResponsiblePerson();
			rowData[11] = customer.getCreatedAt();
			rowData[12] = customer.getNote();

			lstObject.add(rowData);
        }

        return lstObject;
    }

	/*
	 * private static String formatDate(Date date) { if (date == null) { return
	 * null; } return new SimpleDateFormat("yyyy-MM-dd").format(date); }
	 */
}
