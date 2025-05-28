package mks.myworkspace.crm.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.CustomerCare;
import mks.myworkspace.crm.entity.Interaction;
import mks.myworkspace.crm.repository.AppRepository;
import mks.myworkspace.crm.repository.CustomerCareRepository;
import mks.myworkspace.crm.repository.CustomerRepository;
import mks.myworkspace.crm.repository.InteractionRepository;
import mks.myworkspace.crm.service.CustomerCareService;


@Service
@Transactional
@Slf4j
public class CustomerCareServiceImpl implements CustomerCareService{
	@Autowired 
	CustomerCareRepository repo;
	
	@Autowired
	AppRepository appRepository;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	InteractionRepository interactionRepository;
	
	@Value("${customer.care.days-ago-case1}")
	private int daysAgo_case1;
	
	@Value("${customer.care.days-ago-case2}")
	private int daysAgo_case2;
	
	@Value("${customer.care.days-ago-case3}")
	private int daysAgo_case3;
	

	@Override
	public CustomerCareRepository getRepo() {
		return repo;
	}
	
	/**
	 * Loads potential customers who need care into the CustomerCare table.
	 * <p>
	 * This method identifies potential customers based on specific conditions 
	 * (e.g., status is "Mới" or "Tiềm năng", remind dates, and latest interaction dates),
	 * and inserts them into the {@code crm_customer_care} table if they are not already present.
	 * </p>
	 * 
	 * @throws RuntimeException if there are no potential customers to insert,
	 *                          or if an error occurs during insertion.
	 */
	@Override
	public void loadPotentialCustomersIntoCustomerCare() {
		log.debug("Vào được đây");
	    try {
	        LocalDateTime case1DaysAgo = LocalDateTime.now().minusDays(daysAgo_case1);         
	        LocalDateTime case2DaysAgo = LocalDateTime.now().minusDays(daysAgo_case2);
	        LocalDateTime case3DaysAgo = LocalDateTime.now().minusDays(daysAgo_case3);
	        
	        LocalDateTime now = LocalDateTime.now();                                  

	        // Gọi 3 phương thức đã tách riêng
	        List<Customer> newCustomers = repo.findNewCustomersWithEmptyInteraction(case1DaysAgo);
	        List<Customer> potentialCustomers = repo.findPotentialCustomers(case2DaysAgo);
	        List<Customer> newAndInteractionNotNullCustomers = repo.findNewCustomersWithInteractionNotNull(case3DaysAgo);
	        log.debug("Danh sách new + interaction not null: {}", newAndInteractionNotNullCustomers.toString());

	        // Gộp lại
	        Set<Customer> allCustomersSet = new HashSet<>();
	        allCustomersSet.addAll(newCustomers);
	        allCustomersSet.addAll(potentialCustomers);
	        allCustomersSet.addAll(newAndInteractionNotNullCustomers);
	        
	        //log.debug("Danh sách new + interaction not null: {}", newAndInteractionNotNullCustomers.toString());

	        if (allCustomersSet.isEmpty()) {
	            throw new RuntimeException("Không có khách hàng nào đủ điều kiện để nạp vào CustomerCare.");
	        }

	        // Tạo danh sách CustomerCare từ danh sách khách hàng
	        List<CustomerCare> customerCares = allCustomersSet.stream()
	            .map(customer -> new CustomerCare(null, customer, null, null, null)) // ID = null để auto, các field còn lại null
	            .collect(Collectors.toList());

	        // Lưu vào DB
	        appRepository.insertCustomerCare(customerCares, daysAgo_case1, daysAgo_case2, daysAgo_case3);
	        log.debug("Danh sách CustomerCare sau khi insert: {}", 
	        	    customerCares.stream()
	        	        .map(c -> "CustomerCare{customerId=" + c.getCustomer().getId() + ", remindDate=" + c.getRemindDate() + "}")
	        	        .collect(Collectors.joining(", ")));

	    } catch (Exception e) {
	        throw new RuntimeException("Lỗi khi nạp khách hàng vào CustomerCare: " + e.getMessage(), e);
	    }
	}

//	@Override
//	public void loadPotentialCustomersIntoCustomerCare() {
//		try {
//			LocalDateTime twoDaysAgo = LocalDateTime.now().minusDays(daysAgo);
//			LocalDateTime case2DaysAgo = LocalDateTime.now().minusDays(daysAgo_case2);
//			LocalDateTime now = LocalDateTime.now();
//            List<Customer> potentialCustomers = repo.findPotentialCustomers(twoDaysAgo, case2DaysAgo, now);
//
//            if (potentialCustomers.isEmpty()) {
//                throw new RuntimeException("Không có khách hàng tiềm năng nào để nạp vào CustomerCare.");
//            }
//
//            List<CustomerCare> customerCares = potentialCustomers.stream()
//                .map(customer -> new CustomerCare(null, customer, null, null, null)) // ID tự động tăng, các trường còn lại null
//                .collect(Collectors.toList());
//
//            appRepository.insertCustomerCare(customerCares, daysAgo);
//        } catch (Exception e) {
//            throw new RuntimeException("Lỗi khi nạp khách hàng tiềm năng vào CustomerCare: " + e.getMessage());
//        }
//	}
	
