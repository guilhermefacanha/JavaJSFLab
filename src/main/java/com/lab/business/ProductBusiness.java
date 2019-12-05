package com.lab.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.lab.business.core.BaseBusiness;
import com.lab.dao.OrderDetailsDAO;
import com.lab.dao.ProductDAO;
import com.lab.dao.core.BaseDAO;
import com.lab.exception.BusinessException;
import com.lab.model.Customer;
import com.lab.model.Orderdetail;
import com.lab.model.OrderdetailPK;
import com.lab.model.Product;
import com.lab.util.ModelUtils;

public class ProductBusiness extends BaseBusiness<Product> {

	private static final long serialVersionUID = -1174836844990658617L;

	@Inject
	private ProductDAO productDAO;

	@Inject
	private OrderDetailsDAO orderDetailsDAO;

	@Override
	protected BaseDAO<Product> getDao() {
		return productDAO;
	}

	public List<Product> getProductsByCustomer(Customer customer) throws BusinessException {
		List<Orderdetail> orderDetails = orderDetailsDAO.getProductsIdByCustomer(customer.getCustomerNumber());
		List<String> productsId = new ArrayList<String>();
		List<OrderdetailPK> orderDetailsPks = new ArrayList<OrderdetailPK>();
		orderDetails.forEach(od -> {
			if (!productsId.contains(od.getId().getProductCode()))
				productsId.add(od.getId().getProductCode());
			orderDetailsPks.add(od.getId());
		});
		
		if (!ModelUtils.isValidList(productsId))
			throw new BusinessException("No products found in Orders by selected Customer");

		List<Criterion> restrictions = new ArrayList<Criterion>();
		restrictions.add(Restrictions.in("productCode", productsId));
		List<Product> products = productDAO.getFilteredList(restrictions);
		
		final List<String> joinsOd = Arrays.asList("order");
		products.forEach(p->{
			final List<Criterion> restrictionsOd = new ArrayList<Criterion>();
			restrictionsOd.add(Restrictions.in("id", orderDetailsPks));
			restrictionsOd.add(Restrictions.eq("id.productCode", p.getProductCode()));
			p.setOrderdetails(orderDetailsDAO.getFilteredList(restrictionsOd,joinsOd));
		});

		return products;
	}

}
