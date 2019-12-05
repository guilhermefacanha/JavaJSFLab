package com.lab.business.core;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.primefaces.model.SortOrder;

import com.lab.dao.core.BaseDAO;
import com.lab.exception.BusinessException;
import com.lab.model.core.BaseEntity;

public abstract class BaseBusiness<T extends BaseEntity> implements Serializable {
	private static final long serialVersionUID = 7392952886823572130L;

	protected abstract BaseDAO<T> getDao();

	public T save(T entity) throws BusinessException {
		try {
			return getDao().save(entity);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	public void remove(T entity) {
		getDao().remove(entity);
	}

	public void remove(Object id) {
		getDao().remove(id);
	}

	public Long getSizeWithRestrictions(List<Criterion> restrictions) {
		return getDao().getSizeWithRestrictions(restrictions);
	}

	public Long getSize() {
		return getDao().getSize();
	}

	public void testSelectDb() {
		getDao().testSelect();
	}

	public T getObjectById(Object id) {
		return getDao().getObjectById(id);
	}

	public List<T> getAll() {
		return getDao().getAll();
	}

	public List<T> getAll(String orderBy) {
		return getDao().getAll(orderBy);
	}

	public List<T> getAllWithPaging(String orderBy, SortOrder orderDirection, int begin, int qty) {
		return getDao().getAllWithPaging(orderBy, orderDirection, begin, qty);
	}

	public List<T> getFilteredWithPaging(String orderBy, SortOrder orderDirection, int begin, int qty,
			List<Criterion> restrictions) {
		return getDao().getFilteredWithPaging(orderBy, orderDirection, begin, qty, restrictions);
	}

}