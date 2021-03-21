package google.maps.colombia.aplicativoreportedeaccidentes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.animation.content.Content;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_registrar, tv_invitado,  tv_pasarDatos, tv_ruta, tv_pasarPrueba;
    private EditText TextEmail;
    private EditText TextPassword;
    private Button btnLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    private boolean estadoButton;
    private RadioButton RbSesion;
    private static final String STRING_PREFERENCES = "Credenciales";
    private static final String PREFERENCE_ESTADO_BUTON = "EstadoBoton";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        if (obetenerEstadoBoton()){
            Intent intent = new Intent(getApplication(), MenuActivity.class);
            startActivity(intent);
            finish();
        }

        TextEmail = findViewById(R.id.txt_email);
        TextPassword = findViewById(R.id.txt_pass);
        tv_registrar = findViewById(R.id.tv_pasarRegistro);
        tv_invitado = findViewById(R.id.tv_pasarInvitado);
        tv_pasarPrueba = findViewById(R.id.tv_pasarPrueba);
        tv_ruta = findViewById(R.id.tv_pasarRuta);
        btnLogin = findViewById(R.id.botonLogin);
        tv_pasarDatos = findViewById(R.id.tv_pasarDatos);

        RbSesion =  findViewById(R.id.RBSesion);
        estadoButton = RbSesion.isChecked();

        RbSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(estadoButton){
                    RbSesion.setChecked(false);
                }
                    estadoButton = RbSesion.isChecked();
            }
        });


        tv_invitado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInvitado = new Intent(MainActivity.this, InvitadoActivity.class);
                MainActivity.this.startActivity(intentInvitado);
            }
        });

        tv_registrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intentRegistro = new Intent(MainActivity.this, RegistroActivity.class);
                MainActivity.this.startActivity(intentRegistro);
            }
        });

        tv_ruta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRuta = new Intent(MainActivity.this, TrazarRutaActivity.class);
                MainActivity.this.startActivity(intentRuta);
            }
        });


        tv_pasarPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPrueba = new Intent(MainActivity.this, PruebaActivity.class);
                MainActivity.this.startActivity(intentPrueba);
            }
        });

        tv_pasarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDAtos = new Intent(MainActivity.this, DatosActivity.class);
                MainActivity.this.startActivity(intentDAtos);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        btnLogin.setOnClickListener(this);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken", newToken);
            }
        });

    }

    private void loguearUsuario(){
        final String email = TextEmail.getText().toString().trim();
        String password = TextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {//(precio.equals(""))
            Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Se debe ingresar una contraseña", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Verificando datos de usuario");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        guardarEstadoBoton();
                        if (task.isSuccessful()) {
                            int pos = email.indexOf("@");
                            String user = email.substring(0, pos);
                            Toast.makeText(MainActivity.this, "Bienvenido" + TextEmail.getText(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplication(), MenuActivity.class);
                            intent.putExtra(MenuActivity.user, user);
                            startActivity(intent);
                            finish();



                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {//si se presenta una colisión
                                Toast.makeText(MainActivity.this, "Este usuario ya existe ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Los datos son incorrectos, intente nuevamente", Toast.LENGTH_LONG).show();
                            }
                        }
                        progressDialog.dismiss();
                    }
                });

        TextEmail.setText("");
        TextPassword.setText("");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.botonLogin:
                loguearUsuario();
                break;
        }
    }

    public boolean obetenerEstadoBoton(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        return preferences.getBoolean(PREFERENCE_ESTADO_BUTON, false);
    }

    public void guardarEstadoBoton(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCE_ESTADO_BUTON, RbSesion.isChecked()).apply();
    }

    public static void cambiarEstadoBoton(Context c, boolean b){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCE_ESTADO_BUTON, b).apply();
    }
}
