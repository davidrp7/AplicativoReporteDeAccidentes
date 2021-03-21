package google.maps.colombia.aplicativoreportedeaccidentes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import google.maps.colombia.aplicativoreportedeaccidentes.R;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;



    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void rendowWindowText(Marker marker, View view){
        String title = marker.getTitle();
        TextView tvTtile = view.findViewById(R.id.txt_gravedad_ventana);

        final TextView contador = view.findViewById(R.id.txt_contador_venta);

        if (!title.equals("")){
            tvTtile.setText(title);
        }

        String snippet = marker.getSnippet();
        TextView tvSnippet = view.findViewById(R.id.txt_snippet_ventana);

        if (!title.equals("")){
            tvSnippet.setText(snippet);
        }

        Button btn_reportec = view.findViewById(R.id.btn_contar_reporte);
        btn_reportec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contador.setText("1");

            }
        });

    }



    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }
}
