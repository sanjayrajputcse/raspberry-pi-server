package com.moksha.raspberrypi.server.ajay.dao;

import com.google.inject.Inject;

import com.moksha.raspberry.server.models.entities.ajay.UserAction;
import com.moksha.raspberrypi.server.dao.HDao;

import org.hibernate.SessionFactory;

import java.util.Optional;

/**
 * Created by ajay.agarwal on 30/05/19.
 */

public class UserActionDAO extends HDao<UserAction> {

    @Inject
    public UserActionDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<UserActionDAO> addNewActionInTable(String fkAcoountId, String actionName, String actionValue, String listId, String fkAccountId) {
        return Optional.empty();
    }

    public Optional<UserActionDAO> updateStatusOfActionInTable() {
        return Optional.empty();
    }
}

