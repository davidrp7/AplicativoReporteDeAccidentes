package google.maps.colombia.aplicativoreportedeaccidentes;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import google.maps.colombia.aplicativoreportedeaccidentes.Adapters.AdapterFirestore;
import google.maps.colombia.aplicativoreportedeaccidentes.Adapters.AdapterGravedadFirestore;
import google.maps.colombia.aplicativoreportedeaccidentes.Adapters.AdapterTipoFirestore;
import google.maps.colombia.aplicativoreportedeaccidentes.Models.Opciones;
import google.maps.colombia.aplicativoreportedeaccidentes.Models.ReporteFire;
import google.maps.colombia.aplicativoreportedeaccidentes.Models.Tabla;
import google.maps.colombia.aplicativoreportedeaccidentes.Models.Tipo;

public class Estadisticas extends AppCompatActivity {

    private ProgressDialog progressDialog;
    DatabaseReference mDatabase;
    FirebaseFirestore db;
    LinearLayout LinearCotenedor;
    LinearLayout linerGravedad;
    LinearLayout linerTipo;
    LinearLayout linerLugar;
    public Spinner spinnerOpciones;
    String opcionesReporte="";
    TextView txt_heridos;
    TextView txt_direccionFirestore;
    TextView txt_posicion_ultima;
    ScrollView scrollViewRecycler, scrollViewRecyckerTipo, scrollViewRecyclerGravedad;

    /********************  cargar datos a recycler  ******************/
    private RecyclerView rvReportes;
    private RecyclerView rvReportesTipo;
    private RecyclerView rvReportesGravedad;
    AdapterFirestore mAdapter;
    AdapterTipoFirestore mAdapterTipo;
    AdapterGravedadFirestore mAdapterGravedad;
    FirebaseFirestore firebaseFirestore;

    /*****************************************************************/

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        txt_heridos = findViewById(R.id.txt_heridos);

        db = FirebaseFirestore.getInstance();

        spinnerOpciones = findViewById(R.id.spinnerReporte);
        LinearCotenedor = findViewById(R.id.LinearCotenedor);
        linerGravedad = findViewById(R.id.linearGravedad);
        linerTipo = findViewById(R.id.linearTipo);
        linerLugar = findViewById(R.id.linearLugar);
        progressDialog = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        txt_direccionFirestore = findViewById(R.id.id_lugar_direc);
        txt_posicion_ultima = findViewById(R.id.id_lugar_ultimo);

        scrollViewRecycler = findViewById(R.id.scroll_RecyclerView);
        scrollViewRecyckerTipo = findViewById(R.id.scroll_Rv_Tipo);
        scrollViewRecyclerGravedad = findViewById(R.id.scroll_RV_Gravedad);
        loadDataSpinner();


