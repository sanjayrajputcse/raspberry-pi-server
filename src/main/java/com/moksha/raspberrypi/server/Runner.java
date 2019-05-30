package com.moksha.raspberrypi.server;

import com.moksha.raspberrypi.server.fkService.SearchService;
import com.moksha.raspberrypi.server.models.entities.Product;

import java.io.IOException;
import java.util.List;

/**
 * Created by somil.jain on 30/05/19.
 */
public class Runner {
    public static void main(
            String[] args) throws IOException {
        SearchService callSearch = new SearchService();
        final List<Product> cap = callSearch.searchOnAKeyword("cap");
    }
}
