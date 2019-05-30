package com.moksha.raspberrypi.server.InternalClient;

import com.moksha.raspberrypi.server.fkService.CollectionService;
import com.moksha.raspberrypi.server.fkService.SearchService;
import com.moksha.raspberrypi.server.models.entities.CollectionRequest;
import com.moksha.raspberrypi.server.models.entities.CollectionResponse;
import com.moksha.raspberrypi.server.models.entities.Product;
import lombok.Data;

import java.io.IOException;
import java.util.List;

/**
 * Created by somil.jain on 31/05/19.
 */
@Data
public class GetFKDetails implements Action {

    @Override
    public List<Product> searchKeyWordsAndReturnProducts(String word) throws IOException {

        SearchService callSearch = new SearchService();
        return callSearch.searchOnAKeyword(word);
    }

    @Override
    public CollectionResponse createCollection(CollectionRequest collectionRequest) throws IOException {

        CollectionService collectionService = new CollectionService();
        return collectionService.createNewCollection(collectionRequest);
    }

    @Override
    public boolean updateCollection(CollectionRequest collectionRequest,String collectionId) throws IOException {
        CollectionService collectionService = new CollectionService();
        return collectionService.updateCollection(collectionRequest, collectionId);
    }

    @Override
    public String getCollectionUrl(String collectionId) {
        if(collectionId != null) {
            return "https://www.flipkart.com/all/~cs-" + collectionId + "/pr?sid=all";
        }
        return null;
    }

    @Override
    public boolean pushNotification() {
        return false;
    }
}
