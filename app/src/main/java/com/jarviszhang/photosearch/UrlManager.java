package com.jarviszhang.photosearch;

import android.net.Uri;

/**
 * Created by jarvis on 9/7/17.
 */

public class UrlManager {
    final static String API_KEY = "08b989d04095bfea9f201780137fd360";

    final static String PREF_SEARCH_QUERY = "search_query";

    final static String URL_END_POINT = "https://api.flicker.com/service/reset";
    final static String URL_GET_RECENT = "flicker.photos.getRecent";
    final static String URL_SEARCH = "flicker.photos.search";


    private static UrlManager instance;

    public static UrlManager getInstance(){
        if(instance == null){
            synchronized (UrlManager.class){
                if(instance == null){
                    instance = new UrlManager();
                }
            }
        }

        return instance;
    }

    public static String getItemUrl(String query, int page){
        String url = null;

        if(query == null){
            //自动构建请求链接, builder pattern
            url = Uri.parse(URL_END_POINT)
                    .buildUpon()
                    .appendQueryParameter("method", URL_GET_RECENT)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback","1")
                    .appendQueryParameter("page", page + "")
                    .build().toString();
        }else{
            url = Uri.parse(URL_END_POINT)
                    .buildUpon()
                    .appendQueryParameter("method", URL_SEARCH)
                    .appendQueryParameter("text",query)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback","1")
                    .appendQueryParameter("page", page + "")
                    .build().toString();
        }

        return url;
    }
}
