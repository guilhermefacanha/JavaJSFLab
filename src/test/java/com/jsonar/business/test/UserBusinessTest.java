package com.jsonar.business.test;

import static org.junit.Assert.fail;

import javax.inject.Inject;

import org.jglue.cdiunit.AdditionalClasspaths;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import com.lab.business.UserBusiness;
import com.lab.dao.UserDAO;
import com.lab.exception.BusinessException;
import com.lab.model.User;

@RunWith(CdiRunner.class)
@AdditionalClasspaths({ User.class, UserDAO.class, UserBusiness.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserBusinessTest {

	@Inject
	UserBusiness userBusiness;

	@Test(expected = ExceptionInInitializerError.class)
	public void test00UserDaoInitializationFailTest() throws BusinessException {
		new UserDAO();
		fail("Should throw ExceptionInInitializerError");
	}

	@Test(expected = BusinessException.class)
	public void test01UserAuthenticationFailTest() throws BusinessException {
		User u = User.builder().username("Wrong User").password("Wrong Password").build();
		userBusiness.getUserAuthenticated(u);
		fail("Should throw BusinessException");
	}

	@Test
	public void test02UserAuthenticationTest() throws BusinessException {
		User u = User.builder().username("Test1").password("test1@mytest.com").build();
		User userAuthenticated = userBusiness.getUserAuthenticated(u);
		Assert.assertTrue("User should be not null",userAuthenticated!=null);
		u = null;
		userAuthenticated = null;
		
		
		u = User.builder().username("Test2").password("test2@mytest.com").build();
		userAuthenticated = userBusiness.getUserAuthenticated(u);
		Assert.assertTrue("User should be not null",userAuthenticated!=null);

	}
}
