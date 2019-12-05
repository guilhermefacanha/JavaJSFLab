package com.lab.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.lab.business.CustomerBusiness;
import com.lab.model.Customer;
import com.lab.util.ModelUtils;

public class LazyCustomerDataModel extends LazyDataModel<Customer> {

	private static final long serialVersionUID = -3220532054242843335L;
	
	private CustomerBusiness customerBusiness;
	
	private boolean initialized;
	
	@Override
	public Object getRowKey(Customer c) {
		return c.getId();
	}
	
	@Override
	public List<Customer> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
		if(!initialized)
			initialize();
		return customerBusiness.getAllWithPaging("", SortOrder.ASCENDING, first, pageSize);
	}
	
	@Override
	public List<Customer> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		if(!initialized)
			initialize();
		if(filters!=null && !filters.isEmpty()) {
			initialized=false;
			List<Criterion> restrictions = getRestrictions(filters);
			setRowCount(Integer.parseInt(customerBusiness.getSizeWithRestrictions(restrictions)+""));
			return customerBusiness.getFilteredWithPaging(sortField, sortOrder, first, pageSize, restrictions);
		}
		else
			return customerBusiness.getAllWithPaging(sortField, sortOrder, first, pageSize);
	}
	
	private List<Criterion> getRestrictions(Map<String, Object> filters) {
		List<Criterion> restrictions = new ArrayList<>();
		if(filters.containsKey("customerNumber")) {
			Integer customerNumber = ModelUtils.tryParseInt(filters.get("customerNumber").toString());
			restrictions.add(Restrictions.eq("customerNumber", customerNumber));
		}
		if(filters.containsKey("country")) {
			restrictions.add(Restrictions.like("country", "%"+filters.get("country")+"%").ignoreCase());
		}
		if(filters.containsKey("state")) {
			restrictions.add(Restrictions.like("state", "%"+filters.get("state")+"%").ignoreCase());
		}
		if(filters.containsKey("city")) {
			restrictions.add(Restrictions.like("city", "%"+filters.get("city")+"%").ignoreCase());
		}
		if(filters.containsKey("postalCode")) {
			restrictions.add(Restrictions.like("postalCode", "%"+filters.get("postalCode")+"%").ignoreCase());
		}
		if(filters.containsKey("customerName")) {
			restrictions.add(Restrictions.like("customerName", "%"+filters.get("customerName")+"%").ignoreCase());
		}
		return restrictions ;
	}

	public void setCustomerBusiness(CustomerBusiness c) {
		this.customerBusiness = c;
	}

	private void initialize() {
		if(this.customerBusiness!=null) {
			this.setRowCount(Integer.parseInt(customerBusiness.getSize()+""));
			initialized = true;
		}
	}

}
