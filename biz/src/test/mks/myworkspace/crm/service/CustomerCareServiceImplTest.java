package mks.myworkspace.crm.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import mks.myworkspace.crm.entity.CustomerCare;
import mks.myworkspace.crm.repository.CustomerCareRepository;
import mks.myworkspace.crm.service.impl.CustomerCareServiceImpl;

/**
 * This is a class for UNIT testing (do test for serviceimpl class)
 * @author Nguyen Hoang Phuong Ngan
 */
@RunWith(MockitoJUnitRunner.class)
public class CustomerCareServiceImplTest {
	
	@Mock
	private CustomerCareRepository customerCareRepository;
	
	@InjectMocks
	private CustomerCareServiceImpl customerCareServiceImpl;

	@Test
	public void testFindAll() {
		// Fake data
		CustomerCare cc1 = new CustomerCare();
		CustomerCare cc2 = new CustomerCare();
		List<CustomerCare> list = List.of(cc1, cc2);
		
		// Khi gọi repo thì giả lập kết quả
		when(customerCareRepository.findAllCustomerCares()).thenReturn(list);
		
		// Gọi hàm service
		List<CustomerCare> result = customerCareServiceImpl.findAll();

        // Kiểm tra
        assertEquals(2, result.size());
        verify(customerCareRepository, times(1)).findAllCustomerCares();
	}
	
	@Test
	public void testExample() {
	    assertEquals(2, 1 + 1);
	}
}

