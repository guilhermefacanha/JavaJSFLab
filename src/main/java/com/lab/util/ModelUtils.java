package com.lab.util;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ModelUtils implements Serializable {
	private static final long serialVersionUID = -856390333772995856L;
	
	public ModelUtils() {
		throw new ExceptionInInitializerError("Should not initialize ModelUtils class");
	}

	public static boolean isNullEmpty(String str) {
		return str == null || str.isEmpty();
	}

	public static boolean isNullZero(Integer number) {
		return number == null || number.equals(0);
	}

	public static boolean isValidList(List<? extends Object> list) {
		return list != null && list.size() > 0;
	}
	
	public static String formatValue(double value) {
		return NumberFormat.getCurrencyInstance(Locale.CANADA).format(value);
	}

	public static String formatInteger(double value) {
		return NumberFormat.getNumberInstance().format(value);
	}

	public static Integer tryParseInt(String keyword) {
		try {
			return Integer.parseInt(keyword);
		} catch (Exception e) {
			return null;
		}
	}
}
