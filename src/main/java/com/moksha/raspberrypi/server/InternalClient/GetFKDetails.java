package com.moksha.raspberrypi.server.InternalClient;

import com.moksha.raspberrypi.server.fkService.CollectionService;
import com.moksha.raspberrypi.server.fkService.SearchService;
import com.moksha.raspberrypi.server.models.entities.CollectionRequest;
import com.moksha.raspberrypi.server.models.entities.CollectionResponse;
import com.moksha.raspberrypi.server.models.entities.Product;

import java.io.IOException;
import java.util.List;

/**
 * Created by somil.jain on 31/05/19.
 */
public class GetFKDetails implements Get{

    @Override
    public List<Product> searchKeyWordsAndReturnProducts(String word) throws IOException {

        SearchService callSearch = new SearchService();
        return callSearch.searchOnAKeyword(word);
    }

    public CollectionResponse createCollection(CollectionRequest collectionRequest){

        CollectionService collectionService = new CollectionService();
        return collectionService.createNewCollection(collectionRequest);
    }
}
