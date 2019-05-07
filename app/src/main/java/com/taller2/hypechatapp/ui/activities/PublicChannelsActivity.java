package com.taller2.hypechatapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.IMenuItemsClick;
import com.taller2.hypechatapp.adapters.PublicChannelsAdapter;
import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.model.Conversation;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.model.ChannelInvitationRequest;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;
import com.taller2.hypechatapp.services.ChannelService;


import java.util.ArrayList;
import java.util.List;

public class PublicChannelsActivity extends AppCompatActivity implements IMenuItemsClick {

    private ChannelService channelService;
    protected UserManagerPreferences userManagerPreferences;

    private PublicChannelsAdapter channelsAdapter;
    private ProgressBar loadingView;
    private RecyclerView publicChannelsRecyclerView;
    private SearchView searchView;

    private List<Channel> channelsList = new ArrayList<>();
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_channels);

        userId=getIntent().getIntExtra("userId",0);

        channelService=new ChannelService();
        userManagerPreferences = new UserManagerPreferences(this);

        setUpUI();
        getPublicChannels();

    }

    private void getPublicChannels() {
        loadingView.setVisibility(View.VISIBLE);

        channelService.getPublicChannelsByOrganizationAndUser(userManagerPreferences.getSelectedOrganization(), new Client<List<Channel>>() {
            @Override
            public void onResponseSuccess(List<Channel> channels) {
                loadingView.setVisibility(View.INVISIBLE);
                channelsList=channels;
                setUpRecyclerView();
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                loadingView.setVisibility(View.INVISIBLE);
                String textToShow="No fue posible obtener los canales. Intente más tarde.";
                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public Context getContext() {
                return PublicChannelsActivity.this;
            }
        });
    }

    private void setUpRecyclerView() {
        publicChannelsRecyclerView = findViewById(R.id.public_channels_list);
        channelsAdapter = new PublicChannelsAdapter(channelsList,this);
        channelsAdapter.setChannels(channelsList);
        publicChannelsRecyclerView.setAdapter(channelsAdapter);
        publicChannelsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        publicChannelsRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void setUpUI() {
        Toolbar toolbar = findViewById(R.id.toolbar_public_channels);
        setSupportActionBar(toolbar);

        loadingView = findViewById(R.id.loading);
    }

    @Override
    public void onChannelClick(final Channel channel) {
        loadingView.setVisibility(View.VISIBLE);
        ChannelInvitationRequest request=new ChannelInvitationRequest();
        request.userId=String.valueOf(userId);
        channelService.addUserToChannel(channel.getId(), request, new Client<Void>(){

            @Override
            public void onResponseSuccess(Void responseBody) {
                loadingView.setVisibility(View.INVISIBLE);
                userManagerPreferences.saveSelectedChannel(channel.getId());
                Intent intent = new Intent(PublicChannelsActivity.this, ChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                Toast.makeText(getContext(), "Ocurrió un error al intentar acceder al canal público." +
                        " Intente más tarde.", Toast.LENGTH_LONG).show();
            }

            @Override
            public Context getContext() {
                return PublicChannelsActivity.this;
            }
        });
    }

    @Override
    public void onConversationClick(Conversation conversation) {
        //do nothing
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_searchable, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                channelsAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                channelsAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}
