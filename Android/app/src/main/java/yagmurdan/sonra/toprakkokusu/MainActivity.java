package yagmurdan.sonra.toprakkokusu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import yagmurdan.sonra.toprakkokusu.ui.Authentication.LoginActivity;
import yagmurdan.sonra.toprakkokusu.ui.add.AddFragment;
import yagmurdan.sonra.toprakkokusu.ui.home.HomeFragment;
import yagmurdan.sonra.toprakkokusu.ui.profile.ProfileFragment;
import yagmurdan.sonra.toprakkokusu.ui.search.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth=FirebaseAuth.getInstance();

        //Giriş yapmamış kullanıcı var ise login ekranına yönlendirilir
        if(mAuth.getCurrentUser()!=null)
        {
            setContentView(R.layout.activity_main);
        }
        else
            {
                Intent loginIntent=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                Toast.makeText(getApplicationContext(), "Lütfen Giriş Yapınız", Toast.LENGTH_SHORT).show();

            }


        loadFragment(new HomeFragment());
        BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

    }

    //BottomNavigatioView'e seçili navigation butonu için bir listener tanımlanır
    // ve böylelikle seçili buton'un id'si ile birlikte istenilen ekran açılır
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_search:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.nav_add:
                    selectedFragment = new AddFragment();
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }
                loadFragment(selectedFragment);

            return true;
        }
    };

    //parametre olarak gönderilen fragment'ı yükleme fonksiyonu
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }



}