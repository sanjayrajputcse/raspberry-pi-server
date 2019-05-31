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

    public boolean delete(Long id){
        Criteria criteria = this.currentSession().createCriteria(MaterializedFSN.class);
        criteria.add(Restrictions.eq("id", id));
        this.currentSession().delete(criteria.list().get(0));
        /*CriteriaBuilder cb = this.currentSession().getCriteriaBuilder();

        // create delete
        CriteriaDelete<MaterializedFSN> delete = cb.
                createCriteriaDelete(MaterializedFSN.class);

        // set the root class
        Root e = delete.from(MaterializedFSN.class);

        // set where clause
        delete.where(cb.equal(e.get("id"), id));

        // perform update
        final int returnStatus = this.currentSession().createQuery(delete).executeUpdate();

        return 0 == returnStatus ? false : true;
    */
        return true;

    }

    public List<MaterializedFSN> getMaterializedFSNListItemSearch(String fkAccountId, String listItem) {
        Criteria criteria = this.currentSession().createCriteria(MaterializedFSN.class);
        criteria.add(Restrictions.eq("fkAccountId", fkAccountId));
        criteria.add(Restrictions.like("fsnName", "%" + listItem + "%"));
        return criteria.list();
    }
}
