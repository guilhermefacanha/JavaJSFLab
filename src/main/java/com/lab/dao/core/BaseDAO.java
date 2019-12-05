package com.lab.dao.core;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.primefaces.model.SortOrder;

import com.lab.annotation.Transactional;
import com.lab.model.core.BaseEntity;
import com.lab.util.ModelUtils;

public abstract class BaseDAO<T extends BaseEntity> implements Serializable {
	private static final long serialVersionUID = 7392952886823572130L;

	@Inject
	protected EntityManager manager;

	protected Class<T> modelClass;

	@Transactional
	public T save(T obj) {
		return manager.merge(obj);
	}

	@Transactional
	public void remove(Object id) {
		String objClass = getEntityClass().getSimpleName();
		manager.createQuery("DELETE FROM " + objClass + " x WHERE x.id=:id").setParameter("id", id).executeUpdate();
	}

	public Long getSize() {
		Session session = (Session) manager.getDelegate();
		Criteria criteria = session.createCriteria(getEntityClass());
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	public long getSizeWithRestrictions(List<Criterion> restrictions) {
		Session session = (Session) manager.getDelegate();
		Criteria crit = session.createCriteria(getEntityClass());
		crit.setProjection(Projections.count("id"));
		restrictions.forEach(crit::add);
		return (Long) crit.uniqueResult();
	}

	// Metoto para conusltar todos os objs
	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		String objClass = getEntityClass().getSimpleName();
		return manager.createQuery("SELECT x FROM " + objClass + " x").getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll(String orderBy) {
		String objClass = getEntityClass().getSimpleName();
		return manager.createQuery("SELECT x FROM " + objClass + " x ORDER BY " + orderBy).getResultList();
	}

	public boolean isValidManager(EntityManager entityManager) {
		if (this.manager == null)
			this.manager = entityManager;
		return manager != null;
	}

	@SuppressWarnings("unchecked")
	public List<T> getFilteredList(List<Criterion> restrictions) {
		Class<? extends BaseEntity> objClass = getEntityClass();
		Session session = (Session) manager.getDelegate();
		Criteria crit = session.createCriteria(objClass);
		restrictions.forEach(crit::add);

		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> getFilteredList(List<Criterion> restrictions, List<String> joins) {
		Class<? extends BaseEntity> objClass = getEntityClass();
		Session session = (Session) manager.getDelegate();
		Criteria crit = session.createCriteria(objClass);
		joins.forEach(j -> {
			crit.createCriteria(j, j);
			crit.setFetchMode(j, FetchMode.JOIN);
		});

		restrictions.forEach(crit::add);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> getAllWithPaging(String orderBy, SortOrder orderDirection, int begin, int qty) {
		Session session = (Session) manager.getDelegate();
		Criteria crit = session.createCriteria(getEntityClass());
		if (!ModelUtils.isNullEmpty(orderBy)) {
			if (orderDirection.equals(SortOrder.ASCENDING))
				crit.addOrder(Order.asc(orderBy));
			else
				crit.addOrder(Order.desc(orderBy));
		}
		crit.setFirstResult(begin);
		crit.setMaxResults(qty);
		return crit.list();

	}

	@SuppressWarnings("unchecked")
	public List<T> getFilteredWithPaging(String orderBy, SortOrder orderDirection, int begin, int qty,
			List<Criterion> restrictions) {
		Session session = (Session) manager.getDelegate();
		Criteria crit = session.createCriteria(getEntityClass());

		restrictions.forEach(crit::add);

		if (!ModelUtils.isNullEmpty(orderBy)) {
			if (orderDirection.equals(SortOrder.ASCENDING))
				crit.addOrder(Order.asc(orderBy));
			else
				crit.addOrder(Order.desc(orderBy));
		}

		crit.setFirstResult(begin);
		crit.setMaxResults(qty);
		return crit.list();

	}

	@SuppressWarnings("unchecked")
	public T getObjectById(Object id) {
		Class<? extends BaseEntity> objClass = getEntityClass();
		try {
			return (T) manager.find(objClass, id);
		} catch (NoResultException e) {
			return null;
		}
	}

	// Metoto para retornar um obj por atributo especifico com ordenacao
	@SuppressWarnings("unchecked")
	public T getobjPorAtributo(String atributo, String valor, String atributoOrdenacao) throws Exception {
		String objClass = getEntityClass().getSimpleName();
		try {
			return (T) manager.createQuery(
					"SELECT x FROM " + objClass + " x WHERE x." + atributo + " = :valor ORDER BY " + atributoOrdenacao)
					.setParameter("valor", valor).setMaxResults(1).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public void testSelect() {
		// Works for H2, MySQL, Microsoft SQL Server, PostgreSQL, SQLite
		String sql = "SELECT 1";

		// Works for Oracle
		// String sql = "SELECT 1 FROM DUAL";
		manager.createNativeQuery(sql).getFirstResult();
	}

	protected Criteria getCriteria(Class<?> objClass) {
		Session session = (Session) manager.getDelegate();
		return session.createCriteria(objClass);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Class getEntityClass() {
		if (this.modelClass == null) {
			if (getClass().getGenericSuperclass() instanceof ParameterizedType) {
				this.modelClass = (Class) ((ParameterizedType) getClass().getGenericSuperclass())
						.getActualTypeArguments()[0];
			} else if (((Class) getClass().getGenericSuperclass())
					.getGenericSuperclass() instanceof ParameterizedType) {
				this.modelClass = (Class<T>) ((ParameterizedType) ((Class) getClass().getGenericSuperclass())
						.getGenericSuperclass()).getActualTypeArguments()[0];
			}
		}
		return this.modelClass;
	}

	public void clear() {
		manager.clear();
	}
}