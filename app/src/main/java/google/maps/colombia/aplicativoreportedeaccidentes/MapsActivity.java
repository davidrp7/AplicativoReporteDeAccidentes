package google.maps.colombia.aplicativoreportedeaccidentes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import google.maps.colombia.aplicativoreportedeaccidentes.Adapters.CustomInfoWindowAdapter;
import google.maps.colombia.aplicativoreportedeaccidentes.Models.Coordenadas;
import google.maps.colombia.aplicativoreportedeaccidentes.Models.Reporte;
import google.maps.colombia.aplicativoreportedeaccidentes.Request.DirectionsParser;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    /*************************SEARCH*********************/
    private static final String TAG = "MapsActivity";
    private static final float DEFAULT_ZOOM = 15f;
    private EditText mSearchText;
    /***************************************************/

    FirebaseFirestore mFirestoreCont;

    public int contadorreps = 0;

    private GoogleMap mMap;
    private static final int LOCATION_REQUEST = 500;

    ArrayList<LatLng> listPoints;
    private DatabaseReference mDatabase;
    private ArrayList<Marker> tmpRealTimeMarkers = new ArrayList<>();
    private ArrayList<Marker> realTimeMarkers = new ArrayList<>();
    private Switch aSwitch;
    public static Button btnregresaR;
    private Marker marcador;
    private static final float camera_zoom = 15;
    ImageView imgmarker;
    private BottomSheetBehavior mBottomSheetBehavior1;
    LinearLayout tapactionlayout;
    View bottomSheet;

    TextView txtnombre_local; // txt del tipo de accidente
    TextView txtGravedad; // txt de la gravedad accidente
    TextView txtHorario; // txt hora del accidente
    TextView txt_horarioFecha;// txt fecha accidente
    TextView txtDireccion;
    TextView txt_contador;
    Button btn_contador;
    String contadorBase = "";

    //probando aactualizar
    Button btn_actualizar;
    Reporte reporteSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        aSwitch = findViewById(R.id.switchMap);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btn_actualizar = findViewById(R.id.btn_actualizar_gravedad);

        mFirestoreCont = FirebaseFirestore.getInstance();
        //search
        mSearchText = findViewById(R.id.input_search);

        listPoints = new ArrayList<>();
    }

    private void init() {
        Log.d(TAG, "init: initializing");

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    //execute our method for searching
                    geoLocate();
                }
                return false;
            }
        });
    }

    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM);


            /*
                if(mSearchText.getText().toString().equals("Cra. 10 Este #21-35, Pasto")){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("No hay reportes de accidente cercanos.");
                builder.setTitle("INFORME DE CONSULTA");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
             */
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No hay reportes de accidente cercanos.");
            builder.setTitle("INFORME DE CONSULTA");
            AlertDialog dialog = builder.create();
            dialog.show();

            /*AlertDialog.Builder window = new AlertDialog.Builder(this);
            window.setMessage("Se encontro un reporte de accidente cercano.");
            window.setTitle("INFORME DE CONSULTA");
            AlertDialog dialog2 = window.create();
            dialog2.show();*/



        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        MarkerOptions options = new MarkerOptions().position(latLng);
        mMap.addMarker(options);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        btnregresaR = findViewById(R.id.btn_regresar);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }

        /* peticion anterior
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }*/

        mMap.setMyLocationEnabled(true);

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));

        init();

        mDatabase.child("coordenadas").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (Marker marker : realTimeMarkers) {
                    marker.remove();
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Coordenadas coor = snapshot.getValue(Coordenadas.class);
                    Double latitud = coor.getLatitud();
                    Double longitud = coor.getLongitud();
                    String tipo_accidente = "";
                    String tipo_gravedad = "";
                    String tipo_factor = "";
                    String fecha = "";
                    String direccion = "";
                    String hora = "";

                    if (snapshot.hasChild("tipo_accidente")) {
                        tipo_accidente = snapshot.child("tipo_accidente").getValue().toString();
                    }
                    if (snapshot.hasChild("gravedad_accidente")) {
                        tipo_gravedad = snapshot.child("gravedad_accidente").getValue().toString();
                    }
                    if (snapshot.hasChild("factor_accidente")) {
                        tipo_factor = snapshot.child("factor_accidente").getValue().toString();
                    }
                    if (snapshot.hasChild("contador")) {
                        contadorBase = snapshot.child("contador").getValue().toString();
                    }
                    if (snapshot.hasChild("fecha_accidente")) {
                        fecha = snapshot.child("fecha_accidente").getValue().toString();
                    }
                    if (snapshot.hasChild("hora_accidente")) {
                        hora = snapshot.child("hora_accidente").getValue().toString();
                    }
                    if (snapshot.hasChild("direccion_accidente")) {
                        direccion = snapshot.child("direccion_accidente").getValue().toString();
                    }

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(latitud, longitud));
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.sirena64));
                    markerOptions.title("REPORTE DE ACCIDENTE");
                    markerOptions.snippet("Tipo: " + tipo_accidente + " Gravedad: " + tipo_gravedad   + " Factor : " + tipo_factor + " Fecha: " + fecha + " Hora: " + hora + " Dirección: " + direccion);

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitud, longitud))
                            .zoom(13)
                            .bearing(90)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    tmpRealTimeMarkers.add(mMap.addMarker(markerOptions));

                }
                realTimeMarkers.clear();
                realTimeMarkers.addAll(tmpRealTimeMarkers);

                cargarDetalle();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Metodo para trazar ruta
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (listPoints.size() == 2) {
                    listPoints.clear();

                }
                //Save first point select
                listPoints.add(latLng);
                //Create marker
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                if (listPoints.size() == 1) {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                }
                mMap.addMarker(markerOptions);

                if (listPoints.size() == 2) {
                    String url = getRequestUrl(listPoints.get(0), listPoints.get(1));
                    TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                    taskRequestDirections.execute(url);
                }
            }
        });
    }

    private String getRequestUrl(LatLng origin, LatLng dest) {

        String str_org = "origin=" + origin.latitude + "," + origin.longitude;

        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        String sensor = "sensor=false";

        String mode = "mode=driving";

        String param = str_org + "&" + str_dest + "&" + sensor + "&" + mode;

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param + "&key=" + getString(R.string.google_maps_key);
        return url;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            URL url  = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";

            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }
            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null){
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try{
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);

        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>> >{

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {

            ArrayList points = null;

            PolylineOptions polylineOptions = null;
            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat,lon));
                }
                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions!=null) {
                mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "Direction not found!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void MapaSwitch(View view) {
        if (view.getId()==R.id.switchMap){
            if (aSwitch.isChecked()){
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            } else {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        }
    }

    public void btn_regresar_map(View view) {
        Intent intentInvitado = new Intent(MapsActivity.this, ReportarActivity.class);
        MapsActivity.this.startActivity(intentInvitado);
        finish();
    }

    public void cargarDetalle(){
        View headerLayout1 = findViewById(R.id.bottomJsoft);
        imgmarker = findViewById(R.id.ImgMarker);
        txtnombre_local = findViewById(R.id.txtNombreLocal);
        txtGravedad = findViewById(R.id.txt_gravedad);
        txtHorario = findViewById(R.id.txtHorario);
        txtDireccion = findViewById(R.id.txt_direccion);
        txt_horarioFecha = findViewById(R.id.txt_horarioFecha);
        tapactionlayout = findViewById(R.id.tap_action_layout);

        btn_contador = findViewById(R.id.btn_cont_accidentes);
        txt_contador = findViewById(R.id.txt_contador_rep);

        bottomSheet = findViewById(R.id.bottomJsoft);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior1.setPeekHeight(120);
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);


        txt_contador.setText("2");

        btn_contador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //contadorreps = contadorreps + 1;


//              String contadorRep = txt_contador.getText().toString();

                int contador = 1;

                txt_contador.setText("3");
               // txt_contador.setText(contadorreps +1);

                //Map<String, Object> cont = new HashMap<>();
                //cont.put("contador", contadorreps );

              //  mFirestoreCont.collection("Contador").document().set(cont);


            }
        });

        mBottomSheetBehavior1.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    tapactionlayout.setVisibility(View.VISIBLE);
                }
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    tapactionlayout.setVisibility(View.GONE);
                }
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    tapactionlayout.setVisibility(View.GONE);
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        tapactionlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBottomSheetBehavior1.getState()==BottomSheetBehavior.STATE_COLLAPSED)
                {
                    mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        /* -------------------------------- PRUEBA CARGAR DATOS EN DETALLE ------------------------------------------------------ */

        mDatabase.child("coordenadas").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                        if (snapshot.hasChild("tipo_accidente")) {
                            String tipo_accidente = "";
                            tipo_accidente = snapshot.child("tipo_accidente").getValue().toString();
                            txtnombre_local.setText(tipo_accidente);
                        }
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("coordenadas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                    if (snapshot.hasChild("gravedad_accidente")) {
                        String gravedad_accidente = "";
                        gravedad_accidente = snapshot.child("gravedad_accidente").getValue().toString();
                        txtGravedad.setText(gravedad_accidente);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("coordenadas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if (snapshot.hasChild("fecha_accidente")) {
                        String fecha_accidente = "";
                        fecha_accidente = snapshot.child("fecha_accidente").getValue().toString();
                        txtHorario.setText(fecha_accidente);
                    }
                    if (snapshot.hasChild("hora_accidente")) {
                        String hora_accidente = "";
                        hora_accidente = snapshot.child("hora_accidente").getValue().toString();
                        txt_horarioFecha.setText(hora_accidente);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("coordenadas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                    if (snapshot.hasChild("direccion_accidente")) {
                        String direccion_accidente = "";
                        direccion_accidente = snapshot.child("direccion_accidente").getValue().toString();
                        txtDireccion.setText(direccion_accidente);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
