package google.maps.colombia.aplicativoreportedeaccidentes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

public class TrazarRutaActivity extends AppCompatActivity {

    EditText txtLatInicio,txtLongInicio,txtLatFinal,txtLongFinal;

    JsonObjectRequest jsonObjectRequest;
    RequestQueue request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trazar_ruta);

    }
}
