package mks.myworkspace.crm.service.impl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.CustomerCare;
import mks.myworkspace.crm.entity.CustomerStatusHistory;
import mks.myworkspace.crm.entity.EmailToCustomer;
import mks.myworkspace.crm.entity.GoodsCategory;
import mks.myworkspace.crm.entity.Order;
import mks.myworkspace.crm.entity.OrderCategory;
import mks.myworkspace.crm.entity.OrderStatus;
import mks.myworkspace.crm.entity.Profession;
import mks.myworkspace.crm.entity.ResponsiblePerson;
import mks.myworkspace.crm.entity.Status;
import mks.myworkspace.crm.repository.AppRepository;
import mks.myworkspace.crm.repository.CustomerRepository;
import mks.myworkspace.crm.repository.CustomerStatusHistoryRepository;
import mks.myworkspace.crm.repository.GoodsCategoryRepository;
import mks.myworkspace.crm.repository.OrderCategoryRepository;
import mks.myworkspace.crm.repository.OrderRepository;
import mks.myworkspace.crm.repository.OrderStatusRepository;
import mks.myworkspace.crm.repository.ProfessionRepository;
import mks.myworkspace.crm.repository.ResponsiblePersonRepository;
import mks.myworkspace.crm.repository.StatusRepository;
import mks.myworkspace.crm.service.StorageService;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {
	@Autowired
	@Getter
	AppRepository appRepo;

	@Autowired
	@Getter
	OrderCategoryRepository orderCategoryRepository;

	@Autowired
	@Getter
	OrderRepository orderRepo;

	@Autowired
	@Getter
	CustomerRepository customerRepo;

	@Autowired
	@Getter
	ResponsiblePersonRepository responPersonRepo;

	@Autowired
	@Getter
	ProfessionRepository professionRepo;

	@Autowired
	@Getter
	StatusRepository statusRepo;

	@Autowired
	@Getter
	GoodsCategoryRepository goodsCategoryRepo;

	@Autowired
	@Getter
	OrderStatusRepository orderStatusRepository;

	@Autowired
	@Getter
	CustomerStatusHistoryRepository customerStatusHistoryRepository;
	
	@Autowired
	@Getter
	StatusRepository statusRepository;

	@Value("${customer.care.max-care-days-new-case1}")
	private int reminderDaysForNew_Case1;

	@Value("${customer.care.max-care-days-potential-case1}")
	private int reminderDaysForPotential_Case1;

	@Value("${customer.main-status}")
	private String mainStatuses;

	private Set<String> getMainStatuses() {
		return Arrays.stream(mainStatuses.split(",")).map(String::trim).collect(Collectors.toSet());
	}

//	@Override
//	public CustomerRepository getCustomerRepo() {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public AppRepository getAppRepo() {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public Customer saveOrUpdate(Customer customer) {
		Optional<Customer> existingCustomerByEmail = customerRepo.findByEmail(customer.getEmail());
		Optional<Customer> existingCustomerByPhone = customerRepo.findByPhone(customer.getPhone());
		/*
		 * if (existingEmail.isPresent()) { throw new
		 * IllegalArgumentException("Email đã được đăng ký trước đó. Vui lòng thử lại!"
		 * ); }
		 * 
		 * Optional<Customer> existingPhone =
		 * customerRepo.findByPhone(customer.getPhone()); if (existingPhone.isPresent())
		 * { throw new
		 * IllegalArgumentException("Số điện thoại đã được đăng ký trước đó. Vui lòng thử lại!"
		 * ); }
		 * 
		 * if (!isValidPhoneNumber(customer.getPhone())) { throw new
		 * IllegalArgumentException("Số điện thoại không đúng định dạng. Vui lòng nhập lại!"
		 * ); }
		 */

		// Kiểm tra nếu SDT đã có
		if (existingCustomerByPhone.isPresent()) {
			// throw new IllegalArgumentException("Số điện thoại đã được đăng ký trước đó.
			// Vui lòng thử lại!");

			// Nếu SDT đã có, kiểm tra xem SDT này là của khách muốn chỉnh sủa thông tin,
			// hay là của khách hàng khác

			// Lấy khách hàng cũ
			Customer optCustomer = existingCustomerByPhone.get();

			// Nếu đây là thêm mới nhưng trùng sdt khách hàng cũ,
			if (customer.getId() == null) {
				throw new IllegalArgumentException("Số điện thoại đã được đăng ký trước đó. Vui lòng thử lại!");
			}
			// hoặc là khách hàng chỉnh sửa sdt trùng khách hàng cũ
			else if (customer.getId() != optCustomer.getId()) {
				throw new IllegalArgumentException("Số điện thoại đã được đăng ký trước đó. Vui lòng thử lại!");
			}
			if (customer.getPhone().length() != 10) {
				throw new IllegalArgumentException("Số điện thoại chưa đúng định dạng. Vui lòng nhập lại!");
			}
		}
		if (existingCustomerByEmail.isPresent()) {
			// throw new IllegalArgumentException("Số điện thoại đã được đăng ký trước đó.
			// Vui lòng thử lại!");

			// Nếu SDT đã có, kiểm tra xem SDT này là của khách muốn chỉnh sủa thông tin,
			// hay là của khách hàng khác

			// Lấy khách hàng cũ
			Customer optCustomer = existingCustomerByEmail.get();

			// Nếu đây là thêm mới nhưng trùng email khách hàng cũ,
			if (customer.getId() == null) {
				throw new IllegalArgumentException("Email đã được đăng ký trước đó. Vui lòng thử lại!");
			}
			// hoặc là khách hàng chỉnh sửa email trùng khách hàng cũ
			else if (customer.getId() != optCustomer.getId()) {
				throw new IllegalArgumentException("Email đã được đăng ký trước đó. Vui lòng thử lại!");
			}
			/*
			 * if (customer.getPhone().length() != 10) { throw new
			 * IllegalArgumentException("Số điện thoại chưa đúng định dạng. Vui lòng nhập lại!"
			 * ); }
			 */
		}
		Long id = appRepo.saveOrUpdate(customer);
		if (id != null) {
			customer.setId(id);
		}
		return customer;
	}

	private boolean isValidPhoneNumber(String phoneNumber) {
		String phoneRegex = "^[0-9]{10}$";
		return phoneNumber != null && phoneNumber.matches(phoneRegex);
	}

	private boolean isValidEmail(String email) {
		String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
		return email != null && email.matches(emailRegex);
	}

	@Override
	public List<Customer> saveOrUpdate(List<Customer> lstCustomer) {
		List<Long> lstIds = appRepo.saveOrUpdate(lstCustomer);

		// Update the Id of saved task
		int len = (lstIds != null) ? lstIds.size() : 0;
		for (int i = 0; i < len; i++) {
			lstCustomer.get(i).setId(lstIds.get(i));
		}

		return lstCustomer;
	}

	@Override
	public void hideCustomersByIds(List<Long> customerIds) {
		appRepo.hideCustomersByIds(customerIds);
	}

	@Override
	public Order saveOrUpdateOrder(Order order) {

		log.debug("Processing Order with ID: {}", order.getId()); // Check the ID before saving

		Long id = appRepo.saveOrUpdateOrder(order); // This should return the ID of the saved or updated order
		if (id != null) {
			order.setId(id);
		}

		log.debug("Final Order ID after saveOrUpdate: {}", order.getId()); // Log the final ID after save/update
		return order;
	}

	@Override
	public void deleteOrderById(Long orderId) {
		appRepo.deleteOrderById(orderId);
	}

	@Override
	public Order updateOrderStatus(Order order) {
		log.debug("Processing Order with ID: {}", order.getId());

		Long id = appRepo.updateOrderStatus(order);
		if (id != null) {
			order.setId(id);
		}

		log.debug("Final Order ID after saveOrUpdate: {}", order.getId());
		return order;
	}

	@Override
	public List<OrderCategory> saveOrUpdateOrderCategory(List<OrderCategory> lstOrderCategories) {
		List<Long> lstIds = appRepo.saveOrUpdateOrderCategory(lstOrderCategories);

		// Update the Id of saved task
		int len = (lstIds != null) ? lstIds.size() : 0;
		for (int i = 0; i < len; i++) {
			lstOrderCategories.get(i).setId(lstIds.get(i));
		}

		return lstOrderCategories;
	}
	
	@Override
	public Customer updateCustomerStatus(Customer customerInput) {
		log.debug("Processing Customer with ID: {}", customerInput.getId());
		log.debug("Processing Customer with mainStatus: {}", customerInput.getMainStatus().getId());
		
		Set<String> mainStatuses = getMainStatuses();
		// ===== GHI LỊCH SỬ TRẠNG THÁI =====
//		Optional<Customer> optionalCustomer = customerRepo.findById(customerInput.getId());
//		if (optionalCustomer.isEmpty()) {
//			log.warn("Customer not found with ID: {}", customerInput.getId());
//			return customerInput;
//		}
//		Customer customer = optionalCustomer.get();
		 // Lấy trạng thái mới từ DB bằng ID
		
	    Long newStatusId = customerInput.getMainStatus() != null ? customerInput.getMainStatus().getId() : null;
	    Status newStatus = newStatusId != null ? statusRepository.findById(newStatusId).orElse(null) : null;
	    customerInput.setMainStatus(newStatus);
		
		Long id = appRepo.updateCustomerStatus(customerInput);
		if (id != null) {
			customerInput.setId(id);
		}
		log.debug("Final Customer ID : {}", customerInput.getId());
		
		
		if (newStatus != null  && mainStatuses.contains(newStatus.getName())) {
			log.debug("Status has changed and is in allowed mainStatuses. Proceeding to update...");

			customerInput.setMainStatus(newStatus);
			//appRepo.saveOrUpdate(customerInput); // chỉ để lưu, không gán lại
			log.debug("Updated main status for customer in DB.");

			List<CustomerStatusHistory> historyList = customerStatusHistoryRepository
					.findByCustomerOrderByChangeDateAsc(customerInput);
			int currentStage = getCurrentStage(historyList, newStatus);
			log.debug("Calculated current stage: {}", currentStage);

			CustomerStatusHistory mainStatusHistory = new CustomerStatusHistory();
			mainStatusHistory.setCustomer(customerInput);
			mainStatusHistory.setMainStatus(newStatus);
			mainStatusHistory.setChangeDate(LocalDate.now());
			mainStatusHistory.setStage(currentStage);
			appRepo.save(mainStatusHistory);
			log.debug("Saved new CustomerStatusHistory: {}", mainStatusHistory);
		} else {
			log.debug(
					"No status change detected or new status not in allowed mainStatuses list. Skipping history log.");
		}

		return customerInput;
	}

