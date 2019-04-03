package com.example.deeps.ips;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

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

import adapter.Spinner_Adapter;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.example.deeps.ips.R.id.map;

public class OneFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    MapView mMapView;
    private Location Lastlocation = null;
    ArrayList<LatLng> MarkerPoints;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private GoogleMap mMap;
    SpinnerDatabaseHandler db;
    SharedPreferences pref;
    ArrayList<SpinnerGet> spin_list;
    private ArrayList<LatLng> cur_points;
    private LatLng lastlat;
    private Location location;
    private static int spin_posiion;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final SupportMapFragment myMAPF = (SupportMapFragment) getChildFragmentManager().findFragmentById(map);
         myMAPF.getMapAsync(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);
        pref = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        cur_points = new ArrayList<>();
        //cur_points.add(new LatLng(21.091970, 73.104425));
        //cur_points.add(new LatLng(21.091380, 73.104685));
        // Initializing
        db = new SpinnerDatabaseHandler(getContext());
        spin_list = db.getAllBecoan();
        MarkerPoints = new ArrayList<>();

        final Spinner_Adapter spinn;

        final Spinner spin = (Spinner) rootView.findViewById(R.id.spinner1);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getContext(), "" + spin_list.get(position).get_class(), Toast.LENGTH_SHORT).show();
                    spin_posiion=position;
                if (pref.getString("lat", null) != null) {
                    Double lat = Double.parseDouble(pref.getString("lat", null));
                    Double lng = Double.parseDouble(pref.getString("lng", null));
                    Log.e("currentLat", "" + lat);
                    Log.e("currentLng", "" + lng);

                    mMap.clear();

                    LatLng sw = new LatLng(21.091449, 73.104284);
                    LatLng ne = new LatLng(21.091891, 73.104810);
                    LatLng nw = new LatLng(21.091969, 73.104416);
                    LatLng se = new LatLng(21.091378, 73.104680);

                    LatLngBounds newarkBounds = new LatLngBounds(sw, ne).including(nw).including(se);

                    GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                            .image(BitmapDescriptorFactory.fromResource(R.drawable.map5))
                            .positionFromBounds(newarkBounds);
                    mMap.addGroundOverlay(newarkMap);

                    lastlat = null;
                    Double spinLat = spin_list.get(position).get_lat();
                    Double spinLng = spin_list.get(position).get_lng();

                    Log.e("spinLat", "" + spinLat);
                    Log.e("spinLng", "" + spinLng);
                    MarkerOptions options = new MarkerOptions();
                    options.title(spin_list.get(position).get_class());
                    // Setting the position of the marker
                    options.position(new LatLng(spinLat, spinLng));
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                    mMap.addMarker(options);
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(spinLat, spinLng)));
  //                  mMap.animateCamera(CameraUpdateFactory.zoomTo(20));


                    //-----------

                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                    Criteria criteria = new Criteria();

                    LocationRequest l_request = LocationRequest.create();
                    l_request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                      String bestProvider = locationManager.getBestProvider(criteria, true);


                   /*locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
                   locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,10,);*/

                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                     mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    mMap.getUiSettings().setScrollGesturesEnabled(true);


                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                    mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener(){

                        @Override
                        public void onMyLocationChange(Location location) {
                            if (Lastlocation == null)
                                Lastlocation = location;


                            LatLng lastlatlong = LocationToLatLong(Lastlocation);
                            LatLng Curlocation = LocationToLatLong(location);

                            double cur_latitude = location.getLatitude();
                            double cur_longitude = location.getLongitude();

                            Log.d("KK-atlong",""+lastlatlong.latitude);
                            Log.d("KK-atlong",""+Curlocation.latitude);

                            mMap.addPolyline(new PolylineOptions().add(lastlatlong).add(Curlocation).width(10).color(Color.BLUE));
//                          mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Curlocation, 15));


                            Lastlocation = location;
                        }
                    });
                    //-----------
                  /*    LatLng currentPoint = new LatLng(lat, lng);

                    MarkerPoints.add(currentPoint);

                    // Creating MarkerOptions
                    MarkerOptions options = new MarkerOptions();

                    // Setting the position of the marker
                    options.position(currentPoint);
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                    mMap.addMarker(options);

                    int idd = spin_list.get(position).getID();
                    //ArrayList<SpinnerGet> list=db.getBecoan(idd);
                    LatLng destPoint = new LatLng(spin_list.get(position).get_lat(), spin_list.get(position).get_lng());
                    MarkerPoints.add(destPoint);

                    // Creating MarkerOptions
                    MarkerOptions options1 = new MarkerOptions();

                    // Setting the position of the marker
                    options1.position(destPoint);
                    options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            if (MarkerPoints.size() == 1) {
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            } else if (MarkerPoints.size() == 2) {
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }



                    // Add new marker to the Google Map Android API V2
                    mMap.addMarker(options1);

                    String url = getUrl(currentPoint, destPoint);
                    Log.d("onMapClick", url.toString());
                    FetchUrl FetchUrl = new FetchUrl();

                    // Start downloading json data from Google Directions API
                    FetchUrl.execute(url);
                    //move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPoint));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                    // Checks, whether start and end locations are captured
                */}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final SpinnerDatabaseHandler db = new SpinnerDatabaseHandler(getContext());
        ArrayList<SpinnerGet> AllBecoan = db.getAllBecoan();
        //ArrayAdapter<String> dataAdapter;
        spinn = new Spinner_Adapter(getContext(), AllBecoan);
        //setAdapter(dataAdapter);
        //spinn.loadSpinnerData();
        spin.setAdapter(spinn);

        final Handler h1=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                ArrayList<SpinnerGet> AllBecoan = db.getAllBecoan();
                Spinner_Adapter spin_adapter = new Spinner_Adapter(getContext(), AllBecoan);
                spin.setAdapter(spin_adapter);
                spin.setSelection(spin_posiion);
                h1.postDelayed(this,10000);
            }
        };
        h1.postDelayed(runnable,4500);
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        //mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        // Setting onclick event listener for the map
        /*mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                // Already two locations
                if (MarkerPoints.size() > 1) {

                    pref.getString("lng", "");

                    MarkerPoints.clear();
                    mMap.clear();
                }

                // Adding new item to the ArrayList
                MarkerPoints.add(point);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(point);


                if (MarkerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (MarkerPoints.size() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }


                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options);

                // Checks, whether start and end locations are captured
                if (MarkerPoints.size() >= 2) {
                    LatLng origin = MarkerPoints.get(0);
                    LatLng dest = MarkerPoints.get(1);

                    // Getting URL to the Google Directions API
                    String url = getUrl(origin, dest);
                    Log.d("onMapClick", url.toString());
                    FetchUrl FetchUrl = new FetchUrl();

                    // Start downloading json data from Google Directions API
                    FetchUrl.execute(url);
                    //move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
                }

            }
        });*/
        mMap.setIndoorEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Add a marker in Sydney and move the camera
        LatLng TutorialsPoint = new LatLng(21.091380, 73.104685);
        mMap.addMarker(new
                MarkerOptions().position(TutorialsPoint).title("FETR"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(TutorialsPoint));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(TutorialsPoint));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(TutorialsPoint)      // Sets the center of the map to location user
                .zoom(20)                   // Sets the zoom
                .bearing(180)                // Sets the orientation of the camera to east
                .tilt(20)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        LatLng sw = new LatLng(21.091449, 73.104284);
        LatLng ne = new LatLng(21.091891, 73.104810);
        LatLng nw = new LatLng(21.091969, 73.104416);
        LatLng se = new LatLng(21.091378, 73.104680);

        LatLngBounds newarkBounds = new LatLngBounds(sw, ne).including(nw).including(se);

        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.map5))
                .positionFromBounds(newarkBounds);

        /*GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.mapr))
                .anchor(0, 1)
                .position(new LatLng(23.1034986, 72.593471), 860f, 650f);*/
        mMap.addGroundOverlay(newarkMap);
    }

    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "key=AIzaSyBdVMIL10TsaW1rh8V6unbZlneRPH60dQI";
        String mode = "mode=walking";
        String alternativ="alternatives=true";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode+ "&" +alternativ;

        // Output format
        String output = "json";
        //https://www.google.com/maps/embed/v1/directions?key=[YOUR API KEY]&origin=indianapolis&destination=chicago&avoid=tolls|highways&mode=walking
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        //String url = "https://www.google.com/maps/embed/v1/directions?" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            if (result.size() > 0)
            {
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);

                   /* // Add a thin red line from London to New York.
                    Polyline line = mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(23.108619, 72.590216),position)
                            .width(1)
                            .color(Color.RED));*/
                    }

                /*// Add a thin red line from London to New York.
                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0))
                        .width(5)
                        .color(Color.RED));*/
                    // Adding all the points in the route to LineOptions

                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(Color.RED);
                    Log.d("onPostExecute", "onPostExecute lineoptions decoded");

                }


            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }



    @Override
    public void onLocationChanged(Location location) {


        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }



        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("lat", "" + location.getLatitude());
        editor.putString("lng", "" + location.getLongitude());
        editor.commit();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);



   /*     if(lastlat==null) {
            lastlat = new LatLng(location.getLatitude(), location.getLongitude());
            cur_points.add(latLng);
            Log.e("curpoint",""+cur_points.get(0).longitude+"   "+cur_points.get(0).latitude);
        }
        else
        cur_points.add(latLng);
         //added

        Log.e("curpoint",""+cur_points.get(0).longitude+"   "+cur_points.get(0).latitude);
        //redrawLine();
        PolylineOptions options = new PolylineOptions().addAll(cur_points).width(10).color(Color.BLUE);
        for(int k =0;k<cur_points.size();k++)
        {
            options.add(cur_points.get(k));
        }

        mMap.addPolyline(options);



        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));*/


        //---------

   /*     LatLng current = new LatLng(location.getLatitude(), location.getLongitude());

        int flag = 0;
        LatLng prev = null;

        if(flag==0)  //when the first update comes, we have no previous points,hence this
        {
            prev=current;
            flag=1;
        }
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(current, 16);
        mMap.animateCamera(update);
        mMap.addPolyline((new PolylineOptions())
                .add(prev, current).width(6).color(Color.BLUE)
                .visible(true));
        prev=current;
        current = null;*/


        //---------
        //if(pref.getString("lat",null)!=null) {
        //}
        //added
        /*GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.mapr))
                .anchor(0, 1)
                .position(new LatLng(location.getLatitude(), location.getLongitude()), 860f, 650f);
        mMap.addGroundOverlay(newarkMap);
*/
        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    private LatLng LocationToLatLong(Location lastlocation) {
        if (lastlocation != null)
            return new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());
        return null;
    }


    private void redrawLine(){

        mMap.clear();  //clears all Markers and Polylines

        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < cur_points.size(); i++) {
            LatLng point = cur_points.get(i);
            options.add(point);
        }
        //addMarker(); //add Marker in current position
        mMap.addPolyline(options); //add Polyline
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user asynchronously -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }
}