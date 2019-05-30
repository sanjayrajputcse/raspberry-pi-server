package com.moksha.raspberrypi.server.ajay.dao;

import com.google.inject.Inject;

import com.moksha.raspberrypi.server.ajay.models.entities.ListDetail;
import com.moksha.raspberrypi.server.ajay.models.entities.UserAccount;
import com.moksha.raspberrypi.server.dao.HDao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by ajay.agarwal on 30/05/19.
 */

public class UserAccountDAO extends HDao<UserAccount> {

    @Inject
    public UserAccountDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public UserAccount getUserAccount() {
        Criteria criteria = this.currentSession().createCriteria(UserAccount.class);
        final List list = criteria.list();
        if(list != null && list.size() > 0){
            return (UserAccount) list.get(0);
        }
        return new UserAccount("ACYYJ44CD6B6VLFMQ3TAXIUSVKEGC3QB");
    }
}

