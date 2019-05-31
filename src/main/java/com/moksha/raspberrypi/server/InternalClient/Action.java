package com.moksha.raspberrypi.server.InternalClient;

import com.moksha.raspberrypi.server.models.PNRequest;
import com.moksha.raspberrypi.server.models.entities.CartContext;
import com.moksha.raspberrypi.server.models.entities.CollectionRequest;
import com.moksha.raspberrypi.server.models.entities.CollectionResponse;
import com.moksha.raspberrypi.server.models.entities.Product;

import java.io.IOException;
import java.util.List;

/**
 * Created by somil.jain on 31/05/19.
 */
public interface Action {

    public List<Product> searchKeyWordsAndReturnProducts(String word) throws IOException;
    public CollectionResponse createCollection(CollectionRequest collectionRequest) throws IOException;
    public boolean updateCollection(CollectionRequest collectionRequest, String collectionId) throws IOException;
    public String getCollectionUrl(String collectionId);
    public boolean pushNotification(PNRequest pnRequest) throws IOException;
    public boolean addToGroceryBasket(String sn, CartContext cartContext) throws IOException;
    public boolean removeListingFromGroceryBasket(String sn, String listingId) throws IOException;
    public boolean removeGroceryBasket(String sn) throws IOException;
    public String getBasketViewURL();


}
