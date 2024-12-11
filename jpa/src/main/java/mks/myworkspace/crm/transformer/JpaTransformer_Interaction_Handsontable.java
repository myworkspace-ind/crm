package mks.myworkspace.crm.transformer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import mks.myworkspace.crm.entity.Interaction;

public class JpaTransformer_Interaction_Handsontable {	    
    // Hàm chuyển đổi danh sách Interaction thành mảng 2D để sử dụng với Handsontable
    public static List<Object[]> convert2D(List<Interaction> interactions) {
        if (interactions == null) {
            return null;
        }

        List<Object[]> lstObject = new ArrayList<>();
        
        // Định dạng ngày
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (Interaction interaction : interactions) {
            // Tạo một mảng Object để chứa dữ liệu của mỗi Interaction
            Object[] rowData = new Object[5];  // 6 cột trong Handsontable (Ngày, Nội dung, Người trao đổi, Kế hoạch, ID)
            
            // Lấy dữ liệu từ Interaction
            rowData[1] = interaction.getInteraction_date() != null ? sdf.format(interaction.getInteraction_date()) : ""; // Ngày
            rowData[2] = interaction.getContent();  // Nội dung trao đổi
            rowData[0] = interaction.getCustomer() != null ? interaction.getCustomer().getContactPerson() : ""; // Người trao đổi
            rowData[3] = interaction.getNext_plan(); // Kế hoạch tiếp theo
            rowData[4] = interaction.getId(); // ID (để thực hiện các hành động như xóa)

            lstObject.add(rowData);
        }

        return lstObject;
    }
}
