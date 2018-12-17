package com.totophoto;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.totophoto.Image.Image;
import com.totophoto.api.api;
import com.totophoto.api.imgur.imgur;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * These tests requires a valid network connection to work
 */
@RunWith(AndroidJUnit4.class)
public class ApiInstrumentedTest {

    private Context appContext = InstrumentationRegistry.getTargetContext();


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Before
    public void checkInternetAccess() {
        assertEquals(true, isNetworkAvailable());
    }

    @Test
    public void testAPI() throws Exception {
        String response;
        Map<String, String> mParams = new HashMap<>();
        api a = new api(appContext);

        response = a.call("https://httpbin.org/get", "");
        assertEquals(true, response.length() > 0);
        JSONObject o =  new JSONObject(response);

        assertEquals(true, o.has("url"));

        mParams.put("test", "toto");

        o =  new JSONObject(a.call("https://httpbin.org/get?test=toto", ""));

        assertEquals(true, o.has("args"));

        JSONObject args = new JSONObject(o.getString("args"));

        assertEquals(true, args.has("test"));
        assertEquals("toto", args.getString("test"));
    }

    @Test
    public void testImgur() throws Exception {
        imgur a = new imgur(appContext);
        ArrayList<Image> list;

        a.upload(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                assertEquals(true, true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                assertEquals(false, true);
            }
        }, "R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7".getBytes());
    }

    @Test
    public void testImgurParse() throws Exception {
        imgur a = new imgur(appContext);
        ArrayList<Image> list = a.parseSearchResponse("{\"data\":[{\"title\":\"name\",\"link\":\"link\",\"is_album\":false}],\"success\":true,\"status\":200}");
        Image img = a.parseUploadResponse("{\"data\":{\"title\":\"name\",\"link\":\"link\"},\"success\":true,\"status\":200}");

        assertEquals(1, list.size());
        assertEquals("name", img.getName());
        assertEquals("link", img.getLink());
    }
}
