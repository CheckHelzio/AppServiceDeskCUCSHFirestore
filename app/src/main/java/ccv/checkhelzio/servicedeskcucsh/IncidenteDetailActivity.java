package ccv.checkhelzio.servicedeskcucsh;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static ccv.checkhelzio.servicedeskcucsh.Utils.Datos.getFondoPrioridad;

public class IncidenteDetailActivity extends AppCompatActivity {

    private IncidenteBasico incidente;
    private Cliente mCliente;
    private TextView tv_folio, tv_tecnico, tvNombre, tvDependencia, tvUbicacion, tvTelefono, tvCorreo, tvTipoIncidente;
    private TextView tvDescripcion;

    private Animation fadeIn;
    private TextView tvMarcaAgua;
    private DocumentReference mClienteDocumentReference;
    private ListenerRegistration clienteListenerRegistration;
    private NestedScrollView nestedScrollView;

    private final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
    private final SimpleDateFormat formatHora = new SimpleDateFormat("h:mm a", Locale.getDefault());
    private final Calendar calendar = Calendar.getInstance();
    private DocumentReference mIncidenteBasicoReference;
    private ListenerRegistration incidenteBasicoListenerRegistration;
    private RecyclerView recyclerView_progreso;
    private Query queryProgreso;
    private ProgresoAdapter adapter;
    private FloatingActionButton fab, fab_mas;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean tomadoPorMi = false;
    private int nivel_progreso;
    private DocumentReference doc_nuevo_progreso;


