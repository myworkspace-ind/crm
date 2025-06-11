package mks.myworkspace.crm.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.CustomerCare;
import mks.myworkspace.crm.entity.Interaction;
import mks.myworkspace.crm.entity.Status;
import mks.myworkspace.crm.service.CustomerCareService;
import mks.myworkspace.crm.service.CustomerService;
import mks.myworkspace.crm.service.StatusService;
import mks.myworkspace.crm.service.StorageService;
import mks.myworkspace.crm.transformer.JpaTransformer_CustomerCare;

@Controller
@Slf4j
@RequestMapping("/customer-care")
public class CustomerCareController extends BaseController {

	@Autowired
	CustomerCareService customerCareService;

	@Autowired
	CustomerService customerService;

	@Autowired
	StorageService storageService;
	
	@Autowired
	StatusService statusService;

	@Value("${customer.care.days-ago-case1}")
	private int reminderDays;

	@Value("${customer.care.days-ago-case2}")
	private int reminderDays_case2;
	
	@Value("${customer.care.days-ago-case3}")
	private int reminderDays_case3;

	@Value("${customer.care.max-care-days-new-case1}")
	private int checkCareStatusDaysForNew_Case1;

	@Value("${customer.care.max-care-days-potential-case1}")
	private int checkCareStatusDaysForPotential_Case1;
	
