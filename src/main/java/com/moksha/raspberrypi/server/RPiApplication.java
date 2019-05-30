package com.moksha.raspberrypi.server;

import com.google.inject.Guice;

import com.moksha.raspberrypi.server.ajay.models.entities.ListDetail;
import com.moksha.raspberrypi.server.ajay.models.entities.UserAccount;
import com.moksha.raspberrypi.server.ajay.models.entities.UserAction;
import com.moksha.raspberrypi.server.ajay.resources.GAppResource;
import com.moksha.raspberrypi.server.dao.GuiceInjector;
import com.moksha.raspberrypi.server.filters.RequestFilter;
import com.moksha.raspberrypi.server.resources.AppHealthCheck;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class RPiApplication extends io.dropwizard.Application<RPiConfiguration> {

    public static void main(String[] args) throws Exception {
        new RPiApplication().run(args);
    }

    @Override
    public String getName() {
        return "Raspberry Pi Server";
    }

    private final HibernateBundle<RPiConfiguration> hibernate = new HibernateBundle<RPiConfiguration>(
            UserAction.class,
            ListDetail.class,
            UserAccount.class) {
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

        //filters
        environment.jersey().register(GuiceInjector.getInjector().getInstance(RequestFilter.class));

        //app resources
        environment.jersey().register(GuiceInjector.getInjector().getInstance(GAppResource.class));

        //app manage resources
        environment.lifecycle().manage(new RPiManage());

        //app healthchecks
        environment.healthChecks().register("healthCheck", new AppHealthCheck());
    }
}
