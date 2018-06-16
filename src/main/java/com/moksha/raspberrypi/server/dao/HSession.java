package com.moksha.raspberrypi.server.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HSession {
    private static final Logger logger = LoggerFactory.getLogger(HSession.class);

    private SessionFactory sessionFactory;
    private Session parentSession;
    private Session newSession;

    public HSession() {
        this.sessionFactory = GuiceInjector.getInjector().getInstance(SessionFactory.class);
    }

    public void open() throws HibernateException {
        parentSession = getParentSession();

        if(parentSession == null){
            logger.debug("opening new session");
            newSession = sessionFactory.openSession();
            logger.debug("binding session");
            ManagedSessionContext.bind(newSession);
        }
    }

    public void openNewSession() throws HibernateException {
        parentSession = getParentSession();
        logger.debug("opening new session");
        newSession = sessionFactory.openSession();
        logger.debug("binding session");
        ManagedSessionContext.bind(newSession);
    }

    public void openWithTransaction() throws HibernateException {
        parentSession = getParentSession();
        if (parentSession != null) {
            if (parentSession.getTransaction() == null) {
                logger.debug("starting transaction");
                parentSession.beginTransaction();
            }
        } else {
            logger.debug("opening new session");
            newSession = sessionFactory.openSession();
            logger.debug("binding session");
            ManagedSessionContext.bind(newSession);
            logger.debug("starting transaction");
            newSession.beginTransaction();
        }
    }

    public void openWithSeparateTransaction() throws HibernateException {
        parentSession = getParentSession();
        logger.debug("opening new session");
        newSession = sessionFactory.openSession();
        logger.debug("binding session");
        ManagedSessionContext.bind(newSession);
        logger.debug("starting transaction");
        newSession.beginTransaction();
    }

    private Session getParentSession() throws HibernateException {
        try {
            return sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            logger.debug("no current session found");
        }
        return null;
    }

    public void commit() throws HibernateException {
        if(newSession!=null) {
            Transaction transaction = newSession.getTransaction();
            if (transaction != null && transaction.isActive()) {
                logger.debug("committing transaction");
                transaction.commit();
            }
        }
    }

    public void close() throws HibernateException {
        if(newSession!=null){
            logger.debug("closing session");
            newSession.close();
            logger.debug("unbinding session");
            ManagedSessionContext.unbind(sessionFactory);
        }

        if(parentSession !=null) {
            logger.debug("binding parent session");
            ManagedSessionContext.bind(parentSession);
        }
    }
}
