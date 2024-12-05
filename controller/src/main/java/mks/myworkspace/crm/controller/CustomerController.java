package mks.myworkspace.crm.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.common.model.TableStructure;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.Interaction;
import mks.myworkspace.crm.entity.Profession;
import mks.myworkspace.crm.entity.ResponsiblePerson;
import mks.myworkspace.crm.entity.Status;
import mks.myworkspace.crm.service.CustomerService;
import mks.myworkspace.crm.service.ProfessionService;
import mks.myworkspace.crm.service.ResponsiblePersonService;
import mks.myworkspace.crm.service.StatusService;
import mks.myworkspace.crm.service.StorageService;
import mks.myworkspace.crm.transformer.JpaTransformer_Interaction_Handsontable;
import mks.myworkspace.crm.validate.InteractionValidator;

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

	@GetMapping("list")
	public ModelAndView displayCustomerListCRMScreen(@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "statusId", required = false) Long statusId, HttpServletRequest request,
			HttpSession httpSession) {

		log.debug("Display Cusomter list with keyword= {}", keyword);
		ModelAndView mav = new ModelAndView("customer_list_v2");
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

		// Thêm đối tượng Customer mới vào Model để truyền vào form
		mav.addObject("customer", new Customer());

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

		return mav;
	}

	@GetMapping("interact")
	public ModelAndView displayCustomerListScreen(@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "statusId", required = false) Long statusId, HttpServletRequest request,
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

	    // Chuyển đổi danh sách Interaction thành dữ liệu bảng
	    List<Object[]> tblData = InteractionValidator.convertInteractionsToTableData(interactions);

	    // Cấu trúc bảng
	    int[] colWidths = {150, 200, 400, 300, 30};
	    String[] colHeaders = {"Người trao đổi",  "Ngày", "Nội dung trao đổi", "Kế hoạch tiếp theo", ""};

	    // Trả về đối tượng TableStructure
	    return new TableStructure(colWidths, colHeaders, tblData);
	}
	
	@PostMapping(value = "/save-interaction")
	@ResponseBody
	public TableStructure saveInteraction(@RequestBody TableStructure tableData, @RequestParam("customer_id") Long customerId) {
	    log.debug("saveInteraction...{}", tableData);

	    try {
	        List<Interaction> lstInteractions = InteractionValidator.validateAndCleasing(tableData.getData(), customerId);
	        
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
	public ResponseEntity<?> deleteInteractionById(@RequestParam("id") Long interactionId, 
	                                               HttpServletRequest request,
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
}
