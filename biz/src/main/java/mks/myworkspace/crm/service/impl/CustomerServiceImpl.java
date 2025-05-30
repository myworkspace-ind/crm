package mks.myworkspace.crm.service.impl;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.Interaction;
import mks.myworkspace.crm.entity.dto.CustomerCriteriaDTO;
import mks.myworkspace.crm.entity.dto.FilesUploadDTO;
import mks.myworkspace.crm.entity.dto.InteractionDTO;
import mks.myworkspace.crm.repository.AppRepository;
import mks.myworkspace.crm.repository.CustomerRepository;
import mks.myworkspace.crm.repository.CustomerRepository_Son;
import mks.myworkspace.crm.repository.FilesUploadRepository;
import mks.myworkspace.crm.repository.StatusRepository;
import mks.myworkspace.crm.service.CustomerService;
import mks.myworkspace.crm.service.specification.CustomerSpecs;

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
	    private CustomerRepository_Son sonRepo;
    
    @Autowired
    private StatusRepository statusRepo;
    
    @Autowired
    private FilesUploadRepository filesUploadRepository;

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
//            System.out.println("Main Status ID: " + count[0] + ", Count: " + count[1]);
        }
        List<Object[]> subStatusCounts = repo.countCustomersBySubStatus();
        for (Object[] count :subStatusCounts) {
//            System.out.println("Sub Status ID: " + count[0] + ", Count: " + count[1]);
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
//        System.out.println("Final Status Count Map: " + statusCountMap);

        return statusCountMap.isEmpty() ? new HashMap<>() : statusCountMap;
    }

    public long getTotalCustomerCount() {
        return repo.countAllCustomers();
    }
    
    @Override
    public List<Interaction> getAllCustomerInteraction(Long customerId) {
        return repo.getAllCustomerInteraction(customerId);
    };
    
//    @Override
//    public List<InteractionDTO> getAllCustomerInteractionWithFiles(Long customerID) {
//        List<Interaction> interactions = repo.getAllCustomerInteraction(customerID);
//
//        return interactions.stream().map(interaction -> {
//            List<FilesUploadDTO> files = filesUploadRepository.findFilesByInteractionId(interaction.getId());
//
//            return new InteractionDTO(
//                interaction.getId(),
//                interaction.getContactPerson(),
//                interaction.getContent(),
//                interaction.getNextPlan(),
//                interaction.getInteractionDate(),  // Trả về LocalDateTime
//                interaction.getCreatedAt(),        // Trả về LocalDateTime
//                files
//            );
//        }).collect(Collectors.toList());
//    }
//    
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
    	return repo.findAllByAccountStatusTrue(pageable);
    }
    
    public Page<Customer> findAllWithSpecs(Pageable pageable, CustomerCriteriaDTO customerCriteriaDTO)
    {
    	Specification<Customer> combinedSpec = Specification.where(null);
    	if(customerCriteriaDTO.getStatusId() != null &&  customerCriteriaDTO.getStatusId().isPresent() && !customerCriteriaDTO.getStatusId().get().isBlank())
    	{
    		Long statusId = Long.parseLong(customerCriteriaDTO.getStatusId().get());
    		combinedSpec = combinedSpec.and(CustomerSpecs.matchStatus(statusId));
    	}
    	if(customerCriteriaDTO.getCareers() != null &&  customerCriteriaDTO.getCareers().isPresent())
    	{
    		combinedSpec = combinedSpec.and(CustomerSpecs.matchListProfession(customerCriteriaDTO.getCareers().get()));
    	}
    	if(customerCriteriaDTO.getAddress() != null &&  customerCriteriaDTO.getAddress().isPresent())
    	{
    		combinedSpec = combinedSpec.and(CustomerSpecs.matchAddress(customerCriteriaDTO.getAddress().get()));
    	}
    	if(customerCriteriaDTO.getPhone() != null &&  customerCriteriaDTO.getPhone().isPresent())
    	{
    		combinedSpec = combinedSpec.and(CustomerSpecs.matchPhone(customerCriteriaDTO.getPhone().get()));
    	}
    	if(customerCriteriaDTO.getContactPerson() != null &&  customerCriteriaDTO.getContactPerson().isPresent())
    	{
    		combinedSpec = combinedSpec.and(CustomerSpecs.matchContactPerson(customerCriteriaDTO.getContactPerson().get()));
    	}
    	if(customerCriteriaDTO.getNameCompany() != null &&  customerCriteriaDTO.getNameCompany().isPresent())
    	{
    		combinedSpec = combinedSpec.and(CustomerSpecs.matchNameCompany(customerCriteriaDTO.getNameCompany().get()));
    	}
    	if(customerCriteriaDTO.getEmail() != null &&  customerCriteriaDTO.getEmail().isPresent())
    	{
    		combinedSpec = combinedSpec.and(CustomerSpecs.matchEmail(customerCriteriaDTO.getEmail().get()));
    	}
    	combinedSpec = combinedSpec.and(CustomerSpecs.matchaccountStatusTrue());
    	return repo.findAll(combinedSpec, pageable);
    }
    public Page<Customer> findAllKeyword(Pageable pageable, String keyword)
    {
    	Specification<Customer> combinedSpec = CustomerSpecs.matchaccountStatusTrue();
        
        Specification<Customer> keywordSpec = Specification.where(
                CustomerSpecs.matchReponsiblePersonName(keyword)
            ).or(CustomerSpecs.matchNameCompany(keyword))
             .or(CustomerSpecs.matchContactPerson(keyword))
             .or(CustomerSpecs.matchEmail(keyword))
             .or(CustomerSpecs.matchAddress(keyword))
             .or(CustomerSpecs.matchPhone(keyword))
             .or(CustomerSpecs.matchProfession(keyword))
             .or(CustomerSpecs.matchMainStatusName(keyword))
             .or(CustomerSpecs.matchSubStatusName(keyword))
             .or(CustomerSpecs.matchNote(keyword));
        combinedSpec = combinedSpec.and(keywordSpec);
        
        return repo.findAll(combinedSpec, pageable);
    }

	@Override
	public Optional<Customer> findById_ForCustomerCare(Long customerId) {
		 return repo.findById_ForCustomerCare(customerId);
	}

	@Override
	public List<Customer> findCustomersAdvanced(String nameCompany, String phone, List<Long> selectedCareers,
			String contactPerson, String address, String email) {
        return sonRepo.advancedSearchCustomers(nameCompany, phone, selectedCareers, contactPerson, address, email);

	}

	@Override
	public List<Customer> advancedSearchCustomersNotCareer(String nameCompany, String phone, List<Long> selectedCareers,
			String contactPerson, String address, String email) {
        return sonRepo.advancedSearchCustomersNotCareer(nameCompany, phone, selectedCareers, contactPerson, address, email);

	}

	@Override
	public List<Customer> findByselectedCareers(List<Long> selectedCareers) {
		if (selectedCareers == null || selectedCareers.isEmpty()) {
            return new ArrayList<>();
        }
    	return sonRepo.findByselectedCareers(selectedCareers);
	}

//	@Override
//	public List<Customer> findPotentialCustomers() {
//		return repo.findPotentialCustomers();
//	}
}
