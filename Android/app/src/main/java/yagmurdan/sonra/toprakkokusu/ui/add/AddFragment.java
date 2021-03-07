package yagmurdan.sonra.toprakkokusu.ui.add;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import yagmurdan.sonra.toprakkokusu.R;
import yagmurdan.sonra.toprakkokusu.ui.Authentication.LoginActivity;
import yagmurdan.sonra.toprakkokusu.ui.profile.ProfileFragment;


public class AddFragment extends Fragment {
    private Button alanEkle;

    public AddFragment() {

    }


    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = (ViewGroup) inflater.inflate(R.layout.fragment_add, container, false);
        alanEkle = view.findViewById(R.id.alanEkle);


        alanEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addingIntent=new Intent(AddFragment.this.getActivity(), AddingScreen.class);
                startActivity(addingIntent);
            }
        });
        return view;

    }

}