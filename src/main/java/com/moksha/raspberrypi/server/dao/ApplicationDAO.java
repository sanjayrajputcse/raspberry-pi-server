package com.moksha.raspberrypi.server.dao;

import com.google.inject.Inject;
import com.moksha.raspberrypi.server.models.entities.Application;
import com.moksha.raspberrypi.server.models.entities.Device;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Optional;

public class ApplicationDAO extends HDao<Application> {

    @Inject
    public ApplicationDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<Application> getByName(String name) {
        Criteria criteria = currentSession().createCriteria(Application.class);
        criteria.add(Restrictions.eq("name", name));
        List list = criteria.list();
        if (list != null && !list.isEmpty()) {
            return Optional.of((Application) list.get(0));
        }
        return Optional.empty();
    }
}
