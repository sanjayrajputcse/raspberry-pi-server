package com.moksha.raspberrypi.server.models.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by somil.jain on 31/05/19.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CollectionRequest {

    String collection_name;
    String collection_type;
    List semantic_attributes_list;
    Integer minimum_members_in_collection;
    String collection_end_date;
    String collection_entity_type;
    String store_id;
    List <String> added_products;
    List <String> removed_products;

    public CollectionRequest(String collectionName,
                             List<String> listingIdsToAdd,
                             List<String> listingIdsToRemove) {
        this.collection_name = collectionName;
        this.collection_type = "LARGE_ADHOC";
        this.semantic_attributes_list = new ArrayList();
        this.minimum_members_in_collection  = 1;
        this.collection_end_date = "2019-06-03T18:29:59.000Z";
        this.collection_entity_type = "LISTING";
        this.store_id = "all";
        this.added_products =listingIdsToAdd;
        this.removed_products = listingIdsToRemove;
    }

    public CollectionRequest( List<String> listingIdsToAdd,
                              List<String> listingIdsToRemove)
    {
        this.store_id = "all";
        this.added_products =listingIdsToAdd;
        this.removed_products = listingIdsToRemove;
    }
}
