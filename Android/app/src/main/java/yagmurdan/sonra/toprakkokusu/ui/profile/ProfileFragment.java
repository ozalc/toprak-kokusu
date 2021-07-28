package yagmurdan.sonra.toprakkokusu.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;
import yagmurdan.sonra.toprakkokusu.R;
import yagmurdan.sonra.toprakkokusu.ui.Authentication.LoginActivity;
import yagmurdan.sonra.toprakkokusu.ui.Authentication.RegisterActivity;
import yagmurdan.sonra.toprakkokusu.ui.add.AddFragment;

public class ProfileFragment extends Fragment {

    protected Button logOutButton;
    private FloatingActionButton fab_main, fab1_feedback, fab2_share;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    private TextView textView_share, textView_feedback;
    private ImageView profileImage;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView profileName;


    Boolean isOpen = false;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);
        logOutButton=view.findViewById(R.id.logOutButton);
        fab_main = view.findViewById(R.id.hamburger);
        fab1_feedback = view.findViewById(R.id.feedback);
        fab2_share = view.findViewById(R.id.share);
        fab_close = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_rotate_anticlock);
        profileImage = (ImageView) view.findViewById(R.id.circle_profile_image);
        textView_share = (TextView) view.findViewById(R.id.textview_share);
        textView_feedback = view.findViewById(R.id.textview_feedback);
        profileName = (TextView) view.findViewById(R.id.profileName);

        //Default olarak standart bir profil resmi koyuyoruz.
        profileImage.setImageResource(R.drawable.account_image);

        //Şu anki kullanıcının id'sine göre veritabanından ismini alıp ekrana bastırırız
        mAuth=FirebaseAuth.getInstance();
        String user_id=mAuth.getCurrentUser().getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String nameFromDB = snapshot.child("name").getValue().toString();
                    profileName.setText(nameFromDB);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
        return view;
    }
}