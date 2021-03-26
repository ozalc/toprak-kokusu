package yagmurdan.sonra.toprakkokusu.ui.CampingMainUI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import yagmurdan.sonra.toprakkokusu.Model.CampingArea;
import yagmurdan.sonra.toprakkokusu.R;


public class CampingAreaDetailFragment extends Fragment {

    private String selectedCampingAreaName;
    private String selectedCampingAreaLocation;
    private TextView selectedCampingAreaNameDetail;

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
        selectedCampingAreaNameDetail = view.findViewById(R.id.selectedCampingAreaNameDetail);

        Bundle bundle = getArguments();
        if(bundle!=null) {
            selectedCampingAreaName = bundle.getString("selectedCampingAreaName");
            selectedCampingAreaLocation = bundle.getString("selectedCampingAreaLocation");
        }
            getcampingList();
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
                        selectedCampingAreaNameDetail.setText(selectedCampingAreaName);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}