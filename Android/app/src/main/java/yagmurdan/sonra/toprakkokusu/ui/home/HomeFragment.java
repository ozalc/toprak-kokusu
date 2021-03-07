package yagmurdan.sonra.toprakkokusu.ui.home;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import yagmurdan.sonra.toprakkokusu.R;
import yagmurdan.sonra.toprakkokusu.ui.Authentication.LoginActivity;
import yagmurdan.sonra.toprakkokusu.ui.CampingMainUI.CampingMainPageFragment;
import yagmurdan.sonra.toprakkokusu.ui.profile.ProfileFragment;


public class HomeFragment extends Fragment {

        private ImageButton cadirKampi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        cadirKampi = view.findViewById(R.id.campButton);

        cadirKampi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CampingMainPageFragment campingMainPageFragment = new CampingMainPageFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,campingMainPageFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        return view;

    }


}