//	@Override
//	public Customer updateCustomerStatus(Customer customerInput) {
//		log.debug("Processing Customer with ID: {}", customerInput.getId());
//		log.debug("Processing Customer with mainStatus: {}", customerInput.getMainStatus().getName());
//		
//		Set<String> mainStatuses = getMainStatuses();
//		// ===== GHI LỊCH SỬ TRẠNG THÁI =====
////		Optional<Customer> optionalCustomer = customerRepo.findById(customerInput.getId());
////		if (optionalCustomer.isEmpty()) {
////			log.warn("Customer not found with ID: {}", customerInput.getId());
////			return customerInput;
////		}
////		Customer customer = optionalCustomer.get();
//		Status oldStatus = customerInput.getMainStatus(); // DB hiện tại
//		log.debug("Old Status: {}", oldStatus != null ? oldStatus.getName() : "null");
//		
//		Long id = appRepo.updateCustomerStatus(customerInput);
//		if (id != null) {
//			customerInput.setId(id);
//		}
//		log.debug("Final Customer ID : {}", customerInput.getId());
//		
//		Status newStatus = customerInput.getMainStatus(); // Input từ request
//
//		
//		log.debug("New Status: {}", newStatus != null ? newStatus.getName() : "null");
//
//		if (newStatus != null && !Objects.equals(oldStatus, newStatus) && mainStatuses.contains(newStatus.getName())) {
//			log.debug("Status has changed and is in allowed mainStatuses. Proceeding to update...");
//
//			customerInput.setMainStatus(newStatus);
//			//appRepo.saveOrUpdate(customerInput); // chỉ để lưu, không gán lại
//			log.debug("Updated main status for customer in DB.");
//
//			List<CustomerStatusHistory> historyList = customerStatusHistoryRepository
//					.findByCustomerOrderByChangeDateAsc(customerInput);
//			int currentStage = getCurrentStage(historyList, newStatus);
//			log.debug("Calculated current stage: {}", currentStage);
//
//			CustomerStatusHistory mainStatusHistory = new CustomerStatusHistory();
//			mainStatusHistory.setCustomer(customerInput);
//			mainStatusHistory.setMainStatus(newStatus);
//			mainStatusHistory.setChangeDate(LocalDate.now());
//			mainStatusHistory.setStage(currentStage);
//			appRepo.save(mainStatusHistory);
//			log.debug("Saved new CustomerStatusHistory: {}", mainStatusHistory);
//		} else {
//			log.debug(
//					"No status change detected or new status not in allowed mainStatuses list. Skipping history log.");
//		}
//
//		return customerInput;
//	}

	private int getCurrentStage(List<CustomerStatusHistory> historyList, Status newStatus) {
		if (historyList.isEmpty())
			return 1;

		CustomerStatusHistory last = historyList.get(historyList.size() - 1);
		String lastName = last.getMainStatus().getName();
		String newName = newStatus.getName();

		if (lastName.equals("Back")) {
			return last.getStage() + 1;
		}
		
		log.debug("Current stage: {}" + last.getStage());

		return last.getStage();
	}

	@Override
	public void showHidedCustomers() {
		appRepo.showHidedCustomers();
	}

	@Override
	public void deleteCustomersByIds(List<Long> customerIds) {
		appRepo.deleteCustomersByIds(customerIds);
	}

	@Override
	public List<ResponsiblePerson> saveOrUpdateResponsiblePerson(List<ResponsiblePerson> lstResponsiblePerson) {
		List<Long> lstIds = appRepo.saveOrUpdateResponsiblePerson(lstResponsiblePerson);

		// Update the Id of saved task
		int len = (lstIds != null) ? lstIds.size() : 0;
		for (int i = 0; i < len; i++) {
			lstResponsiblePerson.get(i).setId(lstIds.get(i));
		}

		return lstResponsiblePerson;
	}

	@Override
	public List<Profession> saveOrUpdateProfession(List<Profession> lstProfession) {
		List<Long> lstIds = appRepo.saveOrUpdateProfession(lstProfession);

		// Update the Id of saved task
		int len = (lstIds != null) ? lstIds.size() : 0;
		for (int i = 0; i < len; i++) {
			lstProfession.get(i).setId(lstIds.get(i));
		}

		return lstProfession;
	}

	@Override
	public List<Status> saveOrUpdateStatus(List<Status> lstStatus) {
		List<Long> lstIds = appRepo.saveOrUpdateStatus(lstStatus);

		// Update the Id of saved task
		int len = (lstIds != null) ? lstIds.size() : 0;
		for (int i = 0; i < len; i++) {
			lstStatus.get(i).setId(lstIds.get(i));
		}

		return lstStatus;
	}

	@Override
	public void deleteResponPerson(Long id) {
		appRepo.deletePersonById(id);
	}

	@Override
	public void deleteStatusById(Long id) {
		appRepo.deleteStatusById(id);

	}

	@Override
	public void deleteProfessionById(Long id) {
		appRepo.deleteProfessionById(id);
	}

	@Override
	public void swapRowOnHandsontable(Long rowId1, Long rowId2, String type) {
		if (type.equals("customPerson")) {
			appRepo.swapRowOnHandsontable(rowId1, rowId2, "crm_responsible_person");
		} else if (type.equals("customProfession")) {
			appRepo.swapRowOnHandsontable(rowId1, rowId2, "crm_profession");
		} else if (type.equals("customStatus")) {
			appRepo.swapRowOnHandsontable(rowId1, rowId2, "crm_status");
		} else {
			appRepo.swapRowOnHandsontable(rowId1, rowId2, "crm_goodscategory");
		}

	}

	@Override
	public List<GoodsCategory> saveOrUpdateGoodsCategory(List<GoodsCategory> lstGoodsCategory) {
		List<Long> lstIds = appRepo.saveOrUpdateGoodsCategory(lstGoodsCategory);

		// Update the Id of saved task
		int len = (lstIds != null) ? lstIds.size() : 0;
		for (int i = 0; i < len; i++) {
			lstGoodsCategory.get(i).setId(lstIds.get(i));
		}

		return lstGoodsCategory;
	}

	@Override
	public void deleteGoodsCategoryById(Long id) {
		appRepo.deleteGoodsCategoryById(id);
	}

	@Override
	public boolean saveOrUpdateOrderCategoryStatus(Map<String, Object> requestBody) {
		try {
			// Xử lý cập nhật
			List<List<Object>> updateList = (List<List<Object>>) requestBody.get("update");
			if (updateList != null && !updateList.isEmpty()) {
				processChanges(updateList);
			}

			// Xử lý tạo mới
			List<List<Object>> createList = (List<List<Object>>) requestBody.get("create");
			if (createList != null && !createList.isEmpty()) {
				processChanges(createList);
			}

			return true;
		} catch (Exception e) {
			System.err.println("Đã xảy ra lỗi: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	private void processChanges(List<List<Object>> changesList) {
		for (List<Object> change : changesList) {
			Integer row = (Integer) change.get(0);
			Integer col = (Integer) change.get(1);
			String oldValue = (String) change.get(2);
			String newValue = (String) change.get(3);

			Long idTrangThai = extractId(change.get(4)); // Extract ID of TrangThai
			Long idLoaiDonHang = extractId(change.get(5)); // Extract ID of LoaiDonHang

			// Ghi log thông tin
//	        System.out.println("Row: " + row + ", Col: " + col);
//	        System.out.println("Old Value: " + oldValue + ", New Value: " + newValue);
//	        System.out.println("Id Trạng Thái: " + idTrangThai + ", Id Loại Đơn Hàng: " + idLoaiDonHang);

			// Xử lý dữ liệu tùy theo cột
			if (col == 2) {
				// Xử lý trạng thái đơn hàng
				if (oldValue != null) {
					appRepo.deleteOrderCategoryStatus(idLoaiDonHang, idTrangThai);
				}
				if (newValue != null && checkExitsStatusInCategory(newValue, idLoaiDonHang)) {
					OrderStatus newStatus = orderStatusRepository.findByNameIgnoreCase(newValue);
					if (newStatus != null) {
						appRepo.insertOrderCategoryStatus(idLoaiDonHang, newStatus.getId());
					} else {
						appRepo.createOrderStatus(newValue);
						Long idNewStatus = orderStatusRepository.findByNameIgnoreCase(newValue).getId();
						appRepo.insertOrderCategoryStatus(idLoaiDonHang, idNewStatus);
					}
				}
			} else {
				// Xử lý loại đơn hàng
				if (newValue != null && idLoaiDonHang != null) {
					OrderCategory category = orderCategoryRepository.findById(idLoaiDonHang).get();
					category.setName(newValue);
					try {
						appRepo.updateOrderCategory(idLoaiDonHang, newValue);
					} catch (Exception e) {
						System.err.println("Lỗi khi cập nhật hoặc tạo mới loại đơn hàng: " + e.getMessage());
					}
				}
			}
		}
	}

	private Long extractId(Object value) {
		if (value instanceof Integer) {
			return ((Integer) value).longValue();
		} else if (value instanceof Long) {
			return (Long) value;
		}
		return null;
	}

	private boolean checkExitsStatusInCategory(String nameStatus, long idCateory) {
		OrderStatus status = orderStatusRepository.findByNameIgnoreCase(nameStatus);
		if (status == null) {
			return true;
		} else {
			OrderCategory category = orderCategoryRepository.findById(idCateory).get();
			if (category.getOrderStatuses().contains(status)) {
				return false;
			} else {
				return true;
			}
		}
	}

	@Override
	public EmailToCustomer saveEmailToCustomer(EmailToCustomer emailToCustomer) {
		appRepo.saveEmailToCustomer(emailToCustomer);
		return emailToCustomer;
	}
//	@Override
//	public void updatePriority(List<CustomerCare> customerCareRequests) {
//		List<CustomerCare> customerCareLists = customerCareRequests.stream().map(request -> {
//			CustomerCare customerCare = new CustomerCare();
//			customerCare.setId(request.getId());
//			customerCare.setPriority(request.getPriority());
//			return customerCare;
//		}).collect(Collectors.toList());
//		
//		appRepo.updatePriorityCustomerCare(customerCareLists);
//	}

	@Override
	public void updatePriority(CustomerCare customerCare) {
		appRepo.updatePriorityCustomerCare(customerCare);
	}

	@Override
	public int updateCustomerCareStatus(int reminderDaysForNew_Case1, int reminderDaysForPotential_Case1) {
		try {
			int rowsUpdated = appRepo.updateCustomerCareStatus(reminderDaysForNew_Case1,
					reminderDaysForPotential_Case1);
			log.info("Updated care_status for {} records.", rowsUpdated);

			return rowsUpdated;
		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi cập nhật trạng thái chăm sóc khách hàng: " + e.getMessage());
		}
	}

	@Override
	public void toggleById(Long featureReminderId) {
		appRepo.toggleById(featureReminderId);
	}

	@Override
	public boolean isFeatureEnabledByCode(String featureReminderCode) {
		return appRepo.isFeatureEnabledByCode(featureReminderCode);
	}
}