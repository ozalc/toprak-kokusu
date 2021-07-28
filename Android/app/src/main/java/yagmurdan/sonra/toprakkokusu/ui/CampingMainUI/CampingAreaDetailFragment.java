package yagmurdan.sonra.toprakkokusu.ui.CampingMainUI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import yagmurdan.sonra.toprakkokusu.Model.CampingArea;
import yagmurdan.sonra.toprakkokusu.R;


public class CampingAreaDetailFragment extends Fragment {
    CampingArea SelectedArea;
    private String selectedCampingAreaName;
    private String selectedCampingAreaLocation;

    private Button buttonFeatures,buttonDeclaration;
    private GridLayout gridLayout;
    private TextView textViewDeclaration;

    private TextView selectedCampingAreaNameDetail;
    private TextView selectedCampingAreaLocationDetail;
    private ImageView selectedCampingAreaImageDetail;



    public CampingAreaDetailFragment() {

    }


    public static CampingAreaDetailFragment newInstance() {
        CampingAreaDetailFragment fragment = new CampingAreaDetailFragment();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camping_area_detail, container, false);



        buttonFeatures = view.findViewById(R.id.buttonFeatures);
        gridLayout = view.findViewById(R.id.gridLayoutDetail);
        buttonDeclaration = view.findViewById(R.id.buttonDeclaration);
        textViewDeclaration = view.findViewById(R.id.textViewDeclaration);

        selectedCampingAreaNameDetail = view.findViewById(R.id.selectedCampingAreaNameDetail);
        selectedCampingAreaLocationDetail = view.findViewById(R.id.selectedCampingAreaLocationDetail);
        selectedCampingAreaImageDetail = view.findViewById(R.id.selectedCampingAreaImageDetail);

        Bundle bundle = getArguments();
        if(bundle!=null) {
            selectedCampingAreaName = bundle.getString("selectedCampingAreaName");
            selectedCampingAreaLocation = bundle.getString("selectedCampingAreaLocation");
        }

            getcampingList();



        buttonFeatures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gridLayout.getVisibility() == View.VISIBLE)
                {
                    gridLayout.setVisibility(View.GONE);

                }
                else
                {

                    gridLayout.setVisibility(View.VISIBLE);
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



    private void getcampingList() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Camping Areas");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot campingSnapshot : snapshot.getChildren())
                {
                    CampingArea campingArea = campingSnapshot.getValue(CampingArea.class);
                    if(selectedCampingAreaName.equals(campingSnapshot.getValue(CampingArea.class).getName()) ||
                            selectedCampingAreaLocation.equals(campingSnapshot.getValue(CampingArea.class).getLocation()))
                    {
                       selectedCampingAreaNameDetail.setText(campingArea.getName());
                       selectedCampingAreaLocationDetail.setText(campingArea.getLocation());
                       Glide.with(getContext())
                               .load(campingArea.getGonderiResmi())
                               .into(selectedCampingAreaImageDetail);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}