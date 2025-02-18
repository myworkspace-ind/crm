package mks.myworkspace.crm.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.common.model.TableStructure;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.EmailToCustomer;
import mks.myworkspace.crm.entity.GoodsCategory;
import mks.myworkspace.crm.entity.Interaction;
import mks.myworkspace.crm.entity.Profession;
import mks.myworkspace.crm.entity.ResponsiblePerson;
import mks.myworkspace.crm.entity.Status;
import mks.myworkspace.crm.entity.dto.CustomerCriteriaDTO;
import mks.myworkspace.crm.repository.CustomerRepository;
import mks.myworkspace.crm.service.CustomerService;
import mks.myworkspace.crm.service.CustomerService_Son;
import mks.myworkspace.crm.service.ProfessionService;
import mks.myworkspace.crm.service.ResponsiblePersonService;
import mks.myworkspace.crm.service.StatusService;
import mks.myworkspace.crm.service.StorageService;
import mks.myworkspace.crm.service.impl.EmailService;
import mks.myworkspace.crm.transformer.JpaTransformer_Interaction_Handsontable;
import mks.myworkspace.crm.transformer.JpaTransformer_ResponsiblePerson_Handsontable;
import mks.myworkspace.crm.validate.GoodsCategoryValidator;
import mks.myworkspace.crm.validate.InteractionValidator;
import mks.myworkspace.crm.validate.ProfessionValidator;
import mks.myworkspace.crm.validate.ResponsiblePersonValidator;
import mks.myworkspace.crm.validate.StatusValidator;

/**
 * Handles requests for Tasks.
 */
@Controller
@Slf4j
@RequestMapping("/customer")
public class CustomerController extends BaseController {
	/**
	 * This method is called when binding the HTTP parameter to bean (or model).
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// Sample init of Custom Editor

//     Class<List<ItemKine>> collectionType = (Class<List<ItemKine>>)(Class<?>)List.class;
//     PropertyEditor orderNoteEditor = new MotionRuleEditor(collectionType);
//     binder.registerCustomEditor((Class<List<ItemKine>>)(Class<?>)List.class, orderNoteEditor);

	}

	/**
	 * Simply selects the home view to render by returning its name.
	 * 
	 * @return
	 */

	@Autowired
	CustomerService customerService;

	@Autowired
	StorageService storageService;

	@Autowired
	StatusService statusService;

	@Autowired
	ResponsiblePersonService responsiblePersonService;

	@Autowired
	ProfessionService professionService;

	@Autowired
	CustomerService_Son customerServiceSon;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@PostMapping("/send-email-to-customer")
	public ResponseEntity<?> sendEmail(@RequestParam("to") String to,
	                                   @RequestParam("subject") String subject,
	                                   @RequestParam("message") String content,
	                                   @RequestParam("customerId") Long customerId,
	                                   Authentication authentication) {
	    try {
	        String loggedInUserEmail = authentication.getName();
	        log.info("Sender email: {}", loggedInUserEmail);
	        
	        
	        Customer customer = customerRepository.findById(customerId).orElse(null);
	        if (customer == null) {
	            return ResponseEntity.badRequest().body("Không tìm thấy khách hàng!");
	        }
	        
	        EmailToCustomer email = new EmailToCustomer();
	        email.setSender(loggedInUserEmail);
	        email.setCustomer(customer);
//	        email.setSubject(new String(subject.getBytes(), "UTF-8"));
//	        email.setContent(new String(content.getBytes(), "UTF-8"));
	        
//	        email.setSubject(new String(subject.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
//	        email.setContent(new String(content.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
	        
	        email.setSubject(subject);
	        email.setContent(content);
	        
	        email.setStatus(EmailToCustomer.EmailStatus.SENT);
	        
	        emailService.sendEmailToCustomer(email);
	        
	        return ResponseEntity.ok("Gửi email thành công!");
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("Gửi email thất bại!");
	    }
}
	
