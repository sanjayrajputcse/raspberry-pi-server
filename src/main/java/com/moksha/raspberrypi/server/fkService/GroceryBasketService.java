package com.moksha.raspberrypi.server.fkService;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moksha.raspberrypi.server.models.entities.CartContext;
import com.mysql.jdbc.StringUtils;
import okhttp3.*;

import java.io.File;
import java.io.IOException;

/**
 * Created by somil.jain on 31/05/19.
 */
public class GroceryBasketService {

    private final String ADD_ENDPOINT = "https://www.flipkart.com/api/1/cart/browse";
    private final String DELETE_LISTING_ENDPOINT = "https://www.flipkart.com/api/5/cart?listingIds={lId}&view=GROCERY";
    private final String REMOVE_BASKET = "https://www.flipkart.com/api/5/cart?marketplaceIds=GROCERY&view=ALL";
    private final String sn = "2.VI3D6B8E9C91374DD2A1869734A80E0C6C.SIFA609D7B68984EA7A89455B417AD3F07.VS830A0FA54A1B420B9E8863D0B634C8F1.1559270587";
    private final String st = "SIFA609D7B68984EA7A89455B417AD3F07:VI3D6B8E9C91374DD2A1869734A80E0C6C";
    private final String XUserAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36 FKUA/website/42/website/Desktop";

    private final OkHttpClient client;
    private final ObjectMapper objectMapper;


    public GroceryBasketService() {
        client = new OkHttpClient();
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public boolean addListingToMyCart(String sn, CartContext cartContext) throws IOException {
        try {

            File file = new File(getClass().getClassLoader().getResource("local/cartAddRequestData.json").getFile());
            JsonNode jsonNode = objectMapper.readTree(file);

            if (StringUtils.isNullOrEmpty(sn)) {
                sn = this.sn;
            }
            JsonNode browseCartContext = jsonNode.get("browseCartContext");
            JsonNode replace = objectMapper.valueToTree(cartContext.getCartContext());
            ((ObjectNode) browseCartContext).put("cartContext", replace);
            System.out.println("hey" + objectMapper.writeValueAsString(jsonNode));
            return post(ADD_ENDPOINT, objectMapper.writeValueAsString(jsonNode), sn);
        } catch (Exception e) {
            System.out.println("Unable to add in Cart" + e);
        }
        return false;
    }

    public boolean removeListingFromGroceryBucket(String sn, String listingId) throws IOException {
        if (StringUtils.isNullOrEmpty(sn)) {
            sn = this.sn;
        }
        String deleteUrl = org.apache.commons.lang3.StringUtils.replace(DELETE_LISTING_ENDPOINT, "{lId}", listingId);
        return delete(sn ,deleteUrl);
    }

    public boolean removeBasket(String sn) throws IOException {
        if(StringUtils.isNullOrEmpty(sn))
        {
            sn = this.sn;
        }
        return delete(sn ,REMOVE_BASKET);
    }

    private boolean delete(String sn, String deleteUrl) throws IOException {
        boolean deleted = false;
        try {
            Request request = new Request.Builder()
                    .url(deleteUrl)
                    .addHeader("sn", sn)
                    .addHeader("secureToken", st)
                    .addHeader("X-User-Agent", XUserAgent)
                    .delete()
                    .build();
            deleted = client.newCall(request).execute().isSuccessful();
        }catch (Exception e)
        {
            System.out.println("Unable to remove listing");
        }
        return deleted;
    }


    private boolean post(String url, String jsonBody, String sn) {

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonBody);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("sn", sn)
                .addHeader("secureToken", st)
                .addHeader("X-User-Agent", XUserAgent)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println("code" + response.code());
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
