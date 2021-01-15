package com.example.workitapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.workitapp.Fragments.Fragment_Manager_Assignments;
import com.example.workitapp.Fragments.Fragment_Profile;
import com.example.workitapp.More.Constants;
import com.example.workitapp.Fragments.Fragment_Worker_Assignments;
import com.example.workitapp.Fragments.Fragment_Requests;
import com.example.workitapp.Fragments.Fragment_Statistics;
import com.example.workitapp.Objects.MySPV;
import com.example.workitapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Activity_Main extends Activity_Base implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout main_DRL_drawer;
    private NavigationView main_NAV_navigation;

    private androidx.appcompat.widget.Toolbar main_TLB_toolbar;
    private FrameLayout main_FRL_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViews();
        setSupportActionBar(main_TLB_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // hide or show items
        Menu menu = main_NAV_navigation.getMenu();
//        menu.findItem(R.id.menu_ITM_login).setVisible(false);

        main_NAV_navigation.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, main_DRL_drawer, main_TLB_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        main_DRL_drawer.addDrawerListener(toggle);
        toggle.syncState();
        main_NAV_navigation.setNavigationItemSelectedListener(this);

        // show home only if the app start now
        if(savedInstanceState == null) {
            main_NAV_navigation.setCheckedItem(R.id.menu_ITM_home);
//                getSupportFragmentManager().beginTransaction().replace(main_FRL_container.getId(), new Fragment_Statistics()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (main_DRL_drawer.isDrawerOpen(GravityCompat.START)) {
            main_DRL_drawer.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    private void findViews() {
        main_TLB_toolbar = findViewById(R.id.main_TLB_toolbar);
        main_DRL_drawer = findViewById(R.id.main_DRL_drawer);
        main_NAV_navigation = findViewById(R.id.main_NAV_navigation);
        main_FRL_container = findViewById(R.id.main_FRL_container);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_ITM_home:
//                getSupportFragmentManager().beginTransaction().replace(main_FRL_container.getId(), new Fragment_Statistics()).commit();
//                Intent i = new Intent(Activity_Main.this, Activity_Unauthorized.class);
//                startActivity(i);
//                finish();
                getSupportFragmentManager().beginTransaction().replace(main_FRL_container.getId(), new Fragment_Manager_Assignments()).commit();
                break;
            case R.id.menu_ITM_stats:
                getSupportFragmentManager().beginTransaction().replace(main_FRL_container.getId(), new Fragment_Statistics()).commit();
                break;
            case R.id.menu_ITM_assign:
                getSupportFragmentManager().beginTransaction().replace(main_FRL_container.getId(), new Fragment_Worker_Assignments()).commit();
                break;
            case R.id.menu_ITM_requests:
                getSupportFragmentManager().beginTransaction().replace(main_FRL_container.getId(), new Fragment_Requests()).commit();
                break;
            case R.id.menu_ITM_logout:
                signOut();
                break;
            case R.id.menu_ITM_profile:
                getSupportFragmentManager().beginTransaction().replace(main_FRL_container.getId(), new Fragment_Profile()).commit();
                break;

        }
        main_DRL_drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        MySPV.getInstance().putBool(Constants.REMEMBER,false);
        Intent i = new Intent(Activity_Main.this, Activity_Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
//        if(user != null){
//            openApp();
//        }else{
//            login();
//        }
    }
}