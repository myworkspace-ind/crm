package mks.myworkspace.crm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.CustomerCare;
import mks.myworkspace.crm.service.CustomerCareService;
import mks.myworkspace.crm.service.CustomerService;
import mks.myworkspace.crm.transformer.JpaTransformer_CustomerCare;

@Controller
@Slf4j
@RequestMapping("/customer-care")
public class CustomerCareController extends BaseController{
	
	@Autowired
	CustomerCareService customerCareService;
	
	@Autowired
	CustomerService customerService;
	
	@GetMapping(value = "/load-potential", produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> loadPotentialCustomers() {
        try {
            customerCareService.loadPotentialCustomersIntoCustomerCare();
            return ResponseEntity.ok("Nạp khách hàng tiềm năng vào CustomerCare thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Lỗi khi nạp khách hàng: " + e.getMessage());
        }
    }
	
	@GetMapping("/load-customer-care")
	public ResponseEntity<?> getPotentialCustomers() {
		try {
			List<CustomerCare> customerCares = customerCareService.findAll();
			List<Customer> listCustomers = customerService.getAllCustomers();
	        
	        if (customerCares.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NO_CONTENT)
	                                 .body("Không có khách hàng tiềm năng nào.");
	        }
	        
	        List<Object[]> dataSetCustomerCare = JpaTransformer_CustomerCare.convert2D(customerCares, listCustomers);

	        return ResponseEntity.ok(dataSetCustomerCare);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi lấy danh sách khách hàng: " + e.getMessage());
		}
	}
	
    

}