	@Value("classpath:responsible-person/responsible-person-demo.json")
	private Resource resResponsiblePersonDemo;
	@GetMapping("list")
	public ModelAndView displayCustomerListCRMScreen(
			CustomerCriteriaDTO customerCriteriaDTO,
			HttpServletRequest request,
			HttpSession httpSession) {

		ModelAndView mav = new ModelAndView("customer_list_v2");
		initSession(request, httpSession);
		int page = 1;
        try {
            if(customerCriteriaDTO.getPage().isPresent()){
                page = Integer.parseInt(customerCriteriaDTO.getPage().get()); 
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        
        String queryString = request.getQueryString();
        String updatedQueryString = queryString != null
                ? Arrays.stream(queryString.split("&"))
                        .filter(param -> !param.matches("^page=.*$")) // Loại bỏ tham số page
                        .collect(Collectors.joining("&"))
                : ""; // Nếu không có query string, gán chuỗi rỗng
        if(!updatedQueryString.isBlank())
        {
        	updatedQueryString = updatedQueryString + "&";
        }
		Pageable pageable = PageRequest.of(page - 1,10);
		Page<Customer> pageCustomer;
        if(customerCriteriaDTO.getKeyword()!=null && customerCriteriaDTO.getKeyword().isPresent())
        {
        	pageCustomer = customerService.findAllKeyword(pageable, customerCriteriaDTO.getKeyword().get());
        }
        else
        {
        	pageCustomer = customerService.findAllWithSpecs(pageable, customerCriteriaDTO);
        }
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());

		List<Customer> customers = pageCustomer.getContent().size() > 0 ?  pageCustomer.getContent() : new ArrayList<Customer>();
		List<Status> statuses = statusService.getAllStatuses();
		List<ResponsiblePerson> responsiblePersons = responsiblePersonService.getAllResponsiblePersons();
		List<Profession> professions = professionService.getAllProfessions();

		Map<Long, Long> statusCounts = customerService.getCustomerCountsByStatus();

		if (statusCounts == null) {
			statusCounts = new HashMap<>();
		}

		long totalCustomerCount = customerService.getTotalCustomerCount();
		mav.addObject("filter", updatedQueryString);
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", pageCustomer.getTotalPages());
        mav.addObject("totalCustomerWithSpec", pageCustomer.getTotalElements());
		mav.addObject("customers", customers);
		mav.addObject("statuses", statuses);
		mav.addObject("responsiblePersons", responsiblePersons);
		mav.addObject("professions", professions);
		mav.addObject("statusCounts", statusCounts);
		mav.addObject("totalCustomerCount", totalCustomerCount);
		mav.addObject("numberOfElementsInCurrentPage", pageCustomer.getNumberOfElements());
		return mav;
	}

	@RequestMapping(value = { "/customerDetail" }, method = RequestMethod.GET)
	public ModelAndView displaycustomerDetailScreen(@RequestParam("id") Long customerId, HttpServletRequest request,
			HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("customerDetail");

		initSession(request, httpSession);

		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());
		log.debug("Customer Detail is running....");

		Optional<Customer> customerOpt = customerService.findById(customerId);

		// Check if the customer exists and add to model
		customerOpt.ifPresentOrElse(customer -> {
			mav.addObject("customer", customer);
		}, () -> {
			mav.addObject("errorMessage", "Customer not found.");
		});

		List<ResponsiblePerson> responsiblePersons = responsiblePersonService.getAllResponsiblePersons();
		mav.addObject("responsiblePersons", responsiblePersons);

		return mav;
	}

	@PostMapping("/create-customer")
	@ResponseBody
	public ResponseEntity<?> createCustomer(@RequestBody Customer customer, HttpServletRequest request) {
		try {
			customer.setCreatedAt(new Date());
			customer.setSiteId(getCurrentSiteId());
			customer.setAccountStatus(true);

			Customer savedCustomer = storageService.saveOrUpdate(customer);

			return ResponseEntity.ok()
					.body(Map.of("message", "Khách hàng mới đã được thêm thành công!", "customer", savedCustomer));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("errorMessage", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("errorMessage", "Có lỗi xảy ra khi thêm khách hàng. Vui lòng thử lại sau!"));
		}
	}
	
	@Transactional
	@RequestMapping(value = "/delete-customers", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<?> deleteCustomersByIds(@RequestBody List<Long> customerIds, HttpServletRequest request,
			HttpSession httpSession) {
		System.out.println(customerIds.size());
		try {
			if (customerIds == null || customerIds.isEmpty()) {
				return ResponseEntity.badRequest().body(Map.of("errorMessage", "Danh sách ID không được trống."));
			}
			// Gọi service để xóa danh sách khách hàng dựa trên ID
			storageService.deleteCustomersByIds(customerIds);

			// TODO: Cannot DELETE ONE OR MORE CUSTOMER BECAUSE OF ROLL BACK (CANNOT COMMIT)
			// customerService.deleteAllByIds(customerIds);

			return ResponseEntity.ok()
					.body(Map.of("message", "Các khách hàng đã được xóa thành công!", "ids", customerIds));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("errorMessage", e.getMessage()));
		} catch (Exception e) {
			log.debug("Error while deleting customers with IDs: {}. Error: {}", customerIds, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("errorMessage", "Có lỗi xảy ra. Vui lòng thử lại sau!", "details", e.getMessage()));
		}
	}

