package yagmurdan.sonra.toprakkokusu.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import yagmurdan.sonra.toprakkokusu.Adapter.CampingAreaAdapter;
import yagmurdan.sonra.toprakkokusu.Model.CampingArea;
import yagmurdan.sonra.toprakkokusu.R;
import yagmurdan.sonra.toprakkokusu.ui.CampingMainUI.CampingAreaDetailFragment;

public class WentCampingAreasFragment extends Fragment implements CampingAreaAdapter.OnCampingAreaListener {

    private RecyclerView recyclerView;
    private CampingAreaAdapter campingAreaAdapter;
    private List<CampingArea> wentCampingAreas = new ArrayList<>();
    private CampingArea selectedCampingArea;
    private FirebaseAuth mAuth;
    public List<String> wentCampingAreasId=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        wentCampingAreas.removeAll(wentCampingAreas);
        wentCampingAreasId.removeAll(wentCampingAreasId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_went_camping_areas, container, false);
        recyclerView = view.findViewById(R.id.RecyclerViewWentCampingAreas);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        campingAreaAdapter = new CampingAreaAdapter(getContext(), wentCampingAreas, this);
        recyclerView.setAdapter(campingAreaAdapter);

        mAuth= FirebaseAuth.getInstance();
        String user_id=mAuth.getCurrentUser().getUid();

        readWentCampingAreasId(user_id);
        readWentCampingAreasWithId();
        return view;
    }

    private void readWentCampingAreasId(String user_id) {
        DatabaseReference wentRef = FirebaseDatabase.getInstance().getReference("Users").child(user_id).child("wentCampingAreas");
        wentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    wentCampingAreasId.add(dataSnapshot.child("wentCampingAreaID").getValue(String.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readWentCampingAreasWithId() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Camping Areas");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wentCampingAreas.clear();
                for(DataSnapshot campingSnapshot : snapshot.getChildren())
                {
                    for (int i = 0; i<wentCampingAreasId.size();i++)
                    {
                        if(campingSnapshot.getKey().equals(wentCampingAreasId.get(i)))
                        {
                            CampingArea campingArea = campingSnapshot.getValue(CampingArea.class);
                            wentCampingAreas.add(campingArea);
                        }
                    }


                }
                campingAreaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onCampingAreaClick(int position) {
        CampingAreaDetailFragment campingAreaDetailFragment = new CampingAreaDetailFragment();


        selectedCampingArea = wentCampingAreas.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("selectedCampingAreaName",selectedCampingArea.getName());
        bundle.putString("selectedCampingAreaLocation", selectedCampingArea.getLocation());

        campingAreaDetailFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container,campingAreaDetailFragment).commit();
    }
}