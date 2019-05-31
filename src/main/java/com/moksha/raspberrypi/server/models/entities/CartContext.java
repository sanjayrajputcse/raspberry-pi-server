package com.moksha.raspberrypi.server.models.entities;

import lombok.Data;

import java.util.Map;

/**
 * Created by somil.jain on 31/05/19.
 */
@Data
public class CartContext {

    Map<String,Quantity> cartContext;


    public CartContext(Map<String, Quantity> cartContext) {
        this.cartContext = cartContext;
    }
}
