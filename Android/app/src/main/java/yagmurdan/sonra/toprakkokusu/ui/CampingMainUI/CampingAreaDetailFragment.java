package yagmurdan.sonra.toprakkokusu.ui.CampingMainUI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.media.Rating;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.maps.*;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import yagmurdan.sonra.toprakkokusu.Adapter.CommentAdapter;
import yagmurdan.sonra.toprakkokusu.Model.CampingArea;
import yagmurdan.sonra.toprakkokusu.Model.Comment;
import yagmurdan.sonra.toprakkokusu.R;

public class CampingAreaDetailFragment extends Fragment implements OnMapReadyCallback {

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    CampingArea SelectedArea;
    private String selectedCampingAreaName,selectedCampingAreaLocation,CampingAreaKey;
    private Button buttonFeatures,buttonDeclaration, buttonMap,commentButtonYorumYap,buttonShowComments;
    private TextView textViewDeclaration,selectedCampingAreaNameDetail,selectedCampingAreaLocationDetail;
    public EditText commentEditText;
    private ImageView ImageViewTuvalet,ImageViewUcret,ImageViewUlasim,
            ImageViewTesis,ImageViewOtopark,ImageViewCesme,ImageViewYabaniYasam,
            ImageViewAtes,ImageViewSebeke,ImageViewOdun,commentImageView,selectedCampingAreaImageDetail;
    private GridLayout gridLayout;
    private LinearLayout campingAreaDetailLinearComment;
    private View mapOnDetailPage,campingAreaDetailCommentView;
    public GoogleMap gMap;
    double Latitude,Longitute;
    private RatingBar ratingBar;
    public FirebaseAuth mAuth;
    private LikeButton likeButton,gittim,gideceğim;

