package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.INavigation;
import com.taller2.hypechatapp.adapters.NavigationAdapter;
import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.model.NavigationDrawerShowable;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.services.NavigationMenuService;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.taller2.hypechatapp.ui.model.NavigationActionItem.ActionType.CREATE_ORGANIZATION;

public abstract class MenuActivity extends AppCompatActivity implements INavigation {

    private static final int CREATE_ORG_REQUEST_CODE = 1;
    private static final int RESULT_CODE = 300;

    private static final Integer ORG_ID = 1; //TODO: harcoded values, change me!

    private Toolbar toolbar;
    private NavigationAdapter navigationAdapter;

    private NavigationMenuService navigationMenuService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationMenuService = new NavigationMenuService();

        setupUI();
        setUpRecyclerView();
        showOrganizationUserInfo();
    }

    private void setupUI() {
        //setTitle("LALALA");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setUpRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.rvDrawerList);
        navigationAdapter = new NavigationAdapter(this, new ArrayList<NavigationDrawerShowable>());
        recyclerView.setAdapter(navigationAdapter);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this); // (Context context, int spanCount)
        mLinearLayoutManagerVertical.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void showOrganizationUserInfo() {
        ProgressBar loadingView = findViewById(R.id.loading_main);
        loadingView.setVisibility(View.VISIBLE);

        navigationMenuService.getNavigationMenuData(ORG_ID, new Client<List<Comparable>>() {
            @Override
            public void onResponseSuccess(List<Comparable> navigationUserInfo) {
                ProgressBar loadingView = findViewById(R.id.loading_main);
                loadingView.setVisibility(View.INVISIBLE);

                navigationAdapter.refreshAdapter(navigationUserInfo);
            }

            @Override
            public void onResponseError(String errorMessage) {
                ProgressBar loadingView = findViewById(R.id.loading_main);
                loadingView.setVisibility(View.INVISIBLE);
                String textToShow;
                if (!TextUtils.isEmpty(errorMessage)) {
                    textToShow = errorMessage;
                } else {
                    textToShow = "Ha ocurrido un error al intentar obtener los datos del usuario";
                }

                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
            }

            @Override
            public Context getContext() {
                return MenuActivity.this;
            }
        });
    }

    private void viewUserProfile() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    private void viewOrganizationProfile() {
        Intent intent = new Intent(this, OrganizationProfileActivity.class);
        startActivity(intent);
    }

    private void logOut(){
        FirebaseAuthService.logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void createNewChannel() {
        Intent intent = new Intent(this, CreateChannelActivity.class);
        startActivity(intent);
    }

    private void createNewOrganization() {
        Intent intent = new Intent(this, CreateOrganizationActivity.class);
        startActivity(intent);
    }

    private void viewSendInvitations() {
        Intent intent = new Intent(this,SendInvitationsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onViewClick(int position) {
        Toast.makeText(this, "position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onIconClick(String type) {
        if (CREATE_ORGANIZATION.toString().equals(type)) {
            createNewOrganization();
        } else {
            createNewChannel();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;

            case R.id.action_log_out:
                logOut();
                return true;

            case R.id.user_profile:
                viewUserProfile();
                return true;

            case R.id.organization_profile:
                viewOrganizationProfile();
                return true;

            case R.id.send_invitations:
                viewSendInvitations();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}