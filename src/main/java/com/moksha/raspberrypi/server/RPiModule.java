package com.moksha.raspberrypi.server;

import com.google.inject.AbstractModule;
import io.dropwizard.hibernate.HibernateBundle;
import org.hibernate.SessionFactory;

public class RPiModule extends AbstractModule {

    private HibernateBundle<RPiConfiguration> bundle;

    public RPiModule(HibernateBundle<RPiConfiguration> bundle) {
        this.bundle = bundle;
    }

    @Override
    protected void configure() {
        bind(HibernateBundle.class).toInstance(bundle);
        bind(SessionFactory.class).toInstance(bundle.getSessionFactory());
    }
}
