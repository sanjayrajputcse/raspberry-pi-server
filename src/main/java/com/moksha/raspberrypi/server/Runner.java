package com.moksha.raspberrypi.server;

import com.moksha.raspberrypi.server.fkService.CollectionService;
import com.moksha.raspberrypi.server.fkService.SearchService;
import com.moksha.raspberrypi.server.models.entities.CollectionRequest;
import com.moksha.raspberrypi.server.models.entities.CollectionResponse;
import com.moksha.raspberrypi.server.models.entities.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by somil.jain on 30/05/19.
 */
public class Runner {
    public static void main(
            String[] args) throws IOException {
        SearchService callSearch = new SearchService();
        final List<Product> cap = callSearch.searchOnAKeyword("cap");
        CollectionService collectionService = new CollectionService();
        List<String> toAddListingIds = new ArrayList<String>();
        List<String> toRemoveListingIds = new ArrayList<String>();
        toAddListingIds.add("TSHEZU8KMSXHVAJE");
        CollectionRequest collectionRequest = new CollectionRequest("HACK", toAddListingIds, toRemoveListingIds);
        CollectionResponse newCollection = collectionService.createNewCollection(collectionRequest);
        System.out.printf("collectionid"+newCollection.getCollectionId());
        System.out.printf("collectionURL"+newCollection.getCollectionId());

    }
}
