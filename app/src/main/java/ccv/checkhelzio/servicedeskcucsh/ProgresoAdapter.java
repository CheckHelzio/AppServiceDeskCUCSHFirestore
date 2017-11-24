package ccv.checkhelzio.servicedeskcucsh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static ccv.checkhelzio.servicedeskcucsh.Utils.Datos.getStringStatus;


public class ProgresoAdapter extends FirestoreRecyclerAdapter<Progreso, ProgresoAdapter.IncidenteViewHolder> {

    private final Calendar c = Calendar.getInstance();
    private final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    private final SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    ProgresoAdapter(FirestoreRecyclerOptions<Progreso> options) {
        super(options);
    }


    public interface DataChangeListener{
        void onDataChange(int itemCount);
    }

    @Override
    public IncidenteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.progreso_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new IncidenteViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(IncidenteViewHolder holder, int position, Progreso model) {
        holder.bind(model);
    }

    private String getHora() {
        return sdf.format(c.getTime());
    }

    private String getFecha(long fechayHora) {
        c.setTimeInMillis(fechayHora);
        return sdf2.format(c.getTime());
    }

    class IncidenteViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_progreso, tv_nombre, tv_mensaje, tv_fecha, tv_hora;
        private TextView tv_label_mensaje;

        IncidenteViewHolder(View itemView) {
            super(itemView);
            tv_progreso = itemView.findViewById(R.id.tv_progreso);
            tv_nombre = itemView.findViewById(R.id.tv_nombre);
            tv_mensaje = itemView.findViewById(R.id.tv_mensaje);
            tv_fecha = itemView.findViewById(R.id.tv_fecha);
            tv_hora = itemView.findViewById(R.id.tv_hora);
            tv_label_mensaje = itemView.findViewById(R.id.tv_label_mensaje);
        }

        private void bind(Progreso progreso) {

            if (TextUtils.isEmpty(progreso.getMensaje())){
                tv_mensaje.setVisibility(View.GONE);
                tv_label_mensaje.setVisibility(View.GONE);
            }else {
                tv_mensaje.setVisibility(View.VISIBLE);
                tv_label_mensaje.setVisibility(View.VISIBLE);
            }

            tv_progreso.setText(getStringStatus(progreso.getProgreso()));
            tv_nombre.setText(progreso.getNombre());
            tv_mensaje.setText(progreso.getMensaje());
            tv_fecha.setText(getFecha(progreso.getTimestamp()));
            tv_hora.setText(getHora());
        }
    }
}
