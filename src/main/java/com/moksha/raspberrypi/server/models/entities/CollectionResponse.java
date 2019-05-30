package com.moksha.raspberrypi.server.models.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by somil.jain on 31/05/19.
 */
@Data
public class CollectionResponse {

    @JsonProperty("collection_id")
    String collectionId;
    @JsonProperty("collection_url")
    String collectionUrl;



}
