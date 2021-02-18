package com.example.workitapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.workitapp.Fragments.Fragment_Manager_Assignments;
import com.example.workitapp.Fragments.Fragment_Profile;
import com.example.workitapp.Fragments.Fragment_Unauthorized;
import com.example.workitapp.More.Constants;
import com.example.workitapp.Fragments.Fragment_Worker_Assignments;
import com.example.workitapp.Fragments.Fragment_Requests;
import com.example.workitapp.Fragments.Fragment_Statistics;
import com.example.workitapp.Objects.MyFirebase;
import com.example.workitapp.Objects.MySPV;
import com.example.workitapp.Objects.Worker;
import com.example.workitapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Activity_Main extends Activity_Base implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout main_DRL_drawer;
    private NavigationView main_NAV_navigation;
    private ImageView header_IMG_profile;
    private TextView header_LBL_email;
    private TextView header_LBL_name;

    private ValueEventListener workerChanged;

    private androidx.appcompat.widget.Toolbar main_TLB_toolbar;
    private FrameLayout main_FRL_container;
    Worker w = new Worker("Arad Pelled", "ap@gmail.com", "aklaksk", 20);
    private Worker currentWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init w from firebase
        findViews();
        initListener();
        getWorker();
        setSupportActionBar(main_TLB_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // hide or show items
//        showItems
        hideItems();

        main_NAV_navigation.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, main_DRL_drawer, main_TLB_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        main_DRL_drawer.addDrawerListener(toggle);
        toggle.syncState();
        main_NAV_navigation.setNavigationItemSelectedListener(this);

        // show home only if the app start now
        if (savedInstanceState == null) {
            main_NAV_navigation.setCheckedItem(R.id.menu_ITM_home);
            getSupportFragmentManager().beginTransaction().replace(main_FRL_container.getId(), new Fragment_Profile()).commit();
//                getSupportFragmentManager().beginTransaction().replace(main_FRL_container.getId(), new Fragment_Statistics()).commit();
        }
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        MyFirebase.getInstance().getFdb().getReference().removeEventListener(workerChanged);
//    }

    private void initListener() {
        workerChanged = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentWorker = snapshot.getValue(Worker.class);
                showItems();
                Log.d("aaa", "Main : " + currentWorker.getUid());
                initViews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    private void hideItems() {
        Menu menu = main_NAV_navigation.getMenu();
        menu.findItem(R.id.menu_ITM_requests).setVisible(false);
        menu.findItem(R.id.menu_ITM_home).setVisible(false);
        menu.findItem(R.id.menu_ITM_stats).setVisible(false);
        menu.findItem(R.id.menu_ITM_profile).setVisible(false);
        menu.findItem(R.id.menu_ITM_assign).setVisible(false);
    }

    private void showItems() {
        Menu menu = main_NAV_navigation.getMenu();
        if (currentWorker != null) {
            hideItems();
            if (currentWorker.getIsAccepted()) {
//                getSupportFragmentManager().beginTransaction().replace(main_FRL_container.getId(), new Fragment_Profile()).commitAllowingStateLoss();
                switch (currentWorker.getType()) {
                    case Constants.MANAGER_ID:
                        menu.findItem(R.id.menu_ITM_stats).setVisible(true);
                        menu.findItem(R.id.menu_ITM_assign).setVisible(true);
                        menu.findItem(R.id.menu_ITM_profile).setVisible(true);
                        break;
                    case Constants.WORKER_ID:
                        menu.findItem(R.id.menu_ITM_assign).setVisible(true);
                        menu.findItem(R.id.menu_ITM_profile).setVisible(true);
                        break;
                    case Constants.HR_ID:
                        menu.findItem(R.id.menu_ITM_assign).setVisible(true);
                        menu.findItem(R.id.menu_ITM_requests).setVisible(true);
                        menu.findItem(R.id.menu_ITM_profile).setVisible(true);
                        break;
                }
//                getSupportFragmentManager().beginTransaction().replace(main_FRL_container.getId(), new Fragment_Profile()).commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(main_FRL_container.getId(), new Fragment_Unauthorized()).commitAllowingStateLoss();
            }
        }
//        menu.findItem(R.id.menu_ITM_login).setVisible(false);
    }

    private void initViews() {
        if (!currentWorker.getImgUrl().equals(Constants.DEFAULT))
            setImage(currentWorker.getImgUrl(), header_IMG_profile);
        else
            header_IMG_profile.setImageResource(Constants.PROFILE_DEFAULT); // vector drawable
        header_LBL_email.setText(currentWorker.getEmail());
        header_LBL_name.setText(currentWorker.getName());
    }

    private void getWorker() {
        FirebaseUser user = MyFirebase.getInstance().getUser();
        String uid = null;
        if (user != null)
            uid = user.getUid();
        if (uid != null) {
            MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH).child(uid).addValueEventListener(workerChanged);
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

        View header = main_NAV_navigation.getHeaderView(0);
        header_IMG_profile = header.findViewById(R.id.header_IMG_profile);
        header_LBL_email = header.findViewById(R.id.header_LBL_email);
        header_LBL_name = header.findViewById(R.id.header_LBL_name);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        openFragment(item);
        main_DRL_drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openFragment(MenuItem item) {
        if (!currentWorker.getIsAccepted()) {

        }

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
                if (currentWorker.getType() == Constants.MANAGER_ID)
                    getSupportFragmentManager().beginTransaction().replace(main_FRL_container.getId(), new Fragment_Manager_Assignments()).commit();
                else
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
    }

    private void signOut() {
        MyFirebase.getInstance().getFdb().getReference(Constants.WORKER_PATH).child(currentWorker.getUid()).removeEventListener(workerChanged);
        FirebaseAuth auth = MyFirebase.getInstance().getAuth();
        if (auth != null) {
            FirebaseUser user = auth.getCurrentUser();
            auth.signOut();
            Log.d("aaa", "Signed Out.");
        } else
            Log.d("aaa", "Didn't Signed Out.");
        MySPV.getInstance().putBool(Constants.REMEMBER, false);
        MySPV.getInstance().removeKey(Constants.UID);
        Intent i = new Intent(Activity_Main.this, Activity_Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}