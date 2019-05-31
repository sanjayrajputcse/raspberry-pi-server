package com.moksha.raspberrypi.server;

import com.moksha.raspberrypi.server.InternalClient.FkAction;
import com.moksha.raspberrypi.server.fkService.PNService;
import com.moksha.raspberrypi.server.fkService.ProductDetailService;
import com.moksha.raspberrypi.server.models.PNRequest;
import com.moksha.raspberrypi.server.models.entities.CollectionRequest;
import com.moksha.raspberrypi.server.models.entities.CollectionResponse;
import com.moksha.raspberrypi.server.models.entities.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by somil.jain on 30/05/19.
 */
public class Runner {
    public static void main(
            String[] args) throws IOException {
        FkAction getFKDetails = new FkAction();
        final List<Product> cap = getFKDetails.searchKeyWordsAndReturnProducts("cap");
        List<String> toAddListingIds = new ArrayList<String>();
        List<String> toRemoveListingIds = new ArrayList<String>();
        toAddListingIds.add(cap.get(0).getListingId());
//        CollectionRequest collectionRequest = new CollectionRequest("HACK", toAddListingIds, toRemoveListingIds);
//        CollectionResponse newCollection = getFKDetails.createCollection(collectionRequest);
//        System.out.printf("\n collectionid : "+newCollection.getCollectionId());
//        System.out.printf("\n collectionURL: "+newCollection.getCollectionUrl());
//
//        String collectionId = "h2pg3ulcx0";
//        CollectionRequest collectionRequestUpdate = new CollectionRequest(toAddListingIds, toRemoveListingIds);
//        boolean updated = getFKDetails.updateCollection(collectionRequestUpdate, collectionId);
//        System.out.println("updated " + updated);
//        System.out.println(getFKDetails.getCollectionUrl(collectionId));

        System.out.println("\n product title "+ new ProductDetailService().getProductTitle("HOLF8WHUEFDBRXF4"));
        List<String> deviceList = new ArrayList<>(Arrays.asList("17ca9de766aedc444c4b8574e143c3e5", "01d929401523ad5b6faa756e3b648446", "9c41fef0884f9ab8b0ecc1da921d71bb"));
        PNRequest pnRequest = new PNRequest("https://www.flipkart.com/all/~cs-h2pg3ulcx0/pr?sid=all", deviceList);
        pnRequest.setPnTitle("Hahhahahahahhahah");
        PNService pnService = new PNService();
        pnService.sendPushNotification(pnRequest);
    }
}
