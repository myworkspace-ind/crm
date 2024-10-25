/**
 * Licensed to MKS Group under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * MKS Group licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package mks.myworkspace.crm.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.service.StorageService;

/**
 * Handles requests for the application home page.
 */
@Controller
@Slf4j
@RequestMapping("/orders")
public class OrderController extends BaseController {

	/**
	 * This method is called when binding the HTTP parameter to bean (or model).
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// Sample init of Custom Editor

//        Class<List<ItemKine>> collectionType = (Class<List<ItemKine>>)(Class<?>)List.class;
//        PropertyEditor orderNoteEditor = new MotionRuleEditor(collectionType);
//        binder.registerCustomEditor((Class<List<ItemKine>>)(Class<?>)List.class, orderNoteEditor);

	}

	/**
	 * Simply selects the home view to render by returning its name.
	 * 
	 * @return
	 */

	@Value("classpath:task/task-demo.json")
	private Resource resTaskDemo;

	@Autowired
	StorageService storageService;

	@GetMapping("")
	public ModelAndView displayHome(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mav = new ModelAndView("ordersCRMScreen");

		initSession(request, httpSession);
		return mav;
	}

//	@GetMapping("/load")
//	@ResponseBody
//	public Object getTaskData() throws IOException {
//		log.debug("Get sample data from configuration file.");
//		String jsonOrdersTable = getDefaultOrdersData();
//
//		List<Task> lstTasks = storageService.getTaskRepo().findAll();
//
//		if (lstTasks == null || lstTasks.isEmpty()) {
//			return jsonTaskTable;
//		} else {
//			JSONObject jsonObjTableTask = new JSONObject(jsonTaskTable);
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
//			List<Object[]> tblData = JpaTransformer.convert2D(lstTasks);
//
//			TableStructure tblTask = new TableStructure(colWidths, colHeaders, tblData);
//
//			return tblTask;
//		}
//	}
	
	private String getDefaultOrdersData() throws IOException {
		return IOUtils.toString(resTaskDemo.getInputStream(), StandardCharsets.UTF_8);
	}

}
