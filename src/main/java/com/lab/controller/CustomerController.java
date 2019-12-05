package com.lab.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.lab.business.CustomerBusiness;
import com.lab.business.ProductBusiness;
import com.lab.exception.BusinessException;
import com.lab.model.Customer;
import com.lab.model.Order;
import com.lab.model.Product;
import com.lab.util.ViewUtils;

import lombok.Getter;
import lombok.Setter;

@ViewScoped
@Named
public class CustomerController implements Serializable {

	private static final long serialVersionUID = 2157226795467330765L;

	@Inject
	protected CustomerBusiness customerBusiness;

	@Inject
	protected ProductBusiness productBusiness;
	
	@Getter @Setter
	private List<Customer> customers;

	@Getter @Setter
	private Customer selectedCustomer;

	@Getter @Setter
	private Product selectedProduct;

	@Getter @Setter
	private Order selectedOrder;

	@Getter @Setter
	private String filterField = "all";

	@Getter @Setter
	private String keyword;
	
	public void initList() {
		try {
			customers = customerBusiness.getAll();
		} catch (Exception e) {
			ViewUtils.addError("Error initilizing list of Customers!");
		}
	}
	
	public void loadCustomer(Customer c) {
		selectedCustomer = c;
	}
	
	public void loadOrder(Order o) {
		selectedOrder = o;
	}

	public void loadCustomerProcuts(Customer c) {
		try {
			selectedCustomer = c;
			selectedCustomer.setProducts(productBusiness.getProductsByCustomer(selectedCustomer));
		} catch (BusinessException e) {
			ViewUtils.addWarn(e.getMessage());
		}
	}

	public void loadProduct(Product p) {
		selectedProduct = p;
	}
	
	public void filterList() {
		try {
			customers = customerBusiness.filter(filterField, keyword);
		} catch (BusinessException e) {
			ViewUtils.addWarn(e.getMessage());
		}
	}

}
