package google.maps.colombia.aplicativoreportedeaccidentes.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import google.maps.colombia.aplicativoreportedeaccidentes.Models.PruebaReporte;
import google.maps.colombia.aplicativoreportedeaccidentes.R;

public class PruebaReporteAdapter extends RecyclerView.Adapter<PruebaReporteAdapter.ViewHolderPrueba> {

    private int resource;
    private ArrayList<PruebaReporte> pruebaReportes;

    public PruebaReporteAdapter(ArrayList<PruebaReporte> pruebaReportes, int resource){
        this.pruebaReportes = pruebaReportes;
        this.resource = resource;
    }
    @NonNull
    @Override
    public ViewHolderPrueba onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);
        return new ViewHolderPrueba(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPrueba viewHolderPrueba, int i) {
        PruebaReporte pruebaReporte = pruebaReportes.get(i);
        viewHolderPrueba.textViewPrueba.setText(pruebaReporte.getMigravedad());
    }

    @Override
    public int getItemCount() {
        return pruebaReportes.size();
    }

    public class ViewHolderPrueba extends RecyclerView.ViewHolder{

        private TextView textViewPrueba;
        public View view;

        public ViewHolderPrueba(View view){
            super(view);
            this.view = view;
            this.textViewPrueba = (TextView) view.findViewById(R.id.txv_prueba);
        }
    }
}
