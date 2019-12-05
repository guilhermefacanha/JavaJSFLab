package com.jsonar.model.test;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import org.jglue.cdiunit.AdditionalClasspaths;
import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.InRequestScope;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import com.lab.business.CustomerBusiness;
import com.lab.dao.CustomerDAO;
import com.lab.dao.core.BaseDAO;
import com.lab.exception.BusinessException;
import com.lab.model.Customer;
import com.lab.util.ModelUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(CdiRunner.class)
@AdditionalClasspaths({ Customer.class, CustomerDAO.class, BaseDAO.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerTest {

	@Inject
	CustomerBusiness customerBusiness;

	@Getter
	@Setter
	private static Integer customerId;

	@BeforeClass
	public static void init() {
		log.info("=========================================================");
		log.info("========== Test Unit FirstTest ==========");
		log.info("=========================================================");
	}

	@AfterClass
	public static void finish() {
		log.info("=========================================================");
		log.info("========== END Unit FirstTest  ==========");
		log.info("=========================================================");
	}

	@Before
	public void initTest() {
	}

	@InRequestScope
	@Test
	public void test00CDIInjection() {
		Assert.assertTrue(customerBusiness != null);
	}

	@InRequestScope
	@Test
	public void test01DatabaseSelectTest() {
		customerBusiness.testSelectDb();
	}

	@InRequestScope
	@Test
	public void test02CustomerSizeTest() {
		Long size = customerBusiness.getSize();
		log.info("Number of records on Customer Table: {}", size);
		Assert.assertTrue(size > 0);
	}

	@InRequestScope
	@Test
	public void test03CustomerCreateTest() {
		Customer c = Customer.builder().addressLine1("New Address Line 1").addressLine2("New Address Line 2")
				.city("New City").contactFirstName("New Contact First Name").contactLastName("New Contact Last Name")
				.country("New Country").creditLimit(new BigDecimal(1000)).customerName("New Customer Name")
				.phone("9999999999999999").postalCode("VVVTTT").salesRepEmployeeNumber(99).state("New State").build();
		try {
			c = customerBusiness.save(c);
			Assert.assertTrue(!c.isNew());
			setCustomerId(c.getCustomerNumber());
			Customer c2 = customerBusiness.getObjectById(getCustomerId());
			Assert.assertTrue(c2 != null);
		} catch (Exception e) {
			log.error("Error on Test CustomerCreateTest: ", e);
		}
	}

	@InRequestScope
	@Test
	public void test04CustomerUpdateTest() {
		Customer c = null;
		String city = "Updated City";
		try {
			c = customerBusiness.getObjectById(getCustomerId());
			Assert.assertTrue("Customer object should be not null (found)", c != null);
			c.setCity(city);
			customerBusiness.save(c);
			Customer c2 = customerBusiness.getObjectById(getCustomerId());
			Assert.assertEquals("City should be updated to " + city, city, c2.getCity());
		} catch (Exception e) {
			log.error("Error on Test CustomerUpdateTest: ", e);
			Assert.fail("Exception thrown in selecting database object by id");
		}
	}

	@InRequestScope
	@Test
	public void test05CustomerRemoveTest() {
		try {
			customerBusiness.remove(getCustomerId());
			Customer c = customerBusiness.getObjectById(getCustomerId());
			Assert.assertTrue("Customer object should be null (not found)", c == null);
		} catch (Exception e) {
			log.error("Error on Test CustomerRemoveTest: ", e);
			Assert.fail("Exception thrown in removing database object");
		}
	}

	@InRequestScope
	@Test
	public void test06CustomerFilter() {
		try {
			List<Customer> list = customerBusiness.filter("all", "toy");
			Assert.assertTrue("List should return elements", ModelUtils.isValidList(list));
			list = customerBusiness.filter("customerNumber", "112");
			Assert.assertTrue("List should return elements", ModelUtils.isValidList(list));
			list = customerBusiness.filter("country", "USA");
			Assert.assertTrue("List should return elements", ModelUtils.isValidList(list));
			list = customerBusiness.filter("state", "CA");
			Assert.assertTrue("List should return elements", ModelUtils.isValidList(list));
			list = customerBusiness.filter("city", "NYC");
			Assert.assertTrue("List should return elements", ModelUtils.isValidList(list));
			list = customerBusiness.filter("postalCode", "10022");
			Assert.assertTrue("List should return elements", ModelUtils.isValidList(list));
			list = customerBusiness.filter("customerName", "toy");
			Assert.assertTrue("List should return elements", ModelUtils.isValidList(list));
		} catch (BusinessException e) {
			log.error("Error on Test CustomerRemoveTest: ", e);
			Assert.fail("Exception thrown in removing database object");
		}
	}

	@InRequestScope
	@Test(expected = BusinessException.class)
	public void test07CustomerFilterFail() throws BusinessException {
		customerBusiness.filter("all", "asdasdasdasdasdgesbwevwvraev");
		Assert.fail("Exception should be thrown");
	}

}