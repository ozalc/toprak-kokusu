package yagmurdan.sonra.toprakkokusu.ui.CampingMainUI;


import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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


public class CampingMainPageFragment extends Fragment implements CampingAreaAdapter.OnCampingAreaListener {

    private RecyclerView recyclerView;
    private CampingAreaAdapter campingAreaAdapter;
    private List<CampingArea> campingList;
    private CampingArea selectedCampingArea;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camping_main_page, container, false);



        //recyclerview ayarları
        recyclerView = view.findViewById(R.id.RecyclerViewCampingMainPage);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        campingList = new ArrayList<>();

        //adapter oluşturulur
        campingAreaAdapter = new CampingAreaAdapter(getContext(), campingList, this);

        //recyclerview ile adapter eşleştirilir
        recyclerView.setAdapter(campingAreaAdapter);

        //kamp listesini getirme metodu
        campingListRead();



        return view;
    }



    //veritabanından datasnapshot ile camp listesini getiririz
    private void campingListRead ()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Camping Areas");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                campingList.clear();
                for(DataSnapshot campingSnapshot : snapshot.getChildren())
                {

                    CampingArea campingArea = campingSnapshot.getValue(CampingArea.class);
                    campingList.add(campingArea);

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

        selectedCampingArea = campingList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("selectedCampingAreaName",selectedCampingArea.getName());
        bundle.putString("selectedCampingAreaLocation", selectedCampingArea.getLocation());

        campingAreaDetailFragment.setArguments(bundle);


        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container,campingAreaDetailFragment).commit();
    }


}