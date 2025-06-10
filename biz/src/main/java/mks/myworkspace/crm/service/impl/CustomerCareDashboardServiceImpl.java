package mks.myworkspace.crm.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		
		return stats;
	}

}
