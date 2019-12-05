package com.jsonar.util.test;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.lab.util.ModelUtils;

public class ModelUtilsTest {

	@Test(expected = ExceptionInInitializerError.class)
	public void test00ModelUtilsInitializationFail() {
		new ModelUtils();
		fail("Should throw ExceptionInInitializerError");
	}

	@Test
	public void test01StringNull() {
		boolean nullEmptyStr = ModelUtils.isNullEmpty(null);
		Assert.assertTrue("Should be true", nullEmptyStr);
		nullEmptyStr = ModelUtils.isNullEmpty("");
		Assert.assertTrue("Should be true", nullEmptyStr);
		nullEmptyStr = ModelUtils.isNullEmpty("something");
		Assert.assertTrue("Should be false", !nullEmptyStr);
	}

	@Test
	public void test02IntegerNullZero() {
		boolean nullInt = ModelUtils.isNullZero(null);
		Assert.assertTrue("Should be true", nullInt);
		nullInt = ModelUtils.isNullZero(new Integer(0));
		Assert.assertTrue("Should be true", nullInt);
		nullInt = ModelUtils.isNullZero(new Integer(10));
		Assert.assertTrue("Should be false", !nullInt);
	}

	@Test
	public void testValidList() {
		List<String> l = null;
		Assert.assertTrue("Should be False", !ModelUtils.isValidList(l));
		l = new ArrayList<>();
		Assert.assertTrue("Should be False", !ModelUtils.isValidList(l));
		l.add("element");
		Assert.assertTrue("Should be True", ModelUtils.isValidList(l));
	}

	@Test
	public void testFormatters() {
		Assert.assertTrue("Should be False", ModelUtils.formatInteger(0.0).equals("0"));
		Assert.assertTrue("Should be False", ModelUtils.formatValue(0.0).equals("$0.00"));
	}
}
