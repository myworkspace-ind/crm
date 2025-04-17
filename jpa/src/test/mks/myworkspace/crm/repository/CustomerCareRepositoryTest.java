package mks.myworkspace.crm.repository;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.CustomerCare;
import mks.myworkspace.crm.entity.Status;
/**
 * This is a class for INTEGRATION testing (do test for repository interface)
 * @author Nguyen Hoang Phuong Ngan
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/applicationContext-test.xml")
public class CustomerCareRepositoryTest {

	@Autowired
	private CustomerCareRepository customerCareRepository;

	@Autowired
	private EntityManager entityManager;
	
	@Autowired
    @Qualifier("jdbcTemplate0")
    private JdbcTemplate jdbcTemplate;

//	@Test
//	public void test() {
//		int result = 2 + 3;
//		assertEquals(5, result);
//	}

	@Test
	@Transactional
	public void testFindAllCustomerCares() {
	    // Tạo dữ liệu mẫu cho Khách A
		Status existingStatus1 = entityManager.find(Status.class, 1L);
	    //entityManager.merge(existingStatus); // Persist Status trước khi sử dụng

	    Customer customer1 = new Customer();
	    customer1.setCompanyName("Khách A");
	    customer1.setMainStatus(existingStatus1); // Gán Status đã persist
	    entityManager.persist(customer1);

	    CustomerCare cc1 = new CustomerCare();
	    cc1.setCustomer(customer1);
	    entityManager.persist(cc1);

	    // Khách B
	    Status existingStatus2 = entityManager.find(Status.class, 3L);
	    //entityManager.merge(potentialStatus); // Persist Status trước khi sử dụng

	    Customer customer2 = new Customer();
	    customer2.setCompanyName("Khách B");
	    customer2.setMainStatus(existingStatus2); // Gán Status đã persist
	    entityManager.persist(customer2);

	    CustomerCare cc2 = new CustomerCare();
	    cc2.setCustomer(customer2);
	    entityManager.persist(cc2);

	    // Flush để ghi dữ liệu vào DB
	    entityManager.flush();

	    // Kiểm tra kết quả
	    List<CustomerCare> result = customerCareRepository.findAllCustomerCares();
	    
	    log.info("Kết quả tìm thấy: " + result.size() + " bản ghi.");
        if (!result.isEmpty()) {
            log.info("Khách hàng đầu tiên: " + result.get(0).getCustomer().getCompanyName());
        }

	    assertFalse(result.isEmpty());
	    assertEquals(2, result.size());

	    assertEquals("Khách A", result.get(0).getCustomer().getCompanyName());
	    assertEquals("Khách B", result.get(1).getCustomer().getCompanyName());
	}

}
