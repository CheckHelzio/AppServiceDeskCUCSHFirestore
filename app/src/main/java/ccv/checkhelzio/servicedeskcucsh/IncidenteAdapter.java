package ccv.checkhelzio.servicedeskcucsh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static ccv.checkhelzio.servicedeskcucsh.Utils.Datos.getStringStatus;


public class IncidenteAdapter extends FirestoreRecyclerAdapter<IncidenteBasico, IncidenteAdapter.IncidenteViewHolder> {

    public static int selected_position = 0;
    private final Calendar c = Calendar.getInstance();
    final private ListItemClickListener mOnClcikListener;
    final private DataChangeListener mOnDataChangeListener;
    final private ChildChangeListener mOnChildChangeListener;
    private final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    private final SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, View view);
    }

    public interface DataChangeListener{
        void onDataChange(int itemCount);
    }

    public interface ChildChangeListener{
        void onChildChange(ChangeEventType itemCount);
    }

    IncidenteAdapter(FirestoreRecyclerOptions<IncidenteBasico> options, ListItemClickListener listItemClickListener,
                     DataChangeListener dataChangeListener,
                     ChildChangeListener childChangeListener) {
        super(options);
        mOnClcikListener = listItemClickListener;
        mOnDataChangeListener = dataChangeListener;
        mOnChildChangeListener = childChangeListener;
    }

    @Override
    public IncidenteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.incidente_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new IncidenteViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(IncidenteViewHolder holder, int position, IncidenteBasico model) {
        holder.itemView.setPressed(selected_position == position);
        holder.bind(model);
    }

    private String getHora() {
        return sdf.format(c.getTime());
    }

    private int getFondoPrioridad(int prioridadDelServicio) {
        switch (prioridadDelServicio) {
            case 1:
                return R.drawable.prioridad_baja;
            case 2:
                return R.drawable.prioridad_media;
            case 3:
                return R.drawable.prioridad_alta;
        }
        return 0;
    }

    private String getFecha(long fechayHora) {
        c.setTimeInMillis(fechayHora);
        return sdf2.format(c.getTime());
    }

    class IncidenteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_folio, tv_asignado, tv_estatus, tv_fecha, tv_hora;
        private View tv_prioridad;

        IncidenteViewHolder(View itemView) {
            super(itemView);
            tv_prioridad = itemView.findViewById(R.id.tv_prioridad);
            tv_folio = itemView.findViewById(R.id.tv_folio);
            tv_asignado = itemView.findViewById(R.id.tv_asignado);
            tv_estatus = itemView.findViewById(R.id.tv_estatus);
            tv_fecha = itemView.findViewById(R.id.tv_fecha);
            tv_hora = itemView.findViewById(R.id.tv_hora);
            itemView.setOnClickListener(this);
        }

        private void bind(IncidenteBasico incidente) {
            tv_prioridad.setBackgroundResource(getFondoPrioridad(incidente.getPrioridad()));
            tv_folio.setText(String.valueOf(incidente.getFolio()));
            tv_asignado.setText(incidente.getTecnico());
            tv_estatus.setText(getStringStatus(incidente.getEstatus()));
            tv_fecha.setText(getFecha(incidente.getFecha()));
            tv_hora.setText(getHora());
        }

        @Override
        public void onClick(View view) {
           mOnClcikListener.onListItemClick(getAdapterPosition(), view);
        }

    }

    @Override
    public void onChildChanged(ChangeEventType type, DocumentSnapshot snapshot, int newIndex, int oldIndex) {
        super.onChildChanged(type, snapshot, newIndex, oldIndex);
        mOnChildChangeListener.onChildChange(type);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        mOnDataChangeListener.onDataChange(getItemCount());
    }
}
