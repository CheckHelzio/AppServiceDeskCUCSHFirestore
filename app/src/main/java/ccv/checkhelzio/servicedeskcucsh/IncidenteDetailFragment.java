package ccv.checkhelzio.servicedeskcucsh;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static ccv.checkhelzio.servicedeskcucsh.Utils.Datos.getFondoPrioridad;


public class IncidenteDetailFragment extends Fragment {

    private static final String TAG = IncidenteDetailFragment.class.getSimpleName();
    private static final int EDITAR = 2;
    private IncidenteBasico incidente;
    private Cliente mCliente;
    private TextView tv_folio, tv_tecnico, tvNombre, tvDependencia, tvUbicacion, tvTelefono, tvCorreo, tvTipoIncidente;
    private TextView tvDescripcion;

    private View view;
    private ImageView iv_eliminar, iv_editar;
    private Animation fadeIn;
    private TextView tvMarcaAgua;
    private DocumentReference mClienteDocumentReference;
    private DocumentReference mIncidenteBasicoReference;
    private ListenerRegistration clienteListenerRegistration;
    private NestedScrollView nestedScrollView;
    private final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat formatHora = new SimpleDateFormat("h:mm a", Locale.getDefault());
    private final Calendar calendar = Calendar.getInstance();
    private ListenerRegistration incidenteBasicoListenerRegistration;
    private OnEliminarIncidenteListener onEliminarIncidenteListener;
    private RecyclerView recyclerView_progreso;
    private Query queryProgreso;
    private ProgresoAdapter adapter;

    public IncidenteDetailFragment() {
    }

    // Container Activity must implement this interface
    public interface OnEliminarIncidenteListener {
        void onEliminarIncidente();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Traemos el incidente seleccionado
        Bundle bundle = getArguments();
        incidente = bundle.getParcelable("INCIDENTE");
        assert incidente != null;

        onEliminarIncidenteListener = (OnEliminarIncidenteListener) getActivity();

