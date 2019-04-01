package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.services.ChannelService;
import com.taller2.hypechatapp.services.OrganizationService;
import com.taller2.hypechatapp.services.UserService;
import com.taller2.hypechatapp.firebase.FirebaseAuthService;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class ChannelChatActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "CHANNELCHATACTIVITY";
    public static final int ORGANIZATIONS_GROUP_ID = 0;
    public static final int NEW_ORGA_CHANNEL_ID = -1;
    public static final int CHANNELS_GROUP_ID = 1;
    private static final int CREATE_ORG_REQUEST_CODE = 1;
    private static final int RESULT_CODE = 300;

    private NavigationView navigationView;
    private SubMenu organizationsSubMenu;
    private SubMenu channelsSubMenu;

    private UserService userService;
    private OrganizationService organizationService;
    private ChannelService channelsService;

    private List<Channel> channels;
    private List<Organization> organizations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userService=new UserService();
        organizationService=new OrganizationService();
        channelsService=new ChannelService();

        setupUI();
    }

    private void setupUI() {
        /** TODO Usar esto para poner el nombre de la organización seleccionada en este momento*/
        //setTitle("LALALA");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setUpDrawerMenu();
    }

    private void setUpDrawerMenu() {
        Menu menu=navigationView.getMenu();
        menu.clear();
        ProgressBar loadingView = findViewById(R.id.loading_main);
        loadingView.setVisibility(View.VISIBLE);

        organizationService.getOrganizationsByUser(1, new Client<List<Organization>>() {
            @Override
            public void onResponseSuccess(List<Organization> organizations) {
                ProgressBar loadingView = findViewById(R.id.loading_main);
                loadingView.setVisibility(View.INVISIBLE);
                loadOrganizationsToDrawerMenu(organizations);
                //TODO Eliminar esto cuando este el ChannelService funcionando
                loadChannelsToDrawerMenu(new ArrayList<Channel>());
            }

            @Override
            public void onResponseError(String errorMessage) {
                String textToShow;
                if(!TextUtils.isEmpty(errorMessage)){
                    textToShow=errorMessage;
                } else {
                    textToShow="Ha ocurrido un error al intentar obtener las organizaciones del usuario";
                }

                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();

            }

            @Override
            public Context getContext() {
                return ChannelChatActivity.this;
            }
        });

        /*channelService.getChannelsByOrganizationByUser(1, 1,new Client<List<Organization>>() {
            @Override
            public void onResponseSuccess(List<Organization> organizations) {
                ProgressBar loadingView = findViewById(R.id.loading_main);
                loadingView.setVisibility(View.INVISIBLE);
                loadOrganizationsToDrawerMenu(organizations);
            }

            @Override
            public void onResponseError(String errorMessage) {
                String textToShow;
                if(!TextUtils.isEmpty(errorMessage)){
                    textToShow=errorMessage;
                } else {
                    textToShow="Ha ocurrido un error al intentar obtener las organizaciones del usuario";
                }

                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();

            }

            @Override
            public Context getContext() {
                return ChannelChatActivity.this;
            }
        });*/

   }

    private void loadOrganizationsToDrawerMenu(List<Organization> organizations) {
        this.organizations=organizations;
        Menu menu=navigationView.getMenu();
        organizationsSubMenu=menu.addSubMenu("Organizaciones");
        int i=0;
        for (Organization organization : organizations) {
            MenuItem menuItem=organizationsSubMenu.add(ORGANIZATIONS_GROUP_ID,organization.getId(),i,organization.getName());
            i++;
        }

        organizationsSubMenu.add(ORGANIZATIONS_GROUP_ID, NEW_ORGA_CHANNEL_ID,i,"Nueva Organización...");
        navigationView.invalidate();
    }

    private void loadChannelsToDrawerMenu(List<Channel> channels) {
        Menu menu=navigationView.getMenu();
        channelsSubMenu=menu.addSubMenu("Canales");
        int i=0;
        for (Channel channel : channels) {
            MenuItem menuItem=channelsSubMenu.add(CHANNELS_GROUP_ID,channel.getId(),i,channel.getName());
            i++;
        }

        channelsSubMenu.add(CHANNELS_GROUP_ID,NEW_ORGA_CHANNEL_ID,i,"Nuevo Canal...");

        navigationView.invalidate();
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
        switch(id) {
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
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //Toco en una organización
        if(item.getGroupId()==0){
            if(item.getItemId()==NEW_ORGA_CHANNEL_ID);
                createNewOrganization();

        }
        //Toco en un canal
        if(item.getGroupId()==1){
            if(item.getItemId()==NEW_ORGA_CHANNEL_ID);
                createNewChannel();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void viewUserProfile() {
        Intent intent = new Intent(this,UserProfileActivity.class);
        startActivity(intent);
    }

    private void logOut(){
        FirebaseAuthService.logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void viewOrganizationProfile() {
        Intent intent = new Intent(this, OrganizationProfileActivity.class);
        startActivity(intent);
    }

    private void createNewChannel() {
        Intent intent = new Intent(this, CreateChannelActivity.class);
        startActivity(intent);
    }

    private void createNewOrganization() {
        Intent intent = new Intent(this,CreateOrganizationActivityStepOne.class);
        startActivityForResult(intent, CREATE_ORG_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_ORG_REQUEST_CODE && resultCode == RESULT_CODE) {
            Organization createdOrganization = (Organization) data.getSerializableExtra("createdOrganization");
            Toast.makeText(ChannelChatActivity.this, "Se ha creado la Organización con id:"+createdOrganization.getId(),
                   Toast.LENGTH_LONG).show();

            setUpDrawerMenu();
        }
    }
}