        @Override
        public void onMapReady(GoogleMap googleMap) {
            gMap = googleMap;
            LatLng sydney = new LatLng(Latitude, Longitute);
            gMap.addMarker(new MarkerOptions().position(sydney));
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Latitude,Longitute), 12.0f));
            gMap.getUiSettings().setAllGesturesEnabled(false);
        }

    public CampingAreaDetailFragment() { }

    public static CampingAreaDetailFragment newInstance() {
        CampingAreaDetailFragment fragment = new CampingAreaDetailFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.remove(CampingAreaDetailFragment.newInstance()).commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapOnDetailPage);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camping_area_detail, container, false);

        Bundle bundle = getArguments();
        if(bundle!=null) {
            selectedCampingAreaName = bundle.getString("selectedCampingAreaName");
            selectedCampingAreaLocation = bundle.getString("selectedCampingAreaLocation");
        }

        recyclerView=view.findViewById(R.id.RecylerViewCampingDetailPage);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(CampingAreaDetailFragment.newInstance().getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(getContext(), commentList);
        recyclerView.setAdapter(commentAdapter);
        //-----------------------------------------------------------------------

        gittim=view.findViewById(R.id.CampingAreaDetailGittim);
        gideceğim=view.findViewById(R.id.CampingAreaDetailGidilecek);
        likeButton=view.findViewById(R.id.CampingAreaDetailFavorite);
        campingAreaDetailCommentView=view.findViewById(R.id.campingAreaDetailCommentView);
        campingAreaDetailLinearComment=view.findViewById(R.id.CampingAreaDetailLinearComment);
        buttonShowComments=view.findViewById(R.id.buttonShowComments);
        commentImageView = view.findViewById(R.id.campingAreaDetailProfileImageView);
        commentEditText = view.findViewById(R.id.campingAreaDetailEditText);
        commentButtonYorumYap = view.findViewById(R.id.campingAreaDetailButtonYorumYap);
        ratingBar = view.findViewById(R.id.ratingBarCampingDetailPage); ratingBar.setVisibility(View.GONE);
        mapOnDetailPage = view.findViewById(R.id.mapOnDetailPage);
        mapOnDetailPage.setVisibility(View.GONE);
        buttonMap = view.findViewById(R.id.buttonMap);
        buttonFeatures = view.findViewById(R.id.buttonFeatures);
        gridLayout = view.findViewById(R.id.gridLayoutDetail);
        buttonDeclaration = view.findViewById(R.id.buttonDeclaration);
        textViewDeclaration = view.findViewById(R.id.textViewDeclaration);
        selectedCampingAreaNameDetail = view.findViewById(R.id.selectedCampingAreaNameDetail);
        selectedCampingAreaLocationDetail = view.findViewById(R.id.selectedCampingAreaLocationDetail);
        selectedCampingAreaImageDetail = view.findViewById(R.id.selectedCampingAreaImageDetail);
        ImageViewTuvalet = view.findViewById(R.id.ImageViewTuvalet);
        ImageViewUcret = view.findViewById(R.id.ImageViewUcret);
        ImageViewUlasim = view.findViewById(R.id.ImageViewUlasim);
        ImageViewTesis = view.findViewById(R.id.ImageViewTesis);
        ImageViewOtopark = view.findViewById(R.id.ImageViewOtopark);
        ImageViewCesme = view.findViewById(R.id.ImageViewCesme);
        ImageViewYabaniYasam = view.findViewById(R.id.ImageViewYabaniYasam);
        ImageViewAtes = view.findViewById(R.id.ImageViewAtes);
        ImageViewSebeke = view.findViewById(R.id.ImageViewSebeke);
        ImageViewOdun = view.findViewById(R.id.ImageViewOdun);

        mAuth=FirebaseAuth.getInstance();
        String user_id=mAuth.getCurrentUser().getUid();








        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                liked();
            }

            private void liked() {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user_id).child("likedCampingAreas");

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String, String> hashMap= new HashMap<>();
                        hashMap.put("likedCampingAreaID", CampingAreaKey);

                        databaseReference.push().setValue(hashMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user_id).child("likedCampingAreas");

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        {

                            if (dataSnapshot.child("likedCampingAreaID").getValue(String.class).equals(CampingAreaKey))
                            {
                                dataSnapshot.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        gittim.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user_id).child("wentCampingAreas");

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String, String> hashMap= new HashMap<>();
                        hashMap.put("wentCampingAreaID", CampingAreaKey);

                        databaseReference.push().setValue(hashMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user_id).child("wentCampingAreas");

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        {

                            if (dataSnapshot.child("wentCampingAreaID").getValue(String.class).equals(CampingAreaKey))
                            {
                                dataSnapshot.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        gideceğim.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user_id).child("willGoCampingAreas");

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String, String> hashMap= new HashMap<>();
                        hashMap.put("willGoCampingAreaID", CampingAreaKey);

                        databaseReference.push().setValue(hashMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user_id).child("willGoCampingAreas");

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        {

                            if (dataSnapshot.child("willGoCampingAreaID").getValue(String.class).equals(CampingAreaKey))
                            {
                                dataSnapshot.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        buttonShowComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(campingAreaDetailLinearComment.getVisibility()==View.VISIBLE&&recyclerView.getVisibility()==View.VISIBLE)
                {campingAreaDetailLinearComment.setVisibility(View.GONE); recyclerView.setVisibility(View.GONE); campingAreaDetailCommentView.setVisibility(View.GONE);}
                else
                {campingAreaDetailLinearComment.setVisibility(View.VISIBLE); recyclerView.setVisibility(View.VISIBLE); campingAreaDetailCommentView.setVisibility(View.VISIBLE);}
            }
        });

        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mapOnDetailPage.getVisibility()==View.VISIBLE)
                {
                    mapOnDetailPage.setVisibility(View.GONE);
                }
                else
                    mapOnDetailPage.setVisibility(View.VISIBLE);

            }
        });

        commentButtonYorumYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commentEditText.getText().equals(""))
                    Toast.makeText(CampingAreaDetailFragment.newInstance().getContext(), "Boş yorum yapamazsınız!", Toast.LENGTH_SHORT).show();
                else
                    addComment();
            }
        });

        getcampingList();
        commentListRead();
        setProfileImageToCommentSection(user_id);
        readUserCampInfos(user_id);

        buttonFeatures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gridLayout.getVisibility() == View.VISIBLE && ratingBar.getVisibility() == View.VISIBLE)
                {
                    gridLayout.setVisibility(View.GONE);
                    ratingBar.setVisibility(View.GONE);

                }
                else
                {
                    gridLayout.setVisibility(View.VISIBLE);
                    ratingBar.setVisibility(View.VISIBLE);
                }
            }
        });


        buttonDeclaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textViewDeclaration.getVisibility()==View.VISIBLE)
                {
                    textViewDeclaration.setVisibility(View.GONE);
                }
                else
                    textViewDeclaration.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private void commentListRead() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Camping Areas");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for(DataSnapshot camping : snapshot.getChildren()) {
                    if (camping.getKey()==CampingAreaKey) {
                            for (DataSnapshot eachComment : camping.child("Comments").getChildren()) {
                                Comment comment = eachComment.getValue(Comment.class);
                                commentList.add(comment);
                            }
                        commentAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getcampingList() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Camping Areas");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot campingSnapshot : snapshot.getChildren())
                {
                    CampingArea campingArea = campingSnapshot.getValue(CampingArea.class);
                    SelectedArea=campingArea;
                    if(selectedCampingAreaName.equals(campingSnapshot.getValue(CampingArea.class).getName()) &&
                            selectedCampingAreaLocation.equals(campingSnapshot.getValue(CampingArea.class).getLocation()))
                    {
                       CampingAreaKey = campingSnapshot.getKey();
                       selectedCampingAreaNameDetail.setText(campingArea.getName());
                       selectedCampingAreaLocationDetail.setText(campingArea.getLocation());
                       Glide.with(getContext())
                               .load(campingArea.getGonderiResmi())
                               .into(selectedCampingAreaImageDetail);

                       textViewDeclaration.setText(campingArea.getTanitim());

                       Longitute=SelectedArea.getLongitute();
                       Latitude=SelectedArea.getLatitute();

                       ratingBar.setRating(campingArea.getRating());
                       ratingBar.setEnabled(false);


                       if(campingArea.getBoolTuvalet()==Boolean.TRUE) {
                           ImageViewTuvalet.setImageResource(R.drawable.ic_tick_green);
                       }
                       else {
                           ImageViewTuvalet.setImageResource(R.drawable.ic_tick_red);
                       }
                        if(campingArea.getBoolUcret()==Boolean.TRUE)
                            ImageViewUcret.setImageResource(R.drawable.ic_tick_green);
                        else
                            ImageViewUcret.setImageResource(R.drawable.ic_tick_red);
                        if(campingArea.getBoolTopluTasima()==Boolean.TRUE)
                            ImageViewUlasim.setImageResource(R.drawable.ic_tick_green);
                        else
                            ImageViewUlasim.setImageResource(R.drawable.ic_tick_red);
                        if(campingArea.getBoolTesis()==Boolean.TRUE)
                            ImageViewTesis.setImageResource(R.drawable.ic_tick_green);
                        else
                            ImageViewTesis.setImageResource(R.drawable.ic_tick_red);
                        if(campingArea.getBoolOtopark()==Boolean.TRUE)
                            ImageViewOtopark.setImageResource(R.drawable.ic_tick_green);
                        else
                            ImageViewOtopark.setImageResource(R.drawable.ic_tick_red);
                        if(campingArea.getBoolCesme()==Boolean.TRUE)
                            ImageViewCesme.setImageResource(R.drawable.ic_tick_green);
                        else
                            ImageViewCesme.setImageResource(R.drawable.ic_tick_red);
                        if(campingArea.getBoolYabaniHayvan()==Boolean.TRUE)
                            ImageViewYabaniYasam.setImageResource(R.drawable.ic_tick_green);
                        else
                            ImageViewYabaniYasam.setImageResource(R.drawable.ic_tick_red);
                        if(campingArea.getBoolAtesYakilmaz()==Boolean.TRUE)
                            ImageViewAtes.setImageResource(R.drawable.ic_tick_red);
                        else
                            ImageViewAtes.setImageResource(R.drawable.ic_tick_green);
                        if(campingArea.getBoolSinyalVar()==Boolean.TRUE)
                            ImageViewSebeke.setImageResource(R.drawable.ic_tick_green);
                        else
                            ImageViewSebeke.setImageResource(R.drawable.ic_tick_red);
                        if(campingArea.getBoolOdun()==Boolean.TRUE)
                            ImageViewOdun.setImageResource(R.drawable.ic_tick_green);
                        else
                            ImageViewOdun.setImageResource(R.drawable.ic_tick_red);

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void addComment(){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                    .getReference("Camping Areas").child(CampingAreaKey).child("Comments");
        HashMap<String, Object> hashMap= new HashMap<>();
        hashMap.put("Comment", commentEditText.getText().toString());
        hashMap.put("Publisher", mAuth.getCurrentUser().getUid());
        databaseReference.push().setValue(hashMap);
        commentEditText.setText("");
    }

    private void setProfileImageToCommentSection(String user_id){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user_id);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child("image").getValue()!=null)
                    {
                        /*Glide.with(CampingAreaDetailFragment.this)
                                .load(snapshot.child("image").getValue())
                                .into(commentImageView);*/
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    private void readUserCampInfos(String user_id) {
        DatabaseReference likeRef = FirebaseDatabase.getInstance().getReference("Users").child(user_id).child("likedCampingAreas");
        DatabaseReference wentRef = FirebaseDatabase.getInstance().getReference("Users").child(user_id).child("wentCampingAreas");
        DatabaseReference willGoRef = FirebaseDatabase.getInstance().getReference("Users").child(user_id).child("willGoCampingAreas");
        likeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    if (dataSnapshot.child("likedCampingAreaID").getValue().equals(CampingAreaKey))
                    {
                        likeButton.setLiked(true);
                        break;
                    }
                }
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
                    if (dataSnapshot.child("wentCampingAreaID").getValue().equals(CampingAreaKey))
                    {
                        gittim.setLiked(true);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        willGoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    if (dataSnapshot.child("willGoCampingAreaID").getValue().equals(CampingAreaKey))
                    {
                        gideceğim.setLiked(true);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}