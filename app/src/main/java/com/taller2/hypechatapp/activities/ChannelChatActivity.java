package com.taller2.hypechatapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.UserService;

import java.util.ArrayList;
import java.util.List;

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
    UserService userService;
    private NavigationView navigationView;
    private SubMenu organizationsSubMenu;
    private SubMenu channelsSubMenu;
    private List<Channel> channels;
    private List<Organization> organizations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userService=new UserService();

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

//        userService.getUser(1, new Client<User>() {
//            @Override
//            public void onResponseSuccess(User user) {
//                loadOrganizationsToDrawerMenu(user.getOrganizations());
//            }
//
//            @Override
//            public void onResponseError(String errorMessage) {
//                Log.d(TAG,errorMessage);
//            }
//
//            @Override
//            public Context getContext() {
//                return ChannelChatActivity.this;
//            }
//        });

        Organization organization1=new Organization();
        organization1.setId(0);
        organization1.setName("ORGA 1");

        Organization organization2=new Organization();
        organization2.setId(1);
        organization2.setName("ORGA 2");

        Organization organization3=new Organization();
        organization3.setId(2);
        organization3.setName("ORGA 3");

        List<Organization> organizationsMock=new ArrayList<>();
        organizationsMock.add(organization1);
        organizationsMock.add(organization2);
        organizationsMock.add(organization3);

        loadOrganizationsToDrawerMenu(organizationsMock);

        Channel channel1=new Channel();
        channel1.setId(0);
        channel1.setName("CHANNEL 1");

        Channel channel2=new Channel();
        channel2.setId(0);
        channel2.setName("CHANNEL 1");

        Channel channel3=new Channel();
        channel3.setId(0);
        channel3.setName("CHANNEL 1");

        List<Channel> channelMock=new ArrayList<>();
        channelMock.add(channel1);
        channelMock.add(channel2);
        channelMock.add(channel3);


        loadChannelsToDrawerMenu(channelMock);

    }

    private void loadOrganizationsToDrawerMenu(List<Organization> organizations) {
        Menu menu=navigationView.getMenu();
        organizationsSubMenu=menu.addSubMenu("Organizaciones");
        int i=0;
        for (Organization organization : organizations) {
            MenuItem menuItem=organizationsSubMenu.add(ORGANIZATIONS_GROUP_ID,organization.getId(),i,organization.getName());
            //menuItem.setIcon(R.drawable.ic_menu_camera);
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
            //menuItem.setIcon(R.drawable.ic_menu_camera);
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
        if (id == R.id.action_settings) {
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

    private void createNewChannel() {
    }

    private void createNewOrganization() {
        Intent intent = new Intent(this,CreateOrganizationActivityStepOne.class);
        startActivity(intent);
    }
}