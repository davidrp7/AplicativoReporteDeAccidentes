package google.maps.colombia.aplicativoreportedeaccidentes;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import google.maps.colombia.aplicativoreportedeaccidentes.Models.Factor;
import google.maps.colombia.aplicativoreportedeaccidentes.Models.Gravedad;
import google.maps.colombia.aplicativoreportedeaccidentes.Models.Reporte;
import google.maps.colombia.aplicativoreportedeaccidentes.Models.Tipo;

public class ReportarActivity extends AppCompatActivity {


    // ELEMENTOS FIRESTORE
    FirebaseFirestore mFirestore;
    String dateFirestore="";
    String timeFirestore="";
    FirebaseFirestore db;

    public final String contador = "1";

    /***************************************************************/
    private FusedLocationProviderClient mfusedLocationProviderClient;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;
    DatabaseReference mDatabase;
    Spinner spinnerTipo;
    Spinner spinnerGravedad;
    Spinner spinnerFactor;
    TextView tvDireccion;
    String tipoaccidente="";
    String gravedadaccidente="";
    String factoraccidente="";
    String date="";
    String time="";
    Button btn_ayuda_factor;

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportar);
        locationStart();
        //instancia firestore
        mFirestore = FirebaseFirestore.getInstance();
        /********************************************/


        spinnerTipo = findViewById(R.id.spinnerTipo);
        spinnerGravedad = findViewById(R.id.spinnerGravedad);
        spinnerFactor =  findViewById(R.id.spinnerFactor);

        btn_ayuda_factor = findViewById(R.id.btn_ayuda_reporte);
        tvDireccion = findViewById(R.id.txt_obtenerdireccion);

        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        loadDataSpinnerTipo();
        loadDataSpinnerGravedad();

        spinnerFactor.setVisibility(View.INVISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();

        /*************************************************/

        //// nuevo cambio
    }

    public void loadDataSpinnerTipo(){
        final List<Tipo> tipos = new ArrayList<>();
        mDatabase.child("tipo_accidente").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        if (ds.hasChild("nombre_tipo")) {
                            String tipoName = ds.child("nombre_tipo").getValue().toString();
                            tipos.add(new Tipo(tipoName));
                        }
                    }
                    ArrayAdapter<Tipo> arrayAdapter = new ArrayAdapter<Tipo>(ReportarActivity.this, android.R.layout.simple_dropdown_item_1line, tipos);
                    spinnerTipo.setAdapter(arrayAdapter);
                    spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                            tipoaccidente = parent.getItemAtPosition(i).toString();
                            if (tipoaccidente != parent.getItemAtPosition(0).toString()){
                                Toast.makeText(ReportarActivity.this, "Has seleccionado: " + tipoaccidente , Toast.LENGTH_SHORT).show();
                                spinnerFactor.setVisibility(View.INVISIBLE);
                                btn_ayuda_factor.setVisibility(View.INVISIBLE);
                            }
                            if (tipoaccidente == parent.getItemAtPosition(1).toString()){
                                loadDataSpinnerFactor();
                                spinnerFactor.setVisibility(View.VISIBLE);
                                btn_ayuda_factor.setVisibility(View.VISIBLE);

                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void loadDataSpinnerGravedad(){
        final List<Gravedad> gravedads = new ArrayList<>();
        mDatabase.child("gravedad_accidente").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        if (ds.hasChild("nombre_gravedad")) {
                            String gravedadName = ds.child("nombre_gravedad").getValue().toString();
                            gravedads.add(new Gravedad(gravedadName));
                        }
                    }
                    ArrayAdapter<Gravedad> arrayAdapter = new ArrayAdapter<Gravedad>(ReportarActivity.this, android.R.layout.simple_dropdown_item_1line, gravedads);
                    spinnerGravedad.setAdapter(arrayAdapter);
                    spinnerGravedad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                            gravedadaccidente = parent.getItemAtPosition(i).toString();
                            if (gravedadaccidente != parent.getItemAtPosition(0).toString()){
                                Toast.makeText(ReportarActivity.this, "Has seleccionado gravedad: " + gravedadaccidente , Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void loadDataSpinnerFactor(){
        final List<Factor> factors = new ArrayList<>();
        mDatabase.child("factor_accidente").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        if (ds.hasChild("nombre_factor")) {
                            String factorName = ds.child("nombre_factor").getValue().toString();
                            factors.add(new Factor(factorName));
                        }
                    }
                    ArrayAdapter<Factor> arrayAdapter = new ArrayAdapter<Factor>(ReportarActivity.this, android.R.layout.simple_dropdown_item_1line, factors);
                    spinnerFactor.setAdapter(arrayAdapter);
                    spinnerFactor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                            factoraccidente = parent.getItemAtPosition(i).toString();
                            if (factoraccidente != parent.getItemAtPosition(0).toString()){
                                Toast.makeText(ReportarActivity.this, "Has seleccionado: " + factoraccidente , Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public  void locationStart() {
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

    public void setLocation(Location loc) {
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    tvDireccion.setText(DirCalle.getAddressLine(0));

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class Localizacion implements LocationListener {
        ReportarActivity reportarActivity;
        public ReportarActivity getMainActivity() {
            return reportarActivity;
        }
        public void setMainActivity(ReportarActivity reportarActivity) {
            this.reportarActivity = reportarActivity;
        }
        @Override
        public void onLocationChanged(Location loc) {
            loc.getLatitude();
            loc.getLongitude();
            String latitud = String.valueOf(loc.getLatitude());
            String longitud = String.valueOf(loc.getLongitude());
            Log.i("latitud", "latitud: "+ latitud);
            Log.i("longitud", "longitud: "+ longitud);

            this.reportarActivity.setLocation(loc);
        }
        @Override
        public void onProviderDisabled(String provider) {
            tvDireccion.setText("GPS Desactivado");
        }
        @Override
        public void onProviderEnabled(String provider) {
            tvDireccion.setText("GPS Activado");
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }

    private void posicionActual(){

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        date = dateFormat.format(Calendar.getInstance().getTime());

        DateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
        time = dateFormat1.format(Calendar.getInstance().getTime());



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(ReportarActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            return;
        }
        mfusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.e("Latitud:", +location.getLatitude()+"Longitud:"+location.getLongitude());
                            Map<String,Object> latlang = new HashMap<>();
                            //Reporte r = new Reporte();
                            //r.setId_reporte(UUID.randomUUID().toString());
                            latlang.put("latitud", location.getLatitude());
                            latlang.put("longitud", location.getLongitude());
                            latlang.put("tipo_accidente", tipoaccidente);
                            latlang.put("gravedad_accidente", gravedadaccidente);
                            latlang.put("factor_accidente", factoraccidente);
                            latlang.put("fecha_accidente", date);
                            latlang.put("hora_accidente", time);
                            latlang.put("direccion_accidente", tvDireccion.getText());
                            latlang.put("contador", contador);
                            mDatabase.child("coordenadas").push().setValue(latlang);
                            //mDatabase.child("coordenadas").child(r.getId_reporte()).setValue(latlang);
                        }
                    }
                }
         );
    }

   private void datosFirestore(){

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        dateFirestore = dateFormat.format(Calendar.getInstance().getTime());

        DateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
        timeFirestore = dateFormat1.format(Calendar.getInstance().getTime());

        Map<String, Object> map = new HashMap<>();
        map.put("tipoaccidente", tipoaccidente );
        map.put("gravedadaccidente", gravedadaccidente);
        map.put("fecha", dateFirestore);
        map.put("hora", timeFirestore);
        map.put("direccion", tvDireccion.getText());

       mFirestore.collection("Reportes").document().set(map);

   }
/*
    private void leerDatoFirestore (){
        DocumentReference report = db.collection("Reportes").document();
        report.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                }
            }
        });
    }*/

    public void onClickReportar(View view) {

        /*
        posicionActual();
        Toast.makeText(getBaseContext(), "Reporte de accidente realizado con éxito.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ReportarActivity.this, MapsActivity.class);
        startActivity(intent);
        */

        if(tipoaccidente.equals(" ")|| gravedadaccidente.equals(" ")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No puede registrar campos vacios, seleccione alguna opción.");
            builder.setTitle("Excepción de Reporte");
            AlertDialog dialog = builder.create();
            dialog.show();

        } else {
            datosFirestore();
            posicionActual();
            Toast.makeText(getBaseContext(), "Reporte de accidente realizado con éxito.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ReportarActivity.this, MapsActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void btn_ayuda_tipo(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Seleccionar CHOQUE: si se presentó un encuentro violento entre dos (2) o más vehículos, ATROPELLO: si un peaton fue impactado por un vehiculo, VOLCAMIENTO: si el vehículo pierde su posición normal durante el accidente, INCENDIO: vehiculos colisionados que estén en llamas");
        builder.setTitle("AYUDA SELECCIÓN DE TIPO (?) ");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void btn_ayuda_gravedad(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Seleccionar CON MUERTOS: Si el accidente presenta muertos o muertos y heridos. CON HERIDOS: Si el accidente presenta heridos, o heridos y daños materiales. SOLO DAÑOS: Si sólo se presentaron daños materiales");
        builder.setTitle("AYUDA SELECCIÓN DE GRAVEDAD (?) ");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void btn_ayuda_factor(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("VEHÍCULO: Si el choque se produjo contra autmovoiles o motociletas, OBJETO FIJO: Objetos fijos en la calle, SEMOVIENTE: Si el choque se produjo contra vacunos en la vía ");
        builder.setTitle("AYUDA SELECCIÓN DE FACTOR (?) ");
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
