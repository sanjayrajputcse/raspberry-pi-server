package com.moksha.raspberrypi.server.ajay.dao;

import com.google.inject.Inject;

import com.moksha.raspberrypi.server.ajay.models.entities.ListDetail;
import com.moksha.raspberrypi.server.dao.HDao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Optional;

/**
 * Created by ajay.agarwal on 30/05/19.
 */

public class ListDetailDAO extends HDao<ListDetail> {

    @Inject
    public ListDetailDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<ListDetail> get(String listId) {
        Criteria criteria = this.currentSession().createCriteria(ListDetail.class);
        criteria.add(Restrictions.eq("listId", listId));
        final List list = criteria.list();
        if(list != null && list.size()>0){
            Optional.of((ListDetail) list.get(0));
        }
        return Optional.empty();
    }
}

