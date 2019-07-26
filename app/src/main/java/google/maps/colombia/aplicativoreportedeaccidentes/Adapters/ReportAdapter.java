package google.maps.colombia.aplicativoreportedeaccidentes.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import google.maps.colombia.aplicativoreportedeaccidentes.Models.Reporte;
import google.maps.colombia.aplicativoreportedeaccidentes.R;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    private int resource;
    private ArrayList<Reporte> reportesList;

    public ReportAdapter(ArrayList<Reporte> reportesList, int resource) {
        this.reportesList = reportesList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {
        Reporte reporte = reportesList.get(index);
        viewHolder.textViewGravedad.setText(reporte.getGravedad_accidente());
        viewHolder.textViewTipo.setText(reporte.getTipo_accidente());
    }

    @Override
    public int getItemCount() {
        return reportesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewGravedad, textViewTipo;
        public View view;

        public ViewHolder(View view) {
            super(view);

            this.view = view;
            this.textViewGravedad = view.findViewById(R.id.txt_gravedad_reporte);
            this.textViewTipo = view.findViewById(R.id.txt_tipo_reporte);
        }
    }
}