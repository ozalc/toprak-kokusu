package yagmurdan.sonra.toprakkokusu.ui.profile;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;
import yagmurdan.sonra.toprakkokusu.MainActivity;
import yagmurdan.sonra.toprakkokusu.Model.CampingArea;
import yagmurdan.sonra.toprakkokusu.Model.User;
import yagmurdan.sonra.toprakkokusu.R;
import yagmurdan.sonra.toprakkokusu.ui.Authentication.LoginActivity;
import yagmurdan.sonra.toprakkokusu.ui.Authentication.RegisterActivity;
import yagmurdan.sonra.toprakkokusu.ui.CampingMainUI.CampingAreaDetailFragment;
import yagmurdan.sonra.toprakkokusu.ui.add.AddFragment;
import yagmurdan.sonra.toprakkokusu.ui.add.AddingScreen;

public class ProfileFragment extends Fragment {

    protected Button logOutButton;
    private FloatingActionButton fab_main, fab1_feedback, fab2_share;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    private TextView textView_share, textView_feedback,profileName,favoritesNumber,wentNumber,willGoNumber;
    private CircleImageView profileImage;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    Boolean isOpen = false;
    public List<String> favoritedCampingAreasId=new ArrayList<>();
    public List<String> wentCampingAreasId=new ArrayList<>();
    public List<String> willGoCampingAreasId=new ArrayList<>();
    public Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    public StorageReference storageReference;
    public DatabaseReference databaseReference;
    public StorageTask uploadTask;
    private User user = new User();
    public String user_id;
    Uri uri;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth=FirebaseAuth.getInstance();
        user_id=mAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference("Users");
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user_id);

        logOutButton=view.findViewById(R.id.logOutButton);
        fab_main = view.findViewById(R.id.hamburger);
        fab1_feedback = view.findViewById(R.id.feedback);
        fab2_share = view.findViewById(R.id.share);
        fab_close = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_rotate_anticlock);
        profileImage = view.findViewById(R.id.circle_profile_image);
        textView_share = (TextView) view.findViewById(R.id.textview_share);
        textView_feedback = view.findViewById(R.id.textview_feedback);
        profileName = (TextView) view.findViewById(R.id.profileName);
        favoritesNumber=(TextView) view.findViewById(R.id.favoritesNumber);
        wentNumber=(TextView) view.findViewById(R.id.wentNumber);
        willGoNumber=(TextView) view.findViewById(R.id.willGoNumber);
        profileImage.setImageResource(R.drawable.account_image);
        //Şu anki kullanıcının id'sine göre veritabanından ismini alıp ekrana bastırırız
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String nameFromDB = snapshot.child("name").getValue().toString();
                profileName.setText(nameFromDB);
                String image = snapshot.child("image").getValue().toString();
                if (!image.equals("default"))
                {
                    Glide.with(getActivity())
                            .load(image)
                            .into(profileImage);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { chooseImage(); }});

        //FloatingActionButton işlemleri
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen){
                    textView_share.setVisibility((View.INVISIBLE));
                    textView_feedback.setVisibility(View.INVISIBLE);
                    fab2_share.setVisibility(View.INVISIBLE);
                    fab1_feedback.setVisibility(View.INVISIBLE);
                    fab2_share.startAnimation(fab_close);
                    fab1_feedback.startAnimation(fab_close);
                    fab_main.startAnimation(fab_anticlock);
                    fab2_share.setClickable(false);
                    fab1_feedback.setClickable(false);
                    isOpen=false;
                }
                else {
                    textView_share.setVisibility(View.VISIBLE);
                    textView_feedback.setVisibility(View.VISIBLE);
                    fab1_feedback.setVisibility(View.VISIBLE);
                    fab2_share.setVisibility(View.VISIBLE);
                    fab1_feedback.startAnimation(fab_open);
                    fab2_share.startAnimation(fab_open);
                    fab_main.startAnimation(fab_clock);
                    fab2_share.setClickable(true);
                    fab1_feedback.setClickable(true);
                    isOpen = true;
                }
            }
        });

        //çıkış yapma butonu. Tıklanınca login ekranının getirir.
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "Oturum Kapatıldı",Toast.LENGTH_SHORT).show();
                Intent loginIntent=new Intent(ProfileFragment.this.getActivity(), LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        readUserCampInfos(user_id);

        favoritesNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoriteCampingAreasFragment favoriteCampingAreasFragment= new FavoriteCampingAreasFragment();
                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container,favoriteCampingAreasFragment).commit();
            }
        });

        wentNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WentCampingAreasFragment wentCampingAreasFragment = new WentCampingAreasFragment();
                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container,wentCampingAreasFragment).commit();
            }
        });

        willGoNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WillGoCampingAreasFragment willGoCampingAreasFragment=new WillGoCampingAreasFragment();
                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container,willGoCampingAreasFragment).commit();
            }
        });

        return view;
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Resim Seçiniz"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Glide.with(this).load(filePath).into(profileImage);
            saveToDatabase(storageReference);
        }
    }

    private String dosyaUzantisiAl(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void saveToDatabase(StorageReference storageReference) {

        if (filePath != null) {
            StorageReference dosyaYolu = storageReference.child(System.currentTimeMillis()
                    + "." + dosyaUzantisiAl(filePath));

            ProgressDialog progressDialog = new ProgressDialog(getActivity());
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
                        Toast.makeText(getActivity(),"Yükleme Başarılı",Toast.LENGTH_SHORT);
                        user.setimage(task.getResult().toString());
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                HashMap<String, Object> hashMap= new HashMap<>();
                                hashMap.put("image", user.getimage());
                                databaseReference.updateChildren(hashMap);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        progressDialog.dismiss();

                    } else Toast.makeText(getActivity(), "Hata! ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void readUserCampInfos(String user_id) {
        favoritedCampingAreasId.removeAll(favoritedCampingAreasId);
        wentCampingAreasId.removeAll(wentCampingAreasId);
        willGoCampingAreasId.removeAll(willGoCampingAreasId);
        DatabaseReference likeRef = FirebaseDatabase.getInstance().getReference("Users").child(user_id).child("likedCampingAreas");
        DatabaseReference wentRef = FirebaseDatabase.getInstance().getReference("Users").child(user_id).child("wentCampingAreas");
        DatabaseReference willGoRef = FirebaseDatabase.getInstance().getReference("Users").child(user_id).child("willGoCampingAreas");
        likeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    favoritedCampingAreasId.add(dataSnapshot.child("likedCampingAreaID").getValue(String.class));
                }
                favoritesNumber.setText(String.valueOf(favoritedCampingAreasId.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        wentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    wentCampingAreasId.add(dataSnapshot.child("wentCampingAreaID").getValue(String.class));
                }
                wentNumber.setText(String.valueOf(wentCampingAreasId.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        willGoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    willGoCampingAreasId.add(dataSnapshot.child("willGoCampingAreaID").getValue(String.class));
                }
                willGoNumber.setText(String.valueOf(willGoCampingAreasId.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
