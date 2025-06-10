package mks.myworkspace.crm.service;

import java.time.LocalDateTime;

import mks.myworkspace.crm.entity.dto.CustomerCareStatsDTO;

public interface CustomerCareDashboardService {
	CustomerCareStatsDTO getCustomerCareStatistics();
	CustomerCareStatsDTO getFilteredStatistics(LocalDateTime startDate, LocalDateTime endDate);
}
