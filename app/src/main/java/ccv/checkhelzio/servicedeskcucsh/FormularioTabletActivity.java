package ccv.checkhelzio.servicedeskcucsh;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class FormularioTabletActivity extends AppCompatActivity {

    private static final int TIPO_INCIDENTE = 1;
    private static final String TAG = FormularioTabletActivity.class.getSimpleName();
    private EditText et_codigo;
    private AutoCompleteTextView et_nombre;
    private EditText et_dependencia;
    private EditText et_ubicacion;
    private EditText et_telefono;
    private EditText et_correo;
    private TextInputLayout input_codigo;
    private TextInputLayout input_nombre;
    private TextInputLayout input_dependencia;
    private TextInputLayout input_ubicacion;
    private TextInputLayout input_telefono;
    private TextInputLayout input_correo;

    private Button bt_guardar;
    private RadioButton radio_baja;
    private RadioButton radio_media;
    private RadioButton radio_alta;
    private TextInputLayout input_descripcion_del_problema;
    private EditText et_descripcion_del_problema;
    private LinearLayout ly_tipo_problema;
    private TextView tv_tipo_problema;
    private long nuevo_id;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Cliente> listaClientes = new ArrayList<>();
    private ArrayList<String> nombreClientes = new ArrayList<>();
    private View error_tipo_incidente;
    private boolean isEditMode = false;
    private IncidenteBasico incidenteEditar;
    private LinearLayout super_conte;
    private Animation fadeIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        iniciarObjetos();
    }

    @Override
    protected void onStart() {
        super.onStart();
        db.collection("clientes").get().addOnSuccessListener(documentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                Cliente c = documentSnapshot.toObject(Cliente.class);
                listaClientes.add(c);
                nombreClientes.add(c.getNombreDelCliente());
            }

            //Creating the instance of ArrayAdapter containing list of fruit names
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (this, android.R.layout.select_dialog_item, nombreClientes);

            et_nombre.setThreshold(2);//will start working from first character
            et_nombre.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
            et_nombre.setOnItemClickListener((adapterView, view, i, l) -> {
                for (Cliente c : listaClientes) {
                    if (et_nombre.getText().toString().equals(c.getNombreDelCliente())) {
                        et_codigo.setText(c.getCodigoDelCliente());
                        et_dependencia.setText(c.getDependenciaDelCliente());
                        et_ubicacion.setText(c.getUbicacionDelCliente());
                        et_telefono.setText(c.getTelefonoDelCliente());
                        et_correo.setText(c.getCorreoElectronicoDelCliente());
                    }
                }
            });

            if (incidenteEditar != null) {
                et_codigo.setText(incidenteEditar.getCodigoCliente());
                setPrioridad(incidenteEditar.getPrioridad());
                tv_tipo_problema.setText(incidenteEditar.getTipoDeIncidente());
                et_descripcion_del_problema.setText(incidenteEditar.getDescripcionDelReporte());
                isEditMode = true;
                super_conte.startAnimation(fadeIn);
            }

        });

    }

    private void setPrioridad(int prioridad) {
        switch (prioridad) {
            case 1:
                radio_baja.setChecked(true);
                break;
            case 2:
                radio_media.setChecked(true);
                break;
            case 3:
                radio_alta.setChecked(true);
                break;
        }
    }

    private void iniciarObjetos() {

        incidenteEditar = getIntent().getParcelableExtra("INCIDENTE");

        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation){
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                super_conte.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        // INICIAR OBJETOS
        et_codigo = findViewById(R.id.et_codigo);
        et_codigo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        error_tipo_incidente = findViewById(R.id.view_tipo_incidente_error);
        et_nombre = findViewById(R.id.et_nombre);
        et_dependencia = findViewById(R.id.et_dependencia);
        et_ubicacion = findViewById(R.id.et_ubicacion);
        et_telefono = findViewById(R.id.et_telefono);
        et_correo = findViewById(R.id.et_correo);

        input_codigo = findViewById(R.id.input_codigo);
        input_nombre = findViewById(R.id.input_nombre);
        input_dependencia = findViewById(R.id.input_dependencia);
        input_ubicacion = findViewById(R.id.input_ubicacion);
        input_telefono = findViewById(R.id.input_telefono);
        input_correo = findViewById(R.id.input_correo);

        bt_guardar = findViewById(R.id.bt_guardar_reporte);
        bt_guardar.setOnClickListener((View v) -> {
            if (validarItems()) {

                if (!isEditMode) {
                    Toast.makeText(this, "Registrando incidente...", Toast.LENGTH_SHORT).show();
                    bt_guardar.setEnabled(false);
                    final DocumentReference doc_ultimo_id = FirebaseFirestore.getInstance().document("ultimo_id/id");

                    FirebaseFirestore.getInstance().runTransaction((Transaction.Function<Void>) transaction -> {
                        DocumentSnapshot snapshot = transaction.get(doc_ultimo_id);
                        nuevo_id = snapshot.getLong("id") + 1;
                        transaction.update(doc_ultimo_id, "id", nuevo_id);

                        // Success
                        return null;
                    }).addOnSuccessListener(aVoid -> {
                        final Calendar c = Calendar.getInstance();
                        IncidenteBasico incidenteBasico = new IncidenteBasico(
                                getAreas(tv_tipo_problema.getText().toString()),
                                (int) nuevo_id,
                                getPrioridad(),
                                0, // Estatus 0 - Sin asignar
                                "No asignado", // Nombre del técnico
                                c.getTimeInMillis(),
                                IncidenteListActivity.nombreUsuario,
                                tv_tipo_problema.getText().toString(),
                                et_descripcion_del_problema.getText().toString(),
                                et_codigo.getText().toString()

                        );

                        Progreso progreso = new Progreso(
                                c.getTimeInMillis(),
                                0,
                                IncidenteListActivity.nombreUsuario,
                                null
                        );

                        Cliente cliente = new Cliente(
                                TextUtils.isEmpty(et_codigo.getText().toString().trim()) ? "No proporcionado" : et_codigo.getText().toString().trim(),
                                et_nombre.getText().toString().trim(),
                                et_dependencia.getText().toString().trim(),
                                et_ubicacion.getText().toString().trim(),
                                et_telefono.getText().toString().trim(),
                                et_correo.getText().toString().trim()
                        );

                        // Get a new write batch
                        WriteBatch batch = db.batch();

                        // Guardando incidente basico
                        String nombre_incidente = "incidente" + nuevo_id;
                        DocumentReference doc_incidente_basico = FirebaseFirestore.getInstance().collection("incidentes").document(nombre_incidente);
                        batch.set(doc_incidente_basico, incidenteBasico);

                        // Guardando los datos del cliente
                        DocumentReference doc_cliente = FirebaseFirestore.getInstance().collection("clientes").document("cliente" + cliente.getCodigoDelCliente());
                        batch.set(doc_cliente, cliente);

                        // Guardando incidente basico
                        String nombre_incidente_cliente = "incidente" + nuevo_id;
                        DocumentReference doc_incidente_cliente = FirebaseFirestore.getInstance().collection("clientes").document("cliente" + cliente.getCodigoDelCliente() + "/incidentes/" + nombre_incidente_cliente);
                        batch.set(doc_incidente_cliente, incidenteBasico);

                        // Guardando incidente basico
                        String progreso_incidente = "incidente" + nuevo_id + "/progreso";
                        DocumentReference doc_progreso = FirebaseFirestore.getInstance().collection("incidentes").document("incidente" + nuevo_id).collection("progreso").document();
                        batch.set(doc_progreso, progreso);

                        batch.commit().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                setResult(RESULT_OK, getIntent());
                                Toast.makeText(FormularioTabletActivity.this, "Se ha registrado el incidente con folio " + nuevo_id, Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                bt_guardar.setEnabled(true);
                                Toast.makeText(FormularioTabletActivity.this, "A ocurrido un problema. Intentalo nuevamente", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }).addOnFailureListener(e -> {
                        bt_guardar.setEnabled(true);
                        Toast.makeText(FormularioTabletActivity.this, "A ocurrido un problema. Intentalo nuevamente", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    Toast.makeText(this, "Editando incidente...", Toast.LENGTH_SHORT).show();
                    bt_guardar.setEnabled(false);

                    final Calendar c = Calendar.getInstance();
                    IncidenteBasico incidenteBasico = new IncidenteBasico(
                            getAreas(tv_tipo_problema.getText().toString()),
                            incidenteEditar.getFolio(),
                            getPrioridad(),
                            incidenteEditar.getEstatus(), // Estatus 0 - Sin asignar
                            incidenteEditar.getTecnico(), // Nombre del técnico
                            c.getTimeInMillis(),
                            "Administrador",
                            tv_tipo_problema.getText().toString(),
                            et_descripcion_del_problema.getText().toString(),
                            et_codigo.getText().toString()
                    );

                    Cliente cliente = new Cliente(
                            TextUtils.isEmpty(et_codigo.getText().toString().trim()) ? "No proporcionado" : et_codigo.getText().toString().trim(),
                            et_nombre.getText().toString().trim(),
                            et_dependencia.getText().toString().trim(),
                            et_ubicacion.getText().toString().trim(),
                            et_telefono.getText().toString().trim(),
                            et_correo.getText().toString().trim()
                    );

                    // Get a new write batch
                    WriteBatch batch = db.batch();

                    // Guardando incidente basico
                    String nombre_incidente = "incidente" + incidenteBasico.getFolio();
                    DocumentReference doc_incidente_basico = FirebaseFirestore.getInstance().collection("incidentes").document(nombre_incidente);
                    batch.set(doc_incidente_basico, incidenteBasico);

                    // Guardando los datos del cliente
                    DocumentReference doc_cliente = FirebaseFirestore.getInstance().collection("clientes").document("cliente" + cliente.getCodigoDelCliente());
                    batch.set(doc_cliente, cliente);

                    // Guardando incidente basico
                    String nombre_incidente_cliente = "incidente" + incidenteEditar.getFolio();
                    DocumentReference doc_incidente_cliente = FirebaseFirestore.getInstance().collection("clientes").document("cliente" + cliente.getCodigoDelCliente() + "/incidentes/" + nombre_incidente_cliente);
                    batch.set(doc_incidente_cliente, incidenteBasico);

                    batch.commit().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            setResult(RESULT_OK, getIntent());
                            Toast.makeText(FormularioTabletActivity.this, "Se ha editado el incidente con folio " + incidenteEditar.getFolio(), Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            bt_guardar.setEnabled(true);
                            Toast.makeText(FormularioTabletActivity.this, "A ocurrido un problema. Intentalo nuevamente", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });

        radio_baja = findViewById(R.id.radio_baja);
        radio_media = findViewById(R.id.radio_media);
        radio_alta = findViewById(R.id.radio_alta);
        input_descripcion_del_problema = findViewById(R.id.input_descripcion);
        et_descripcion_del_problema = findViewById(R.id.et_descripcion);
        et_descripcion_del_problema.setImeOptions(EditorInfo.IME_ACTION_DONE);
        et_descripcion_del_problema.setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        ly_tipo_problema = findViewById(R.id.tipo_problema);
        tv_tipo_problema = (TextView) ly_tipo_problema.getChildAt(1);

        ly_tipo_problema.setOnClickListener(view -> {
            Intent intent = new Intent(this, DialogTipoIncidente.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivityForResult(intent, TIPO_INCIDENTE, bundle);
        });

        et_codigo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                for (Cliente c : listaClientes) {
                    if (charSequence.toString().equals(c.getCodigoDelCliente())) {
                        et_nombre.setText(c.getNombreDelCliente());
                        et_dependencia.setText(c.getDependenciaDelCliente());
                        et_ubicacion.setText(c.getUbicacionDelCliente());
                        et_telefono.setText(c.getTelefonoDelCliente());
                        et_correo.setText(c.getCorreoElectronicoDelCliente());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        super_conte = findViewById(R.id.super_conte);

        if (incidenteEditar == null) {
            super_conte.startAnimation(fadeIn);
        }
    }

    private Map<String, Boolean> getAreas(String s) {
        Map<String, Boolean> areas = new HashMap<>();

        if (s.contains("Taller de computo -") || s.contains("Aviso - Taller de computo")) {
            areas.put("taller", true);
            return areas;
        } else if (s.contains("Redes y telefonía -") || s.contains("Aviso - Redes y telefonía")) {
            areas.put("redes", true);
            return areas;
        } else if (s.contains("Multimedia - ") || s.contains("Aviso - Multimedia")) {
            areas.put("multimedia", true);
            return areas;
        } else if (s.contains("Aviso - Aviso general")) {
            areas.put("taller", true);
            areas.put("redes", true);
            areas.put("multimedia", true);
            areas.put("belenes", true);
            return areas;
        } else if (s.contains("Aviso - Redes y Taller")) {
            areas.put("taller", true);
            areas.put("redes", true);
            return areas;
        } else if (s.contains("Aviso - Redes y Multimedia")) {
            areas.put("redes", true);
            areas.put("multimedia", true);
            return areas;
        } else if (s.contains("Aviso - Belenes")) {
            areas.put("belenes", true);
            return areas;
        } else { //  "Aviso - Taller y Multimedia"
            areas.put("taller", true);
            areas.put("multimedia", true);
            return areas;
        }
    }

    public boolean validarItems() {
        boolean isValid = true;

        if (et_descripcion_del_problema.length() == 0) {
            input_descripcion_del_problema.setError(" ");
            isValid = false;
        } else {
            input_descripcion_del_problema.setError(null);
        }

        if (et_codigo.getText().toString().length() == 0) {
            input_codigo.setError(" ");
            isValid = false;
        } else {
            input_codigo.setError(null);
        }

        if (et_nombre.getText().toString().length() == 0) {
            input_nombre.setError(" ");
            isValid = false;
        } else {
            input_nombre.setError(null);
        }

        if (et_dependencia.getText().toString().length() == 0) {
            input_dependencia.setError(" ");
            isValid = false;
        } else {
            input_dependencia.setError(null);
        }

        if (et_ubicacion.getText().toString().length() == 0) {
            input_ubicacion.setError(" ");
            isValid = false;
        } else {
            input_ubicacion.setError(null);
        }

        if (et_telefono.getText().toString().length() == 0) {
            input_telefono.setError(" ");
            isValid = false;
        } else {
            input_telefono.setError(null);
        }

        if (et_correo.getText().toString().length() == 0) {
            input_correo.setError(" ");
            isValid = false;
        } else {
            input_correo.setError(null);
        }

        if (tv_tipo_problema.getText().toString().equals("Selecciona el tipo de incidente")) {
            error_tipo_incidente.setBackgroundColor(getResources().getColor(R.color.fui_errorColor));
        } else {
            error_tipo_incidente.setBackgroundColor(Color.parseColor("#86ffffff"));
        }

        return isValid;
    }

    private int getPrioridad() {
        int i = 1;
        if (radio_media.isChecked()) {
            i = 2;
        } else if (radio_alta.isChecked()) {
            i = 3;
        }
        return i;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TIPO_INCIDENTE && resultCode == RESULT_OK) {
            tv_tipo_problema.setText(data.getStringExtra("TIPO_DE_INCIDENTE"));
            error_tipo_incidente.setBackgroundColor(Color.parseColor("#86ffffff"));
        }
    }
}