    public IncidenteDetailActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_incidente);

        // Traemos el incidente seleccionado
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        incidente = bundle.getParcelable("INCIDENTE");
        assert incidente != null;

        // Con el incidente creamos las rutas para obtener el resto de la información
        String st_ruta_incidente = "incidentes/incidente" + incidente.getFolio();
        mClienteDocumentReference = FirebaseFirestore.getInstance().document("/clientes/cliente" + incidente.getCodigoCliente());
        mIncidenteBasicoReference = FirebaseFirestore.getInstance().document(st_ruta_incidente);
        iniciarObjetos();
        llenarDatos();

        iniciarRecyclerView();

        if (IncidenteListActivity.nombreUsuario.equals(incidente.getTecnico())) {
            fab.hide();
            tomadoPorMi = true;
            fab_mas.show();
        }
    }

    private void iniciarRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView_progreso.setLayoutManager(layoutManager);
        queryProgreso = FirebaseFirestore.getInstance().collection("incidentes").document("incidente" + incidente.getFolio()).collection("progreso")
                .orderBy("timestamp", Query.Direction.ASCENDING);


        // Opciones para el adaptador de FirebaseUI
        FirestoreRecyclerOptions<Progreso> options = new FirestoreRecyclerOptions.Builder<Progreso>()
                .setQuery(queryProgreso, Progreso.class)
                .build();

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(this,
                layoutManager.getOrientation());
        mDividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divisor, null));
        recyclerView_progreso.addItemDecoration(mDividerItemDecoration);


        adapter = new ProgresoAdapter(options);
        recyclerView_progreso.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();

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
                mCliente = documentSnapshot.toObject(Cliente.class);
                tvNombre.setText(mCliente.getNombreDelCliente());
                tvDependencia.setText(mCliente.getDependenciaDelCliente());
                tvUbicacion.setText(mCliente.getUbicacionDelCliente());
                tvTelefono.setText(mCliente.getTelefonoDelCliente());
                tvCorreo.setText(mCliente.getCorreoElectronicoDelCliente());
                if (nestedScrollView.getVisibility() != View.VISIBLE) {
                    nestedScrollView.startAnimation(fadeIn);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.stopListening();
        clienteListenerRegistration.remove();
        incidenteBasicoListenerRegistration.remove();
    }

    private void iniciarObjetos() {
        recyclerView_progreso = findViewById(R.id.rv_progreso);
        tv_folio = findViewById(R.id.tv_folio);
        tv_tecnico = findViewById(R.id.tv_tecnico);
        tvNombre = findViewById(R.id.tv_nombre);
        tvDependencia = findViewById(R.id.tv_dependencia);
        tvUbicacion = findViewById(R.id.tv_ubicacion);
        tvTelefono = findViewById(R.id.tv_telefono);
        tvCorreo = findViewById(R.id.tv_correo);
        tvTipoIncidente = findViewById(R.id.tv_incidente);
        tvDescripcion = findViewById(R.id.tv_descripcion);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        tvMarcaAgua = findViewById(R.id.tv_marca_agua);
        fab = findViewById(R.id.fab);
        fab_mas = findViewById(R.id.fab_mas);

        fab.setOnClickListener(view -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("\n" + "¿Deseas encargarte de la solución de este incidente?");
            alertDialogBuilder.setPositiveButton("ACEPTAR", (dialogInterface, i) -> {
                // Get a new write batch
                WriteBatch batch = db.batch();

                // Guardando incidente basico
                String nombre_incidente = "incidente" + incidente.getFolio();
                DocumentReference doc_incidente_basico = FirebaseFirestore.getInstance().collection("incidentes").document(nombre_incidente);
                Map<String, Object> updateValues = new HashMap<>();
                updateValues.put("estatus", 1);
                updateValues.put("tecnico", IncidenteListActivity.nombreUsuario);
                batch.update(doc_incidente_basico, updateValues);

                // Guardando nuevo progreso
                doc_nuevo_progreso =
                        FirebaseFirestore.getInstance().collection("incidentes").document(nombre_incidente)
                                .collection("progreso").document();
                batch.set(doc_nuevo_progreso,
                        new Progreso(Calendar.getInstance().getTimeInMillis(),
                                1,
                                IncidenteListActivity.nombreUsuario,
                                null
                        ));

                batch.commit().addOnCompleteListener(task -> {
                    fab.hide();
                    tomadoPorMi = true;
                    new Handler().postDelayed(() -> fab_mas.show(), 500);
                });

            });
            alertDialogBuilder.setNegativeButton("CANCELAR",
                    null);

            alertDialogBuilder.create().show();
        });

        fab_mas.setOnClickListener(view -> {
            mostrarDialog();
        });

        recyclerView_progreso.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                nestedScrollView.scrollTo(0, 0);
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {

            }
        });
        fadeIn = AnimationUtils.loadAnimation(IncidenteDetailActivity.this, R.anim.fade_in);

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

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                if (!tomadoPorMi) {
                    fab.hide();
                } else {
                    fab_mas.hide();
                }
            } else {
                if (!tomadoPorMi) {
                    fab.show();
                } else {
                    fab_mas.show();
                }
            }
        });

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
        findViewById(R.id.appbar_detalle).setBackgroundColor(Color.parseColor(getFondoPrioridad(incidente.getPrioridad())));
        if (!TextUtils.isEmpty(incidente.getDescripcionDelReporte())) {
            tvDescripcion.setText(incidente.getDescripcionDelReporte());
            tvTipoIncidente.setText(incidente.getTipoDeIncidente());
        }
    }

    private void comprobarError(FirebaseFirestoreException e) {
        if (e != null) {
            Toast.makeText(this, "A ocurrido un problema con la conexión. Intentalo nuevamente.", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        View inflateView = getLayoutInflater().inflate(R.layout.layout_agregar_estatus, null);
        final TextInputLayout input_descripcion = inflateView.findViewById(R.id.input_descripcion_del_problema);
        final EditText et_descripcion = inflateView.findViewById(R.id.et_descripcion_del_problema);
        RadioButton radioEnProgreso = inflateView.findViewById(R.id.radio_en_progreso);
        RadioButton radioEnEspera = inflateView.findViewById(R.id.radio_en_espera);
        RadioButton radioFinalizado = inflateView.findViewById(R.id.radio_finalizado);

        nivel_progreso = 2;
        radioEnProgreso.setOnClickListener(view -> {
            input_descripcion.setHint("Comentario:");
            nivel_progreso = 2;
        });

        radioEnEspera.setOnClickListener(view -> {
            input_descripcion.setHint("¿Por qué?:");
            nivel_progreso = 3;
        });

        radioFinalizado.setOnClickListener(view -> {
            input_descripcion.setHint("¿Cómo te fue?:");
            nivel_progreso = 4;
        });

        final boolean[] clickeado = {false};

        alertDialogBuilder.setTitle("Agregar progreso");
        alertDialogBuilder.setPositiveButton("ACEPTAR",
                (arg0, arg1) -> {
                    if (!clickeado[0]) {
                        clickeado[0] = true;
                        Progreso progreso = new Progreso();
                        progreso.setMensaje(et_descripcion.getText().toString());
                        progreso.setProgreso(nivel_progreso);
                        progreso.setQuien(IncidenteListActivity.nombreUsuario);
                        progreso.setTimestamp(Calendar.getInstance().getTimeInMillis());

                        WriteBatch batch = db.batch();

                        // Guardando incidente basico
                        String nombre_incidente = "incidente" + incidente.getFolio();
                        DocumentReference doc_incidente_basico = FirebaseFirestore.getInstance().collection("incidentes").document(nombre_incidente);
                        Map<String, Object> updateValues = new HashMap<>();
                        updateValues.put("estatus", nivel_progreso);
                        updateValues.put("tecnico", IncidenteListActivity.nombreUsuario);
                        batch.update(doc_incidente_basico, updateValues);

                        // Guardando nuevo progreso
                        doc_nuevo_progreso =
                                FirebaseFirestore.getInstance().collection("incidentes").document(nombre_incidente)
                                        .collection("progreso").document();
                        batch.set(doc_nuevo_progreso,
                                new Progreso(Calendar.getInstance().getTimeInMillis(),
                                        nivel_progreso,
                                        IncidenteListActivity.nombreUsuario,
                                        et_descripcion.getText().toString().trim()
                                ));

                        batch.commit().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(IncidenteDetailActivity.this, "El progreso del incidente ha sido agregado exitosamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(IncidenteDetailActivity.this, "Ha ocurrido un problema... intentalo nuevamente", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                });
        alertDialogBuilder.setNegativeButton("CANCELAR",
                null);

        alertDialogBuilder.setView(inflateView);
        alertDialogBuilder.setCancelable(false);
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setEnabled(false);
        input_descripcion.setError("Introduce un comentario para continuar");
        et_descripcion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    // Disable ok button
                    dialog.getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    // Something into edit text. Enable the button.
                    dialog.getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });


    }
}
