package mks.myworkspace.crm.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.repository.AppRepository;
import mks.myworkspace.crm.repository.CustomerRepository;
import mks.myworkspace.crm.repository.StatusRepository;
import mks.myworkspace.crm.service.CustomerService;

@Transactional
@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository repo;

	@Autowired
	private AppRepository apprepo;
	
	@Autowired
	private StatusRepository statusrepo;

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
		return repo.searchCustomers(keyword, keyword, keyword, keyword);
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
		// Kiem tra sdt ton tai hay chua (truong hop database da duoc kiem tra khong co
		// sdt trung)
		Optional<Customer> existingCustomer = repo.findByPhone(customer.getPhone());
		if (existingCustomer.isPresent()) {
			throw new IllegalArgumentException("Số điện thoại đã được đăng ký trước đó. Vui lòng thử lại!");
		}

		// Dung cai nay trong truong hop database chua hoan toan chinh xac (truong hop
		// da co so dien thoai trung lap trong db)
//		List<Customer> existingCustomers = repo.findByPhone(customer.getPhone());
//	    if (!existingCustomers.isEmpty()) {
//	        throw new IllegalArgumentException("Số điện thoại đã được đăng ký trước đó. Vui lòng thử lại!");
//	    }

		log.debug("Saving customer: " + customer.toString());
		Customer savedCustomer = repo.save(customer);
		log.debug("Customer saved with ID: " + savedCustomer.getId()); // Sau khi lưu
		return savedCustomer;
	}

	@Override
	public Long getNextCustomerId() {
		Long maxId = repo.findMaxId();
		return maxId + 1; // Trả về ID mới
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void deleteCustomersByIds(Iterable<Long> ids) {
		// Kiểm tra xem danh sách ID có trống không
		if (ids == null || !ids.iterator().hasNext()) {
			throw new IllegalArgumentException("Danh sách ID không được trống.");
		}

		try {
			statusrepo.deleteByCustomerIdIn(ids);
			
			log.debug("Deleting customers with IDs: {}", ids);
			
			repo.deleteAllByIdInBatch(ids);

			log.debug("Customers with provided IDs have been deleted.");
		} catch (Exception e) {
			log.error("Error occurred while deleting customers with IDs: {}. Error: {}", ids, e.getMessage());
			throw new RuntimeException("Có lỗi xảy ra trong quá trình xóa khách hàng.", e);
		}
	}

}
