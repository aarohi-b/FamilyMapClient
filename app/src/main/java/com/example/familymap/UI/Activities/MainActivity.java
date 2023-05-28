package com.example.familymap.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import com.example.familymap.R;
import com.example.familymap.UI.LoginFragment;
import com.example.familymap.UI.MapFragment;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener {
    private FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Iconify.with(new FontAwesomeModule());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment frag = fm.findFragmentById(R.id.mainLayout);

        //if user logged in, go to map fragment
        if ((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey("Re-sync"))){
            Fragment mapFragment = new MapFragment();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.add(R.id.mainLayout, mapFragment).commit();
        }
        //else goto login fragment
        else if (frag == null) {
            frag = new LoginFragment();
            ((LoginFragment) frag).setLoginListener(this);
            fm.beginTransaction().add(R.id.mainLayout, frag).commit();
        }
    }

    //goto map fragment when user done logging in
    public void loginComplete() {
        Fragment mapFragment = new MapFragment();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.mainLayout, mapFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}