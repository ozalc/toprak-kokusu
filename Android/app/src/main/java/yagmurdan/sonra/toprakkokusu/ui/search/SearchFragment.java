package yagmurdan.sonra.toprakkokusu.ui.search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import yagmurdan.sonra.toprakkokusu.R;
import yagmurdan.sonra.toprakkokusu.ui.Authentication.LoginActivity;
import yagmurdan.sonra.toprakkokusu.ui.profile.ProfileFragment;

public class SearchFragment extends Fragment {

    public EditText EditTextSearchFragment;
    protected Button ButtonSearchFragment;


    public SearchFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        EditTextSearchFragment = view.findViewById(R.id.EditTextSearchFragment);
        ButtonSearchFragment = view.findViewById(R.id.ButtonSearchFragment);


        ButtonSearchFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bundle yapısı ile fragment'tan fragment'a veri gönderme
                //Kullanıcının girdiği şehir-kamp adı bilgisi diğer fragment'a gönderilir
                SearchFragmentResultFragment searchFragmentResultFragment = new SearchFragmentResultFragment();
                Bundle bundle = new Bundle();
                bundle.putString("EditTextValue",EditTextSearchFragment.getText().toString());

                searchFragmentResultFragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.fragment_container,searchFragmentResultFragment).commit();



            }
        });


        return view;
    }
}