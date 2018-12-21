package com.totophoto.api.imgur;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.totophoto.Image.Image;
import com.totophoto.api.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class imgur extends api {

    private String clientID = "200bbef5eb30fff";
    private String entryPoint = "https://api.imgur.com/3";
    private String sort = "time";
    private String type = "&q_type=";

    public imgur(Context context) {
        super(context);
    }

    private String call(String url) {
        return super.call(url, "Client-ID " + clientID);
    }

    private void call(Response.Listener<String> onSuccess, Response.ErrorListener onError, String url, Map<String, String> mParams, byte[] bBody, int method) {
        super.call(onSuccess, onError, url, "Client-ID " + clientID, mParams, bBody, method);
    }

    /**
     * Search a picture with the API
     * @param search the terms to search
     * @param page the number of the page to get
     * @return a list of images
     */
    public void search(Response.Listener<String> onSuccess, String search, int page) {
        search = search.replace(" ", "+");
        String url = entryPoint + "/gallery/search/" + sort + "/" + page + "?q_all=" + search + type;

        call(onSuccess, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, url, null, null, Request.Method.GET);
    }

    /**
     * Upload a picture
     * @param image Picture encrypted in base64 or in binary
     */
    public void upload(Response.Listener<String> onSuccess, Response.ErrorListener onError, byte[] image) {
        String url = entryPoint + "/image";

        call(onSuccess, onError, url, null, image, Request.Method.POST);
    }

    /**
     * parse the search response of imgur API
     * @param response json response
     * @return List of images cont
     */
    public ArrayList<Image> parseSearchResponse(String response) {
        ArrayList<Image> imageList = new ArrayList<>();
        JSONObject Response = null;
        try {
            Response = new JSONObject(response);
        } catch (JSONException e) {
        }
        JSONArray jsArray = null;
        int i = 0;
        int j;

        try {
            if (Response != null && Response.getString("success").equals("true")) {
                jsArray = new JSONArray(Response.getString("data"));
                while (i < jsArray.length()) {
                    JSONObject item = jsArray.getJSONObject(i);
                    if (item.getBoolean("is_album")) {
                        if (this.getType().equals("all") || this.getType().equals("album")) {
                            JSONArray images = item.getJSONArray("images");
                            j = 0;
                            while (j < images.length()) {
                                JSONObject pic = images.getJSONObject(j);
                                String title = pic.getString("title");
                                if (title.equals("null"))
                                    title = item.getString("title");
                                imageList.add(new Image(title, pic.getString("link")));
                                j++;
                            }
                        }
                    } else {
                        if (!this.getType().equals("album")) {
                            imageList.add(new Image(item.getString("title"), item.getString("link")));
                        }
                    }
                    i++;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imageList;
    }

    /**
     * parse the upload response of imgur API
     * @param response json response
     * @return Image uploaded
     */
    public Image parseUploadResponse(String response) throws JSONException {
        Image img = null;
        JSONObject o = null;
        o = new JSONObject(response);
        o = o.getJSONObject("data");
        img = new Image(o.getString("title"), o.getString("link"));
        return img;
    }

    /**
     * set the sorting parameter
     * @param _sort sort type
     */
    public void setSort(String _sort) {
        sort = _sort;
    }

    /**
     *
     * @param _type Type of the image searched
     */
    public void setType(String _type) {
        if (!_type.equals("all"))
            type = "&q_type=" + _type;
        else
            type = "&q_type=";
    }

    public String getType() {
        String t = type.replace("&q_type=", "");

        if (t.length() <= 0)
            t = "all";
        return t;
    }

    /**
     * Reset filters to initials
     */
    public void reset() {
        sort = "time";
        type = "&q_type=";
    }
}
