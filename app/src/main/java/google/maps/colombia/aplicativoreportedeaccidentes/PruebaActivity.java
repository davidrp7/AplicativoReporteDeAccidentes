package google.maps.colombia.aplicativoreportedeaccidentes;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import google.maps.colombia.aplicativoreportedeaccidentes.Adapters.PruebaReporteAdapter;
import google.maps.colombia.aplicativoreportedeaccidentes.Models.PruebaReporte;
import google.maps.colombia.aplicativoreportedeaccidentes.Models.Reporte;

public class PruebaActivity extends AppCompatActivity {

    private DatabaseReference pDatabase;
    private PruebaReporteAdapter pruebaReporteAdapter;
    private RecyclerView recyclerView;
    private ArrayList<PruebaReporte> pruebaReportesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        recyclerView = findViewById(R.id.recyclerViewPrueba);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pDatabase = FirebaseDatabase.getInstance().getReference("prueba_reporte");

        getPruebaReporte();
    }


    private void getPruebaReporte(){
        pDatabase.child("prueba_reporte").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        String migravedad = ds.child("migravedad").getValue().toString();
                        pruebaReportesList.add(new PruebaReporte(migravedad));
                    }
                    pruebaReporteAdapter = new PruebaReporteAdapter(pruebaReportesList, R.layout.prueba_view);
                    recyclerView.setAdapter(pruebaReporteAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