        /********************  cargar datos a recycler  ******************/
        firebaseFirestore = FirebaseFirestore.getInstance();
        rvReportes = findViewById(R.id.recyclerDatosFirestore);
        rvReportes.setLayoutManager(new LinearLayoutManager(this));
        Query query = firebaseFirestore.collection("Reportes");
        FirestoreRecyclerOptions<ReporteFire> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<ReporteFire>().setQuery(query, ReporteFire.class).build();
        mAdapter = new AdapterFirestore(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        rvReportes.setAdapter(mAdapter);


        rvReportesTipo= findViewById(R.id.recyclerTipoFirestore);
        rvReportesTipo.setLayoutManager(new LinearLayoutManager(this));
        Query queryTipo = firebaseFirestore.collection("Reportes");
        FirestoreRecyclerOptions<ReporteFire> firestoreRecyclerOptionsTipo = new FirestoreRecyclerOptions.Builder<ReporteFire>().setQuery(queryTipo, ReporteFire.class).build();
        mAdapterTipo = new AdapterTipoFirestore(firestoreRecyclerOptionsTipo);
        mAdapterTipo.notifyDataSetChanged();
        rvReportesTipo.setAdapter(mAdapterTipo);


        rvReportesGravedad = findViewById(R.id.recyclerGravedadFirestore);
        rvReportesGravedad.setLayoutManager(new LinearLayoutManager(this));
        Query queryGravedad = firebaseFirestore.collection("Reportes");
        FirestoreRecyclerOptions<ReporteFire> firestoreRecyclerOptionsGravedad = new FirestoreRecyclerOptions.Builder<ReporteFire>().setQuery(queryGravedad, ReporteFire.class).build();
        mAdapterGravedad = new AdapterGravedadFirestore(firestoreRecyclerOptionsGravedad);
        rvReportesGravedad.setAdapter(mAdapterGravedad);
        /*****************************************************************/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void leerDatoFirestore (){
        DocumentReference report = db.collection("Reportes").document("zL5LVjTlpwUt73z6jZu9");
        report.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    StringBuilder data = new StringBuilder("");
                    data.append(doc.getString("direccion"));
                    txt_direccionFirestore.setText(data.toString());
                }
            }
        });
    }

    public void loadDataSpinner(){

        final List<Opciones> opciones = new ArrayList<>();
        mDatabase.child("opciones").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        if (ds.hasChild("nombre_opcion")) {
                            String opcionName = ds.child("nombre_opcion").getValue().toString();
                            opciones.add(new Opciones(opcionName));
                        }
                    }
                    ArrayAdapter<Opciones> arrayAdapter = new ArrayAdapter<Opciones>(Estadisticas.this, android.R.layout.simple_dropdown_item_1line, opciones);
                    spinnerOpciones.setAdapter(arrayAdapter);
                    spinnerOpciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                            opcionesReporte = parent.getItemAtPosition(i).toString();
                            if (opcionesReporte != parent.getItemAtPosition(0).toString()){
                                Toast.makeText(Estadisticas.this, "Has seleccionado: " + opcionesReporte , Toast.LENGTH_SHORT).show();
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

    public void onClickCargarOpcion(View view) {

        if (opcionesReporte.equals("GRAVEDAD")){
            progressDialog.setMessage("Cargando datos");
            progressDialog.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    linerGravedad.setVisibility(View.VISIBLE);
                    linerLugar.setVisibility(View.GONE);
                    linerTipo.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }

            }, 3000);

        } else  if (opcionesReporte.equals("TIPO")){

            progressDialog.setMessage("Cargando datos");
            progressDialog.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    linerTipo.setVisibility(View.VISIBLE);
                    linerGravedad.setVisibility(View.GONE);
                    linerLugar.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }

            }, 3000);

        } else  if (opcionesReporte.equals("LUGAR")){

            leerDatoFirestore();
            txt_posicion_ultima.setVisibility(View.VISIBLE);
            progressDialog.setMessage("Cargando datos");
            progressDialog.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    linerLugar.setVisibility(View.VISIBLE);
                    linerGravedad.setVisibility(View.GONE);
                    linerTipo.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }

            }, 3000);

        } else  if (opcionesReporte.equals("VER TODO")){
            progressDialog.setMessage("Cargando datos");
            progressDialog.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    linerLugar.setVisibility(View.VISIBLE);
                    linerGravedad.setVisibility(View.VISIBLE);
                    linerTipo.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }

            }, 5000);

        }
        else if (opcionesReporte.equals(" ")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Debe seleccionar alguna opción para generar estadísticas");
            builder.setTitle("(?)");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


    /********************  cargar datos a recycler  ******************/
    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
        mAdapterTipo.startListening();
        mAdapterGravedad.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
        mAdapterTipo.stopListening();
        mAdapterGravedad.stopListening();

    }
    /*****************************************************************/

    public void onclicCargarVista(View view) {

        if (opcionesReporte.equals("GRAVEDAD")){
            progressDialog.setMessage("Cargando datos");
            progressDialog.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollViewRecyclerGravedad.setVisibility(View.VISIBLE);
                    scrollViewRecycler.setVisibility(View.GONE);
                    scrollViewRecyckerTipo.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }

            }, 3000);

        } else  if (opcionesReporte.equals("TIPO")){

            progressDialog.setMessage("Cargando datos");
            progressDialog.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollViewRecyckerTipo.setVisibility(View.VISIBLE);
                    scrollViewRecycler.setVisibility(View.GONE);
                    scrollViewRecyclerGravedad.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }

            }, 3000);

        } else  if (opcionesReporte.equals("LUGAR")){
            progressDialog.setMessage("Cargando datos");
            progressDialog.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollViewRecycler.setVisibility(View.VISIBLE);
                    scrollViewRecyckerTipo.setVisibility(View.GONE);
                    scrollViewRecyclerGravedad.setVisibility(View.GONE);

                    progressDialog.dismiss();
                }

            }, 3000);

        } else  if (opcionesReporte.equals("VER TODO")){
            progressDialog.setMessage("Cargando datos");
            progressDialog.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollViewRecyclerGravedad.setVisibility(View.VISIBLE);
                    scrollViewRecycler.setVisibility(View.VISIBLE);
                    scrollViewRecyckerTipo.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }

            }, 4500);

        }
        else if (opcionesReporte.equals(" ")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Debe seleccionar alguna opción para generar estadísticas");
            builder.setTitle("ERROR DE CONSULTA");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
