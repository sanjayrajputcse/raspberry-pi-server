package com.moksha.raspberrypi.server.dao.fvo.backend;

import com.google.inject.Inject;

import com.moksha.raspberrypi.server.dao.HDao;
import com.moksha.raspberrypi.server.models.entities.fvo.backend.ActiveAccounts;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;


public class ActiveAccountsDAO extends HDao<ActiveAccounts> {
    @Inject
    public ActiveAccountsDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public String getDeviceId(String fkAccountId) {
        Criteria criteria = this.currentSession().createCriteria(ActiveAccounts.class);
        criteria.add(Restrictions.eq("fkAccountId", fkAccountId));
        final List list2 = criteria.list();
        final List<ActiveAccounts> list = list2;
        if (list != null && list.size() > 0) {
            return list.get(0).getDeviceId();
        }
        return null;
    }

    public String getSecurityToken(String fkAccountId) {
        Criteria criteria = this.currentSession().createCriteria(ActiveAccounts.class);
        criteria.add(Restrictions.eq("fkAccountId", fkAccountId));
        final List list2 = criteria.list();
        final List<ActiveAccounts> list = list2;
        if (list != null && list.size() > 0) {
            return list.get(0).getSecurityToken();
        }
        return null;
    }
}
