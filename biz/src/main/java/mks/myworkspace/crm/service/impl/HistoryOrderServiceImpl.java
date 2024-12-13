package mks.myworkspace.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.HistoryOrder;
import mks.myworkspace.crm.repository.AppRepository;
import mks.myworkspace.crm.repository.HistoryOrderRepository;
import mks.myworkspace.crm.service.HistoryOrderService;

@Service
@Transactional
@Slf4j
public class HistoryOrderServiceImpl implements HistoryOrderService{
	@Autowired
	@Getter
	HistoryOrderRepository historyRepo;

	@Autowired
	@Getter
	AppRepository appRepo;

	@Override
	public List<HistoryOrder> findAll() {
		return historyRepo.findAll();
	}

	@Override
	public void saveHistory(HistoryOrder historyOrder) {
		appRepo.saveHistory(historyOrder);
	}
	
	 
	
}
