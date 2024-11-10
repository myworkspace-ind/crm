package mks.myworkspace.crm.common;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class AppUtilityTest {

	@Test
	public void testCheckPhone() {
		//fail("Not yet implemented");
		Boolean isOK = AppUtility.checkPhone("0987654321");
		//Boolean isOK = AppUtility.isValidPhoneNumber("0987654321");
		Assert.assertTrue(isOK);
	}
	
	@Test
	public void testCheckPhone2() {
		//fail("Not yet implemented");
		Boolean isOK = AppUtility.checkPhone("098765432");
		//Boolean isOK = AppUtility.isValidPhoneNumber("0987654321");
		Assert.assertFalse(isOK);
	}

}
