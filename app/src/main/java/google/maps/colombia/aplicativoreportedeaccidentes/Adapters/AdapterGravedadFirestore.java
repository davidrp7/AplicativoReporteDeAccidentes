package google.maps.colombia.aplicativoreportedeaccidentes.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import google.maps.colombia.aplicativoreportedeaccidentes.Models.ReporteFire;
import google.maps.colombia.aplicativoreportedeaccidentes.R;

public class AdapterGravedadFirestore extends FirestoreRecyclerAdapter<ReporteFire, AdapterGravedadFirestore.ViewHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdapterGravedadFirestore(@NonNull FirestoreRecyclerOptions<ReporteFire> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ReporteFire model) {
        holder.textViewGravedad.setText(model.getGravedadaccidente());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_reportes_gravedad, viewGroup, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewGravedad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewGravedad = itemView.findViewById(R.id.txt_view_gravedad);

        }
    }
}
