package com.moksha.raspberrypi.server.models;

import lombok.Data;

import java.util.List;

/**
 * Created by somil.jain on 31/05/19.
 */
@Data
public class PNRequest {
    String collectionUrl;
    List<String> deviceIds;
    String pnTitle;
    boolean isCart;

    public PNRequest(String collectionUrl, List<String> deviceIds, String pnTitle,boolean isCart) {
        this.collectionUrl = collectionUrl;
        this.deviceIds = deviceIds;
        this.pnTitle = pnTitle;
        this.isCart = isCart;
    }
}
