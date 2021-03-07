package yagmurdan.sonra.toprakkokusu.ui.add;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.UUID;

import yagmurdan.sonra.toprakkokusu.MainActivity;
import yagmurdan.sonra.toprakkokusu.Model.CampingArea;
import yagmurdan.sonra.toprakkokusu.R;
import yagmurdan.sonra.toprakkokusu.ui.Authentication.LoginActivity;
import yagmurdan.sonra.toprakkokusu.ui.Authentication.RegisterActivity;
import yagmurdan.sonra.toprakkokusu.ui.CampingMainUI.CampingMainPageFragment;
import yagmurdan.sonra.toprakkokusu.ui.profile.ProfileFragment;

public class AddingScreen extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseStorage mStorage;
    private StorageReference storageReference;
    private StorageTask uploadTask;


    private EditText EditTextName;
    private EditText EditTextLocationName;
    private Switch SwitchUcret;
    private Switch SwitchTesis;
    private Switch SwitchTopluTasima;
    private Switch SwitchOtopark;
    private Switch SwitchCesme;
    private Switch SwitchTuvalet;
    private Switch SwitchYabaniHayvan;
    private Switch SwitchAtesYakilmaz;
    private Switch SwitchSinyalVar;
    private Switch SwitchOdun;


    private Button Kaydet;
    private Button ResimSec;
    private ImageView AddingScreenImageView;


    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    CampingArea campingArea = new CampingArea();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_screen);
        storageReference = FirebaseStorage.getInstance().getReference("Camping images");
        databaseReference = FirebaseDatabase.getInstance().getReference("Camping Areas");

        EditTextName = (EditText) findViewById(R.id.EditTextKampAdi);
        EditTextLocationName = findViewById(R.id.EditTextLokasyonAdi);
        SwitchUcret = findViewById(R.id.SwitchUcretli);
        SwitchTesis = findViewById(R.id.SwitchTesis);
        SwitchTopluTasima = findViewById(R.id.SwitchTopluTasima);
        SwitchOtopark = findViewById(R.id.SwitchOtopark);
        SwitchCesme = findViewById(R.id.SwitchCesme);
        SwitchTuvalet = findViewById(R.id.SwitchTuvalet);
        SwitchYabaniHayvan = findViewById(R.id.SwitchYabaniHayvan);
        SwitchAtesYakilmaz = findViewById(R.id.SwitchAtesYakilmaz);
        SwitchSinyalVar = findViewById(R.id.SwitchSinyalVar);
        SwitchOdun = findViewById(R.id.SwitchOdun);
        AddingScreenImageView = findViewById(R.id.AddingScreenImageView);
        EditTextName = findViewById(R.id.EditTextKampAdi);
        EditTextLocationName = findViewById(R.id.EditTextLokasyonAdi);

        Kaydet = findViewById(R.id.KaydetButton);
        ResimSec = findViewById(R.id.ResimSec);
        AddingScreenImageView = findViewById(R.id.AddingScreenImageView);

        //kaydet butonuna tıklanınca saveToDatabase metodu çalıştırılır
        Kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveToDatabase();

            }
        });

        //resim seç butonuna tıklanınca chooseImage metodu çalıştırılır
        ResimSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        SwitchUcret.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    campingArea.setBoolUcret(true);
                } else {
                    campingArea.setBoolUcret(false);
                }
            }
        });
        SwitchTesis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    campingArea.setBoolTesis(true);
                } else {
                    campingArea.setBoolTesis(false);
                }
            }
        });
        SwitchTopluTasima.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    campingArea.setBoolTopluTasima(true);
                } else {
                    campingArea.setBoolTopluTasima(false);
                }
            }
        });
        SwitchOtopark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    campingArea.setBoolOtopark(true);
                } else {
                    campingArea.setBoolOtopark(false);
                }
            }
        });
        SwitchCesme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    campingArea.setBoolCesme(true);
                } else {
                    campingArea.setBoolCesme(false);
                }
            }
        });
        SwitchTuvalet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    campingArea.setBoolTuvalet(true);
                } else {
                    campingArea.setBoolTuvalet(false);
                }
            }
        });
        SwitchYabaniHayvan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    campingArea.setBoolYabaniHayvan(true);
                } else {
                    campingArea.setBoolYabaniHayvan(false);
                }
            }
        });
        SwitchAtesYakilmaz.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    campingArea.setBoolAtesYakilmaz(true);
                } else {
                    campingArea.setBoolAtesYakilmaz(false);
                }
            }
        });
        SwitchSinyalVar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    campingArea.setBoolSinyalVar(true);
                } else {
                    campingArea.setBoolSinyalVar(false);
                }
            }
        });
        SwitchOdun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    campingArea.setBoolOdun(true);
                } else {
                    campingArea.setBoolOdun(false);
                }
            }
        });

    }

    //resim seç butonuna tıklayınca çalışan metot
    //belirlenen PICKIMAGEREQUEST koduna göre onActivityResult kodu çalıştırılır
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Resim Seçiniz"), PICK_IMAGE_REQUEST);
    }

    //PICK_IMAGE_REQUEST koduna bakılarak gerekli işlemler gerçekleştirilir
    //kullanıcının seçtiği resmin Uri'si (bir nevi adresi) alınır ve glide ile AddingScreenImageView'e gönderilir.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            //AddingScreenImageView.setImageURI(filePath);
            Glide.with(this).load(filePath).into(AddingScreenImageView);
        }
    }

    //Firebase Storage'a resmi yüklerken uzantısını da yazmak için uzantı al fonksiyonu
    private String dosyaUzantisiAl(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //kaydet butonuna tıklayınca çalışan fonksiyon
    private void saveToDatabase() {

        if (filePath != null) {
            StorageReference dosyaYolu = storageReference.child(System.currentTimeMillis()
                    + "." + dosyaUzantisiAl(filePath));

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Yükleniyor...");
            progressDialog.show();

            //dosyayı storage'a gönder
            uploadTask = dosyaYolu.putFile(filePath);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful())
                        throw task.getException();

                    return dosyaYolu.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        //task başarılı ise Modelin bir örneği oluşturulur ve bu örnek üzerinden işlemler yapılır.
                        Toast.makeText(AddingScreen.this, "Yükleme Başarılı", Toast.LENGTH_SHORT);
                        CampingArea upload = new CampingArea(task.getResult().toString(), EditTextName.getText().toString(), EditTextLocationName.getText().toString());
                        //veritabanındaki o yüklemeye ait key'i alıyoruz ve o key'i kullanarak bir node oluşturuyoruz
                        // o node içine setValue kullanarak oluşturduğumuz modelin örneğini gönderiyoruz
                        String uploadID = databaseReference.push().getKey();
                        databaseReference.child(uploadID).setValue(upload);
                        progressDialog.dismiss();

                        Intent intent = new Intent(AddingScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else Toast.makeText(AddingScreen.this, "Hata! ", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
}