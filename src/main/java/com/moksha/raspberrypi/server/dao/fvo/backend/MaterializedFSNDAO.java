package com.moksha.raspberrypi.server.dao.fvo.backend;

import com.google.inject.Inject;

import com.moksha.raspberrypi.server.dao.HDao;
import com.moksha.raspberrypi.server.models.entities.fvo.backend.MaterializedFSN;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class MaterializedFSNDAO extends HDao<MaterializedFSN> {

    @Inject
    public MaterializedFSNDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<MaterializedFSN> getMaterializedAllFSN(String fkAccountId, String listName) {
        Criteria criteria = this.currentSession().createCriteria(MaterializedFSN.class);
        criteria.add(Restrictions.eq("fkAccountId", fkAccountId));
        criteria.add(Restrictions.eq("listName", listName));
        final List list = criteria.list();
        return list;
    }

    public MaterializedFSN getMaterializedFSN(String fkAccountId, String listName, String listItem) {
        Criteria criteria = this.currentSession().createCriteria(MaterializedFSN.class);
        criteria.add(Restrictions.eq("fkAccountId", fkAccountId));
        criteria.add(Restrictions.eq("listName", listName));
        criteria.add(Restrictions.eq("listItem", listItem));
        final List list = criteria.list();
        if(list != null && list.size()>0){
            return (MaterializedFSN) list.get(0);
        }
        return null;
    }
}
