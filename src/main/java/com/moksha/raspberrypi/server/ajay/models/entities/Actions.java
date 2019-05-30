package com.moksha.raspberrypi.server.ajay.models.entities;

/**
 * Created by ajay.agarwal on 30/05/19.
 */
public enum Actions {
    CREATE_LIST("createList"),
    REMOVE_LIST("removeList"),
    ADD_ITEM_TO_LIST("addItemToList"),
    REMOVE_ITEM_FROM_LIST("removeItemFromList");

    String value;

    Actions(String value) {
        this.value = value;
    }
}
