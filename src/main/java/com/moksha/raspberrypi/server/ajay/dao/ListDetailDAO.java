package com.moksha.raspberrypi.server.ajay.dao;

import com.google.inject.Inject;

import com.moksha.raspberrypi.server.ajay.models.entities.ListDetail;
import com.moksha.raspberrypi.server.dao.HDao;

import org.hibernate.SessionFactory;

import java.util.Optional;

/**
 * Created by ajay.agarwal on 30/05/19.
 */

public class ListDetailDAO extends HDao<ListDetail> {

    @Inject
    public ListDetailDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<ListDetailDAO> createList(String listId, String fkAccountId) {
        return Optional.empty();
    }

    public Optional<ListDetailDAO> removeList(String listId) {
        return Optional.empty();
    }

    public Optional<ListDetailDAO> addItemToList(String listId, String actionValue) {
        return Optional.empty();
    }

    public Optional<ListDetailDAO> removeItemFromList(String listId, String actionValue) {
        return Optional.empty();
    }
}

