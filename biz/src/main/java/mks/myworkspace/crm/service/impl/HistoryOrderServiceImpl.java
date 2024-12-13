package mks.myworkspace.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.HistoryOrder;
import mks.myworkspace.crm.repository.HistoryOrderRepository;
import mks.myworkspace.crm.service.HistoryOrderService;

@Service
@Transactional
@Slf4j
public class HistoryOrderServiceImpl implements HistoryOrderService{
	@Autowired HistoryOrderRepository historyRepo;

	@Override
	public List<HistoryOrder> findAll() {
		return historyRepo.findAll();
	}

	
	@Override
	public void saveHistory(HistoryOrder historyOrder) {
        // Ghi log để kiểm tra
        System.out.println("Saving HistoryOrder: " + historyOrder);
        
        // Gọi repository để lưu
        historyRepo.save(historyOrder);
    }
	
	 
	
}
