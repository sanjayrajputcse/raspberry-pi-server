package com.moksha.raspberrypi.server.dao.fvo.backend;

import com.google.inject.Inject;

import com.moksha.raspberrypi.server.dao.HDao;
import com.moksha.raspberrypi.server.models.entities.fvo.backend.MaterializedCollection;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;


public class ActiveAccountsDAO extends HDao<MaterializedCollection> {
    @Inject
    public ActiveAccountsDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public String getDeviceId(String fkAccountId) {
        Criteria criteria = this.currentSession().createCriteria(MaterializedCollection.class);
        criteria.add(Restrictions.eq("fkAccountId", fkAccountId));
        final List list = criteria.list();
        if (list != null && list.size() > 0) {
            return (String) list.get(0);
        }
        return null;
    }
}
