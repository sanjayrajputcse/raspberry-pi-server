package com.moksha.raspberrypi.server.dao;

import com.google.inject.Inject;
import com.moksha.raspberrypi.server.models.entities.Application;
import com.moksha.raspberrypi.server.models.entities.ApplicationAction;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class ApplicationActionDAO extends HDao<ApplicationAction> {

    @Inject
    public ApplicationActionDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<ApplicationAction> getNewActions(Application application) {
        Criteria criteria = this.currentSession().createCriteria(ApplicationAction.class);
        criteria.add(Restrictions.eq("done", false));
        criteria.add(Restrictions.eq("application", application));
        return criteria.list();
    }
}
