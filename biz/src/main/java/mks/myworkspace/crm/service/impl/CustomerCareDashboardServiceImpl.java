package mks.myworkspace.crm.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.CustomerCare;
import mks.myworkspace.crm.entity.Interaction;
import mks.myworkspace.crm.entity.dto.CustomerCareStatsDTO;
import mks.myworkspace.crm.repository.CustomerCareRepository;
import mks.myworkspace.crm.service.CustomerCareDashboardService;

@Service
public class CustomerCareDashboardServiceImpl implements CustomerCareDashboardService{
	@Autowired
	CustomerCareRepository customerCareRepository;

	@Override
	public CustomerCareStatsDTO getCustomerCareStatistics() {
		LocalDateTime endDate = LocalDateTime.now();
		LocalDateTime startDate = endDate.minusMonths(1);
		return getFilteredStatistics(startDate, endDate);
	}

	@Override
	public CustomerCareStatsDTO getFilteredStatistics(LocalDateTime startDate, LocalDateTime endDate) {
		CustomerCareStatsDTO stats = new CustomerCareStatsDTO();
		
		//Tong so ban ghi cham soc
		stats.setTotalCustomerCares(customerCareRepository.count());
		
		//So KH chua cham soc
		Long notCaredCount = customerCareRepository.countByCareStatus()
				.stream()
				.filter(arr -> {
					if (arr[0] == null)
						return true; // care_status là NULL thì tính vào
					if (!(arr[0] instanceof String))
						return false;
		
					String careStatus = ((String) arr[0]).trim();
					return careStatus.contains("Chưa chăm sóc") || careStatus.isEmpty();
				}).map(arr -> (Long) arr[1]).reduce(0L, Long::sum);
		stats.setNotCaredCount(notCaredCount);
		
		// So KH cham soc dung han
		Long caredOnTimeCount = customerCareRepository.countByCareStatus().stream()
			    .filter(arr -> arr[0] instanceof String && ((String) arr[0]).contains("Chăm sóc đúng hạn"))
			    .map(arr -> (Long) arr[1])
			    .findFirst()
			    .orElse(0L);
		stats.setCaredOnTimeCount(caredOnTimeCount);
		
		// So KH cham soc dung han
		Long caredLateCount = customerCareRepository.countByCareStatus().stream()
			    .filter(arr -> arr[0] instanceof String && ((String) arr[0]).contains("Chăm sóc trễ hạn"))
			    .map(arr -> (Long) arr[1])
			    .findFirst()
			    .orElse(0L);
		stats.setCaredLateCount(caredLateCount);
		
		//Phan bo theo trang thai chinh
		Map<String, Long> statusDistribution = customerCareRepository.countByMainStatus().stream()
				.collect(Collectors.toMap(
						arr -> (String) arr[0],
						arr -> (Long) arr[1]
				));
		stats.setStatusDistribution(statusDistribution);
		
		List<CustomerCare> allCares = customerCareRepository.findAllCustomerCaresWithInteraction();
		Map<String, List<Long>> employeeDelays = new HashMap<>();
		
		for (CustomerCare care : allCares) {
			 LocalDateTime remindDate = care.getRemindDate();
			 Customer customer = care.getCustomer();
			 
			 if (remindDate == null || customer == null || customer.getResponsiblePerson() == null)
		            continue;
			 
			 String employeeName = customer.getResponsiblePerson().getName();
			 List<Interaction> interactions = customer.getInteractions();
			 
			 // Tim lan tuong tac dau tien sau ngay nhac nho (remindDate):
			 // + Loai bo cac tuong tac null hoac xay ra truoc remindDate
			 // + Sap xep tang dan theo thoi gian tao tuong tac (createdAt)
			 // + Lay lan dau tien (findFirst)
			 Optional<Interaction> firstAfterRemind = interactions.stream()
			            .filter(i -> i.getCreatedAt() != null && !i.getCreatedAt().isBefore(remindDate))
			            .sorted(Comparator.comparing(Interaction::getCreatedAt))
			            .findFirst();
			 
			 // Neu co lan tuong tac sau remindDate:
			 // + Tinh so gio tre giua thoi diem can cham soc (remindDate) va lan tuong tac thuc te (createdAt)
			 // + Luu vao employeeDelays, theo ten nhan vien
			 if (firstAfterRemind.isPresent()) {
		            long delayHours = Duration.between(remindDate, firstAfterRemind.get().getCreatedAt()).toHours();
		            employeeDelays.computeIfAbsent(employeeName, k -> new ArrayList<>()).add(delayHours);

			 }
		}
		
		//Tinh toan du lieu cuoi cung cho radarData: 
		// + Voi moi nhan vien (key trong employeeDelays)
		// + Tinh trung binh so gio tre tu danh sach nay
		// + Luu vao radarData: key -> tenNhanVien, value -> trungBinhSoGioDelay
		Map<String, Double> radarData = employeeDelays.entrySet().stream()
		        .collect(Collectors.toMap(
		            Map.Entry::getKey,
		            e -> e.getValue().stream().mapToLong(Long::longValue).average().orElse(0.0)
		        ));

		stats.setRadarData(radarData);
		
		return stats;
	}

}
