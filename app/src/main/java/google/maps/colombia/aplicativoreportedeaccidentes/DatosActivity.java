package google.maps.colombia.aplicativoreportedeaccidentes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import google.maps.colombia.aplicativoreportedeaccidentes.Adapters.AdapterFirestore;
import google.maps.colombia.aplicativoreportedeaccidentes.Models.ReporteFire;

public class DatosActivity extends AppCompatActivity {

    private RecyclerView rvReportes;
    AdapterFirestore mAdapter;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);

        firebaseFirestore = FirebaseFirestore.getInstance();
        rvReportes = findViewById(R.id.recyclerDatosFirestore);
        rvReportes.setLayoutManager(new LinearLayoutManager(this));
        Query query = firebaseFirestore.collection("Reportes");
        FirestoreRecyclerOptions<ReporteFire> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<ReporteFire>().setQuery(query, ReporteFire.class).build();
        mAdapter = new AdapterFirestore(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        rvReportes.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}
