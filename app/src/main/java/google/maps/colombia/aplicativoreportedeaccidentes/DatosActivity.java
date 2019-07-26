package google.maps.colombia.aplicativoreportedeaccidentes;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import google.maps.colombia.aplicativoreportedeaccidentes.Adapters.ReportAdapter;
import google.maps.colombia.aplicativoreportedeaccidentes.Models.Factor;
import google.maps.colombia.aplicativoreportedeaccidentes.Models.Reporte;

public class DatosActivity extends AppCompatActivity {

    private RecyclerView rv;
    private DatabaseReference mDatabase;
    private ReportAdapter rAdapter;
    private ArrayList<Reporte> mReportList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);

        rv = findViewById(R.id.recyclerViewDatos);
        rv.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getTipo();
        getGravedad();

    }

    private void getTipo(){
        mDatabase.child("coordenadas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        if (ds.hasChild("tipo_accidente")){
                            String tipo_accidente = ds.child("tipo_accidente").getValue().toString();
                            mReportList.add(new Reporte(tipo_accidente));
                        }
                    }
                    rAdapter = new ReportAdapter(mReportList, R.layout.row_recycler);
                    rv.setAdapter(rAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getGravedad(){
        mDatabase.child("coordenadas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        if (ds.hasChild("gravedad_accidente")){
                            String gravedad_accidente = ds.child("gravedad_accidente").getValue().toString();
                            mReportList.add(new Reporte(gravedad_accidente));
                        }
                    }
                }
                rAdapter = new ReportAdapter(mReportList, R.layout.row_recycler);
                rv.setAdapter(rAdapter);
                Toast.makeText(DatosActivity.this, "Gravedad de accidentes cargada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
