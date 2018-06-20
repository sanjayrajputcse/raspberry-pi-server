package com.moksha.raspberrypi.server.dao;

import com.google.inject.Inject;
import com.moksha.raspberrypi.server.models.entities.Application;
import com.moksha.raspberrypi.server.models.entities.ApplicationAction;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class ApplicationActionDAO extends HDao<ApplicationAction> {

    @Inject
    public ApplicationActionDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<ApplicationAction> getActions(Application application, Boolean done, String orderBy, String orderType, int limit) {
        Criteria criteria = this.currentSession().createCriteria(ApplicationAction.class);
        criteria.add(Restrictions.eq("application", application));
        if (done != null) {
            criteria.add(Restrictions.eq("done", done));
        }
        if (orderBy != null && orderType != null) {
            if (orderType.equalsIgnoreCase("asc")) {
                criteria.addOrder(Order.asc(orderBy));
            } else if (orderType.equalsIgnoreCase("desc")) {
                criteria.addOrder(Order.desc(orderBy));
            }
        }
        if (limit > 0) {
            criteria.setMaxResults(limit);
        }
        return criteria.list();
    }
}
