package com.lab.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;

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
public class CustomerControllerAjax implements Serializable {

	private static final long serialVersionUID = 4495451106950494893L;

	@Inject
	protected CustomerBusiness customerBusiness;

	@Inject
	protected ProductBusiness productBusiness;
	
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
	
	@Inject
	private LazyCustomerDataModel customerDataModel;
	
	public void initList() {
		customerDataModel.setCustomerBusiness(this.customerBusiness);
	}
	
	public LazyDataModel<Customer> getCustomersAjax(){
		return customerDataModel;
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
	
	
}
