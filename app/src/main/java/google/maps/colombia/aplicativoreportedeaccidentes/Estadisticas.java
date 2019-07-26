package google.maps.colombia.aplicativoreportedeaccidentes;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import google.maps.colombia.aplicativoreportedeaccidentes.Models.Tabla;

public class Estadisticas extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvDetalle, tvLatitud, tvLongitud, tvFecha, tvHora, tvTexto, tvUsuario, btn_Estadisticas;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    LinearLayout LinearCotenedor;
    public Spinner spinner;
    public String[] reporteAcc = {" ", "GRAVEDAD", "TIPO", "LUGAR", "TODO"};


    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        spinner = findViewById(R.id.spinnerReporte);
        tvLatitud = findViewById(R.id.txt_latitud);
        btn_Estadisticas = findViewById(R.id.btn_generarestadisticas);
        tvLongitud = findViewById(R.id.txt_longitud);
        LinearCotenedor = findViewById(R.id.LinearCotenedor);
        tvDetalle = findViewById(R.id.txt_direccion);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);


        //locationStart();
        loadDataOpciones();

        btn_Estadisticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Cargando datos");
                progressDialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LinearCotenedor.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    }

                }, 6000);
            }
        });

        String[] reportes = {" ", "GRAVEDAD", "TIPO", "LUGAR", "VER TODO"};



        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, reporteAcc));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void loadDataOpciones(){

        if (spinner.equals("GRAVEDAD")){
            progressDialog.setMessage("Cargando datos");
            progressDialog.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LinearCotenedor.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }

            }, 6000);
        }
    }

    /*
    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    public void setLocation(Location location){
        if (location.getLatitude() != 0.0 && location.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        location.getLatitude(), location.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    tvDetalle.setText(DirCalle.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class Localizacion implements LocationListener {

        Estadisticas estadisticas;

        public Estadisticas getEstadisticas(){
            return estadisticas;
        }

        public void setMainActivity(Estadisticas estadisticas) {
            this.estadisticas = estadisticas;
        }

        @Override
        public void onLocationChanged(Location location) {

            location.getLatitude();
            location.getLongitude();
            String latitud = String.valueOf(location.getLatitude());
            String longitud = String.valueOf(location.getLongitude());
            Log.i("latitud", "latitud: "+ latitud);
            Log.i("longitud", "longitud: "+ longitud);
            tvLatitud.setText(latitud);
            tvLongitud.setText(longitud);
            this.estadisticas.setLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            switch (i){
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvide.AVAILABLE");
                    break;

                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvide.OUT_OF_SERVICE");
                    break;

                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvide.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String s) {
            tvDetalle.setText("GPS Activado");
        }

        @Override
        public void onProviderDisabled(String s) {
            tvDetalle.setText("GPS Desactivado");
        }
    }
        */
    }
