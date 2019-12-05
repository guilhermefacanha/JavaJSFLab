package com.lab.interceptors;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.lab.annotation.Transactional;

/**
 * Interceptor Class for {@link com.lab.annotation.Transactional} Annotation
 */
@Interceptor
@Transactional
public class TransactionInterceptor implements Serializable {

	private static final long serialVersionUID = 3546523855635450592L;
	private @Inject EntityManager manager;
	
	@AroundInvoke
	public Object invoke(InvocationContext context) throws Exception {
            EntityTransaction trx = manager.getTransaction();
            try {
                if (!trx.isActive()) {
                    trx.begin();
                }
                return context.proceed();
            } catch (Exception e) {
                if (trx != null) {
                    trx.rollback();
                }

                    throw e;
            } finally {
                if (trx != null && trx.isActive()) {
                    trx.commit();
                }
            }
	}
	
}