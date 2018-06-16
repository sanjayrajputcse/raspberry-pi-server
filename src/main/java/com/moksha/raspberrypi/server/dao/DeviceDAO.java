package com.moksha.raspberrypi.server.dao;

import com.google.inject.Inject;
import com.moksha.raspberrypi.server.models.entities.Device;
import org.hibernate.SessionFactory;

public class DeviceDAO extends HDao<Device> {

    @Inject
    public DeviceDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
