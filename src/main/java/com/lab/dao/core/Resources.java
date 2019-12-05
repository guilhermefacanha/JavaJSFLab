package com.lab.dao.core;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import lombok.extern.slf4j.Slf4j;

/**
 * Injection Resource Class {@link EntityManager}
 */
@Slf4j
@WebListener
public class Resources implements ServletContextListener {

	private static EntityManagerFactory factory = null;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContextListener.super.contextInitialized(sce);
		initializeFactory();
		
	}

	private void initializeFactory() {
		try {
			factory = Persistence.createEntityManagerFactory("pu");
		} catch (Exception e) {
			log.error("Error Initializing Persistence Unit", e);
		}
	}

	@Produces
	@RequestScoped
	public EntityManager getEntityManager() {
		if(factory==null) initializeFactory();
		return factory.createEntityManager();
	}

	public void dispose(@Disposes EntityManager em) {
		em.close();
	}
}