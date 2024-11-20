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

package mks.myworkspace.crm.common;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lombok.extern.slf4j.Slf4j;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import mks.myworkspace.crm.common.AppUtility;
import mksgroup.java.common.CommonUtil;

/**
 * An example Utility.
 * 
 * @author Thach Ngoc Le (ThachLN@mks.com.vn)
 *
 */
@Slf4j
public class AppUtility {

    /**
     * Parse a XML File into a document model.
     * @param xmlFile file of xml.
     * @return Document model if no error.
     */
    public static Document parseXML(File xmlFile) {
        Document xmlDoc = null;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;

        try {
            db = dbf.newDocumentBuilder();
            xmlDoc = db.parse(xmlFile);
        } catch (ParserConfigurationException ex) {
            log.error("Could not parse the file.", ex);
        } catch (SAXException ex) {
        	log.error("Error in XML file.", ex);
        } catch (IOException ex) {
        	log.error("Could not open file.", ex);
        }
        
        return xmlDoc;
    }
    
    /** 
     * Kiem tra dinh dang cua so dien thoai. Check format of phone number
     * Noi dung kiem tra gom: Conditions to check:
     * - SDT phai co 10 chu so - Phone considers 10 digits
     * - 
     * @param phone (Khong bao gom ma vung Not include area code) 
     * @return true neu phone thoa man dieu kien o tren if it satisfies the ahea.
     * Nguoc lai, thi la false.
     */
    public static Boolean checkPhone(String phone) {
    	//String newPhone = CommonUtil.parsePattern(phone, "\\d\\\\d\\\\d\\\\d\\\\d\\\\d\\\\d\\\\d\\\\d\\\\d");
    	return phone != null && phone.length() == 10 && phone.chars().allMatch(Character::isDigit);
    }
    
    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^\\d{10}$";
        return phoneNumber.matches(regex);
    }
}