	@Transactional
	@RequestMapping(value = "/hide-customers", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<?> hideCustomersByIds(@RequestBody List<Long> customerIds, HttpServletRequest request,
			HttpSession httpSession) {
		System.out.println(customerIds.size());
		try {
			if (customerIds == null || customerIds.isEmpty()) {
				return ResponseEntity.badRequest().body(Map.of("errorMessage", "Danh sách ID không được trống."));
			}
			// Gọi service để xóa danh sách khách hàng dựa trên ID
			storageService.hideCustomersByIds(customerIds);

			// TODO: Cannot DELETE ONE OR MORE CUSTOMER BECAUSE OF ROLL BACK (CANNOT COMMIT)
			// customerService.deleteAllByIds(customerIds);

			return ResponseEntity.ok()
					.body(Map.of("message", "Các khách hàng đã được ẩn thành công!", "ids", customerIds));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("errorMessage", e.getMessage()));
		} catch (Exception e) {
			log.debug("Error while hiding customers with IDs: {}. Error: {}", customerIds, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("errorMessage", "Có lỗi xảy ra. Vui lòng thử lại sau!", "details", e.getMessage()));
		}
	}

	@Transactional
	@RequestMapping(value = "/show-hidedcustomers", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<?> showHidedCustomers(HttpServletRequest request, HttpSession httpSession) {
		try {
			storageService.showHidedCustomers();
			return ResponseEntity.ok().body(Map.of("message", "Hiển thị các khách hàng bị ẩn thành công!"));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("errorMessage", e.getMessage()));
		} catch (Exception e) {
			log.debug("Error while showing all hided. Error: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("errorMessage", "Có lỗi xảy ra. Vui lòng thử lại sau!", "details", e.getMessage()));
		}
	}

	@GetMapping("/get-customer/{id}")
	@ResponseBody
	public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
		try {
			if (id == null) {
				return ResponseEntity.badRequest().body(Map.of("errorMessage", "ID khách hàng không được để trống."));
			}

			Optional<Customer> customerOpt = customerService.findById(id);
			if (customerOpt.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(Map.of("errorMessage", "Khách hàng với ID " + id + " không tồn tại."));
			}

			return ResponseEntity.ok()
					.body(Map.of("message", "Thông tin khách hàng đã được tìm thấy!", "customer", customerOpt.get()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("errorMessage",
					"Có lỗi xảy ra khi lấy thông tin khách hàng. Vui lòng thử lại sau!", "details", e.getMessage()));
		}
	}

	@PostMapping("/update-customer-status")
	@ResponseBody
	public ResponseEntity<?> updateCustomerStatus(@RequestBody Customer customer) {
		try {
			Customer updatedCustomer = storageService.updateCustomerStatus(customer);
			return ResponseEntity.ok()
					.body(Map.of("message", "Cập nhật trạng thái thành công!", "customer", updatedCustomer));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("errorMessage", "Có lỗi xảy ra khi cập nhật trạng thái"));
		}
	}

//	@RequestMapping(value = { "/create-customer" }, method = RequestMethod.POST)
//	public ModelAndView saveCustomer(@ModelAttribute("customer") Customer customer, HttpServletRequest request, HttpSession httpSession) {
//	    ModelAndView mav = new ModelAndView();
//
//	    try {
//	    	storageService.saveOrUpdate(customer);
//	    	
//	     // Điều hướng về trang danh sách khách hàng sau khi lưu thành công
//	        mav.setViewName("redirect:/customer-list");
//	        //mav.addObject("successMessage", "Khách hàng đã được thêm thành công!");
//	    
//	    } catch (IllegalArgumentException e) {
//	    	mav.setViewName("createCustomer");
//	    	mav.addObject("errorMessage", e.getMessage()); 
//	        mav.addObject("customer", customer); 
//	    }
//
//	    initSession(request, httpSession);
//	    mav.addObject("currentSiteId", getCurrentSiteId());
//	    mav.addObject("userDisplayName", getCurrentUserDisplayName());
//
//	    return mav;
//	}

//	@PostMapping("/save-customer")
//	public ModelAndView addCustomer(@ModelAttribute Customer customer) {
//		try {
//			customerService.createCustomer(customer);
//
//			ModelAndView mav = new ModelAndView("redirect:/customer-list");
//			mav.addObject("successMessage", "Customer has been added successfully!");
//			return mav;
//
//		} catch (Exception e) {
//			ModelAndView mav = new ModelAndView("createCustomer");
//			mav.addObject("errorMessage", "Error occurred while adding the customer.");
//			return mav;
//		}
//	}
//	@RequestMapping(value = { "/add-customer" }, method = RequestMethod.GET)
//	public ModelAndView displayAddCustomerScreen(HttpServletRequest request, HttpSession httpSession) {
//		ModelAndView mav = new ModelAndView("createCustomer");
//		
//		mav.addObject("customer", new Customer());
//
//		initSession(request, httpSession);
//		mav.addObject("currentSiteId", getCurrentSiteId());
//		mav.addObject("userDisplayName", getCurrentUserDisplayName());
//		
//		//Long newId = customerService.getNextCustomerId(); // Lấy ID tiếp theo
//	    //Customer customer = new Customer(); // Tạo đối tượng Customer mới
//	    //customer.setId(newId); // Thiết lập ID mới cho khách hàng
//	    //mav.addObject("customer", customer); // Thêm khách hàng vào model
//		return mav;
//	}

