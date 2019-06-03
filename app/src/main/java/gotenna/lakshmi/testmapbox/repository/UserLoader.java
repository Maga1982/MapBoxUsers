package gotenna.lakshmi.testmapbox.repository;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import gotenna.lakshmi.testmapbox.modal.Users;

public class UserLoader  {

    ArrayList<Users> arrayList;
    public  static UserLoader userLoader;
    public static UserLoader getInstance(){
        if(userLoader == null) {
            userLoader = new UserLoader();
        }
         return userLoader;

    }

    public MutableLiveData<ArrayList<Users>> getMutableLiveData (Context context) {
        Log.v("inside","getmutablelivedata");
        volleyRequest(context);
        MutableLiveData<ArrayList<Users>> mutableLiveData=new MutableLiveData<ArrayList<Users>>();
        mutableLiveData.setValue(arrayList);
        return mutableLiveData;
    }

    public void volleyRequest(Context context) {
        arrayList = new ArrayList<Users>();
        Log.v("inside","volleyrequest");
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://annetog.gotenna.com/development/scripts/get_map_pins.php";

        // Request a string response from the provided URL.

        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET,url ,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {

                        try {
                            Log.v("response",jsonArray.get(0).toString());
                              for (int i = 0; i < jsonArray.length(); i++) {
                                DecimalFormat df = new DecimalFormat("##.######");
                                JSONObject arrayJSONObject = jsonArray.getJSONObject(i);
                                String name = arrayJSONObject.getString("name");
                                String description = arrayJSONObject.getString("description");
                                Log.v("formatted", df.format(arrayJSONObject.getDouble("latitude")));
                                String latitude = df.format(arrayJSONObject.getDouble("latitude"));
                                String longitude = df.format(arrayJSONObject.getDouble("longitude"));
                                arrayList.add(new Users(name, description, latitude, longitude));
                                Log.v("name", name);
                                Log.v("desc", description);
                                Log.v("latitude", latitude);
                                Log.v("desc", longitude);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               //  Log.v("voleyerror","error");
            }
        });
        queue.add(stringRequest);


    }

}
