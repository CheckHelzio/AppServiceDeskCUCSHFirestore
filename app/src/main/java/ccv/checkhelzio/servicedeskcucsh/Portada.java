package ccv.checkhelzio.servicedeskcucsh;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;

public class Portada extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = Portada.class.getSimpleName();
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private Snackbar snackbar;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portada);

        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        // Inicia los componentes de Firebase
        mFirebaseAuth = FirebaseAuth.getInstance();

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mAuthStateListener == null){
            mAuthStateListener = firebaseAuth -> {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    // Usuario logueado
                    new Handler().postDelayed(() -> {
                        startActivity(new Intent(Portada.this, IncidenteListActivity.class));
                        finish();
                    }, 2000);

                }else {
                    // Usuario no logueado
                    new Handler().postDelayed(() -> startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setTheme(R.style.LoginTheme)
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Collections.singletonList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN),2000);
                }
            };
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null){
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
            mAuthStateListener = null;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                // Inicio de sesión correcto

            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    finish();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    if (snackbar == null) {
                        snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Verifica la conexión a internet", Snackbar.LENGTH_INDEFINITE);
                        snackbar.show();
                    }
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "Inicio de sesión cancelado", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}