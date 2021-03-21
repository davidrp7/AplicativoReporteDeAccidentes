package google.maps.colombia.aplicativoreportedeaccidentes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Map;

public class InvitadoActivity extends AppCompatActivity {

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitado);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void onCLickConsultarInvitado(View view) {
        Intent intent = new Intent(InvitadoActivity.this,MapsActivity.class);
        startActivity(intent);
    }

    public void pasarEstadisticasInvitado(View view) {
        Intent intent = new Intent(InvitadoActivity.this,Estadisticas.class);
        startActivity(intent);
    }

    public void onCLickIniciarSesionInvitado(View view) {
        Intent intent = new Intent(InvitadoActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
