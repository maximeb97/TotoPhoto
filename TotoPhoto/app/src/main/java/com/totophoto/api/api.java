package com.totophoto.api;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.totophoto.Image.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class api {
    private Context context = null;

    public api(Context _context) {
        context = _context;
    }

    /**
     *
     * @param url
     * @param auth
     * @return request response
     */
    public String call(String url, String auth) {
        return call(url, auth, null, null, Request.Method.GET);
    }

    /**
     *
     * @param url
     * @param auth
     * @param method
     * @return request response
     */
    public String call(String url, String auth, int method) {
        return call(url, auth, null, null, method);
    }

    /**
     *
     * @param url
     * @param auth
     * @param mParams
     * @param bBody
     * @param method
     * @return request response
     */
    public String call(String url, final String auth, final Map<String, String> mParams, final byte[] bBody, int method) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final String[] mResponse = new String[1];

        mResponse[0] = "";
        RequestFuture<String> future = RequestFuture.newFuture();

            StringRequest stringRequest = new StringRequest(method, url,
                future, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", auth);
                return params;
            }
            @Override
            public Map<String, String> getParams() {
                return mParams;
            }

            @Override
            public byte[] getBody() {
                return bBody;
            }
        };
        stringRequest.setShouldCache(false);

        queue.add(stringRequest);
        try {
            mResponse[0] = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return mResponse[0];
    }

    /**
     *
     * @param onSuccess
     * @param url
     * @param auth
     * @param mParams
     * @param bBody
     * @param method
     */
    public void call(Response.Listener<String> onSuccess, Response.ErrorListener onError, String url, final String auth, final Map<String, String> mParams, final byte[] bBody, int method) {
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(method, url,
                onSuccess, onError){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", auth);
                return params;
            }
            @Override
            public Map<String, String> getParams() {
                return mParams;
            }

            @Override
            public byte[] getBody() {
                return bBody;
            }
        };
        queue.add(stringRequest);
    }

    public ArrayList<Image> search(String search, int page) {
        return null;
    }

    public Image upload(String image) {
        return null;
    }
}
