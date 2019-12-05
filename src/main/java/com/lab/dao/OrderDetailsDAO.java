package com.lab.dao;

import java.util.List;

import com.lab.dao.core.BaseDAO;
import com.lab.model.Orderdetail;

public class OrderDetailsDAO extends BaseDAO<Orderdetail> {

	private static final long serialVersionUID = 3910769509549502072L;

	@SuppressWarnings("unchecked")
	public List<Orderdetail> getProductsIdByCustomer(Integer customerId) {
		return manager.createQuery(
				"Select x FROM Orderdetail x where x.order.customer.customerNumber = :customerId")
				.setParameter("customerId", customerId).getResultList();
	}

}
