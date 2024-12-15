package mks.myworkspace.crm.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.Interaction;
import mks.myworkspace.crm.repository.AppRepository;
import mks.myworkspace.crm.repository.CustomerRepository;
import mks.myworkspace.crm.repository.StatusRepository;
import mks.myworkspace.crm.service.CustomerService;

@Service
@Transactional
@Slf4j
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	@Getter
	AppRepository appRepo;
	
	@Autowired
    private CustomerRepository repo;
    
    @Autowired
    private StatusRepository statusRepo;

    @Override
    public CustomerRepository getRepo() {
        return repo;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return repo.findAll();
    }

    @Override
    public List<Customer> searchCustomers(String keyword) {
        return repo.searchCustomers(keyword);
    }

    @Override
    public List<Customer> getAllCustomersWithStatuses() {
        return repo.findAllWithStatuses();
    }

    @Override
    public List<Customer> findCustomersByStatus(Long statusId) {
        return repo.findByStatusId(statusId);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return repo.findById(id);
    }

    @Transactional
    @Override
    public Customer createCustomer(Customer customer) {
        // Kiểm tra số điện thoại tồn tại hay chưa
        Optional<Customer> existingCustomer = repo.findByPhone(customer.getPhone());
        if (existingCustomer.isPresent()) {
            throw new IllegalArgumentException("Số điện thoại đã được đăng ký trước đó. Vui lòng thử lại!");
        }

        log.debug("Saving customer: " + customer.toString());
        Customer savedCustomer = repo.save(customer);
        log.debug("Customer saved with ID: " + savedCustomer.getId()); // Sau khi lưu
        return savedCustomer;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCustomersByIds(Iterable<Long> ids) {
        // Kiểm tra xem danh sách ID có trống không
        if (ids == null || !ids.iterator().hasNext()) {
            throw new IllegalArgumentException("Danh sách ID không được trống.");
        }

        try {            
            log.debug("Deleting customers with IDs: {}", ids);
            repo.deleteAllByIds((List<Long>) ids); // Chuyển đổi iterable sang list
            log.debug("Customers with provided IDs have been deleted.");
        } catch (Exception e) {
            log.error("Error occurred while deleting customers with IDs: {}. Error: {}", ids, e.getMessage());
            throw new RuntimeException("Có lỗi xảy ra trong quá trình xóa khách hàng.", e);
        }
    }
    
    @Transactional
    @Override
    public void deleteAllByIds(List<Long> customerIds) {
        // Xóa các mối quan hệ trong bảng trung gian customer_status
        statusRepo.deleteByCustomerIds(customerIds);
        repo.deleteAllByIds(customerIds);
    }
    
    @Override
    public Map<Long, Long> getCustomerCountsByStatus() {
        List<Object[]> mainStatusCounts = repo.countCustomersByMainStatus();
        for (Object[] count : mainStatusCounts) {
            System.out.println("Main Status ID: " + count[0] + ", Count: " + count[1]);
        }
        List<Object[]> subStatusCounts = repo.countCustomersBySubStatus();
        for (Object[] count :subStatusCounts) {
            System.out.println("Sub Status ID: " + count[0] + ", Count: " + count[1]);
        }
        
        Map<Long, Long> statusCountMap = new HashMap<>();

        // Thêm kết quả từ mainStatusCounts
        for (Object[] row : mainStatusCounts) {
            Long statusId = (Long) row[0];
            Long count = (Long) row[1];
            statusCountMap.put(statusId, count);
        }

        // Cộng kết quả từ subStatusCounts vào map đã có
        for (Object[] row : subStatusCounts) {
            Long statusId = (Long) row[0];
            Long count = (Long) row[1];
            statusCountMap.merge(statusId, count, Long::sum);
        }
        
        // In ra map kết quả để kiểm tra
        System.out.println("Final Status Count Map: " + statusCountMap);

        return statusCountMap.isEmpty() ? new HashMap<>() : statusCountMap;
    }

    public long getTotalCustomerCount() {
        return repo.countAllCustomers();
    }
    
    @Override
    public List<Interaction> getAllCustomerInteraction(Long customerId) {
        return repo.getAllCustomerInteraction(customerId);
    };
    
    @Override
    public List<Interaction> saveOrUpdateInteraction(List<Interaction> lstInteractions) {
        // Gọi repository để lưu hoặc cập nhật và lấy danh sách ID sau khi xử lý
        List<Long> lstIds = appRepo.saveOrUpdateInteraction(lstInteractions);

        // Gán lại ID cho từng Interaction đã lưu hoặc cập nhật
        for (int i = 0; i < lstIds.size(); i++) {
            lstInteractions.get(i).setId(lstIds.get(i));
        }

        return lstInteractions;
    }
    
    @Override
    public void deleteInteractionById(Long interactionId) {
        try {     
            // Gọi repository để xóa Interaction
            appRepo.deleteInteractionById(interactionId);           
        } catch (Exception e) {       
            throw new RuntimeException("Có lỗi xảy ra trong quá trình xóa Interaction.", e);
        }
    }

    public List<Customer> findByInteractDateRange(Date startDate, Date enDate) {
        return repo.findByInteractDateRange(startDate,enDate);
    }
    
    public Page<Customer> findAllWithStatuses(Pageable pageable)
    {
    	return repo.findByAccountStatusTrue(pageable);
    }
}
