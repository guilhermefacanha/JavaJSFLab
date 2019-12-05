package com.lab.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.lab.business.core.BaseBusiness;
import com.lab.dao.CustomerDAO;
import com.lab.dao.core.BaseDAO;
import com.lab.exception.BusinessException;
import com.lab.model.Customer;
import com.lab.util.ModelUtils;

public class CustomerBusiness extends BaseBusiness<Customer> implements Serializable {

	private static final long serialVersionUID = -1561887107455496586L;
	
	@Inject private CustomerDAO customerDAO;

	@Override
	protected BaseDAO<Customer> getDao() {
		return customerDAO;
	}

	public List<Customer> filter(String filterField, String keyword) throws BusinessException {
		List<Criterion> restrictions = new ArrayList<Criterion>();

		Integer customerNumber = ModelUtils.tryParseInt(keyword);
		if(filterField.equals("all")){
			restrictions.add(Restrictions.disjunction()
					.add(Restrictions.like("country", "%"+keyword+"%").ignoreCase())
					.add(Restrictions.like("state", "%"+keyword+"%").ignoreCase())
					.add(Restrictions.like("city", "%"+keyword+"%").ignoreCase())
					.add(Restrictions.like("postalCode", "%"+keyword+"%").ignoreCase())
					.add(Restrictions.like("customerName", "%"+keyword+"%").ignoreCase())
					.add(Restrictions.eq("customerNumber", customerNumber))
					);
		}
		else if(filterField.equals("customerNumber")) {
			restrictions.add(Restrictions.eq("customerNumber", customerNumber));
		}
		else if(filterField.equals("country")) {
			restrictions.add(Restrictions.like("country", "%"+keyword+"%").ignoreCase());
		}
		else if(filterField.equals("state")) {
			restrictions.add(Restrictions.like("state", "%"+keyword+"%").ignoreCase());
		}
		else if(filterField.equals("city")) {
			restrictions.add(Restrictions.like("city", "%"+keyword+"%").ignoreCase());
		}
		else if(filterField.equals("postalCode")) {
			restrictions.add(Restrictions.like("postalCode", "%"+keyword+"%").ignoreCase());
		}
		else if(filterField.equals("customerName")) {
			restrictions.add(Restrictions.like("customerName", "%"+keyword+"%").ignoreCase());
		}
		
		List<Customer> filteredList = customerDAO.getFilteredList(restrictions);
		if(!ModelUtils.isValidList(filteredList))
			throw new BusinessException("No records found for selected Filter");
		
		
		return filteredList;
	}

	

}
