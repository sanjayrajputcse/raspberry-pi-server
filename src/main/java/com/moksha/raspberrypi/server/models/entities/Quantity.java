package com.moksha.raspberrypi.server.models.entities;

import lombok.Data;

/**
 * Created by somil.jain on 31/05/19.
 */
@Data
public class Quantity {

    int quantity;

    public Quantity(int quantity) {
        this.quantity = quantity;
    }
}
