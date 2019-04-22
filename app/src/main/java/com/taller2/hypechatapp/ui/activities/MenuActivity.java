package com.taller2.hypechatapp.ui.activities;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
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
import com.taller2.hypechatapp.components.PicassoLoader;
import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.model.Conversation;
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;
import com.taller2.hypechatapp.services.ChannelService;
import com.taller2.hypechatapp.services.ConversationService;
import com.taller2.hypechatapp.services.OrganizationService;
import com.taller2.hypechatapp.services.UserService;

import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class MenuActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, IMenuItemsClick {

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

        setupUI();
        addOrganizationsInSpinner();
        addListenerOnSpinnerOrganizationSelection();
        setUpChannels();
        setUpConversations();
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
                Toast.makeText(getContext(), R.string.fail_getting_info, Toast.LENGTH_SHORT).show();
            }

            @Override
            public Context getContext() {
                return MenuActivity.this;
            }
        });
    }

    private Integer getSelectedOrganizationPosition(List<Organization> organizations) {
        Integer selectedOrganizationId = userManagerPreferences.getSelectedOrganization();
        for (int idx = 0; idx < organizations.size(); idx ++) {
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
        channelsAdapter = new MenuChannelsAdapter(this);
        rvChannels.setAdapter(channelsAdapter);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this);
        mLinearLayoutManagerVertical.setOrientation(RecyclerView.VERTICAL);
        rvChannels.setLayoutManager(mLinearLayoutManagerVertical);
        rvChannels.setItemAnimator(new DefaultItemAnimator());

        View newChannel = findViewById(R.id.new_channel_layout);
        TextView channelTitle = newChannel.findViewById(R.id.item_title);
        channelTitle.setText(R.string.menu_channels);
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
            public void onResponseError(String errorMessage) {
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
            public void onResponseError(String errorMessage) {
                Toast.makeText(getContext(), R.string.fail_getting_info, Toast.LENGTH_LONG).show();
            }

            @Override
            public Context getContext() {
                return MenuActivity.this;
            }
        });
    }

    private void selectChannel() {
        if (userManagerPreferences.getSelectedConversation() > 0){
            return;
        }

        List<Channel> channels = channelsAdapter.getChannels();
        if (channels.size() == 0){
            userManagerPreferences.clearSelectedChannel();
            if (conversationsAdapter.getItemCount() > 0){
                this.selectConversation();
            } else {
                this.onChatSelected();
            }
            return;
        }
        Integer selectedChannelId = userManagerPreferences.getSelectedChannel();
        for (Channel channel: channels) {
            if (channel.getId().equals(selectedChannelId)) {
                toolbar.setTitle(channel.getName());
                this.onChatSelected();
                return;
            }
        }
        userManagerPreferences.saveSelectedChannel(channels.get(0).getId());
        toolbar.setTitle(channels.get(0).getName());
        this.onChatSelected();
    }

    private void selectConversation() {
        if (userManagerPreferences.getSelectedChannel() > 0){
            return;
        }

        List<Conversation> conversations = conversationsAdapter.getConversations();
        if (conversations.size() == 0){
            userManagerPreferences.clearSelectedConversation();
            this.selectChannel();
            return;
        }
        Integer selectedConversationId = userManagerPreferences.getSelectedConversation();
        for (Conversation conversation: conversations) {
            if (conversation.id.equals(selectedConversationId)) {
                toolbar.setTitle(conversation.getName());
                this.onChatSelected();
                return;
            }
        }

        userManagerPreferences.clearSelectedConversation();
        this.selectChannel();
    }

    private void viewUserProfile() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    private void viewOrganizationProfile() {
        Intent intent = new Intent(this, OrganizationProfileActivity.class);
        intent.putExtra("ORGANIZATION_ID", userManagerPreferences.getSelectedOrganization());
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

    private void createNewConversation() {
        Intent intent = new Intent(this, CreateConversationActivity.class);
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

    private void viewReceivedInvitations() {
        Intent intent = new Intent(this,ReceivedInvitationsActivity.class);
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

            case R.id.send_invitations:
                viewSendInvitations();
                return true;

            case R.id.received_invitations:
                viewReceivedInvitations();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // new organization selected
        Integer selectedOrganization = ((Organization) parent.getSelectedItem()).getId();
        Integer preferenceOrganization = userManagerPreferences.getSelectedOrganization();
        if (!selectedOrganization.equals(preferenceOrganization)) {
            userManagerPreferences.saveSelectedOrganization(selectedOrganization);
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
        // TODO Auto-generated method stub
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
        public void onResponseError(String errorMessage) {
            Toast.makeText(getContext(), R.string.fail_getting_info, Toast.LENGTH_SHORT).show();

        }

        @Override
        public Context getContext() {
            return MenuActivity.this;
        }

    }

    protected abstract void onChatSelected();

    @Override
    public void onChannelClick(Channel channel){
        toolbar.setTitle(channel.getName());
        userManagerPreferences.saveSelectedChannel(channel.getId());
        drawerLayout.closeDrawer(GravityCompat.START);
        onChatSelected();
    }

    @Override
    public void onConversationClick(Conversation conversation){
        toolbar.setTitle(conversation.getName());
        userManagerPreferences.saveSelectedConversation(conversation.id);
        drawerLayout.closeDrawer(GravityCompat.START);
        onChatSelected();
    }
}