	@GetMapping(value = "/dashboard")
	public ModelAndView displayDashboard (HttpServletRequest httpRequest, HttpSession httpSession ) {
		ModelAndView mav = new ModelAndView("customerCareDashboard_Draft");
		initSession(httpRequest, httpSession);
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());
		return mav;
	}

	@GetMapping("/calendar")
	public ModelAndView displayCalendar(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("calendar");
		initSession(request, httpSession);
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());

		return mav;
	}

	@GetMapping("/general")
	public ModelAndView displayCustomerCarePage(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("customerCare");
		initSession(request, httpSession);
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());

		return mav;
	}

	@GetMapping(value = "/check-exist/{customerCareId}")
	public ResponseEntity<Map<String, Boolean>> checkCustomerCare(@PathVariable Long customerCareId) {
		boolean exists = customerCareService.checkCustomerCareIDExists(customerCareId);
		Map<String, Boolean> response = new HashMap<>();
		response.put("exists", exists);

		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/save-customer-care", produces = "application/json; charset=UTF-8")
	public ResponseEntity<?> saveCustomerCare() {
		log.debug("Đã tới được đây");
		try {
			if (customerCareService == null) {
				log.error("customerCareService đang NULL. Kiểm tra @Autowired hoặc Bean configuration.");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Lỗi nội bộ: customerCareService chưa được khởi tạo.");
			}
			customerCareService.loadPotentialCustomersIntoCustomerCare();
			//customerCareService.saveCustomerCare();
			return ResponseEntity.ok("Nạp khách hàng vào CustomerCare thành công!");
		} catch (Exception e) {
			log.error("Lỗi khi chạy loadPotentialCustomersIntoCustomerCare(): {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Lỗi khi nạp khách hàng: " + e.getMessage());
		}
	}

	@GetMapping(value = "/check-unsaved-customercare", produces = "application/json; charset=UTF-8")
	public ResponseEntity<?> checkUnsavedCustomerCare() {
		try {
			List<Status> allStatuses = statusService.getAllStatuses();
			List<Customer> customersNeedCares = customerCareService.findAllCustomerCare();// Dành cho KH hiện đang có trạng Chưa chăm sóc
			List<CustomerCare> customersWithCareData = customerCareService.findAll();// Danh sách đã có sẵn trong bảng
																						// crm_customer_care
			List<Customer> allCustomers = customerService.getAllCustomers();
			// Danh sách chứa dữ liệu từ crm_customer_care
			List<CustomerCare> customersWithData = new ArrayList<>(); // --> danh sách sẽ hiện lên giao diện
			// Danh sách chưa có dữ liệu
			List<Customer> customersWithoutData = new ArrayList<>(); // --> danh sách sẽ hiện lên giao diện
			
			if (customersNeedCares != null && !customersNeedCares.isEmpty()) {
				log.debug("=== Danh sách customersNeedCares ({} bản ghi chăm sóc) ===",
						customersNeedCares.size());
				for (Customer c : customersNeedCares) {
					log.debug("Customer => ID: {}, Name: {}", c.getId(), c.getCompanyName());
				}
			} else {
				log.debug("Danh sách customersWithCareData trống hoặc null");
			}

			if (customersWithCareData != null && !customersWithCareData.isEmpty()) {
				log.debug("=== Danh sách customersWithCareData ({} bản ghi chăm sóc) ===",
						customersWithCareData.size());
				for (CustomerCare c : customersWithCareData) {
					log.debug("CustomerCare => ID: {}, Name: {}", c.getId(), c.getCustomer().getCompanyName());
				}
			} else {
				log.debug("Danh sách customersWithCareData trống hoặc null");
			}

//			if (customersNeedCares.isEmpty()) {
//				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Không có khách hàng nào cần chăm sóc.");
//			}

			// Nếu danh sách cần chăm sóc lấy từ customer bị trống → mặc định hiển thị toàn
			// bộ dữ liệu chăm sóc

			Set<Long> customersWithCareIds = customersWithCareData.stream()
					.filter(c -> c.getId() != null && c.getRemindDate() != null).map(c -> c.getCustomer().getId())
					.collect(Collectors.toSet());

			// TODO: Nạp những khách hàng cần chăm sóc mà CHƯA CÓ DỮ LIỆU trong
			// customer_care
			for (Customer customer : customersNeedCares) {
				if (customersWithCareIds.contains(customer.getId())) {
					// Lấy CustomerCare tương ứng
					customersWithCareData.stream().filter(cc -> cc.getCustomer().getId().equals(customer.getId()))
							.findFirst().ifPresent(customersWithData::add);
				} else {
					customersWithoutData.add(customer);
				}
			}

			// TODO: Bổ sung các bản ghi còn thiếu từ customersWithCareData vào
			// customersWithData đối với những trường hợp có careStatus là Đã chăm sóc nhưng không hiện lên(Dành cho khách hàng Mơi)`
//			Set<Long> existingCustomerIds = customersWithData.stream().map(c -> c.getCustomer().getId())
//					.collect(Collectors.toSet());
//			
//			Long careCustomerId = null;
//			for (CustomerCare care : customersWithCareData) {
//				careCustomerId = care.getCustomer().getId();
//				if (!existingCustomerIds.contains(careCustomerId)) {
//					customersWithData.add(care);
//					log.debug("✅ Bổ sung thêm CustomerCare => ID: {}, Name: {}", care.getId(),
//							care.getCustomer().getCompanyName());
//				}
//			}
			
			Set<Long> existingCustomerCareIds = customersWithData.stream().map(c -> c.getId())
					.collect(Collectors.toSet());
			
			Long careCustomerCareId = null;
			for (CustomerCare care : customersWithCareData) {
				careCustomerCareId = care.getId();
				if (!existingCustomerCareIds.contains(careCustomerCareId)) {
					customersWithData.add(care);
					log.debug("✅ Bổ sung thêm CustomerCare => ID: {}, Name: {}", care.getId(),
							care.getCustomer().getCompanyName());
				}
			}

			if (customersWithData != null && !customersWithData.isEmpty()) {
				log.debug("=== Danh sách customersWithData ({} bản ghi có dữ liệu) ===", customersWithData.size());
				for (CustomerCare c : customersWithData) {
					log.debug("CustomerCare => ID: {}, Name: {}", c.getId(), c.getCustomer().getCompanyName());
				}
			} else {
				log.debug("Danh sách customersWithData trống hoặc null");
			}

			if (customersWithoutData != null && !customersWithoutData.isEmpty()) {
				log.debug("=== Danh sách customersWithoutData ({} bản ghi không có dữ liệu) ===",
						customersWithoutData.size());
				for (Customer customer : customersWithoutData) {
					log.debug("Customer => ID: {}, Name: {}", customer.getId(), customer.getCompanyName());
				}
			} else {
				log.debug("Danh sách customersWithoutData trống hoặc null");
			}
			
			String latestCreatedAtInteraction = "Không có tương tác";
			for (Customer c : customersNeedCares) {
				//String latestCreatedAtInteraction = "Không có tương tác";

				if (c.getInteractions() != null && !c.getInteractions().isEmpty()) {
					Optional<Interaction> latestInteraction = c.getInteractions().stream()
							.filter(i -> i.getCreatedAt() != null).max(Comparator.comparing(Interaction::getCreatedAt));

					if (latestInteraction.isPresent()) {
						LocalDateTime interactionTime = latestInteraction.get().getCreatedAt();
						latestCreatedAtInteraction = interactionTime.toString();

						// TODO: Bổ sung logic: Nếu là khách hàng Potential & latestCreatedAtInteraction
						// + reminderDays_case2 < thời gian hiện tại
						// thì thêm vào customersWithoutData
						if ("Potential".equalsIgnoreCase(c.getMainStatus().getName()) ) {
							LocalDateTime remindTime = interactionTime.plusDays(reminderDays_case2);

							if (remindTime.isBefore(LocalDateTime.now())) {
								boolean isAlreadyReminded = customersWithCareData.stream()
										.anyMatch(cc -> cc.getCustomer().getId().equals(c.getId())
												&& cc.getRemindDate() != null && cc.getRemindDate().equals(remindTime));

								if (!isAlreadyReminded) {
									log.debug(
											"⚠️ Thêm khách hàng Potential cần chăm sóc (New thời điểm): ID {}, Name {}, RemindTime {}",
											c.getId(), c.getCompanyName(), remindTime);
									customersWithoutData.add(c);
								}
							}
						}
						
						if ("New".equalsIgnoreCase(c.getMainStatus().getName()) ) {
							LocalDateTime remindTime = interactionTime.plusDays(reminderDays_case3);

							if (remindTime.isBefore(LocalDateTime.now())) {
								boolean isAlreadyReminded = customersWithCareData.stream()
										.anyMatch(cc -> cc.getCustomer().getId().equals(c.getId())
												&& cc.getRemindDate() != null && cc.getRemindDate().equals(remindTime));

								if (!isAlreadyReminded) {
									log.debug(
											"⚠️ Thêm khách hàng New cần chăm sóc (New thời điểm): ID {}, Name {}, RemindTime {}",
											c.getId(), c.getCompanyName(), remindTime);
									customersWithoutData.add(c);
								}
							}
						}
					}
				}

				log.debug("Check customers with latest interaction => ID: {}, Name: {}, Phone: {}, Email: {}, LatestCreatedAtInteraction: {}",
						c.getId(), c.getCompanyName(), c.getPhone(), c.getEmail(), latestCreatedAtInteraction);
			}

			// Chuyển đổi từng danh sách riêng
//			List<Object[]> convertedWithData = JpaTransformer_CustomerCare.convert2D_CustomerCares(customersWithData,
//					customersNeedCares);
			List<Object[]> convertedWithData = JpaTransformer_CustomerCare.convert2D_CustomerCares(customersWithData,
					allCustomers, allStatuses);
			List<Object[]> convertedWithoutData = JpaTransformer_CustomerCare.convert2D_Customers(customersWithoutData,
					reminderDays, reminderDays_case2, reminderDays_case3);

			// Gom tất cả lại và trả về response
			Map<String, Object> response = new HashMap<>();
			response.put("customersWithData", convertedWithData);
			response.put("customersWithoutData", convertedWithoutData);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Lỗi khi lấy danh sách khách hàng: " + e.getMessage());
		}
	}

	@GetMapping(value = "/load-customer-care", produces = "application/json; charset=UTF-8")
	public ResponseEntity<?> displayCustomerCareList() {
		log.debug("Here!");
		try {
			List<Status> allStatuses = statusService.getAllStatuses();
			List<Customer> customersNeedCares = customerCareService.findAllCustomerCare();// Dành cho KH hiện đang có trạng Chưa chăm sóc
			List<CustomerCare> customersWithCareData = customerCareService.findAll();// Danh sách đã có sẵn trong bảng
																						// crm_customer_care
			List<Customer> allCustomers = customerService.getAllCustomers();
			// Danh sách chứa dữ liệu từ crm_customer_care
			List<CustomerCare> customersWithData = new ArrayList<>(); // --> danh sách sẽ hiện lên giao diện
			// Danh sách chưa có dữ liệu
			List<Customer> customersWithoutData = new ArrayList<>(); // --> danh sách sẽ hiện lên giao diện
			
			if (customersNeedCares != null && !customersNeedCares.isEmpty()) {
				log.debug("=== Danh sách customersNeedCares ({} bản ghi chăm sóc) ===",
						customersNeedCares.size());
				for (Customer c : customersNeedCares) {
					log.debug("Customer => ID: {}, Name: {}", c.getId(), c.getCompanyName());
				}
			} else {
				log.debug("Danh sách customersWithCareData trống hoặc null");
			}

			if (customersWithCareData != null && !customersWithCareData.isEmpty()) {
				log.debug("=== Danh sách customersWithCareData ({} bản ghi chăm sóc) ===",
						customersWithCareData.size());
				for (CustomerCare c : customersWithCareData) {
					log.debug("CustomerCare => ID: {}, Name: {}", c.getId(), c.getCustomer().getCompanyName());
				}
			} else {
				log.debug("Danh sách customersWithCareData trống hoặc null");
			}

//			if (customersNeedCares.isEmpty()) {
//				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Không có khách hàng nào cần chăm sóc.");
//			}

			// Nếu danh sách cần chăm sóc lấy từ customer bị trống → mặc định hiển thị toàn
			// bộ dữ liệu chăm sóc

			Set<Long> customersWithCareIds = customersWithCareData.stream()
					.filter(c -> c.getId() != null && c.getRemindDate() != null).map(c -> c.getCustomer().getId())
					.collect(Collectors.toSet());

			// TODO: Nạp những khách hàng cần chăm sóc mà CHƯA CÓ DỮ LIỆU trong
			// customer_care
			for (Customer customer : customersNeedCares) {
				if (customersWithCareIds.contains(customer.getId())) {
					// Lấy CustomerCare tương ứng
					customersWithCareData.stream().filter(cc -> cc.getCustomer().getId().equals(customer.getId()))
							.findFirst().ifPresent(customersWithData::add);
				} else {
					customersWithoutData.add(customer);
				}
			}

			// TODO: Bổ sung các bản ghi còn thiếu từ customersWithCareData vào
			// customersWithData đối với những trường hợp có careStatus là Đã chăm sóc nhưng không hiện lên(Dành cho khách hàng Mơi)`
//			Set<Long> existingCustomerIds = customersWithData.stream().map(c -> c.getCustomer().getId())
//					.collect(Collectors.toSet());
//			
//			Long careCustomerId = null;
//			for (CustomerCare care : customersWithCareData) {
//				careCustomerId = care.getCustomer().getId();
//				if (!existingCustomerIds.contains(careCustomerId)) {
//					customersWithData.add(care);
//					log.debug("✅ Bổ sung thêm CustomerCare => ID: {}, Name: {}", care.getId(),
//							care.getCustomer().getCompanyName());
//				}
//			}
			
			Set<Long> existingCustomerCareIds = customersWithData.stream().map(c -> c.getId())
					.collect(Collectors.toSet());
			
			Long careCustomerCareId = null;
			for (CustomerCare care : customersWithCareData) {
				careCustomerCareId = care.getId();
				if (!existingCustomerCareIds.contains(careCustomerCareId)) {
					customersWithData.add(care);
					log.debug("✅ Bổ sung thêm CustomerCare => ID: {}, Name: {}", care.getId(),
							care.getCustomer().getCompanyName());
				}
			}

			if (customersWithData != null && !customersWithData.isEmpty()) {
				log.debug("=== Danh sách customersWithData ({} bản ghi có dữ liệu) ===", customersWithData.size());
				for (CustomerCare c : customersWithData) {
					log.debug("CustomerCare => ID: {}, Name: {}", c.getId(), c.getCustomer().getCompanyName());
				}
			} else {
				log.debug("Danh sách customersWithData trống hoặc null");
			}

			if (customersWithoutData != null && !customersWithoutData.isEmpty()) {
				log.debug("=== Danh sách customersWithoutData ({} bản ghi không có dữ liệu) ===",
						customersWithoutData.size());
				for (Customer customer : customersWithoutData) {
					log.debug("Customer => ID: {}, Name: {}", customer.getId(), customer.getCompanyName());
				}
			} else {
				log.debug("Danh sách customersWithoutData trống hoặc null");
			}
			
			String latestCreatedAtInteraction = "Không có tương tác";
			for (Customer c : customersNeedCares) {
				//String latestCreatedAtInteraction = "Không có tương tác";

				if (c.getInteractions() != null && !c.getInteractions().isEmpty()) {
					Optional<Interaction> latestInteraction = c.getInteractions().stream()
							.filter(i -> i.getCreatedAt() != null).max(Comparator.comparing(Interaction::getCreatedAt));

					if (latestInteraction.isPresent()) {
						LocalDateTime interactionTime = latestInteraction.get().getCreatedAt();
						latestCreatedAtInteraction = interactionTime.toString();

						// TODO: Bổ sung logic: Nếu là khách hàng Potential & latestCreatedAtInteraction
						// + reminderDays_case2 < thời gian hiện tại
						// thì thêm vào customersWithoutData
						if ( "Potential".equalsIgnoreCase(c.getMainStatus().getName()) ) {
							LocalDateTime remindTime = interactionTime.plusDays(reminderDays_case2);

							if (remindTime.isBefore(LocalDateTime.now())) {
								boolean isAlreadyReminded = customersWithCareData.stream()
										.anyMatch(cc -> cc.getCustomer().getId().equals(c.getId())
												&& cc.getRemindDate() != null && cc.getRemindDate().equals(remindTime));

								if (!isAlreadyReminded) {
									log.debug(
											"⚠️ Thêm khách hàng Potential cần chăm sóc (New thời điểm): ID {}, Name {}, RemindTime {}",
											c.getId(), c.getCompanyName(), remindTime);
									customersWithoutData.add(c);
								}
							}
						}
						// TODO: Bổ sung logic: Nếu là khách hàng New & latestCreatedAtInteraction
						// + reminderDays_case3 < thời gian hiện tại
						// thì thêm vào customersWithoutData
						if ("New".equalsIgnoreCase(c.getMainStatus().getName()) ) {
							LocalDateTime remindTime = interactionTime.plusDays(reminderDays_case3);

							if (remindTime.isBefore(LocalDateTime.now())) {
								boolean isAlreadyReminded = customersWithCareData.stream()
										.anyMatch(cc -> cc.getCustomer().getId().equals(c.getId())
												&& cc.getRemindDate() != null && cc.getRemindDate().equals(remindTime));

								if (!isAlreadyReminded) {
									log.debug(
											"⚠️ Thêm khách hàng New cần chăm sóc (New thời điểm): ID {}, Name {}, RemindTime {}",
											c.getId(), c.getCompanyName(), remindTime);
									customersWithoutData.add(c);
								}
							}
						}
					}
				}

				log.debug("Check customers with latest interaction => ID: {}, Name: {}, Phone: {}, Email: {}, LatestCreatedAtInteraction: {}",
						c.getId(), c.getCompanyName(), c.getPhone(), c.getEmail(), latestCreatedAtInteraction);
			}

			// Chuyển đổi từng danh sách riêng
//			List<Object[]> convertedWithData = JpaTransformer_CustomerCare.convert2D_CustomerCares(customersWithData,
//					customersNeedCares);
			List<Object[]> convertedWithData = JpaTransformer_CustomerCare.convert2D_CustomerCares(customersWithData,
					allCustomers, allStatuses);
			List<Object[]> convertedWithoutData = JpaTransformer_CustomerCare.convert2D_Customers(customersWithoutData,
					reminderDays, reminderDays_case2, reminderDays_case3);

			// Gom tất cả lại và trả về response
			Map<String, Object> response = new HashMap<>();
			response.put("customersWithData", convertedWithData);
			response.put("customersWithoutData", convertedWithoutData);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Lỗi khi lấy danh sách khách hàng: " + e.getMessage());
		}
	}

//	@GetMapping(value = "/load-customer-care", produces = "application/json; charset=UTF-8")
//	public ResponseEntity<?> getPotentialCustomers() {
//		try {
//			List<Customer> customersNeedCares = customerCareService.findAllCustomerCare();
//			List<CustomerCare> customersWithCareData = customerCareService.findAll();
//			
//			if (customersNeedCares != null && !customersNeedCares.isEmpty()) {
//			    log.debug("=== Danh sách customersNeedCares ({} khách hàng) ===", customersNeedCares.size());
//			    for (Customer c : customersNeedCares) {
//			        log.debug("Customer => ID: {}, Name: {}, Phone: {}, Email: {}", 
//			                  c.getId(), c.getCompanyName(), c.getPhone(), c.getEmail());
//			    }
//			} else {
//			    log.debug("Danh sách customersNeedCares trống hoặc null");
//			}
//
////			if (customersNeedCares.isEmpty()) {
////				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Không có khách hàng nào cần chăm sóc.");
////			}
//
//			// Nếu danh sách cần chăm sóc lấy từ customer bị trống → mặc định hiển thị toàn bộ dữ liệu chăm sóc trong customer care
//			if (customersNeedCares == null || customersNeedCares.isEmpty()) {
//				
//				log.debug("customersNeedCares đang null!!");
//				
//				List<Object[]> fallbackData = JpaTransformer_CustomerCare.convert2D_CustomerCares(
//					    customersWithCareData, 
//					    customersWithCareData.stream()
//					        				 .map(CustomerCare::getCustomer)
//					        				 .collect(Collectors.toList())
//					);
//				Map<String, Object> response = new HashMap<>();
//				response.put("customersWithData", fallbackData);
//				response.put("customersWithoutData", new ArrayList<>()); // không có khách chưa chăm
//				return ResponseEntity.ok(response);
//				
//			} 
//			else {
//				return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Chức năng đang được phát triển");
//			}
////				else {
////				Set<Long> customersWithCareIds = customersWithCareData.stream().map(c -> c.getCustomer().getId())
////						.collect(Collectors.toSet());
////
////				// Danh sách chứa dữ liệu từ crm_customer_care
////				List<CustomerCare> customersWithData = new ArrayList<>();
////				// Danh sách chưa có dữ liệu
////				List<Customer> customersWithoutData = new ArrayList<>();
////
////				for (Customer customer : customersNeedCares) {
////					if (customersWithCareIds.contains(customer.getId())) {
////						// Lấy CustomerCare tương ứng
////						customersWithCareData.stream().filter(cc -> cc.getCustomer().getId().equals(customer.getId()))
////								.findFirst().ifPresent(customersWithData::add);
////					} else {
////						customersWithoutData.add(customer);
////					}
////				}
////
////				// Chuyển đổi từng danh sách riêng
////				List<Object[]> convertedWithData = JpaTransformer_CustomerCare.convert2D_CustomerCares(customersWithData,
////						customersNeedCares);
////				List<Object[]> convertedWithoutData = JpaTransformer_CustomerCare.convert2D_Customers(customersWithoutData,
////						reminderDays, reminderDays_case2);
////
////				// Gom tất cả lại và trả về response
////				Map<String, Object> response = new HashMap<>();
////				response.put("customersWithData", convertedWithData);
////				response.put("customersWithoutData", convertedWithoutData);
////
////				return ResponseEntity.ok(response);
////			}
//			
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("Lỗi khi lấy danh sách khách hàng: " + e.getMessage());
//		}
//	}

//	@GetMapping(value = "/notify-potential", produces = "application/json; charset=UTF-8")
//	public ResponseEntity<?> getPotentialCustomersForNotification() {
//	    try {
//	        List<Customer> customers = customerCareService.findAllCustomerCare();
//
//	        if (customers.isEmpty()) {
//	            return ResponseEntity.status(HttpStatus.NO_CONTENT)
//	                                 .body("Không có khách hàng Potential nào.");
//	        }
//
//	        return ResponseEntity.ok(customers);
//	    } catch (Exception e) {
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//	                .body("Lỗi khi lấy thông báo khách hàng cần chăm sóc: " + e.getMessage());
//	    }
//	}

//	@PutMapping(value = "/update-priority", produces = "application/json; charset=UTF-8")
//	public ResponseEntity<?> updatePriority(@RequestBody List<CustomerCare> customerCareLists){
//		try {
//			if (customerCareLists == null || customerCareLists.isEmpty()) {
//				return ResponseEntity.badRequest().body("Danh sách khách hàng cần chăm sóc trống.");
//			}
//			
//			storageService.updatePriority(customerCareLists);
//			return ResponseEntity.ok("Cập nhật priority thành công cho " + customerCareLists.size() + " khách hàng.");
//			
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//	                .body("Lỗi khi cập nhật priority: " + e.getMessage());
//		}
//	}

	@PutMapping(value = "/update-priority", produces = "application/json; charset=UTF-8")
	public ResponseEntity<?> updatePriority(@RequestBody CustomerCare customerCare) {
		try {
			if (customerCare == null || customerCare.getId() == null) {
				return ResponseEntity.badRequest().body(Map.of("error", "Dữ liệu khách hàng không hợp lệ."));
			}

			storageService.updatePriority(customerCare);
			return ResponseEntity.ok(
					Map.of("message", "Cập nhật priority thành công cho khách hàng có ID: " + customerCare.getId()));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Lỗi khi cập nhật priority: " + e.getMessage()));
		}
	}

	@PutMapping(value = "/update-care-status", produces = "application/json; charset=UTF-8")
	public ResponseEntity<?> updateCustomerCareStatus() {
		try {
			int rowsUpdated = storageService.updateCustomerCareStatus(checkCareStatusDaysForNew_Case1,
					checkCareStatusDaysForPotential_Case1);
			log.info("Updated care_status for {} records.", rowsUpdated);

			if (rowsUpdated > 0) {
				return ResponseEntity.ok(Map.of("message", "Cập nhật thành công", "rowsUpdated", rowsUpdated));
			} else {
				return ResponseEntity.ok(Map.of("message", "Không có bản ghi nào được cập nhật"));
			}
		} catch (Exception e) {
			log.error("Lỗi khi cập nhật care_status: {}", e.getMessage());
			return ResponseEntity.status(500).body(Map.of("error", "Lỗi hệ thống", "message", e.getMessage()));
		}
	}

}
