package com.moksha.raspberrypi.server.ajay.dao;

import com.google.inject.Inject;

import com.moksha.raspberrypi.server.ajay.models.entities.UserAction;
import com.moksha.raspberrypi.server.dao.HDao;

import org.hibernate.SessionFactory;

/**
 * Created by ajay.agarwal on 30/05/19.
 */

public class UserActionDAO extends HDao<UserAction> {

    @Inject
    public UserActionDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}

