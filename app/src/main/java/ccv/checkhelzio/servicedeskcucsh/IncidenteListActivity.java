package ccv.checkhelzio.servicedeskcucsh;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class IncidenteListActivity extends AppCompatActivity implements IncidenteAdapter.ListItemClickListener,
        IncidenteAdapter.DataChangeListener, IncidenteAdapter.ChildChangeListener, IncidenteDetailFragment.OnEliminarIncidenteListener {

    private static final String TAG = IncidenteListActivity.class.getSimpleName();
    private static final int REGISTRAR = 1;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane = false;
    private IncidenteAdapter adapter;
    private ImageView emptyState;
    private RecyclerView recyclerView;
    private Query queryIncidentes;
    private Animation fadeIn;
    private Animation fadeOut;
    public static String nombreUsuario;
    private FloatingActionButton fab;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidente_list);

        // Bolean para saber si estamos en Tablet y usaremos dos paneles
        if (findViewById(R.id.incidente_detail_container) != null) {
            mTwoPane = true;
            Log.d(TAG, "onCreate: TWO PANEL");
        }
        iniciarObjetos();
        iniciarRecyclerView();

    }

    private void iniciarObjetos() {

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.incidente_list);
        emptyState = findViewById(R.id.empty_state);

        fadeIn = AnimationUtils.loadAnimation(IncidenteListActivity.this, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(IncidenteListActivity.this, R.anim.fade_out);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (mTwoPane) {
                    ponerFragment(adapter.getItem(0));
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                recyclerView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                emptyState.setVisibility(View.GONE);
                recyclerView.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent i = new Intent(IncidenteListActivity.this, FormularioTabletActivity.class);
            startActivityForResult(i, REGISTRAR);
        });
    }

    private void iniciarRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            email = user.getEmail();
            Log.d(TAG, "iniciarRecyclerView: nombre de usuario" + user.getDisplayName());
            nombreUsuario = user.getDisplayName();
            assert email != null;
            Log.d(TAG, "onStart: " + email);
            switch (email) {
                case "oscar.mendez@csh.udg.mx":
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING)
                            .whereEqualTo("areas.multimedia", true);
                    break;
                case "deleon.jonathan@csh.udg.mx":
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING);
                    fab.show();
                    break;
                /*case "auditorio.adalberto@csh.udg.mx": // GINA PENDIENTE
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING);
                    break;*/
                case "omara@csh.udg.mx":
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING)
                            .whereEqualTo("areas.multimedia", true);
                    break;
                case "octavio.cortazar@csh.udg.mx":
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING)
                            .whereEqualTo("areas.redes", true);
                    break;
                case "enrique.vega@csh.udg.mx":
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING)
                            .whereEqualTo("areas.taller", true);
                    break;
                case "eduardo.zalazar@csh.udg.mx":
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING)
                            .whereEqualTo("areas.multimedia", true);
                    break;
                case "roberto_028@outlook.com":
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING)
                            //.whereEqualTo("areas.redes", true)
                            .whereEqualTo("areas.taller", true);
                    break;
                /*case "angelmolina.87@hotmail.com": // ANGEL - PENDIENTE
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING);
                    break;*/
                case "jose.carrillom@csh.udg.mx":
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING)
                            .whereEqualTo("areas.redes", true);
                    break;
                /*case "maria.mlomeli@csh.udg.mx": // PENDIENTE
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING);
                    break;*/
                /*case "jose.moralesr@csh.udg": // DON JOSE - PENDIENTE
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING);
                    break;*/
                /*case "guillermo.ruizgl@csh.udg.mx": // DON MEMO - PENDIENTE
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING);
                    break;*/
                /*case "francisco.sandoval@csh.udg.mx": // PENDIENTE
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING);
                    break;*/
                case "jorge.plascencia@redudg.udg.mx":
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING)
                            .whereEqualTo("areas.belenes", true);
                    break;
                /*case "humberto.velazquez@csh.udg.mx": // DON HUMBER - PENDIENTE
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING);
                    break;*/
                /*case "clara.cuellar@csh.udg.mx": // PENDIENTE
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING);
                    break;*/
                case "ricardo.cortes@csh.udg.mx":
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING)
                            .whereEqualTo("areas.taller", true);
                    break;
                case "juan.mancilla@csh.udg.mx":
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING)
                            .whereEqualTo("areas.taller", true);
                    break;
                case "teresa.dlsantos@csh.udg.mx":
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING);
                    fab.show();
                    break;
                /*case "victormc@csh.udg.mx": // PENDIENTE
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING);
                    break;*/
                case "elba.moralesg@csh.udg.mx":
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING);
                    fab.show();
                    break;
                case "ariana.meraz@csh.udg.mx":
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING)
                            .whereEqualTo("areas.taller", true);
                    break;
                case "estrada5791@gmail.com":
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING)
                            .whereEqualTo("areas.taller", true);
                    break;
                case "alberto.franco@csh.udg.mx":
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING)
                            .whereEqualTo("areas.belenes", true);
                    break;
                case "zyanya.lopez@csh.udg.mx":
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING)
                            .whereEqualTo("areas.multimedia", true);
                    break;
                case "hiram.franco@csh.udg.mx":
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING)
                            .whereEqualTo("areas.multimedia", true);
                    break;
                case "oswaldo.mendoza@csh.udg.mx":
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING)
                            .whereEqualTo("areas.belenes", true);
                    break;
                case "idania.gomez@csh.udg.mx":
                    Log.d(TAG, "iniciarRecyclerView: MOSTRAR FAB");
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING);
                    fab.show();
                    break;
                case "eduardo.solano@redudg.udg.mx":
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING);
                    fab.show();
                    break;
                case "hector.shimizu@csh.udg.mx":
                    queryIncidentes = FirebaseFirestore.getInstance().collection("incidentes")
                            .orderBy("folio", Query.Direction.DESCENDING);
                    fab.show();
                    break;
                default:
                    break;
            }
        }

        // Opciones para el adaptador de FirebaseUI
        FirestoreRecyclerOptions<IncidenteBasico> options = new FirestoreRecyclerOptions.Builder<IncidenteBasico>()
                .setQuery(queryIncidentes, IncidenteBasico.class)
                .build();

        if (mTwoPane) {
            // Si la pantalla es grande agregamos la linea a cada Item de la lista
            DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(this,
                    layoutManager.getOrientation());
            mDividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divisor, null));
            recyclerView.addItemDecoration(mDividerItemDecoration);
        }


        adapter = new IncidenteAdapter(options, this, this, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            try {
                adapter.startListening();
            } catch (Exception ignored) {
                Snackbar.make((View) fab.getParent().getParent(), "El correo electrónico " + email + " no es válido...", BaseTransientBottomBar.LENGTH_INDEFINITE).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cerrar_sesion:
                AuthUI.getInstance().signOut(this);
                startActivity(new Intent(this, Portada.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(int position, View view) {
        if (mTwoPane) {
            adapter.notifyItemChanged(IncidenteAdapter.selected_position);
            IncidenteAdapter.selected_position = position;
            adapter.notifyItemChanged(IncidenteAdapter.selected_position);
            ponerFragment(adapter.getItem(position));
        } else {
            IncidenteBasico incidenteBasico = adapter.getItem(position);
            Intent intent = new Intent(this, IncidenteDetailActivity.class);
            intent.putExtra("INCIDENTE", incidenteBasico);
            startActivity(intent);
        }
    }

    private void ponerFragment(IncidenteBasico incidenteBasico) {
        Bundle arguments = new Bundle();
        arguments.putParcelable("INCIDENTE", incidenteBasico);
        IncidenteDetailFragment fragment = new IncidenteDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.incidente_detail_container, fragment)
                .commit();
    }

    @Override
    public void onDataChange(int itemCount) {
        if (itemCount > 0) {
            if (emptyState.getVisibility() == View.VISIBLE) {
                emptyState.startAnimation(fadeOut);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.notifyItemChanged(IncidenteAdapter.selected_position);
        if (requestCode == REGISTRAR && resultCode == RESULT_OK && mTwoPane) {
            recyclerView.smoothScrollToPosition(0);
            adapter.notifyItemChanged(IncidenteAdapter.selected_position);
            IncidenteAdapter.selected_position = 0;
            adapter.notifyItemChanged(IncidenteAdapter.selected_position);
            ponerFragment(adapter.getItem(0));
        }
    }

    @Override
    public void onChildChange(ChangeEventType itemCount) {

    }

    @Override
    public void onEliminarIncidente() {
        Toast.makeText(this, "El incidente ha sido eliminado correctamente", Toast.LENGTH_SHORT).show();
        adapter.notifyItemChanged(IncidenteAdapter.selected_position);
        IncidenteAdapter.selected_position = 0;
        adapter.notifyItemChanged(IncidenteAdapter.selected_position);
        if (mTwoPane) {
            ponerFragment(adapter.getItem(0));
        }
    }
}
