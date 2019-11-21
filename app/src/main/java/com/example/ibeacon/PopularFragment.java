package com.example.ibeacon;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PopularFragment extends Fragment {
    public static ListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popular,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super .onActivityCreated(savedInstanceState);
        listView=(ListView)getActivity().findViewById(R.id.listviewJsonData);
        String url = "http://192.168.0.102:8080/api/populer_sort";
        getData(url);
    }
    public String getData(String urlString){
        String result="";
        //使用JsonObjectRequest類別要求JSON資料
        JsonObjectRequest jsonObjectRequest=
                new JsonObjectRequest(urlString, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            //Velloy採非同步作業，Response.Listener  監聽回應
                            public void onResponse(JSONObject response) {
                                Log.d("回傳結果", "結果=" + response.toString());

                                try {
                                    parseJSON(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    //Response.ErrorListener 監聽錯誤
                    public void onErrorResponse(VolleyError error) {
                        Log.d("回傳結果", "錯誤訊息:" + error.toString());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
        return result;
    }
    private void parseJSON(JSONObject jsonObject) throws JSONException{
        ArrayList<Map<String,Object>> list = new ArrayList<Map<String, Object>>();

        JSONArray data = jsonObject.getJSONArray("data");
        Log.d("msg","data="+data.getJSONObject(0).get("article_title"));

        for  (int i = 0; i < data.length(); i++){

            HashMap<String,Object> item  = new HashMap<String, Object>();
            JSONObject o = data.getJSONObject(i);
            Log.d("msg","name="+o.get("article_title"));
            item.put("name",o.getString("article_title"));
            list.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(
                getActivity(),
                list,
                R.layout.activity_populor_listview,
                new String[]{"name"},
                new int[]{R.id.txt_name}
        );
        listView.setAdapter(adapter);
    }
}
