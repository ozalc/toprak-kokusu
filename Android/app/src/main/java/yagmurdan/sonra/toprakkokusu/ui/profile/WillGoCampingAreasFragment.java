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

public class WillGoCampingAreasFragment extends Fragment implements CampingAreaAdapter.OnCampingAreaListener {

    private RecyclerView recyclerView;
    private CampingAreaAdapter campingAreaAdapter;
    private List<CampingArea> willGoCampingAreas = new ArrayList<>();
    private CampingArea selectedCampingArea;
    private FirebaseAuth mAuth;
    public List<String> willGoCampingAreasId=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public void onResume() {
        super.onResume();
        willGoCampingAreas.removeAll(willGoCampingAreas);
        willGoCampingAreasId.removeAll(willGoCampingAreasId);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_will_go_camping_areas, container, false);
        recyclerView = view.findViewById(R.id.RecyclerViewWillGoCampingAreas);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        campingAreaAdapter = new CampingAreaAdapter(getContext(), willGoCampingAreas, this);
        recyclerView.setAdapter(campingAreaAdapter);

        mAuth= FirebaseAuth.getInstance();
        String user_id=mAuth.getCurrentUser().getUid();

        readWillGoCampingAreasId(user_id);
        readWillGoCampingAreasWithId();
        return view;
    }
    private void readWillGoCampingAreasId(String user_id) {
        DatabaseReference willGoRef = FirebaseDatabase.getInstance().getReference("Users").child(user_id).child("willGoCampingAreas");
        willGoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    willGoCampingAreasId.add(dataSnapshot.child("willGoCampingAreaID").getValue(String.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readWillGoCampingAreasWithId() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Camping Areas");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                willGoCampingAreas.clear();
                for(DataSnapshot campingSnapshot : snapshot.getChildren())
                {
                    for (int i = 0; i<willGoCampingAreasId.size();i++)
                    {
                        if(campingSnapshot.getKey().equals(willGoCampingAreasId.get(i)))
                        {
                            CampingArea campingArea = campingSnapshot.getValue(CampingArea.class);
                            willGoCampingAreas.add(campingArea);
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


        selectedCampingArea = willGoCampingAreas.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("selectedCampingAreaName",selectedCampingArea.getName());
        bundle.putString("selectedCampingAreaLocation", selectedCampingArea.getLocation());

        campingAreaDetailFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container,campingAreaDetailFragment).commit();
    }
}