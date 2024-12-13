package mks.myworkspace.crm.service;

import java.util.List;

import mks.myworkspace.crm.entity.HistoryOrder;

public interface HistoryOrderService {

	List<HistoryOrder> findAll();

	

	void saveHistory(HistoryOrder historyOrder);

}
