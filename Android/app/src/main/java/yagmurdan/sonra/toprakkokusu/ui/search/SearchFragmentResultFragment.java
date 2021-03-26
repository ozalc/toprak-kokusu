package yagmurdan.sonra.toprakkokusu.ui.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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



public class SearchFragmentResultFragment extends Fragment implements CampingAreaAdapter.OnCampingAreaListener {

    private RecyclerView recyclerView;
    private CampingAreaAdapter campingAreaAdapter;
    private List<CampingArea> campingList;
    private String EditTextValue = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_result, container, false);


        //önceki fragment'dan gönderilen veriler alınır
        Bundle bundle = getArguments();
        if(bundle!=null) {
            EditTextValue = bundle.getString("EditTextValue");
        }



        //getirilecek sonuçlar için recyclerview kullanılır
        recyclerView = view.findViewById(R.id.RecyclerViewSearchResult);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        campingList = new ArrayList<>();

        campingAreaAdapter = new CampingAreaAdapter(getContext(), campingList, this);

        recyclerView.setAdapter(campingAreaAdapter);

        getcampingList();

        return view;
    }

    private void getcampingList() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Camping Areas");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                campingList.clear();

                for (DataSnapshot campingSnapshot : snapshot.getChildren())
                {
                   CampingArea campingArea = campingSnapshot.getValue(CampingArea.class);
                   if(campingArea.getName().equalsIgnoreCase(EditTextValue) || campingArea.getLocation().equalsIgnoreCase(EditTextValue))
                   {
                       campingList.add(campingArea);
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

    }
}