        // Con el incidente creamos las rutas para obtener el resto de la información
        String st_ruta_incidente = "incidentes/incidente" + incidente.getFolio();
        mIncidenteBasicoReference = FirebaseFirestore.getInstance().document(st_ruta_incidente);
        mClienteDocumentReference = FirebaseFirestore.getInstance().document("clientes/cliente" + incidente.getCodigoCliente());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detalle_incidente, container, false);
        iniciarObjetos();
        llenarDatos();
        return view;
    }

    private void iniciarObjetos() {
        iv_eliminar = view.findViewById(R.id.iv_eliminar);
        iv_editar = view.findViewById(R.id.iv_editar);
        tv_folio = view.findViewById(R.id.tv_folio);
        tv_tecnico = view.findViewById(R.id.tv_tecnico);
        tvNombre = view.findViewById(R.id.tv_nombre);
        tvDependencia = view.findViewById(R.id.tv_dependencia);
        tvUbicacion = view.findViewById(R.id.tv_ubicacion);
        tvTelefono = view.findViewById(R.id.tv_telefono);
        tvCorreo = view.findViewById(R.id.tv_correo);
        tvTipoIncidente = view.findViewById(R.id.tv_incidente);
        tvDescripcion = view.findViewById(R.id.tv_descripcion);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        tvMarcaAgua = view.findViewById(R.id.tv_marca_agua);
        recyclerView_progreso = view.findViewById(R.id.rv_progreso);

        fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                nestedScrollView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        iv_eliminar.setOnClickListener(view1 -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setMessage("\n" + "¿Deseas eliminar este incidente?");
            alertDialogBuilder.setPositiveButton("ACEPTAR",
                    (arg0, arg1) -> FirebaseFirestore.getInstance().collection("incidentes").document("incidente" + incidente.getFolio())
                            .delete()
                            .addOnSuccessListener(aVoid -> onEliminarIncidenteListener.onEliminarIncidente())
                            .addOnFailureListener(e -> Toast.makeText(getActivity(), "Ha ocurrido un problema al intentar eliminar este incidente... Intentalo nuevamente", Toast.LENGTH_SHORT).show()));
            alertDialogBuilder.setNegativeButton("CANCELAR",
                    null);

            alertDialogBuilder.create().show();
        });

        iv_editar.setOnClickListener(view1 -> {
            Intent i = new Intent(getActivity(), FormularioTabletActivity.class);
            i.putExtra("INCIDENTE", incidente);
            startActivityForResult(i, EDITAR);
        });

        iniciarRecyclerView();
    }

    private void iniciarRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView_progreso.setLayoutManager(layoutManager);
        queryProgreso = FirebaseFirestore.getInstance().collection("incidentes").document("incidente" + incidente.getFolio()).collection("progreso")
                .orderBy("timestamp", Query.Direction.ASCENDING);


        // Opciones para el adaptador de FirebaseUI
        FirestoreRecyclerOptions<Progreso> options = new FirestoreRecyclerOptions.Builder<Progreso>()
                .setQuery(queryProgreso, Progreso.class)
                .build();

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(getActivity(),
                layoutManager.getOrientation());
        mDividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divisor, null));
        recyclerView_progreso.addItemDecoration(mDividerItemDecoration);


        adapter = new ProgresoAdapter(options);
        recyclerView_progreso.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: iniciar listeners");

        adapter.startListening();

        incidenteBasicoListenerRegistration = mIncidenteBasicoReference.addSnapshotListener((documentSnapshot, e) -> {
            comprobarError(e);
            if (documentSnapshot.exists()) {
                incidente = documentSnapshot.toObject(IncidenteBasico.class);
                llenarDatos();
            }
        });

        clienteListenerRegistration = mClienteDocumentReference.addSnapshotListener((documentSnapshot, e) -> {
            comprobarError(e);
            if (documentSnapshot.exists()) {
                Log.d(TAG, "onStart: document exist");
                mCliente = documentSnapshot.toObject(Cliente.class);
                tvNombre.setText(mCliente.getNombreDelCliente());
                tvDependencia.setText(mCliente.getDependenciaDelCliente());
                tvUbicacion.setText(mCliente.getUbicacionDelCliente());
                tvTelefono.setText(mCliente.getTelefonoDelCliente());
                tvCorreo.setText(mCliente.getCorreoElectronicoDelCliente());
                nestedScrollView.startAnimation(fadeIn);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.stopListening();
        Log.d(TAG, "onPause: remove listeners");
        clienteListenerRegistration.remove();
        incidenteBasicoListenerRegistration.remove();
    }

    private void llenarDatos() {
        String folio = "Folio " + String.format(Locale.getDefault(), "%03d", incidente.getFolio());
        String tecnico = "Técnico: " + incidente.getTecnico();
        tv_folio.setText(folio);
        tv_tecnico.setText(tecnico);
        String marca_agua;
        calendar.setTimeInMillis(incidente.getFecha());
        if (incidente.getQuienRegistro() != null) {
            marca_agua = "Registrado por: " + incidente.getQuienRegistro() + " - " + format.format(calendar.getTime()) + " a las " + formatHora.format(calendar.getTime());
        } else {
            marca_agua = "Registrado el día " + format.format(calendar.getTime()) + " a las " + formatHora.format(calendar.getTime());
        }
        tvMarcaAgua.setText(marca_agua);
        view.findViewById(R.id.appbar_detalle).setBackgroundColor(Color.parseColor(getFondoPrioridad(incidente.getPrioridad())));
        if (!TextUtils.isEmpty(incidente.getDescripcionDelReporte())) {
            tvDescripcion.setText(incidente.getDescripcionDelReporte());
            tvTipoIncidente.setText(incidente.getTipoDeIncidente());
        }
    }

    private void comprobarError(FirebaseFirestoreException e) {
        if (e != null) {
            Toast.makeText(getActivity(), "A ocurrido un problema con la conexión. Intentalo nuevamente.", Toast.LENGTH_SHORT).show();
        }
    }

}