	/**
	 * Find/Fetch all customer_care's data in table crm_customer_care (data from customer_care)
	 */
	@Override
	public List<CustomerCare> findAll() {
		return repo.findAllCustomerCares();
	}
	
	/**
	 * Find/Fetch all customers' data in table crm_customer who need care (data from customer)
	 */
	@Override
	@Transactional
	public List<Customer> findAllCustomerCare() {
	    LocalDateTime case1DaysAgo = LocalDateTime.now().minusDays(daysAgo_case1);
	    LocalDateTime case2DaysAgo = LocalDateTime.now().minusDays(daysAgo_case2);
	    LocalDateTime case3DaysAgo = LocalDateTime.now().minusDays(daysAgo_case3);
	    LocalDateTime now = LocalDateTime.now();
	    
	    // Gọi từng repo riêng biệt
	    List<Customer> newCustomers = repo.findNewCustomersWithEmptyInteraction(case1DaysAgo);
	    List<Customer> potentialCustomers = repo.findPotentialCustomers(case2DaysAgo);
	    List<Customer> newAndInteractionNotNullCustomers = repo.findNewCustomersWithInteractionNotNull(case3DaysAgo);
	    
	    //Gộp danh sách lại
	    Set<Customer> combinedSet = new HashSet<>();
	    combinedSet.addAll(newCustomers);
	    combinedSet.addAll(potentialCustomers);
	    combinedSet.addAll(newAndInteractionNotNullCustomers);
	    
	    List<Customer> finalList = new ArrayList<>(combinedSet);

//	    // Fetch the list of customers
//	    List<Customer> customers = repo.findPotentialCustomers(twoDaysAgo, case2DaysAgo, now);
//	    // Initialize the interactions collection explicitly
	    
	    for (Customer customer : finalList) {
	        Hibernate.initialize(customer.getInteractions());
	    }

	    // Log the customers list
	    for (Customer customer : finalList) {
	        Hibernate.initialize(customer.getInteractions());
	        // Ghi log từng khách hàng
	        log.info("Customer ID: {}, Name: {}, Status: {}, CreatedAt: {}", 
	                 customer.getId(), 
	                 customer.getCompanyName(), 
	                 customer.getMainStatus() != null ? customer.getMainStatus().getName() : "null", 
	                 customer.getCreatedAt());
	    }

	    log.info("Total customers to care: {}", finalList.size());

	    return finalList;
	}
//	@Override
//	@Transactional
//	public List<Customer> findAllCustomerCare() {
//	    LocalDateTime twoDaysAgo = LocalDateTime.now().minusDays(daysAgo);
//	    LocalDateTime case2DaysAgo = LocalDateTime.now().minusDays(daysAgo_case2);
//	    LocalDateTime now = LocalDateTime.now();
//
//	    // Fetch the list of customers
//	    List<Customer> customers = repo.findPotentialCustomers(twoDaysAgo, case2DaysAgo, now);
//	    
//	    // Initialize the interactions collection explicitly
//	    for (Customer customer : customers) {
//	        Hibernate.initialize(customer.getInteractions());
//	    }
//
//	    // Log the customers list
//	    log.info("List of potential customers: {}", customers);
//
//	    return customers;
//	}

	@Override
	public boolean existsInCustomerCares(Long customerId) {
		return repo.existsInCustomerCares(customerId);
	}

	@Override
	public boolean checkCustomerCareIDExists(Long customerCareId) {
		return repo.existsByCustomeCareId(customerCareId);
	}

	@Override
	public void createRemindersForCustomer(Customer customer) {
		 // Lấy lần interaction gần nhất
	    Interaction lastInteraction = interactionRepository.findLastInteractionForCustomer(customer.getId());

	    if (lastInteraction != null) {
	        LocalDateTime lastInteractionDate = lastInteraction.getCreatedAt();
	        LocalDateTime today = LocalDateTime.now();

	        // Duyệt từ ngày tương tác gần nhất đến ngày hiện tại
//	        for (LocalDateTime reminderDate = lastInteractionDate.plusDays(daysAgo_case2); reminderDate.isBefore(today); reminderDate = reminderDate.plusDays(1)) {
//	            // Kiểm tra nếu chưa có interaction vào ngày này, tạo bản ghi nhắc nhở
//	            boolean existsReminder = customerCareRepository.existsByCustomerAndRemindDate(customer, reminderDate);
//	            if (!existsReminder) {
//	                CustomerCare reminder = new CustomerCare();
//	                reminder.setCustomer(customer);
//	                reminder.setRemindDate(reminderDate);
//	                customerCareRepository.save(reminder);
//	                log.debug("Tạo bản ghi nhắc nhở cho customer {} vào ngày {}", customer.getCompanyName(), reminderDate);
//	            }
//	        }
	    }
		
	}

//	@Override
//	public void saveCustomerCare() {
//		log.debug("Vào được đây");
//	}

}
