package com.moksha.raspberrypi.server.InternalClient;

import com.moksha.raspberrypi.server.models.entities.Product;

import java.io.IOException;
import java.util.List;

/**
 * Created by somil.jain on 31/05/19.
 */
public interface Get {

    public List<Product> searchKeyWordsAndReturnProducts(String word) throws IOException;

}
