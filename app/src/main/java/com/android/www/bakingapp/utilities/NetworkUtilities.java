package com.android.www.bakingapp.utilities;

import android.text.TextUtils;
import android.util.Log;

import com.android.www.bakingapp.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by OWNER on 4/29/2018.
 */

public final class NetworkUtilities {

    private static final String LOG_TAG = NetworkUtilities.class.getSimpleName();

    private NetworkUtilities(){

    }

    public static List<Recipe> getRecipeFromJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        Gson jsonString = new Gson();
        Type recipeListType = new TypeToken<ArrayList<Recipe>>() {
        }.getType();

        return jsonString.fromJson(jsonResponse, recipeListType);
    }

    public static URL buildUrl(String recipesRequestUrl){
        URL requestUrl;
        try {
            requestUrl = new URL(recipesRequestUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        return requestUrl;
    }

    public static String getJsonResponseFromHttpUrl(URL requestUrl) {
        if (requestUrl == null) {
            return null;
        }
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) requestUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                inputStream = httpURLConnection.getInputStream();

                Scanner scanner = new Scanner(inputStream);
                scanner.useDelimiter("\\A");

                if (scanner.hasNext()) {
                    return scanner.next();
                } else {
                    return null;
                }
            } else {
                Log.e(LOG_TAG, "Error response code: " + httpURLConnection.getResponseCode());
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
    }

}
