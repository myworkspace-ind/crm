package mks.myworkspace.crm.validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.Interaction;
import mksgroup.java.common.CommonUtil;

public class InteractionValidator {
	public static List<Object[]> convertInteractionsToTableData(List<Interaction> interactions) {
	    List<Object[]> tableData = new ArrayList<>();
	    
	    // Định dạng ngày
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	    for (Interaction interaction : interactions) {
	        if (interaction != null) {
	            String formattedDate = interaction.getInteraction_date() != null 
	                ? sdf.format(interaction.getInteraction_date()) 
	                : "";

	            Object[] rowData = new Object[]{
		            interaction.getCustomer().getContactPerson(),  // Người trao đổi
	                formattedDate,                                 // Ngày
	                interaction.getContent(),                      // Nội dung trao đổi
	                interaction.getNext_plan(),                         // Kế hoạch tiếp theo
	                interaction.getId(),                            // Hành động (ID để xóa hoặc thực hiện các thao tác khác)
	            };

	            tableData.add(rowData);
	        }
	    }
	    return tableData;
	}	
	
	public static List<Interaction> validateAndCleasing(List<Object[]> data, Long customerId) {
	    List<Interaction> paramList = new ArrayList<>();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Định dạng ngày tháng tùy theo dữ liệu của bạn
	    
	    for (Object[] rowData : data) {
	        if (CommonUtil.isNNNE(rowData)) {
	        	try {
		            // Parse các trường cần thiết từ rowData
		        	// Kiểm tra xem rowData[0] có phải là String và chuyển đổi nó thành Date
	                String dateString = (String) rowData[1]; // Ngày dưới dạng String
	                Date date = dateFormat.parse(dateString); // Chuyển String thành Date
	                
		            String content = (String) rowData[2]; // Nội dung trao đổi
		            String nextPlan = (String) rowData[3]; // Kế hoạch tiếp theo
		            Long interactionId = null;
		            
		            if (rowData[4] != null) {
			            interactionId = Long.valueOf((Integer) rowData[4]);
		            }
		            else
		            {
		            	interactionId = -1L;
		            }
	
		            // Tạo đối tượng Interaction từ các trường đã parse
		            Interaction interaction = new Interaction(interactionId, date, content, nextPlan, customerId);
	
		            // Thêm đối tượng vào danh sách kết quả
		            paramList.add(interaction);
	        	} catch (ParseException e) {
	                // Xử lý ngoại lệ nếu dữ liệu ngày tháng không hợp lệ
	                System.err.println("Lỗi khi phân tích ngày: " + rowData[1]);
	            }
	        }
	    }

	    return paramList;
	}

}
