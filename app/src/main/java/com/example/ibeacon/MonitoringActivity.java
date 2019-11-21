package com.example.ibeacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.gson.Gson;
import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.MonitorNotifier;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;

public class MonitoringActivity extends Activity implements IBeaconConsumer  {
    private IBeaconManager iBeaconManager = IBeaconManager.getInstanceForApplication(this);
    private ArrayList<IBeacon> arrayL = new ArrayList<IBeacon>();
    private ListView list = null;
    private BeaconAdapter adapter = null;
    private LayoutInflater inflater;
    private BeaconServiceUtility beaconUtill = null;
    List<HashMap> arrayList = new ArrayList<>();
    HashMap<String,Object> objMap = new HashMap<>();

    String json = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
        beaconUtill = new BeaconServiceUtility(this);
        list = (ListView) findViewById(R.id.list);
        adapter = new BeaconAdapter();
        list.setAdapter(adapter);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //initialization python
        if (! Python.isStarted()) {
            Context context = this;
            Python.start(new AndroidPlatform(context));
        }

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        beaconUtill.onStart(iBeaconManager, this);
    }

    @Override
    protected void onStop() {
        beaconUtill.onStop(iBeaconManager, this);
        super.onStop();
    }

    @Override
    public void onIBeaconServiceConnect(){
        iBeaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons, Region region) {
                arrayL.clear();
                arrayL.addAll((ArrayList<IBeacon>) iBeacons);
                Log.d("msg","onIBeaconServiceConnect="+iBeacons.size());
                if(iBeacons.size()>0){
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            logToDisplay();
                        }
                    });

                }
            }
        });
        try{
            iBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId",null,null,null));
        }catch (RemoteException e){}
        try {
            iBeaconManager.startMonitoringBeaconsInRegion(new Region("myRangingUniqueId",null,null,null));
        }catch (RemoteException e){}
    }

    private class BeaconAdapter extends BaseAdapter{

        @Override
        public int getCount(){
            if(arrayL !=null && arrayL.size()>0){
                return arrayL.size();
            }
            return  0;
        }

        @Override
        public IBeacon getItem(int arg0){
            return arrayL.get(arg0);
        }

        @Override
        public long getItemId(int arg0){
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            try {
                ViewHolder holder;

                if(convertView != null){
                    holder = (ViewHolder) convertView.getTag();
                }else{
                    holder = new ViewHolder(convertView = inflater.inflate(R.layout.tupple_ranging,null));
                }

                holder.beacon_uuid.setText("UUID: "+arrayL.get(position).getProximityUuid());
                holder.beacon_major.setText("Major: "+arrayL.get(position).getMajor());
                holder.beacon_minor.setText(",Minor: "+arrayL.get(position).getMinor());
                holder.beacon_proximity.setText("Proximity: "+arrayL.get(position).getProximity());
                holder.beacon_rssi.setText("Rssi: "+arrayL.get(position).getRssi());
                holder.beacon_txpower.setText("TxPower: "+arrayL.get(position).getTxPower());

            }catch (Exception e){
                e.printStackTrace();
            }
            return convertView;
        }
        private class ViewHolder {
            private TextView beacon_uuid;
            private TextView beacon_major;
            private TextView beacon_minor;
            private TextView beacon_proximity;
            private TextView beacon_rssi;
            private TextView beacon_txpower;
            private TextView beacon_range;

            public ViewHolder(View view) {
                beacon_uuid = (TextView) view.findViewById(R.id.BEACON_uuid);
                beacon_major = (TextView) view.findViewById(R.id.BEACON_major);
                beacon_minor = (TextView) view.findViewById(R.id.BEACON_minor);
                beacon_proximity = (TextView) view.findViewById(R.id.BEACON_proximity);
                beacon_rssi = (TextView) view.findViewById(R.id.BEACON_rssi);
                beacon_txpower = (TextView) view.findViewById(R.id.BEACON_txpower);
                beacon_range = (TextView) view.findViewById(R.id.BEACON_range);

                view.setTag(this);
            }
        }
    }
    private void logToDisplay() {
        runOnUiThread(new Runnable() {
            public void run() {
                    List<HashMap<String,String>>  List = new ArrayList<>();
                    for(int i=1;i<=arrayL.size();i++){
                        HashMap<String,String> map = new HashMap<>();
                        map.put("UUID"+i+"",arrayL.get(i-1).getProximityUuid());
                        map.put("Major",Integer.toString(arrayL.get(i-1).getMajor()));
                        map.put("Minjor",Integer.toString(arrayL.get(i-1).getMinor()));
                        map.put("Rssi",Integer.toString(arrayL.get(i-1).getRssi()));
                        map.put("TxPower",Integer.toString(arrayL.get(i-1).getTxPower()));

                        Python py = Python.getInstance();
                        PyObject pym = py.getModule("read_location");
                        PyObject pyf = pym.callAttr("test");
                        Log.d("msg","beacon_location="+pyf.toString());
                        /*if(arrayL.get(i-1).getMajor()==1914){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MonitoringActivity.this);
                            builder.setTitle("訊息");
                            builder.setMessage("尋找熱門巧克力");
                            AlertDialog showDialog = builder.create();
                            showDialog.show();
                        }*/
                        Log.d("msg","beaconm[ "+i+" ]="+map);
                        List.add(map);
                    }

                    Log.d("msg","beaconL="+list);
                    objMap.put("IBeaon",List);
                    Log.d("msg","objMap="+objMap);


            }
        });
    }

}
