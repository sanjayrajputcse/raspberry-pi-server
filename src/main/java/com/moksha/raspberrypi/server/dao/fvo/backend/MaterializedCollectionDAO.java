package com.moksha.raspberrypi.server.dao.fvo.backend;

import com.google.inject.Inject;

import com.moksha.raspberrypi.server.dao.HDao;
import com.moksha.raspberrypi.server.models.entities.fvo.backend.MaterializedCollection;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class MaterializedCollectionDAO extends HDao<MaterializedCollection> {

    @Inject
    public MaterializedCollectionDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public MaterializedCollection getMaterializedCollection(String fkAccountId, String listName) {
        Criteria criteria = this.currentSession().createCriteria(MaterializedCollection.class);
        criteria.add(Restrictions.eq("fkAccountId", fkAccountId));
        criteria.add(Restrictions.eq("listName", listName));
        final List list = criteria.list();
        if (list != null && list.size() > 0) {
            return (MaterializedCollection) list.get(0);
        }
        return null;
    }


}
