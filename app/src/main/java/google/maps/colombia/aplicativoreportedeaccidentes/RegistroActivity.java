package google.maps.colombia.aplicativoreportedeaccidentes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegistroActivity extends AppCompatActivity implements View.OnClickListener  {

    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;
    DatabaseReference mDatabase;
    FirebaseDatabase firebaseDatabase;

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
    private EditText TextEmail;
    private EditText TextPassword, TextPasswordConf;
    private EditText edt_nombre;
    private EditText edt_apellido;
    private Button btnRegistrar;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        getSupportActionBar().hide();

        TextEmail = findViewById(R.id.txt_email);
        TextPassword = findViewById(R.id.txt_pass);
        TextPasswordConf = findViewById(R.id.txt_pass_conf);
        edt_nombre = findViewById(R.id.edtx_nombreRegistro);
        edt_apellido = findViewById(R.id.edtx_apellidoRegistro);
        btnRegistrar = findViewById(R.id.botonRegistrar);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        btnRegistrar.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void registrarUsuario(){

        String nombreReg = edt_nombre.getText().toString();
        String apellidoReg = edt_apellido.getText().toString();
        String email = TextEmail.getText().toString().trim();
        String password = TextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {//(precio.equals(""))
            Toast.makeText(this, "Se deben llenar los campos", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Se ingresar una contraseña", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Registrando datos de usuario");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            Toast.makeText(RegistroActivity.this, "Se ha registrado el usuario con el email: " + TextEmail.getText(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplication(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {//si se presenta una colisión
                                Toast.makeText(RegistroActivity.this, "Este usuario ya existe", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegistroActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_LONG).show();
                            }
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.botonRegistrar:
                 registrarUsuario();
                break;
        }
    }
}
