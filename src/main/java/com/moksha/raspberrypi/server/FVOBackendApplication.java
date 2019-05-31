package com.moksha.raspberrypi.server;

import com.google.inject.Guice;

import com.moksha.raspberrypi.server.InternalClient.FkAction;
import com.moksha.raspberrypi.server.ajay.models.entities.Action;
import com.moksha.raspberrypi.server.ajay.models.entities.UserAction;
import com.moksha.raspberrypi.server.dao.GuiceInjector;
import com.moksha.raspberrypi.server.dao.HSession;
import com.moksha.raspberrypi.server.dao.fvo.backend.ActiveAccountsDAO;
import com.moksha.raspberrypi.server.dao.fvo.backend.MaterializedCollectionDAO;
import com.moksha.raspberrypi.server.dao.fvo.backend.MaterializedFSNDAO;
import com.moksha.raspberrypi.server.filters.RequestFilter;
import com.moksha.raspberrypi.server.fkService.GCPConnectService;
import com.moksha.raspberrypi.server.models.PNRequest;
import com.moksha.raspberrypi.server.models.entities.CartContext;
import com.moksha.raspberrypi.server.models.entities.CollectionRequest;
import com.moksha.raspberrypi.server.models.entities.Product;
import com.moksha.raspberrypi.server.models.entities.Quantity;
import com.moksha.raspberrypi.server.models.entities.fvo.backend.ActiveAccounts;
import com.moksha.raspberrypi.server.models.entities.fvo.backend.MaterializedCollection;
import com.moksha.raspberrypi.server.models.entities.fvo.backend.MaterializedFSN;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class FVOBackendApplication extends io.dropwizard.Application<RPiConfiguration> {

    GCPConnectService gcpConnectService = new GCPConnectService();

    FkAction fkAction = new FkAction();

    ActiveAccountsDAO activeAccountsDAO;

    MaterializedCollectionDAO materializedCollectionDAO;

    MaterializedFSNDAO materializedFSNDAO;

    public static void main(String[] args) throws Exception {
        new FVOBackendApplication().run(args);
    }

    @Override
    public String getName() {
        return "FVO Backend Server";
    }

    private final HibernateBundle<RPiConfiguration> hibernate = new HibernateBundle<RPiConfiguration>(MaterializedFSN.class, MaterializedCollection.class, ActiveAccounts.class) {
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

        materializedCollectionDAO = GuiceInjector.getInjector().getInstance(MaterializedCollectionDAO.class);
        activeAccountsDAO = GuiceInjector.getInjector().getInstance(ActiveAccountsDAO.class);
        materializedFSNDAO = GuiceInjector.getInjector().getInstance(MaterializedFSNDAO.class);


        while(true){
            // fetch actions from fvo thin server
            // createList, addItem, removeItem, deleteList, sendListToDevice

            HSession hsession = new HSession();
            hsession.openWithTransaction();

            final List<UserAction> allPendingUserActions = gcpConnectService.getAllPendingUserActions();

            if(allPendingUserActions.isEmpty()){
                System.out.println("No Actions");
            }

            allPendingUserActions.stream().forEachOrdered(userAction -> {
                int retryCount = 0;
                UserAction processedUserAction = null;
                while(retryCount++ < 3 && !userAction.isDone()) {
                    processedUserAction = processUserAction(userAction);
                }
                if(processedUserAction == null){
                    processedUserAction = userAction;
                    processedUserAction.setDone(true);
                    processedUserAction.setTalkBackText("Error");
                }
                if (processedUserAction.isDone()) {
                    try {
                        hsession.commit();
                        gcpConnectService.setStatusAndDesc(processedUserAction.getId(), processedUserAction.getTalkBackText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            hsession.commit();
            hsession.close();
            Thread.sleep(500);
        }
    }

    private UserAction processUserAction(UserAction userAction){
        final String actionNameString = userAction.getActionName();
        final Action action = Action.getActionFromString(actionNameString);
        final String fkAccountId = userAction.getFkAccountId();
        final String actionValue = userAction.getActionValue();
        final String listId = userAction.getListId();
        switch (action){
            case CREATE_LIST:

                CollectionRequest collectionRequest = new CollectionRequest(listId, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
                /*try {
                    final CollectionResponse collection = fkAction.createCollection(collectionRequest);
                    final String collectionId = collection.getCollectionId();
                    final String collectionUrl = fkAction.getCollectionUrl(collectionId);*/

                    final String collectionId = "0bselsmaur";
                    final String collectionUrl = "https://www.flipkart.com/all/~cs-0bselsmaur/pr?sid=all";

                    MaterializedCollection materializedCollection = new MaterializedCollection();
                    materializedCollection.setFkAccountId(fkAccountId);
                    materializedCollection.setListName(listId);
                    materializedCollection.setUrl(collectionUrl);
                    materializedCollection.setCollectionId(collectionId);

                    materializedCollectionDAO.create(materializedCollection);

                    userAction.setDone(true);
                    userAction.setTalkBackText(listId + " shopping list created!");
                /*} catch (IOException e) {
                    e.printStackTrace();
                }*/
                break;
            case REMOVE_LIST:
                final MaterializedCollection materializedCollectionToRemove = materializedCollectionDAO.getMaterializedCollection(fkAccountId, listId);
                final String collectionIdToBeRemoved = materializedCollectionToRemove.getCollectionId();
                // stub to be implemented later.
                break;
            case ADD_ITEM_TO_LIST:
                final String listIdAddItem = userAction.getListId();
                try {
                    final String itemQuery = actionValue;
                    final List<Product> products = fkAction.searchKeyWordsAndReturnProducts(itemQuery);
                    final Product product = products.get(0);
                    final String productId = product.getProductId();
                    final String listingId = product.getListingId();
                    final String productTitle = product.getProductTitle() == null ? itemQuery : product.getProductTitle().toLowerCase();

                    final MaterializedCollection materializedCollectionToBeUpdated = materializedCollectionDAO.getMaterializedCollection(fkAccountId, listIdAddItem);

                    if(isAbleToAddProductToCollection(materializedCollectionToBeUpdated, productId)) {
                        MaterializedFSN materializedFSN = new MaterializedFSN();
                        materializedFSN.setFkAccountId(fkAccountId);
                        materializedFSN.setFsnId(productId);
                        materializedFSN.setFsnName(productTitle);
                        materializedFSN.setListItem(itemQuery);
                        materializedFSN.setListName(listIdAddItem);
                        materializedFSN.setListingId(listingId);

                        materializedFSNDAO.create(materializedFSN);

                        userAction.setDone(true);
                        userAction.setTalkBackText(productTitle + " Added to List");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case REMOVE_ITEM_FROM_LIST:
                HSession hSession = new HSession();
                hSession.openWithSeparateTransaction();

                final MaterializedCollection materializedCollectionToBeUpdated = materializedCollectionDAO.getMaterializedCollection(fkAccountId, listId);

                List<MaterializedFSN> materializedFSNListItemSearch = materializedFSNDAO.getMaterializedFSNListItemSearch(fkAccountId, actionValue);

                final List<String> searchedFsnIds = materializedFSNListItemSearch
                        .stream().map(materializedFSN -> materializedFSN.getFsnId())
                        .collect(Collectors.toList());

                if(isAbleToRemoveProductFromCollection(materializedCollectionToBeUpdated, searchedFsnIds)) {
                    materializedFSNListItemSearch
                            .stream().forEach(materializedFSN -> {
                                materializedFSNDAO.delete(materializedFSN);
                                hSession.commit();
                                hSession.close();
                            });
                    userAction.setDone(true);
                    userAction.setTalkBackText(actionValue + " Removed from List");
                }

                break;
            case ADD_LIST_TO_BASKET:
                final List<MaterializedFSN> materializedFSNList = materializedFSNDAO.getMaterializedFSNListItemSearch(fkAccountId, listId);
                String securityToken = activeAccountsDAO.getSecurityToken(fkAccountId);
                Map<String,Quantity> map = new HashMap<>();
                materializedFSNList.forEach(materializedFSN -> map.put(materializedFSN.getListingId(), new Quantity(1)));
                CartContext cartContext = new CartContext(map);
                try {
                    final boolean success = fkAction.addToGroceryBucket(securityToken, cartContext);
                    if(success) {
                        userAction.setDone(true);
                        userAction.setTalkBackText(actionValue + " List Added to Basket!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                break;
            case SEND_LIST_TO_PN:
                final String sendToDeviceListId = userAction.getListId();
                final String currentAccountId = userAction.getFkAccountId();
                final String deviceId = activeAccountsDAO.getDeviceId(currentAccountId);
                final MaterializedCollection materializedCollectionToSend = materializedCollectionDAO.getMaterializedCollection(currentAccountId, sendToDeviceListId);

                PNRequest pnRequest = new PNRequest(materializedCollectionToSend.getUrl(),Arrays.asList(deviceId),"udpate");

                try {
                    final boolean success = fkAction.pushNotification(pnRequest);
                    if(success) {
                        userAction.setDone(true);
                        userAction.setTalkBackText(actionValue + " List send to Device!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

        }
        return userAction;
    }

    private boolean isAbleToAddProductToCollection(MaterializedCollection materializedCollectionToBeUpdated, String productId) {
        final String collectionId = materializedCollectionToBeUpdated.getCollectionId();

        CollectionRequest collectionRequest = new CollectionRequest(Arrays.asList(productId) , Collections.EMPTY_LIST);

        try {
            return fkAction.updateCollection(collectionRequest, collectionId);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isAbleToRemoveProductFromCollection(MaterializedCollection materializedCollectionToBeUpdated, List<String> productIds){
        final String collectionId = materializedCollectionToBeUpdated.getCollectionId();

        CollectionRequest collectionRequest = new CollectionRequest(Collections.EMPTY_LIST , productIds);

        try {
            return fkAction.updateCollection(collectionRequest, collectionId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
