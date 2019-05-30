package com.moksha.raspberrypi.server;

import com.moksha.raspberrypi.server.InternalClient.Action;
import com.moksha.raspberrypi.server.InternalClient.GetFKDetails;
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
        GetFKDetails getFKDetails = new GetFKDetails();
        final List<Product> cap = getFKDetails.searchKeyWordsAndReturnProducts("cap");
        List<String> toAddListingIds = new ArrayList<String>();
        List<String> toRemoveListingIds = new ArrayList<String>();
        toAddListingIds.add(cap.get(0).getListingId());
//        CollectionRequest collectionRequest = new CollectionRequest("HACK", toAddListingIds, toRemoveListingIds);
//        CollectionResponse newCollection = getFKDetails.createCollection(collectionRequest);
//        System.out.printf("\n collectionid : "+newCollection.getCollectionId());
//        System.out.printf("\n collectionURL: "+newCollection.getCollectionUrl());

        String collectionId = "h2pg3ulcx0";
        CollectionRequest collectionRequestUpdate = new CollectionRequest(toAddListingIds, toRemoveListingIds);
        boolean updated = getFKDetails.updateCollection(collectionRequestUpdate, collectionId);
        System.out.println("updated " + updated);

        System.out.println(getFKDetails.getCollectionUrl(collectionId));
    }
}
