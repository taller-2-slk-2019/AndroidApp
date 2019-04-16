package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.INavigation;
import com.taller2.hypechatapp.adapters.NavigationAdapter;
import com.taller2.hypechatapp.adapters.OrganizationSpinnerAdapter;
import com.taller2.hypechatapp.components.PicassoLoader;
import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.model.NavigationDrawerShowable;
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;
import com.taller2.hypechatapp.services.NavigationMenuService;
import com.taller2.hypechatapp.services.OrganizationService;
import com.taller2.hypechatapp.services.UserService;

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

import static com.taller2.hypechatapp.ui.model.NavigationActionItem.ActionType.CREATE_DIRECT_MESSAGE;

public abstract class MenuActivity extends AppCompatActivity implements INavigation, AdapterView.OnItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ImageView userImage;
    private TextView userName;
    private ImageButton addOrganizationButton;
    private Spinner organizationsSpinner;
    private NavigationAdapter navigationAdapter;

    private OrganizationService organizationService;
    private NavigationMenuService navigationMenuService;
    private UserService userService;
    private UserManagerPreferences userManagerPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationMenuService = new NavigationMenuService();
        organizationService = new OrganizationService();
        userService = new UserService();
        userManagerPreferences = new UserManagerPreferences(this);

        setupUI();
        addOrganizationsInSpinner();
        addListenerOnSpinnerOrganizationSelection();
        setUpRecyclerView();
    }

    private void setupUI() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        userImage = findViewById(R.id.profile_image_view);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewUserProfile();
            }
        });

        userName = findViewById(R.id.txt_username);
        organizationsSpinner = findViewById(R.id.organizations_spinner);
        addOrganizationButton = findViewById(R.id.ib_add_organization);

        userService.getUser(new Client<User>() {

            @Override
            public void onResponseSuccess(User responseBody) {
                userName.setText(responseBody.getUsername());
                String url = responseBody.getPicture();
                PicassoLoader.load(getApplicationContext(), String.format("%s?type=large", url), userImage);
            }

            @Override
            public void onResponseError(String errorMessage) {

            }

            @Override
            public Context getContext() {
                return null;
            }
        });

        addOrganizationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewOrganization();
            }
        });

    }

    // add items into spinner dynamically
    public void addOrganizationsInSpinner() {
        organizationService.getOrganizationsByUser(new Client<List<Organization>>() {
            @Override
            public void onResponseSuccess(List<Organization> organizations) {

                //first time, the user does not have organization.
                if (organizations.isEmpty()) {
                    createNewOrganization();
                    finish();

                } else {

                    OrganizationSpinnerAdapter dataAdapter = new OrganizationSpinnerAdapter(getContext(),
                            android.R.layout.simple_spinner_item, organizations);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    organizationsSpinner.setAdapter(dataAdapter);

                    Integer selectedOrganizationPosition = getSelectedOrganizationPosition(organizations);
                    organizationsSpinner.setSelection(selectedOrganizationPosition);
                    userManagerPreferences.saveSelectedOrganization(organizations.get(selectedOrganizationPosition).getId());

                    showOrganizationUserInfo();
                }
            }

            @Override
            public void onResponseError(String errorMessage) {

            }

            @Override
            public Context getContext() {
                return MenuActivity.this;
            }
        });
    }

    private Integer getSelectedOrganizationPosition(List<Organization> organizations) {
        Integer position = 0;
        Integer selectedOrganizationId = userManagerPreferences.getSelectedOrganization();
        if (selectedOrganizationId > -1) {
            for (int idx = 0; idx < organizations.size(); idx ++) {
                if (organizations.get(idx).getId() == selectedOrganizationId) {
                    return idx;
                }
            }
        }

        return position;
    }

    public void addListenerOnSpinnerOrganizationSelection() {
        organizationsSpinner = findViewById(R.id.organizations_spinner);
        organizationsSpinner.setOnItemSelectedListener(this);
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

        navigationMenuService.getNavigationMenuData(userManagerPreferences.getSelectedOrganization(), new Client<List<Comparable>>() {
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
        intent.putExtra("ORGANIZATION_ID", userManagerPreferences.getSelectedOrganization());
        startActivity(intent);
    }

    private void createNewOrganization() {
        Intent intent = new Intent(this, CreateOrganizationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onViewClick(int position) {
        Toast.makeText(this, "position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onIconClick(String type) {
        if (CREATE_DIRECT_MESSAGE.toString().equals(type)) {
            //createNewOrganization();
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

            case R.id.organization_profile:
                viewOrganizationProfile();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Integer selectedOrganization = ((Organization) parent.getSelectedItem()).getId();
        Integer preferenceOrganization = userManagerPreferences.getSelectedOrganization();
        if (selectedOrganization != preferenceOrganization) {
            userManagerPreferences.saveSelectedOrganization(selectedOrganization);
            finish();
            startActivity(getIntent());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}