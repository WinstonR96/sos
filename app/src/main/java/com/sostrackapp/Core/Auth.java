package com.sostrackapp.Core;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sostrackapp.Common.Respuesta;
import com.sostrackapp.Core.Helpers.VolleyHelper;
import com.sostrackapp.Core.Interfaces.IAuth;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Hashtable;
import java.util.Map;

import static com.sostrackapp.Common.Constante.LOGIN_URL;

public class Auth implements IAuth {

    private static Auth instance;
    private static Context ctx;
    // Get a RequestQueue
    private static RequestQueue queue;


    private Auth(Context context) {
        this.ctx = context;
        this.queue = VolleyHelper.getInstance(context).getRequestQueue();

    }

    public static synchronized Auth getInstance(Context context) {
        if (instance == null) {
            instance = new Auth(context);
        }
        return instance;
    }

    public Respuesta SendLogin(final String usuario, final String pass) {
        Respuesta res = null;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("json", "" + s);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //res = new Respuesta(false,volleyError.getMessage());
                        Log.e("ERROR_VOLLEY", "" + volleyError.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                //Adding parameters
                params.put("user", usuario);
                params.put("password", pass);
                params.put("regid", "djfdkjfdjf");
                params.put("devid", "4815191515");
                //returning parameters
                return params;
            }
        };
        VolleyHelper.getInstance(ctx).addToRequestQueue(stringRequest);
        return res;
    }

}



