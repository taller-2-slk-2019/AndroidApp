package com.taller2.hypechatapp.ui.activities;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.IMenuItemsClick;
import com.taller2.hypechatapp.adapters.MenuChannelsAdapter;
import com.taller2.hypechatapp.adapters.MenuConversationsAdapter;
import com.taller2.hypechatapp.adapters.OrganizationSpinnerAdapter;
import com.taller2.hypechatapp.components.PermissionsRequester;
import com.taller2.hypechatapp.components.PicassoLoader;
import com.taller2.hypechatapp.components.UserLocationService;
import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.model.Conversation;
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.model.UserLocationRequest;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;
import com.taller2.hypechatapp.services.ChannelService;
import com.taller2.hypechatapp.services.ConversationService;
import com.taller2.hypechatapp.services.OrganizationService;
import com.taller2.hypechatapp.services.UserService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class MenuActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, IMenuItemsClick, UserLocationService.UserLocationListener {

    protected Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ImageView userImage;
    private TextView userName;
    private Spinner organizationsSpinner;
    private UserProfileUpdate userProfileUpdate;
    private MenuChannelsAdapter channelsAdapter;
    private MenuConversationsAdapter conversationsAdapter;

    private OrganizationService organizationService;
    private UserService userService;
    private ChannelService channelsService;
    private ConversationService conversationsService;
    protected UserManagerPreferences userManagerPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        organizationService = new OrganizationService();
        userService = new UserService();
        channelsService = new ChannelService();
        conversationsService = new ConversationService();
        userManagerPreferences = new UserManagerPreferences(this);
        userProfileUpdate = new UserProfileUpdate();


        setUserPreferences();
        setupUI();
        addOrganizationsInSpinner();
        addListenerOnSpinnerOrganizationSelection();
        setUpChannels();
        setUpConversations();

        EventBus.getDefault().register(this);

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
        ImageButton addOrganizationButton = findViewById(R.id.ib_add_organization);

        userService.getUser(userProfileUpdate);

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
                    welcome();
                    finish();
                } else {
                    setOrganizationsToSpinner(organizations);
                    showOrganizationUserInfo();
                }
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                Toast.makeText(getContext(), R.string.fail_getting_info, Toast.LENGTH_SHORT).show();
            }

            @Override
            public Context getContext() {
                return MenuActivity.this;
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onJoinOrganizationEvent(Organization organization) {
        OrganizationSpinnerAdapter dataAdapter = (OrganizationSpinnerAdapter) organizationsSpinner.getAdapter();
        dataAdapter.add(organization);
        dataAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onJoinChannelEvent(Channel channel) {
        channelsAdapter.add(channel);
        if (channel.getId().equals(userManagerPreferences.getSelectedChannel())) {
            toolbar.setTitle(channel.getName());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onJoinConversationEvent(Conversation conversation) {
        conversationsAdapter.add(conversation);
    }

    private void setOrganizationsToSpinner(List<Organization> organizations) {
        OrganizationSpinnerAdapter dataAdapter = new OrganizationSpinnerAdapter(this,
                android.R.layout.simple_spinner_item, organizations);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        organizationsSpinner.setAdapter(dataAdapter);

        Integer selectedOrganizationPosition = getSelectedOrganizationPosition(organizations);
        organizationsSpinner.setSelection(selectedOrganizationPosition);

        Organization selectedOrganization = organizations.get(selectedOrganizationPosition);
        userManagerPreferences.saveSelectedOrganization(selectedOrganization.getId());
        userManagerPreferences.saveOrganizationRole(selectedOrganization.getRole());
    }

    private Integer getSelectedOrganizationPosition(List<Organization> organizations) {
        Integer selectedOrganizationId = userManagerPreferences.getSelectedOrganization();
        for (int idx = 0; idx < organizations.size(); idx++) {
            if (organizations.get(idx).getId().equals(selectedOrganizationId)) {
                return idx;
            }
        }

        return 0;
    }

    public void addListenerOnSpinnerOrganizationSelection() {
        organizationsSpinner.setOnItemSelectedListener(this);
    }

    private void setUpChannels() {
        RecyclerView rvChannels = findViewById(R.id.rvChannels);
        channelsAdapter = new MenuChannelsAdapter(this, false);
        rvChannels.setAdapter(channelsAdapter);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this);
        mLinearLayoutManagerVertical.setOrientation(RecyclerView.VERTICAL);
        rvChannels.setLayoutManager(mLinearLayoutManagerVertical);
        rvChannels.setItemAnimator(new DefaultItemAnimator());

        View newChannel = findViewById(R.id.new_channel_layout);
        TextView channelTitle = newChannel.findViewById(R.id.item_title);
        channelTitle.setText(R.string.menu_channels);
        channelTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, PublicChannelsActivity.class);
                startActivity(intent);
            }
        });
        ImageView channelButton = newChannel.findViewById(R.id.item_img);
        channelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewChannel();
            }
        });
    }

    private void setUpConversations() {
        RecyclerView rvConversations = findViewById(R.id.rvConversations);
        conversationsAdapter = new MenuConversationsAdapter(this);
        rvConversations.setAdapter(conversationsAdapter);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this);
        mLinearLayoutManagerVertical.setOrientation(RecyclerView.VERTICAL);
        rvConversations.setLayoutManager(mLinearLayoutManagerVertical);
        rvConversations.setItemAnimator(new DefaultItemAnimator());

        View newConversation = findViewById(R.id.new_conversation_layout);
        TextView conversationTitle = newConversation.findViewById(R.id.item_title);
        conversationTitle.setText(R.string.menu_conversations);
        ImageView conversationButton = newConversation.findViewById(R.id.item_img);
        conversationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewConversation();
            }
        });
    }

    private void showOrganizationUserInfo() {
        channelsService.getChannelsByOrganizationAndUser(userManagerPreferences.getSelectedOrganization(), new Client<List<Channel>>() {
            @Override
            public void onResponseSuccess(List<Channel> channels) {
                channelsAdapter.setChannels(channels);
                selectChannel();
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                Toast.makeText(getContext(), R.string.fail_getting_info, Toast.LENGTH_LONG).show();
            }

            @Override
            public Context getContext() {
                return MenuActivity.this;
            }
        });

        conversationsService.getConversationsByOrganizationAndUser(userManagerPreferences.getSelectedOrganization(), new Client<List<Conversation>>() {
            @Override
            public void onResponseSuccess(List<Conversation> conversations) {
                conversationsAdapter.setConversations(conversations);
                selectConversation();
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                Toast.makeText(getContext(), R.string.fail_getting_info, Toast.LENGTH_LONG).show();
            }

            @Override
            public Context getContext() {
                return MenuActivity.this;
            }
        });
    }

    private void setUserPreferences() {
        if (getIntent().getExtras() != null) {
            int organizationId = getIntent().getExtras().getInt("organizationId", 0);
            if (organizationId > 0) {
                userManagerPreferences.saveSelectedOrganization(organizationId);
            }
            int channelId = getIntent().getExtras().getInt("channelId", 0);
            if (channelId > 0) {
                userManagerPreferences.saveSelectedChannel(channelId);
            }
            int conversationId = getIntent().getExtras().getInt("conversationId", 0);
            if (conversationId > 0) {
                userManagerPreferences.saveSelectedConversation(conversationId);
            }
        }
    }

    private void selectChannel() {
        if (userManagerPreferences.getSelectedConversation() > 0) {
            return;
        }

        List<Channel> channels = channelsAdapter.getChannels();
        Integer selectedChannelId = userManagerPreferences.getSelectedChannel();
        for (Channel channel : channels) {
            if (channel.getId().equals(selectedChannelId)) {
                toolbar.setTitle(channel.getName());
                this.onChatSelected();
                return;
            }
        }
        userManagerPreferences.clearSelectedChannel();
        this.onChatSelected();
    }

    private void selectConversation() {
        if (userManagerPreferences.getSelectedChannel() > 0) {
            return;
        }

        List<Conversation> conversations = conversationsAdapter.getConversations();
        Integer selectedConversationId = userManagerPreferences.getSelectedConversation();
        for (Conversation conversation : conversations) {
            if (conversation.id.equals(selectedConversationId)) {
                toolbar.setTitle(conversation.getName());
                this.onChatSelected();
                return;
            }
        }

        userManagerPreferences.clearSelectedConversation();
        this.onChatSelected();
    }

    private void viewUserProfile() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    private void showOrganizationProfile() {
        Intent intent = new Intent(this, OrganizationProfileActivity.class);
        startActivity(intent);
    }

    private void logOut() {
        FirebaseAuthService.logOut(this);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void welcome() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

    private void createNewChannel() {
        Intent intent = new Intent(this, CreateChannelActivity.class);
        startActivity(intent);
    }

    private void createNewConversation() {
        Intent intent = new Intent(this, CreateConversationActivity.class);
        startActivity(intent);
    }

    private void createNewOrganization() {
        Intent intent = new Intent(this, CreateOrganizationActivity.class);
        startActivity(intent);
    }

    private void viewReceivedInvitations() {
        Intent intent = new Intent(this, ReceivedInvitationsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
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
            case R.id.action_log_out:
                logOut();
                return true;
            case R.id.received_invitations:
                viewReceivedInvitations();
                return true;
            case R.id.organization_profile:
                showOrganizationProfile();
                return true;
            case R.id.update_location:
                updateUserLocation();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // new organization selected
        Organization selectedOrganization = (Organization) parent.getSelectedItem();
        Integer preferenceOrganization = userManagerPreferences.getSelectedOrganization();
        if (!selectedOrganization.getId().equals(preferenceOrganization)) {
            userManagerPreferences.saveSelectedOrganization(selectedOrganization.getId());
            userManagerPreferences.saveOrganizationRole(selectedOrganization.getRole());
            finish();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(getIntent(),
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            } else {
                startActivity(getIntent());
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // Auto-generated method stub
    }

    @Override
    protected void onResume() {
        super.onResume();
        userService.getUser(userProfileUpdate);
    }

    public class UserProfileUpdate implements Client<User> {

        @Override
        public void onResponseSuccess(User responseBody) {
            userName.setText(responseBody.getUsername());
            String url = responseBody.getPicture();
            PicassoLoader.load(getApplicationContext(), String.format("%s?type=large", url), R.drawable.default_user, userImage);
        }

        @Override
        public void onResponseError(boolean connectionError, String errorMessage) {
            Toast.makeText(getContext(), R.string.fail_getting_info, Toast.LENGTH_SHORT).show();

        }

        @Override
        public Context getContext() {
            return MenuActivity.this;
        }

    }

    protected abstract void onChatSelected();

    @Override
    public void onChannelClick(Channel channel) {
        toolbar.setTitle(channel.getName());
        userManagerPreferences.saveSelectedChannel(channel.getId());
        drawerLayout.closeDrawer(GravityCompat.START);
        onChatSelected();
    }

    @Override
    public void onConversationClick(Conversation conversation) {
        toolbar.setTitle(conversation.getName());
        userManagerPreferences.saveSelectedConversation(conversation.id);
        drawerLayout.closeDrawer(GravityCompat.START);
        onChatSelected();
    }

    private void updateUserLocation() {
        if (PermissionsRequester.hasPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            getAndUpdateUserLocation();
        } else {
            PermissionsRequester.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        boolean hasPermission = PermissionsRequester.analyzeResults(requestCode, grantResults);
        if (hasPermission) {
            getAndUpdateUserLocation();
        } else {
            Toast.makeText(this,
                    "Necesitas aceptar los permisos para enviar tu ubicación", Toast.LENGTH_LONG).show();
        }
    }

    private void getAndUpdateUserLocation() {
        UserLocationService.getUserLocation(this, this);
    }

    @Override
    public void userLocationReceived(Location location) {
        UserLocationRequest request = new UserLocationRequest(location);
        userService.updateUserLocation(request, new Client<Void>() {
            @Override
            public void onResponseSuccess(Void responseBody) {
                Toast.makeText(getContext(), "Ubicación actualizada", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                userLocationError();
            }

            @Override
            public Context getContext() {
                return MenuActivity.this;
            }
        });
    }

    @Override
    public void userLocationError() {
        Toast.makeText(this, "No se pudo actualizar la ubicación", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}