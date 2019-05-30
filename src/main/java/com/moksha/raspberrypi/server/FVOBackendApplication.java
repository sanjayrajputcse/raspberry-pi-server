package com.moksha.raspberrypi.server;

import com.google.inject.Guice;

import com.moksha.raspberrypi.server.InternalClient.GetFKDetails;
import com.moksha.raspberrypi.server.ajay.models.entities.Action;
import com.moksha.raspberrypi.server.ajay.models.entities.UserAction;
import com.moksha.raspberrypi.server.dao.GuiceInjector;
import com.moksha.raspberrypi.server.filters.RequestFilter;
import com.moksha.raspberrypi.server.fkService.GCPConnectService;
import com.moksha.raspberrypi.server.fkService.SearchService;
import com.moksha.raspberrypi.server.models.entities.fvo.backend.MaterializedCollection;
import com.moksha.raspberrypi.server.models.entities.fvo.backend.MaterializedFSN;
import com.moksha.raspberrypi.server.resources.AppHealthCheck;
import com.moksha.raspberrypi.server.resources.ApplicationResource;
import com.moksha.raspberrypi.server.resources.DeviceResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class FVOBackendApplication extends io.dropwizard.Application<RPiConfiguration> {

    GCPConnectService gcpConnectService = new GCPConnectService();
    SearchService callSearch = new SearchService();
    GetFKDetails getFKDetails = new GetFKDetails();

    public static void main(String[] args) throws Exception {
        new FVOBackendApplication().run(args);
    }

    @Override
    public String getName() {
        return "FVO Backend Server";
    }

    private final HibernateBundle<RPiConfiguration> hibernate = new HibernateBundle<RPiConfiguration>(MaterializedFSN.class, MaterializedCollection.class) {
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
        System.out.println("start");

        GuiceInjector.assignInjector(Guice.createInjector(new RPiModule(hibernate)));

        environment.jersey().register(GuiceInjector.getInjector().getInstance(RequestFilter.class));

        //app resources
        environment.jersey().register(GuiceInjector.getInjector().getInstance(DeviceResource.class));
        environment.jersey().register(GuiceInjector.getInjector().getInstance(ApplicationResource.class));

        //app manage resources
        environment.lifecycle().manage(new RPiManage());

        //app healthchecks
        environment.healthChecks().register("healthCheck", new AppHealthCheck());

        while(true){
            // fetch actions from fvo thin server
            // createList, addItem, removeItem, deleteList, sendListToDevice

            final List<UserAction> allPendingUserActions = gcpConnectService.getAllPendingUserActions();

            final List<UserAction> allCompletedUserActions = new ArrayList<>();

            allPendingUserActions.stream().forEachOrdered(userAction -> {
                final UserAction processedUserAction = processUserAction(userAction);
                if(processedUserAction.isDone()){
                    try {
                        gcpConnectService.setStatusAndDesc(processedUserAction.getId(), processedUserAction.getTalkBackText());
                        allCompletedUserActions.add(processedUserAction);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            //createList
            //1. call collection service to create a page
            //2. create an entry in MaterializedCollections

            //addItem
            //1. search for fsn
            //2. add fsn to page using collection service
            //3. add an entry in MaterializedFsn.

            // push result to fvo thin server
            // addItem: FSN desc
            // createList/removeItem/deleteList/removeItem/sendListToDevice: successful/error

            // sleeping to avoid thrashing
            Thread.sleep(500);
        }
    }

    private UserAction processUserAction(UserAction userAction) {
        final String actionNameString = userAction.getActionName();
        final Action action = Action.getActionFromString(actionNameString);
        switch (action){
            case CREATE_LIST:
                final String listId = userAction.getListId();
                final String fkAccountId = userAction.getFkAccountId();
                break;
            case REMOVE_LIST:
                break;
            case ADD_ITEM_TO_LIST:
                break;
            case REMOVE_ITEM_FROM_LIST:
                break;
            case SEND_LIST_TO_PN:
                break;

        }
        return userAction;
    }
}
