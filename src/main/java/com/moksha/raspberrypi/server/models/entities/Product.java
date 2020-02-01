package com.moksha.raspberrypi.server.models.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by somil.jain on 30/05/19.
 */

@Data
public class Product {

    String productId;
    String listingId;
    @Setter
    String productTitle;

    public Product(String productId, String listingId) {
        this.productId = productId;
        this.listingId = listingId;


    }
}
