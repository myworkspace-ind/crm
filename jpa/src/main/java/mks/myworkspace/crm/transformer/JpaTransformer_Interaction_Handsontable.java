package mks.myworkspace.crm.transformer;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Interaction interaction : interactions) {
            // Tạo một mảng Object để chứa dữ liệu của mỗi Interaction
            Object[] rowData = new Object[7];  // 7 cột trong Handsontable 
            
            // Lấy dữ liệu từ Interaction
            rowData[1] = interaction.getInteractionDate() != null ? sdf.format(interaction.getInteractionDate()) : ""; // Ngày
            rowData[2] = interaction.getContent();  // Nội dung trao đổi
            rowData[0] = interaction.getContactPerson(); // Người trao đổi
            rowData[3] = interaction.getNextPlan(); // Kế hoạch tiếp theo
            rowData[4] = interaction.getCreatedAt() != null ? formatter.format(interaction.getCreatedAt()) : "";
            rowData[5] = interaction.getId(); // ID (để thực hiện các hành động như xóa)

            lstObject.add(rowData);
        }

        return lstObject;
    }
}
