package google.maps.colombia.aplicativoreportedeaccidentes;

import android.Manifest;
import android.arch.lifecycle.ReportFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {
    public static final String user="names";



    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }

        //String user = getIntent().getStringExtra("names");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }



    public void pasarDatos(View view) {
        Intent intent = new Intent(MenuActivity.this,MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Saliste de tu sesión de usuario", Toast.LENGTH_SHORT).show();
    }

    public void onCLickConsultar(View view) {
        Intent intent = new Intent(MenuActivity.this,MapsActivity.class);
        startActivity(intent);
    }

    public void pasarEstadisticas(View view) {
        Intent intent = new Intent(MenuActivity.this,Estadisticas.class);
        startActivity(intent);
    }

    public void onClickPasaraReporte(View view) {
        Intent intent = new Intent(MenuActivity.this, ReportarActivity.class);
        MenuActivity.this.startActivity(intent);
    }
}
