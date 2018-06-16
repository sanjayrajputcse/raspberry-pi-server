package com.moksha.raspberrypi.server;

import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.moksha.raspberrypi.server.dao.GuiceInjector;
import com.moksha.raspberrypi.server.models.entities.*;
import com.moksha.raspberrypi.server.resources.AppHealthCheck;
import com.moksha.raspberrypi.server.resources.DeviceResource;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.SessionFactoryFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.persistence.Entity;

public class RPiApplication extends io.dropwizard.Application<RPiConfiguration> {

    public static void main(String[] args) throws Exception {
        new RPiApplication().run(args);
    }

    @Override
    public String getName() {
        return "Raspberry Pi Server";
    }

    private final HibernateBundle<RPiConfiguration> hibernate = new HibernateBundle<RPiConfiguration>(Device.class, Unit.class,
            Sensor.class, DeviceSensor.class, Specification.class, DeviceSpecification.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(RPiConfiguration piConfiguration) {
            return piConfiguration.getDatabase();
        }
    };

    @Override
    public void initialize(Bootstrap<RPiConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(RPiConfiguration rPiConfiguration, Environment environment) throws Exception {
        GuiceInjector.assignInjector(Guice.createInjector(new RPiModule(hibernate)));
        environment.jersey().register(GuiceInjector.getInjector().getInstance(DeviceResource.class));
        environment.lifecycle().manage(new RPiManage());
        environment.healthChecks().register("healthCheck", new AppHealthCheck());
    }
}
