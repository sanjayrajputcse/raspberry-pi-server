package com.moksha.raspberrypi.server.ajay.models.entities;

/**
 * Created by ajay.agarwal on 30/05/19.
 */
public enum Action {
    CREATE_LIST("createList"),
    REMOVE_LIST("removeList"),
    ADD_ITEM_TO_LIST("addItemToList"),
    REMOVE_ITEM_FROM_LIST("removeItemFromList"),
    SEND_LIST("sendListToDevice"),
    INVALID("invalid");

    String value;

    Action(String value) {
        this.value = value;
    }

    public static Action getActionFromString(String actionStr) {
        for (Action action : Action.values()) {
            if (action.value.equals(actionStr))
                return action;

        }
        return INVALID;
    }

    public String getValue(){
        return value;
    }
}
