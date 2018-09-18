package com.example.mana1.sampletest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.example.mana1.sampletest.location.newresult01;
import static com.example.mana1.sampletest.location.newresult02;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static String duration,distance;
    private GoogleMap mMap;
    private static final int LOCATION_REQUEST=500;
    ArrayList<LatLng> listPoints;
    TextView tv_result1,tv_result2;
    private static final  double EARTH_RADIUS = 6378137;
    private String mode, url;
    private LatLng location1, location2;
    private Double strLocation_Lat, strLocation_Lat2, strLocation_Lng, strLocation_Lng2;
    private Button WALKING, DRIVING,TRANSIT;
    private Button mbtnRoutes_Detail;
    String[] routes_detail_array;
    private TextView mtxtMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mtxtMode=(TextView)findViewById(R.id.txtMode);

        listPoints=new ArrayList<>();
        tv_result1= (TextView) findViewById(R.id.textView_result1);
        tv_result2=(TextView) findViewById(R.id.textView_result2);
        tv_result1.setText(distance);
        tv_result2.setText(duration);

        DRIVING= (Button) findViewById(R.id.btnDriving);
        WALKING= (Button) findViewById(R.id.btnWalking);
        TRANSIT= (Button) findViewById(R.id.btnTransit);
        DRIVING.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode="mode=driving";
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(location1).flat(true));//然後就把剛剛建立好的LatLng物件放進去currentMarker
                //String la=String.valueOf(strLocation_Lat);
                //String lo= String.valueOf(strLocation_Lng);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location1));
                location2 = new LatLng(strLocation_Lat2, strLocation_Lng2);
                mMap.addMarker(new MarkerOptions().position(location2));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(location2)              // Sets the center of the map to ZINTUN
                        .zoom(11)                   // Sets the zoom// Sets the orientation of the camera to east
                        .tilt(90)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                url = getRequestUrl(location1,location2);
                TaskRequestDirections taskRequestDirections=new TaskRequestDirections();
                taskRequestDirections.execute(url);
                mtxtMode.setText("Driving Mode");
            }
        });
        WALKING.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode="mode=walking";
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(location1).flat(true));//然後就把剛剛建立好的LatLng物件放進去currentMarker
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location1));
                location2 = new LatLng(strLocation_Lat2, strLocation_Lng2);
                mMap.addMarker(new MarkerOptions().position(location2));
                CameraPosition cameraPosition2 = new CameraPosition.Builder()
                        .target(location2)              // Sets the center of the map to ZINTUN
                        .zoom(11)                   // Sets the zoom// Sets the orientation of the camera to east
                        .tilt(90)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2));
                url = getRequestUrl(location1,location2);
                TaskRequestDirections taskRequestDirections2=new TaskRequestDirections();
                taskRequestDirections2.execute(url);
                mtxtMode.setText("Walking Mode");
            }
        });
        TRANSIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode="mode=transit";
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(location1).flat(true));//然後就把剛剛建立好的LatLng物件放進去currentMarker
                //String la=String.valueOf(strLocation_Lat);
                //String lo= String.valueOf(strLocation_Lng);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location1));
                location2 = new LatLng(strLocation_Lat2, strLocation_Lng2);
                mMap.addMarker(new MarkerOptions().position(location2));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(location2)              // Sets the center of the map to ZINTUN
                        .zoom(11)                   // Sets the zoom// Sets the orientation of the camera to east
                        .tilt(90)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                url = getRequestUrl(location1,location2);
                TaskRequestDirections taskRequestDirections=new TaskRequestDirections();
                taskRequestDirections.execute(url);
                mtxtMode.setText("Trasnsit Mode");
            }
        });
        mbtnRoutes_Detail=(Button)findViewById(R.id.btnRoutes_Detail);
        mbtnRoutes_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog_list=new AlertDialog.Builder(MapsActivity.this);
                dialog_list.setTitle("詳細導航路線");
                dialog_list.setItems(routes_detail_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog_list.setNegativeButton("跳出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog_list.show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST);
            return;
        }

        Geocoder geoCoder = new Geocoder(MapsActivity.this, Locale.getDefault());//這就是主要的轉換方法囉
        try {
            List<Address> addressLocation = geoCoder.getFromLocationName(newresult01, 1);//把地點的string放進去
            List<Address> addressLocation2 = geoCoder.getFromLocationName(newresult02, 1);

            strLocation_Lat = addressLocation.get(0).getLatitude();//抓經度
            strLocation_Lng = addressLocation.get(0).getLongitude();//抓緯度
            strLocation_Lat2 = addressLocation2.get(0).getLatitude();//抓經度
            strLocation_Lng2 = addressLocation2.get(0).getLongitude();//抓緯度
            location1 = new LatLng(strLocation_Lat, strLocation_Lng);
            Log.d("test",location1.toString());
            //設立一個用剛剛抓到經緯度為參數的LagLng物件
            mMap.addMarker(new MarkerOptions().position(location1).flat(true));//然後就把剛剛建立好的LatLng物件放進去currentMarker
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location1));
            location2 = new LatLng(strLocation_Lat2, strLocation_Lng2);
            mMap.addMarker(new MarkerOptions().position(location2).title("Marker in 台北信義區"));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(location2)              // Sets the center of the map to ZINTUN
                    .zoom(11)                   // Sets the zoom// Sets the orientation of the camera to east
                    .tilt(90)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            // Add a marker in Sydney and move the camera
            url = getRequestUrl(location1,location2);
            TaskRequestDirections taskRequestDirections=new TaskRequestDirections();
            taskRequestDirections.execute(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getRequestUrl(LatLng origin, LatLng dest) {
        //value of origin
        String str_org="origin="+ origin.latitude +","+origin.longitude;
        //value of destination
        String str_dest="destination="+ dest.latitude+","+dest.longitude;
        //set value enable the sensor
        String sensor="sensor=false";
        //build the full param
        String param=str_org+"&"+str_dest+"&"+sensor+"&"+mode;
        //output format
        String output="json";
        //creat url
        String url="https://maps.googleapis.com/maps/api/directions/"+output+"?"+"language=zh-TW&"+param+"&key=AIzaSyCyDJrORGg_ee345tIF-TFv4seXH-eMZyI"+"&alternatives=true";//+"https://maps.googleapis.com/maps/api/distancematrix/"+output+"?"+param;
        return url;
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString ="";
        InputStream inputStream=null;
        HttpURLConnection httpURLConnection=null;
        try{
            URL url=new URL(reqUrl);
            httpURLConnection= (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //get the response result
            inputStream=httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer=new StringBuffer();
            String line ="";
            while((line=bufferedReader.readLine())!=null){
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();

            bufferedReader.close();
            inputStreamReader.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(inputStream != null){
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    @SuppressLint("MissingPermission")



    public class TaskRequestDirections extends AsyncTask<String,Void,String>{
        @Override
        protected  String doInBackground(String... strings){
            String responseString="";

            try{
                responseString=requestDirection(strings[0]);
            }catch(IOException e){
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //parse json here
            TaskParser taskParser= new TaskParser();
            taskParser.execute(s);

        }
    }
    public class TaskParser extends AsyncTask<String,Void,List<List<HashMap<String,String>>>>{

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject=null;

            List<List<HashMap<String,String>>> routes=null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionParser=new DirectionsParser();
                routes=directionParser.parse(jsonObject);

            }catch(JSONException e){

                e.printStackTrace();
            }
            return routes;
        }

        //Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists){
            int result_size=lists.size();
            Log.d("result", String.valueOf(result_size)+lists);
            //get list route and display it into th map
            ArrayList points =null;
            PolylineOptions polylineOptions=null;
            ArrayList<String> html_str_array = null;
            ArrayList<String> html_transit=null;

            for(int i = 0; i < lists.size(); i=i+1){
                if(i==0){
                    points = new ArrayList<LatLng>();
                    polylineOptions = new PolylineOptions();
                    // Fetching i-th route
                    List<HashMap<String, String>> path = lists.get(i);
                    for(int j=0;j <path.size();j++){
                        HashMap<String, String> point =path.get(j);
                        if(j==0){ // Get distance from the list
                            distance = point.get("distance");
                            continue;
                        }else if(j==1){ // Get duration from the list
                            duration = point.get("duration");
                            continue;
                        }
                        double lat=Double.parseDouble(point.get("lat"));
                        double lon=Double.parseDouble(point.get("lon"));
                        points.add(new LatLng(lat,lon));
                    }
                    polylineOptions.addAll(points);
                    polylineOptions.width(15);
                    polylineOptions.color(Color.BLUE);
                    polylineOptions.geodesic(true);
                }

                if(i==1){
                    html_str_array = new ArrayList<String>();
                    html_transit=new ArrayList<String>();
                    List<HashMap<String, String>> html_list = lists.get(i);

                    for (int k = 0; k < html_list.size(); k++) {
                        HashMap<String, String> html_map = html_list.get(k);
                        String html_str = html_map.get("html_str");
                        String html_transit_depart= html_map.get("html_tran_departure");
                        String html_transit_arrive= html_map.get("html_tran_arrive");
                        if(html_transit_depart!=null){
                            html_str_array.add(html_transit_depart);
                            html_transit.add(html_transit_depart);
                        }
                        if(html_transit_arrive!=null){
                            html_str_array.add(html_transit_arrive);
                            html_transit.add(html_transit_arrive);
                        }
                        if(html_str!=null){
                            html_str_array.add(html_str);
                        }//為了解決陣列中出現null的問題，所以直接在這邊就規定不是null不能加進去
                    }
                    Log.d("revised_str_array", Html.fromHtml(String.valueOf(html_str_array)).toString());
                    if(!html_transit.isEmpty()){
                        routes_detail_array= Html.fromHtml(String.valueOf(html_str_array)).toString().replace("[","").replace("]","").replace("null"," ").replace(" ","").split(",");
                    }else {
                        routes_detail_array= Html.fromHtml(String.valueOf(html_str_array)).toString().replace("[","").replace("]","").replace(" ","").split(",");
                    }
                }
            }
            if (polylineOptions!= null){
                mMap.addPolyline(polylineOptions);
                tv_result2.setText(distance);
                tv_result1.setText(duration);
            }else{
                Toast.makeText(getApplicationContext(),"Direction not found",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
