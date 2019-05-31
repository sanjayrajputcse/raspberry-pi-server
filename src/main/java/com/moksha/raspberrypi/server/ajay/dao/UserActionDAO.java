package com.moksha.raspberrypi.server.ajay.dao;

import com.google.inject.Inject;

import com.moksha.raspberrypi.server.ajay.models.entities.UserAction;
import com.moksha.raspberrypi.server.dao.HDao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by ajay.agarwal on 30/05/19.
 */

public class UserActionDAO extends HDao<UserAction> {

    @Inject
    public UserActionDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public List<UserAction> getPendingUserActions() {
        Criteria criteria = this.currentSession().createCriteria(UserAction.class);
        criteria.add(Restrictions.eq("isDone", false));
        criteria.addOrder(Order.asc("id"));

        return criteria.list();
    }
}

