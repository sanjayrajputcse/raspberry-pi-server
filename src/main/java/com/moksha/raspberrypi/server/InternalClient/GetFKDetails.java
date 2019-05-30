package com.moksha.raspberrypi.server.InternalClient;

import com.moksha.raspberrypi.server.CallSearch;
import com.moksha.raspberrypi.server.models.entities.Product;

import java.io.IOException;
import java.util.List;

/**
 * Created by somil.jain on 31/05/19.
 */
public class GetFKDetails implements Get{

    @Override
    public List<Product> searchKeyWordsAndReturnProducts(String word) throws IOException {

        CallSearch callSearch = new CallSearch();
        return callSearch.searchOnAKeyword(word);
    }
}
