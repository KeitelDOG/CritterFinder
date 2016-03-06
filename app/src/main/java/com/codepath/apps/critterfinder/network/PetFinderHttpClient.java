package com.codepath.apps.critterfinder.network;

import com.codepath.apps.critterfinder.models.SearchFilter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * Created by srichard on 2/24/16.
 */
public class PetFinderHttpClient  {
    private static PetFinderHttpClient mInstance = null;
    AsyncHttpClient client = null;
    public static final String REST_URL = "http://api.petfinder.com"; // PetFinder, base API URL
    public static final String REST_CONSUMER_KEY = "7afaf97ae32a304d684d620381a63e3b";       // PetFinder
    public static final String REST_CONSUMER_SECRET = "f83cf7247c48af6df08bb61064ba01ab"; 	// Petfinder Secret

//    public static PetFinderHttpClient getInstance(){
//        if(mInstance == null)
//        {
//            mInstance = new PetFinderHttpClient();
//        }
//        return mInstance;
//    }

    public PetFinderHttpClient() {
        client = new AsyncHttpClient();
    }

    // PetFinder get list of pets
    public void findPetList(JsonHttpResponseHandler handler) {
        String apiUrl = getApiUrl("pet.find");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("format", "json");
//		params.put("count", 25);
        params.put("key", REST_CONSUMER_KEY);
        params.put("location", "94941");        // TO DO don't hard code Zip Code
        client.get(apiUrl, params, handler);
    }

    /**
     * Return a list of breeds for a particular species
     * @param species
     * @param handler
     */
    public void getBreedList(SearchFilter.Species species, TextHttpResponseHandler handler) {
        String apiUrl = getApiUrl("breed.list");

        RequestParams params = new RequestParams();
        params.put("format", "json");
        params.put("animal", species.toString());
        params.put("key", REST_CONSUMER_KEY);

        client.get(apiUrl, params, handler);
    }

    private String getApiUrl(String appendToUrl) {
        String newUrl = REST_URL;
        newUrl = newUrl.concat("/" + appendToUrl);
        return newUrl;
    }
}
