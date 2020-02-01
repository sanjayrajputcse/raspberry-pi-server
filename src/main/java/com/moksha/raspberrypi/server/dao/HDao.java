package com.moksha.raspberrypi.server.dao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

import io.dropwizard.hibernate.AbstractDAO;

public abstract class HDao<T> extends AbstractDAO<T> {

    public HDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public T get(Serializable id) {
        return super.get(id);
    }

    public T create(T t) throws HibernateException {
        super.currentSession().save(t);
        return t;
    }

    public T update(T t) throws HibernateException {
        super.currentSession().update(t);
        return t;
    }

    public List<T> getAll(Class<T> criteriaType) throws HibernateException {
        Criteria criteria = currentSession().createCriteria(criteriaType);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public void delete(T t) {
        super.currentSession().delete(t);
        super.currentSession().flush();
    }

    //-------------------------- calls with new session/transaction --------------------------
    public T getWithSession(Serializable id) throws HibernateException {
        HSession hSession = new HSession();
        hSession.open();
        try {
            return super.get(id);
        } finally {
            hSession.close();
        }
    }

    public T createWithSession(T t) throws HibernateException {
        HSession hSession = new HSession();
        hSession.openWithTransaction();
        try {
            this.currentSession().save(t);
            hSession.commit();
        } finally {
            hSession.close();
        }
        return t;
    }

    public void createWithSeparateTransaction(T t) throws HibernateException {
        HSession hSession = new HSession();
        hSession.openWithSeparateTransaction();
        try {
            super.persist(t);
            hSession.commit();
        } finally {
            hSession.close();
        }
    }

    public T updateWithSession(T t) throws HibernateException {
        HSession hSession = new HSession();
        hSession.openWithTransaction();
        try {
            this.currentSession().update(t);
            hSession.commit();
        } finally {
            hSession.close();
        }
        return t;
    }

    public List<T> getAllWithSession(Class<T> criteriaType) throws HibernateException {
        HSession hSession = new HSession();
        hSession.open();
        try {
            Criteria criteria = this.currentSession().createCriteria(criteriaType);
            return criteria.list();
        } finally {
            hSession.close();
        }
    }

    public void deleteWithSession(T t) throws HibernateException {
        HSession hSession = new HSession();
        hSession.openWithTransaction();
        try {
            this.currentSession().delete(t);
            hSession.commit();
        } finally {
            hSession.close();
        }
    }
}
