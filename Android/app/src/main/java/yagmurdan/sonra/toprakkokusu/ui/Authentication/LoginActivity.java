package yagmurdan.sonra.toprakkokusu.ui.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import yagmurdan.sonra.toprakkokusu.MainActivity;
import yagmurdan.sonra.toprakkokusu.R;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail;
    private EditText loginPassword;
    private Button loginButton;
    private TextView loginToRegister;
    private ProgressDialog loginProgress;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEmail=(EditText) findViewById(R.id.loginEmail);
        loginPassword=(EditText) findViewById(R.id.loginPassword);
        loginButton=(Button) findViewById(R.id.loginButton);
        loginToRegister=(TextView) findViewById(R.id.loginscreenTextview);
        loginProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        loginToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        //kullanıcının girdiği bilgileri alarak loginUser metodunu çalıştırır
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();
                    if(!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password)){
                        loginProgress.setTitle("Oturum Açılıyor");
                        loginProgress.setMessage("Hesabınıza giriş yapılıyor, lütfen bekleyiniz...");
                        loginProgress.setCanceledOnTouchOutside(false);
                        loginProgress.show();

                        loginUser(email, password);
                    }
            }

            //giriş yap butonuna tıklayınca çalışan metot
            private void loginUser(String email, String password) {
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                loginProgress.dismiss();
                                Toast.makeText(getApplicationContext(), "Giriş Başarılı", Toast.LENGTH_SHORT).show();
                                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                            }
                            else
                            {
                                loginProgress.dismiss();
                                Toast.makeText(getApplicationContext(), "Giriş Yapılamadı"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                    }
                });
            }
        });
    }
}