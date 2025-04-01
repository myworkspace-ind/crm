package mks.myworkspace.crm.validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.Interaction;
import mksgroup.java.common.CommonUtil;

public class InteractionValidator {
	public static List<Object[]> convertInteractionsToTableData(List<Interaction> interactions) {
		List<Object[]> tableData = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Định dạng ngày giờ
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

		for (Interaction interaction : interactions) {
			if (interaction != null) {
				String formattedDate = interaction.getInteractionDate() != null
						? sdf.format(interaction.getInteractionDate())
						: "";

				String createdAt = interaction.getCreatedAt() != null 
						? formatter.format(interaction.getCreatedAt()) 
						: "";

				Object[] rowData = new Object[] { interaction.getContactPerson(), // Người trao đổi
						formattedDate, // Ngày
						interaction.getContent(), // Nội dung trao đổi
						interaction.getNextPlan(), // Kế hoạch tiếp theo
						createdAt, // Ngày tạo (createdAt)
						interaction.getId(), // ID (để xóa/thao tác khác)
						
				};

				tableData.add(rowData);
			}
		}
		return tableData;
	}

	public static List<Interaction> validateAndCleasing(List<Object[]> data, Long customerId) {
		List<Interaction> paramList = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Định dạng ngày giờ

		for (Object[] rowData : data) {
			if (CommonUtil.isNNNE(rowData)) {
				try {
					// Lấy ngày interaction
					String dateString = (String) rowData[1];
					Date date = dateFormat.parse(dateString);

					// Lấy createdAt (nếu có)
					LocalDateTime createdAt = null;
					if (rowData[5] != null && !rowData[5].toString().isEmpty()) {
						Date createdAtDate = dateTimeFormat.parse((String) rowData[5]);
						createdAt = createdAtDate.toInstant().atZone(java.time.ZoneId.systemDefault())
								.toLocalDateTime();
					}

					Customer customer = new Customer(customerId);
					String contactPerson = (String) rowData[0];
					String content = (String) rowData[2];
					String nextPlan = (String) rowData[3];
					Long interactionId = null;

					if (rowData[4] != null) {
						interactionId = Long.valueOf((Integer) rowData[4]);
					}

					// Tạo đối tượng Interaction từ các trường đã parse
					Interaction interaction = new Interaction(interactionId, date, content, nextPlan, customer,
							contactPerson);

					// Nếu interaction mới, đặt createdAt = now
					if (interactionId == null) {
						interaction.setCreatedAt(LocalDateTime.now());
					} else {
						interaction.setCreatedAt(createdAt);
					}

					// Thêm vào danh sách kết quả
					paramList.add(interaction);
				} catch (ParseException e) {
					System.err.println("Lỗi khi phân tích ngày: " + rowData[1]);
				}
			}
		}

		return paramList;
	}
}
