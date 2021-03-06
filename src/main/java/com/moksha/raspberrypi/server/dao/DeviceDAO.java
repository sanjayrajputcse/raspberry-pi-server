package com.moksha.raspberrypi.server.dao;

import com.google.inject.Inject;
import com.moksha.raspberrypi.server.models.entities.Device;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Optional;

public class DeviceDAO extends HDao<Device> {

    @Inject
    public DeviceDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<Device> getByName(String name) {
        Criteria criteria = currentSession().createCriteria(Device.class);
        criteria.add(Restrictions.eq("name", name));
        List list = criteria.list();
        if (list != null && !list.isEmpty()) {
            return Optional.of((Device) list.get(0));
        }
        return Optional.empty();
    }

}
