package com.moksha.raspberrypi.server.fkService;

import com.moksha.raspberrypi.server.models.entities.CollectionRequest;
import com.moksha.raspberrypi.server.models.entities.CollectionResponse;
import okhttp3.OkHttpClient;

/**
 * Created by somil.jain on 31/05/19.
 */
public class CollectionService {

    String METHOD_GET = "GET";
    String METHOD_POST = "POST";
    String HOST = "http://10.47.7.31";
    String API = "/collection-service/v0/collection/large-adhoc/4slr3oprtl";

    OkHttpClient client ;

    public CollectionService() {

        client = new OkHttpClient();
    }

    public CollectionResponse createNewCollection(CollectionRequest collectionRequest) {

       return null;
    }
}
