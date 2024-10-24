package mks.myworkspace.crm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.common.model.TableStructure;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.service.StorageService;
import mks.myworkspace.crm.transformer.JpaTransformer;
import mks.myworkspace.crm.validate.CustomerValidator;

/**
 * Handles requests for Tasks.
 */
@Controller
@Slf4j
@RequestMapping("/customer-management")
public class CustomerManagementController extends BaseController {

//	@Value("classpath:customer/demo.json")
//	private Resource resTaskDemo;

	@Autowired
	StorageService storageService;

	@GetMapping("")
	public ModelAndView displayHome(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("multicustomer");

		initSession(request, httpSession);
		return mav;
	}

	@GetMapping("/load")
	@ResponseBody
	public Object getCustomerData() throws IOException {
		log.debug("Get sample data from configuration file.");
//		String jsonCustomerTable = getDefaultTaskData();

//		List<Customer> lstCustomers = storageService.getCustomerRepo().findAll();

		int[] colWidths= {
		        50,
		        130,
		        300,
		        100
		};
		String[] colHeaders = {
		        "ID",
		        "Name",
		        "Address",
		        "Phone"
		};
		List<Object[]> tblData = new ArrayList<>();
		Object[] data1 = new Object[] {"","Nguyễn Văn Anh", "170 Phan Đăng Lưu, Phường 3, Phú Nhuận, Hồ Chí Minh", "0909353535"};
		Object[] data2 = new Object[] {"","Lê Thị Hoa", "16 Lê Thị Hoa, Phường Bình Chiểu, Thủ Đức, Hồ Chí Minh", "0909454545"};
		
		tblData.add(data1);
		tblData.add(data2);
		// if (lstCustomers == null || lstCustomers.isEmpty()) {
////			return jsonCustomerTable;
//		} else {
//			JSONObject jsonObjTableTask = new JSONObject(jsonCustomerTable);
//
//			JSONArray jsonObjColWidths = jsonObjTableTask.getJSONArray("colWidths");
//			int len = (jsonObjColWidths != null) ? jsonObjColWidths.length() : 0;
//			int[] colWidths = new int[len];
//			for (int i = 0; i < jsonObjColWidths.length(); i++) {
//				colWidths[i] = jsonObjColWidths.getInt(i);
//			}
//
//			JSONArray jsonObjColHeaders = jsonObjTableTask.getJSONArray("colHeaders");
//			len = (jsonObjColHeaders != null) ? jsonObjColHeaders.length() : 0;
//			String[] colHeaders = new String[len];
//			for (int i = 0; i < jsonObjColHeaders.length(); i++) {
//				colHeaders[i] = jsonObjColHeaders.getString(i);
//			}
//
//			List<Object[]> tblData = JpaTransformer.convert2D(lstCustomers);
//
//			TableStructure tblTask = new TableStructure(colWidths, colHeaders, tblData);
//
//			return tblTask;
//		}
//		
		TableStructure tblTask = new TableStructure(colWidths, colHeaders, tblData);

		return tblTask;
	}

	@PostMapping(value = "/save")
	@ResponseBody
	public TableStructure saveTask(@RequestBody TableStructure tableData) {
		log.debug("saveCustomer...{}", tableData);

		try {
			List<Customer> lstCustomers = CustomerValidator.validateAndCleasing(tableData.getData());
			lstCustomers = storageService.saveOrUpdate(lstCustomers);

			List<Object[]> tblData = JpaTransformer.convert2D(lstCustomers);
			tableData.setData(tblData);
		} catch (Exception ex) {
			log.error("Could not save task.", ex);
		}

		return tableData;
	}

//	private String getDefaultTaskData() throws IOException {
//		return IOUtils.toString(resTaskDemo.getInputStream(), StandardCharsets.UTF_8);
//	}
}
