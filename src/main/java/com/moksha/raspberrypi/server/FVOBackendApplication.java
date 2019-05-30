package com.moksha.raspberrypi.server;

import com.google.inject.Guice;
import com.google.inject.Inject;

import com.moksha.raspberrypi.server.InternalClient.GetFKDetails;
import com.moksha.raspberrypi.server.ajay.models.entities.Action;
import com.moksha.raspberrypi.server.ajay.models.entities.UserAction;
import com.moksha.raspberrypi.server.dao.GuiceInjector;
import com.moksha.raspberrypi.server.dao.fvo.backend.ActiveAccountsDAO;
import com.moksha.raspberrypi.server.dao.fvo.backend.MaterializedCollectionDAO;
import com.moksha.raspberrypi.server.dao.fvo.backend.MaterializedFSNDAO;
import com.moksha.raspberrypi.server.filters.RequestFilter;
import com.moksha.raspberrypi.server.fkService.GCPConnectService;
import com.moksha.raspberrypi.server.models.entities.CollectionRequest;
import com.moksha.raspberrypi.server.models.entities.CollectionResponse;
import com.moksha.raspberrypi.server.models.entities.Product;
import com.moksha.raspberrypi.server.models.entities.fvo.backend.MaterializedCollection;
import com.moksha.raspberrypi.server.models.entities.fvo.backend.MaterializedFSN;
import com.moksha.raspberrypi.server.resources.AppHealthCheck;
import com.moksha.raspberrypi.server.resources.ApplicationResource;
import com.moksha.raspberrypi.server.resources.DeviceResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class FVOBackendApplication extends io.dropwizard.Application<RPiConfiguration> {

    GCPConnectService gcpConnectService = new GCPConnectService();
    GetFKDetails getFKDetails = new GetFKDetails();

    @Inject
    ActiveAccountsDAO activeAccountsDAO;

    @Inject
    MaterializedCollectionDAO materializedCollectionDAO;

    @Inject
    MaterializedFSNDAO materializedFSNDAO;

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

    private UserAction processUserAction(UserAction userAction){
        final String actionNameString = userAction.getActionName();
        final Action action = Action.getActionFromString(actionNameString);
        final String fkAccountId = userAction.getFkAccountId();
        final String deviceId = activeAccountsDAO.getDeviceId(fkAccountId);
        final String actionValue = userAction.getActionValue();
        switch (action){
            case CREATE_LIST:
                final String listId = userAction.getListId();

                CollectionRequest collectionRequest = new CollectionRequest(listId, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
                try {
                    final CollectionResponse collection = getFKDetails.createCollection(collectionRequest);
                    final String collectionId = collection.getCollectionId();
                    final String collectionUrl = getFKDetails.getCollectionUrl(collectionId);

                    MaterializedCollection materializedCollection = new MaterializedCollection();
                    materializedCollection.setFkAccountId(fkAccountId);
                    materializedCollection.setDevice_id(deviceId);
                    materializedCollection.setListName(listId);
                    materializedCollection.setUrl(collectionUrl);
                    materializedCollection.setCollectionId(collectionId);

                    materializedCollectionDAO.create(materializedCollection);

                    userAction.setDone(true);
                    userAction.setTalkBackText(listId + " shopping list created!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case REMOVE_LIST:
                final String removeListId = userAction.getListId();
                final MaterializedCollection materializedCollection = materializedCollectionDAO.getMaterializedCollection(fkAccountId, removeListId);
                final String collectionIdToBeRemoved = materializedCollection.getCollectionId();
                // stub to be implemented later.
                break;
            case ADD_ITEM_TO_LIST:
                final String listIdAddItem = userAction.getListId();
                try {
                    final String itemQuery = actionValue;
                    final List<Product> products = getFKDetails.searchKeyWordsAndReturnProducts(itemQuery);
                    final Product product = products.get(0);
                    final String productId = product.getProductId();
                    final String productTitle = "Sugar";

                    final MaterializedCollection materializedCollectionToBeUpdated = materializedCollectionDAO.getMaterializedCollection(fkAccountId, listIdAddItem);

                    if(isAbleToAddProductToCollection(materializedCollectionToBeUpdated, productId)) {
                        MaterializedFSN materializedFSN = new MaterializedFSN();
                        materializedFSN.setFkAccountId(fkAccountId);
                        materializedFSN.setDeviceId(deviceId);
                        materializedFSN.setFsnId(productId);
                        materializedFSN.setFsnName(productTitle);
                        materializedFSN.setListItem(itemQuery);
                        materializedFSN.setListName(listIdAddItem);

                        materializedFSNDAO.create(materializedFSN);

                        userAction.setDone(true);
                        userAction.setTalkBackText(productTitle + " Added to List");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case REMOVE_ITEM_FROM_LIST:
                final String listIdRemoveItem = userAction.getListId();

                final MaterializedCollection materializedCollectionToBeUpdated = materializedCollectionDAO.getMaterializedCollection(fkAccountId, listIdRemoveItem);

                List<MaterializedFSN> materializedFSNListItemSearch = materializedFSNDAO.getMaterializedFSNListItemSearch(fkAccountId, actionValue);


                final List<String> searchedFsnIds = materializedFSNListItemSearch
                        .stream().map(materializedFSN -> materializedFSN.getFsnId())
                        .collect(Collectors.toList());

                if(isAbleToRemoveProductFromCollection(materializedCollectionToBeUpdated, searchedFsnIds)) {
                    materializedFSNListItemSearch
                            .stream().forEach(materializedFSN -> {
                                materializedFSNDAO.delete(materializedFSN);
                    });
                    userAction.setDone(true);
                    userAction.setTalkBackText(actionValue + " Removed from List");
                }

                break;
            case SEND_LIST_TO_PN:
                break;

        }
        return userAction;
    }

    private boolean isAbleToAddProductToCollection(MaterializedCollection materializedCollectionToBeUpdated, String productId) {
        final String collectionId = materializedCollectionToBeUpdated.getCollectionId();

        CollectionRequest collectionRequest = new CollectionRequest(Arrays.asList(productId) , Collections.EMPTY_LIST);

        try {
            getFKDetails.updateCollection(collectionRequest, collectionId);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isAbleToRemoveProductFromCollection(MaterializedCollection materializedCollectionToBeUpdated, List<String> productIds){
        final String collectionId = materializedCollectionToBeUpdated.getCollectionId();

        CollectionRequest collectionRequest = new CollectionRequest(Collections.EMPTY_LIST , productIds);

        try {
            getFKDetails.updateCollection(collectionRequest, collectionId);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
