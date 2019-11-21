package com.example.ibeacon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PopolarActivity extends AppCompatActivity {
    public static ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popolar);

        listView=(ListView)findViewById(R.id.listviewJsonData);
        String url = "http://172.20.10.7:8080/connect.php";
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
                                    String data = response.getString("a");
                                    Log.d("msg","name="+data);
                                    //parseJSON(response);
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
        Volley.newRequestQueue(this).add(jsonObjectRequest);
        return result;
    }
        private void parseJSON(JSONObject jsonObject) throws JSONException{
        ArrayList<Map<String,Object>> list = new ArrayList<Map<String, Object>>();

        JSONArray data = jsonObject.getJSONArray("posts");
        Log.d("msg","data="+data.getJSONObject(0).get("a"));

        for  (int i = 0; i < data.length(); i++){

            HashMap<String,Object> item  = new HashMap<String, Object>();
            JSONObject o = data.getJSONObject(i);
            Log.d("msg","name="+o.get("article_title"));
            item.put("name",o.getString("article_title"));
            list.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                list,
                R.layout.activity_populor_listview,
                new String[]{"name"},
                new int[]{R.id.txt_name}
        );
        listView.setAdapter(adapter);
    }
}