	// Hiển thị trang thêm mới khách hàng
	@GetMapping("/add")
	public ModelAndView displayAddCustomerScreen(HttpServletRequest request, HttpSession httpSession) {
		// ModelAndView mav = new ModelAndView("addCustomer");
		ModelAndView mav = new ModelAndView("addCustomer_v2");
		Customer customer = new Customer();

		// Thêm đối tượng Customer mới vào Model để truyền vào form
		mav.addObject("customer", customer);

		// Lấy danh sách Status để đổ vào các dropdown chọn trạng thái
		List<Status> statuses = statusService.getAllStatuses();
		mav.addObject("statuses", statuses);

		List<ResponsiblePerson> responsiblePersons = responsiblePersonService.getAllResponsiblePersons();
		mav.addObject("responsiblePersons", responsiblePersons);

		List<Profession> professions = professionService.getAllProfessions();
		mav.addObject("professions", professions);

		// Thiết lập các thuộc tính của session
		initSession(request, httpSession);
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());

		long selectedProfession = 3L;
		mav.addObject("selectedProfession", selectedProfession);

		return mav;
	}

	@GetMapping("interact")
	public ModelAndView displayCustomerListScreen(@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "statusId", required = false) Long statusId,
			@RequestParam(value = "week", required = false) String week, HttpServletRequest request,
			HttpSession httpSession) {

		log.debug("Display Cusomter list with keyword= {}", keyword);
		ModelAndView mav = new ModelAndView("customerInteraction");
		initSession(request, httpSession);

		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());
		List<Customer> customers;

		if (statusId != null) {
			customers = customerService.findCustomersByStatus(statusId);
			mav.addObject("statusId", statusId);

		} else if (keyword != null && !keyword.isEmpty()) {
			customers = customerService.searchCustomers(keyword);
			mav.addObject("keyword", keyword);
		} else if (week != null && !week.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			LocalDate endDate = LocalDate.now();
			LocalDate startDate = null;

			if (week.equals("0")) {
				customers = customerService.getAllCustomersWithStatuses();

			} else {
				startDate = endDate.minusWeeks(Integer.parseInt(week));
				java.sql.Date sqlStartDate = java.sql.Date.valueOf(startDate);
				java.sql.Date sqlEndDate = java.sql.Date.valueOf(endDate);
				log.debug("" + sqlEndDate);
				log.debug("" + sqlStartDate);
				customers = customerService.findByInteractDateRange(sqlStartDate, sqlEndDate);
			}
		} else {
			customers = customerService.getAllCustomersWithStatuses();
			log.debug("No keyword or statusId provided. Fetching all customers.");
		}

		List<Status> statuses = statusService.getAllStatuses();
		List<ResponsiblePerson> responsiblePersons = responsiblePersonService.getAllResponsiblePersons();
		List<Profession> professions = professionService.getAllProfessions();

		Map<Long, Long> statusCounts = customerService.getCustomerCountsByStatus();

		if (statusCounts == null) {
			statusCounts = new HashMap<>();
		}

		long totalCustomerCount = customerService.getTotalCustomerCount();

		mav.addObject("customers", customers);
		mav.addObject("statuses", statuses);
		mav.addObject("responsiblePersons", responsiblePersons);
		mav.addObject("professions", professions);
		mav.addObject("statusCounts", statusCounts);
		mav.addObject("totalCustomerCount", totalCustomerCount);

		return mav;
	}

	/**
	 * @author Khoa
	 * @param customerId
	 * @param request
	 * @param httpSession
	 * @return
	 */
	@GetMapping("edit")
	public ModelAndView edit(@RequestParam("id") Long customerId, HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("editCustomerStatus_khoa");

		initSession(request, httpSession);
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());
		log.debug("Customer Detail is running....");

		Optional<Customer> customerOpt = customerService.findById(customerId);

		// Check if the customer exists and add to model
		customerOpt.ifPresentOrElse(customer -> {
			mav.addObject("customer", customer);
		}, () -> {
			mav.addObject("errorMessage", "Customer not found.");
		});

		return mav;
	}

	@GetMapping("/load-interaction")
	@ResponseBody
	public Object getInteractionData(@RequestParam("id") Long customerId) throws IOException {
		log.debug("Get interaction data for customer with ID: " + customerId);

		// Lấy tất cả các tương tác của khách hàng từ service
		List<Interaction> interactions = customerService.getAllCustomerInteraction(customerId);

		// Lấy danh sách tất cả contactPerson từ các tương tác của khách hàng
		List<String> contactPersons = interactions.stream().map(Interaction::getContactPerson) // Giả sử bạn có phương
																								// thức
																								// getContactPerson()
																								// trong Interaction
				.distinct() // Loại bỏ các giá trị trùng lặp
				.collect(Collectors.toList());

		// Chuyển đổi danh sách Interaction thành dữ liệu bảng
		List<Object[]> tblData = InteractionValidator.convertInteractionsToTableData(interactions);

		// Cấu trúc bảng
		int[] colWidths = { 200, 200, 380, 300, 30 };
		String[] colHeaders = { "Người trao đổi", "Ngày", "Nội dung trao đổi", "Kế hoạch tiếp theo", "" };

		// Tạo đối tượng trả về chứa các dữ liệu bảng và contactPersons
		Map<String, Object> response = new HashMap<>();
		response.put("colWidths", colWidths);
		response.put("colHeaders", colHeaders);
		response.put("data", tblData);
		response.put("contactPersons", contactPersons); // Thêm contactPersons vào response

		// Trả về đối tượng chứa các thông tin bảng và contactPersons
		return response;
	}

	@PostMapping(value = "/save-interaction")
	@ResponseBody
	public TableStructure saveInteraction(@RequestBody TableStructure tableData,
			@RequestParam("customer_id") Long customerId) {
		log.debug("saveInteraction...{}", tableData);

		try {
			List<Interaction> lstInteractions = InteractionValidator.validateAndCleasing(tableData.getData(),
					customerId);

			// Bước 2: Lưu hoặc cập nhật danh sách Interaction
			lstInteractions = customerService.saveOrUpdateInteraction(lstInteractions);

			// Bước 3: Chuyển đổi dữ liệu Interaction thành định dạng 2D để trả về giao diện
			List<Object[]> tblData = JpaTransformer_Interaction_Handsontable.convert2D(lstInteractions);
			tableData.setData(tblData);
		} catch (Exception ex) {
			log.error("Không thể lưu Interaction.", ex);
		}

		return tableData;
	}

	@RequestMapping(value = "/delete-interaction", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<?> deleteInteractionById(@RequestParam("id") Long interactionId, HttpServletRequest request,
			HttpSession httpSession) {
		try {
			if (interactionId == null) {
				return ResponseEntity.badRequest().body(Map.of("errorMessage", "ID không được để trống."));
			}
			// Gọi service để xóa Interaction dựa trên ID
			customerService.deleteInteractionById(interactionId);

			return ResponseEntity.ok()
					.body(Map.of("message", "Interaction đã được xóa thành công!", "id", interactionId));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("errorMessage", e.getMessage()));
		} catch (Exception e) {
			log.debug("Error while deleting interaction with ID: {}. Error: {}", interactionId, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("errorMessage", "Có lỗi xảy ra. Vui lòng thử lại sau!", "details", e.getMessage()));
		}
	}

	// Hàm edit hoặc add customer của Tấn Đạt
	@Transactional
	@RequestMapping(value = "/newEditCustomer")
	@ResponseBody
	public ModelAndView newEditCustomer(@RequestParam(value = "id", required = false) Long customerId,
			HttpServletRequest request, HttpSession httpSession) {

		ModelAndView mav = new ModelAndView("newEditCustomer");

		// Nếu customerId không tồn tại hoặc không tìm thấy, tạo mới một Customer
		Customer customer = (customerId == null) ? new Customer()
				: customerService.findById(customerId).orElse(new Customer());

		mav.addObject("customer", customer);

		// Lấy danh sách Status, ResponsiblePersons và Professions
		List<Status> statuses = statusService.getRepo().findAllOrderBySeqno();
		mav.addObject("statuses", statuses);

		List<ResponsiblePerson> responsiblePersons = responsiblePersonService.getRepo().findAllOrderBySeqno();
		mav.addObject("responsiblePersons", responsiblePersons);

		List<Profession> professions = professionService.getRepo().findAllOrderBySeqno();
		mav.addObject("professions", professions);

		// Thiết lập các thuộc tính của session
		initSession(request, httpSession);
		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());

		return mav;
	}
	
	private String getDefaultResponsiblePerson() throws IOException {
		return IOUtils.toString(resResponsiblePersonDemo.getInputStream(), StandardCharsets.UTF_8);
	}
	
	@GetMapping("/load-responsible-person")
	@ResponseBody
	public Object getResponsiblePersonData(@RequestParam("type") String type) throws IOException {
		log.debug("Get sample data from responsible person file.");
		//String jsonResponPersonTable = getDefaultResponsiblePerson();
		
		List<ResponsiblePerson> lstResponPerson;
		List<Profession> lstProfession;
		List<Status> lstStatus;
		List<GoodsCategory> lstGoodsCategory;
		int[] colWidths = {50, 300, 300, 100, 100};
		String[] colHeaders;
		List<Object[]> tblData;
		if(type.equals("customPerson")) {
			lstResponPerson = storageService.getResponPersonRepo().findAllOrderBySeqno();
			colHeaders = new String[]{"ID", "Tên người phụ trách", "Ghi chú", "Thứ tự ưu tiên","Hành động"};
			List<String> fields = List.of("id", "name", "note", "seqno");
			tblData = JpaTransformer_ResponsiblePerson_Handsontable.convert2DGeneric(lstResponPerson, fields);
		}
		else if(type.equals("customProfession")) {
			lstProfession = storageService.getProfessionRepo().findAllOrderBySeqno();
			colHeaders = new String[]{"ID", "Tên ngành nghề", "Ghi chú","Thứ tự ưu tiên", "Hành động"};
			List<String> fields = List.of("id", "name", "note", "seqno");
			tblData = JpaTransformer_ResponsiblePerson_Handsontable.convert2DGeneric(lstProfession, fields);
		}
		else if(type.equals("customStatus")){
			lstStatus = storageService.getStatusRepo().findAllOrderBySeqno();
			colHeaders = new String[]{"ID", "Tên trạng thái" , "BackgroundColor","Thứ tự ưu tiên","Hành động"};
			List<String> fields = List.of("id", "name", "backgroundColor", "seqno");
			tblData = JpaTransformer_ResponsiblePerson_Handsontable.convert2DGeneric(lstStatus, fields);
		}
		else {
			lstGoodsCategory = storageService.getGoodsCategoryRepo().findAllOrderBySeqno();
			colHeaders = new String[]{"ID", "Tên hàng hóa", "Ghi chú","Thứ tự ưu tiên", "Hành động"};
			List<String> fields = List.of("id", "name", "note", "seqno");
			tblData = JpaTransformer_ResponsiblePerson_Handsontable.convert2DGeneric(lstGoodsCategory, fields);
		}
		TableStructure tblOrderCate = new TableStructure(colWidths, colHeaders, tblData);

		return tblOrderCate;
		
	}
	
	@PostMapping(value = "/save-responsible-person")
	@ResponseBody
	public TableStructure saveResponsiblePerson(@RequestBody TableStructure tableData, @RequestParam("type") String type) {
		log.debug("saveResponsiblePerson...{}", tableData);

		try {
			List<ResponsiblePerson> lstResponsiblePerson;
			List<Profession> lstProfession;
			List<Status> lstStatus;
			List<Object[]> tblData;
			List<GoodsCategory> lstGoodsCategory;

			if(type.equals("customPerson")) {
				lstResponsiblePerson = ResponsiblePersonValidator.validateAndCleasing(tableData.getData());	
				lstResponsiblePerson = storageService.saveOrUpdateResponsiblePerson(lstResponsiblePerson);
				List<String> fields = List.of("id", "name", "note", "seqno");
				tblData = JpaTransformer_ResponsiblePerson_Handsontable.convert2DGeneric(lstResponsiblePerson, fields);
			}
			else if(type.equals("customProfession")) {
				lstProfession = ProfessionValidator.validateAndCleasing(tableData.getData());				
				lstProfession = storageService.saveOrUpdateProfession(lstProfession);
				List<String> fields = List.of("id", "name", "note", "seqno");
				tblData = JpaTransformer_ResponsiblePerson_Handsontable.convert2DGeneric(lstProfession, fields);
			}
			else if(type.equals("customStatus")){
				lstStatus = StatusValidator.validateAndCleasing(tableData.getData());
				lstStatus = storageService.saveOrUpdateStatus(lstStatus);
				List<String> fields = List.of("id", "name", "backgroundColor", "seqno");
				tblData = JpaTransformer_ResponsiblePerson_Handsontable.convert2DGeneric(lstStatus, fields);
			}
			else {
				lstGoodsCategory = GoodsCategoryValidator.validateAndCleasing(tableData.getData());
				lstGoodsCategory = storageService.saveOrUpdateGoodsCategory(lstGoodsCategory);
				List<String> fields = List.of("id", "name", "note", "seqno");
				tblData = JpaTransformer_ResponsiblePerson_Handsontable.convert2DGeneric(lstGoodsCategory, fields);
			}

			tableData.setData(tblData);
		} catch (Exception ex) {
			log.error("Could not save task.", ex);
		}
		return tableData;
	}
	
	@RequestMapping(value = "/delete-object", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<?> deleteObjectById(@RequestParam("id") Long id, @RequestParam("type") String type) {
		try {
			if (id == null) {
				return ResponseEntity.badRequest().body(Map.of("errorMessage", "ID không được để trống."));
			}
			if(type.equals("customPerson")) {
				storageService.deleteResponPerson(id);
			}
			else if(type.equals("customProfession")) {
				storageService.deleteProfessionById(id);
			}
			else if(type.equals("customStatus")){
				storageService.deleteStatusById(id);
			}
			else {
				storageService.deleteGoodsCategoryById(id);
			}
			return ResponseEntity.ok()
					.body(Map.of("message", "Đã được xóa thành công!", "id", id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("errorMessage", e.getMessage()));
		} catch (Exception e) {
			log.debug("Error while deleting with ID: {}. Error: {}", id, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("errorMessage", "Có lỗi xảy ra. Vui lòng thử lại sau!", "details", e.getMessage()));
		}
	}
	
	@PostMapping("/swap")
    public ResponseEntity<?> swapRows(@RequestParam("rowIndex1") Long rowIndex1, 
							            @RequestParam("rowIndex2") Long rowIndex2,
							            @RequestParam("type") String type) {
		try {
			storageService.swapRowOnHandsontable(rowIndex1, rowIndex2, type);
			return ResponseEntity.ok()
					.body(Map.of("message", "Đã hoán đổi thành công!"));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("errorMessage", e.getMessage()));
		} catch (Exception e) {
			log.debug("Error while swap. Error: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("errorMessage", "Có lỗi xảy ra. Vui lòng thử lại sau!", "details", e.getMessage()));
		}
    }
	
	// Hàm edit-customer của Đạt
	@PutMapping("/newedit-customer")
	@ResponseBody
	public ResponseEntity<?> editCustomer(@RequestBody Customer customer, HttpServletRequest request) {
		System.out.println(customer.getId());
		Customer updatedCustomer;
		try {

			// Lấy khách hàng cũ từ cơ sở dữ liệu
			Optional<Customer> customerOpt = customerService.findById(customer.getId());

			if (customerOpt.isPresent()) {

				// Cập nhật thông tin khách hàng
				Customer existingCustomer = customerOpt.get();

				existingCustomer.setCompanyName(customer.getCompanyName());
				existingCustomer.setContactPerson(customer.getContactPerson());
				existingCustomer.setEmail(customer.getEmail());
				existingCustomer.setPhone(customer.getPhone());
				existingCustomer.setAddress(customer.getAddress());
				existingCustomer.setResponsiblePerson(customer.getResponsiblePerson());
				existingCustomer.setNote(customer.getNote());
				existingCustomer.setProfession(customer.getProfession());
				existingCustomer.setMainStatus(customer.getMainStatus());
				existingCustomer.setSubStatus(customer.getSubStatus());
				// existingCustomer.setUpdatedAt(new Date()); // Cập nhật thời gian sửa nếu cần

				// Lưu lại khách hàng đã cập nhật
				updatedCustomer = storageService.saveOrUpdate(existingCustomer);

			}

			else {
				customer.setCreatedAt(new Date());
				customer.setSiteId(getCurrentSiteId());
				updatedCustomer = storageService.saveOrUpdate(customer);
			}

			log.info("Khách hàng đã được cập nhật thành công:");
			log.info("ID: {}", updatedCustomer.getId());
			log.info("Tên công ty: {}", updatedCustomer.getCompanyName());
			log.info("Người liên hệ: {}", updatedCustomer.getContactPerson());
			log.info("Email: {}", updatedCustomer.getEmail());
			log.info("Số điện thoại: {}", updatedCustomer.getPhone());
			log.info("Địa chỉ: {}", updatedCustomer.getAddress());
			// log.info("Ngày sửa: {}", updatedCustomer.getUpdatedAt()); // Cập nhật ngày
			// sửa nếu có
			log.info("Ghi chú: {}", updatedCustomer.getNote());

			if (updatedCustomer.getProfession() != null) {
				log.info("Ngành nghề: {}", updatedCustomer.getProfession().getName());
			} else {
				log.info("Ngành nghề: Không có");
			}

			if (updatedCustomer.getResponsiblePerson() != null) {
				log.info("Người phụ trách: {}", updatedCustomer.getResponsiblePerson().getName());
			} else {
				log.info("Người phụ trách: Không có");
			}
			if (customerOpt.isPresent()) {
				return ResponseEntity.ok().body(
						Map.of("message", "Khách hàng đã được cập nhật thành công!", "customer", updatedCustomer));
			} else {
				return ResponseEntity.ok().body(
						Map.of("message", "Khách hàng đã được thêm mới thành công!", "customer", updatedCustomer));
			}
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("errorMessage", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("errorMessage",
					"Có lỗi xảy ra khi cập nhật khách hàng. Vui lòng kiểm tra lại độ dài SDT!" + customer.getId()));
		}
	}

	@RequestMapping(value = { "/customer-list-search-son" }, method = RequestMethod.GET)
	public ModelAndView displayCustomerListCRMSearch(
			@RequestParam(value = "nameCompany", required = false) String nameCompany,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "selectedCareers", required = false) List<Long> selectedCareers,
			@RequestParam(value = "contactPerson", required = false) String contactPerson,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "email", required = false) String email, HttpServletRequest request,
			HttpSession httpSession) {

		ModelAndView mav = new ModelAndView("customer_list_v2");
		initSession(request, httpSession);

		mav.addObject("currentSiteId", getCurrentSiteId());
		mav.addObject("userDisplayName", getCurrentUserDisplayName());
		List<Customer> customers;

		log.debug("Selected Careers: {}", selectedCareers);

		if ((nameCompany == null || nameCompany.isEmpty()) && (phone == null || phone.isEmpty())
				&& (selectedCareers == null || selectedCareers.isEmpty())
				&& (contactPerson == null || contactPerson.isEmpty()) && (address == null || address.isEmpty())
				&& (email == null || email.isEmpty())) {
			customers = customerService.getAllCustomersWithStatuses();
			log.debug("No keyword or field provided. Fetching all customers.");
		} else if (selectedCareers == null || selectedCareers.isEmpty()) {
			customers = customerServiceSon.advancedSearchCustomersNotCareer(nameCompany, phone, selectedCareers,
					contactPerson, address, email);

		}

		else {
			customers = customerServiceSon.findCustomersAdvanced(nameCompany, phone, selectedCareers, contactPerson,
					address, email);

			mav.addObject("nameCompany", nameCompany);
			mav.addObject("phone", phone);
			mav.addObject("selectedCareers", selectedCareers);
			mav.addObject("contactPerson", contactPerson);
			mav.addObject("address", address);
			mav.addObject("email", email);
		}

		List<Status> statuses = statusService.getAllStatuses();
		List<ResponsiblePerson> responsiblePersons = responsiblePersonService.getAllResponsiblePersons();
		List<Profession> professions = professionService.getAllProfessions();

		Map<Long, Long> statusCounts = customerService.getCustomerCountsByStatus();

		if (statusCounts == null) {
			statusCounts = new HashMap<>();
		}
		long totalCustomerCount = customerService.getTotalCustomerCount();

		mav.addObject("customers", customers);
		mav.addObject("statuses", statuses);
		mav.addObject("responsiblePersons", responsiblePersons);
		mav.addObject("professions", professions);
		mav.addObject("statusCounts", statusCounts);
		mav.addObject("totalCustomerCount", totalCustomerCount);

		return mav;
	}
}
