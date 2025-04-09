package mks.myworkspace.crm.validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.controller.CustomerController;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.Interaction;
import mksgroup.java.common.CommonUtil;

@Slf4j
public class InteractionValidator {
	public static List<Object[]> convertInteractionsToTableData(List<Interaction> interactions) {
		List<Object[]> tableData = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Định dạng ngày giờ
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		for (Interaction interaction : interactions) {
			if (interaction != null) {
				String formattedDate = interaction.getInteractionDate() != null
						? sdf.format(interaction.getInteractionDate())
						: "";

				String createdAt = interaction.getCreatedAt() != null ? formatter.format(interaction.getCreatedAt())
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
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy
		// HH:mm:ss");

		for (Object[] rowData : data) {
			if (CommonUtil.isNNNE(rowData)) {
				try {
					String contactPerson = (String) rowData[0];

					String dateString = (String) rowData[1];
					Date date = dateFormat.parse(dateString);

					String content = (String) rowData[2];

					String nextPlan = (String) rowData[3];

					// Xử lý createdAt
	                LocalDateTime createdAt = LocalDateTime.now(); 
	                if (rowData[4] != null && !rowData[4].toString().isEmpty()) {
	                    try {
	                        String createdAtStr = rowData[4].toString().trim();
	                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	                        createdAt = LocalDateTime.parse(createdAtStr, formatter);
	                    } catch (DateTimeParseException e) {
	                        log.warn("⚠ Lỗi khi chuyển đổi createdAt (sử dụng thời gian hiện tại): {}", e.getMessage());
	                    }
	                }

	                // Xử lý interactionId
	                Long interactionId = null;
	                if (rowData[5] != null) {
	                    try {
	                        interactionId = Long.valueOf(rowData[5].toString());
	                    } catch (NumberFormatException e) {
	                        log.error("❌ Lỗi khi chuyển đổi interactionId từ rowData[5]: {}", rowData[5]);
	                    }
	                }

					Customer customer = new Customer(customerId);

					Interaction interaction = new Interaction(interactionId, date, content, nextPlan, customer, contactPerson, createdAt);
					
	                log.debug("✅ Interaction được lưu: ID={}, ContactPerson={}, Date={}, Content={}, NextPlan={}, CreatedAt={}",
	                    interactionId, contactPerson, date, content, nextPlan, createdAt);

					paramList.add(interaction);
				} catch (ParseException e) {
					log.info("Lỗi khi phân tích ngày: {}" + rowData[1]);
					 e.printStackTrace(); 
				}
			}
		}

		return paramList;
	}
}
