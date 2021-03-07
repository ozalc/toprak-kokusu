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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import yagmurdan.sonra.toprakkokusu.MainActivity;
import yagmurdan.sonra.toprakkokusu.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText registerName;
    private EditText registerEmail;
    private EditText registerPassword;
    private TextView registerToLogin;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private ProgressDialog registerProgress;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerButton=(Button) findViewById(R.id.registerButton);
        registerEmail=(EditText) findViewById(R.id.registerEmail);
        registerName=(EditText) findViewById(R.id.registerUsername);
        registerPassword=(EditText) findViewById(R.id.registerPassword);
        registerToLogin=(TextView) findViewById(R.id.registerscreenTextview);
        mAuth = FirebaseAuth.getInstance();
        registerProgress=new ProgressDialog(this);

        //zaten bir hesabım var butonu. Tıklayınca login ekranına gönderir
        registerToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        //kullanıcının girdiği verileri alarak bunları register_user metoduna gönderir
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = registerName.getText().toString();
                String email = registerEmail.getText().toString();
                String password = registerPassword.getText().toString();

                if (!TextUtils.isEmpty(name)||!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password))
                {
                    registerProgress.setTitle("Kaydediliyor");
                    registerProgress.setMessage("Hesabınızı Oluşturuyoruz Lütfen Bekleyiniz");
                    registerProgress.setCanceledOnTouchOutside(false);
                    registerProgress.show();
                    register_user(name,password,email);


                }
            }
        });
    }

    //kayıt ol butonuna tıklanınca çalışan metot
    private void register_user(String name, String password, String email) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    //kullanıcının userid'sini alıp hashmap yapısı ile veri tabanına göndeririz
                    String user_id=mAuth.getCurrentUser().getUid();
                    mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                    HashMap<String,String> userMap=new HashMap<>();
                    userMap.put("name",name);
                    userMap.put("image","default");
                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                //yüklendikten sonra ana sayfaya yönlendirilir
                                registerProgress.dismiss();
                                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                            }
                        }
                    });


                }
                else
                {
                    registerProgress.dismiss();
                    Toast.makeText(getApplicationContext(), "Hata: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}