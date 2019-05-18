package com.taller2.hypechatapp.ui.model;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.UserListActionListener;
import com.taller2.hypechatapp.components.PicassoLoader;
import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.model.roles.Role;
import com.taller2.hypechatapp.model.roles.RoleFactory;
import com.taller2.hypechatapp.model.roles.RoleTranslator;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListUserViewHolder extends RecyclerView.ViewHolder {

    private TextView userName;
    private ImageView profile;
    private Spinner rolesSpinner;
    private UserListActionListener listener;
    private User user;
    private Context context;
    private UserManagerPreferences prefs;
    private List<String> roles;
    private Button deleteButton;

    public ListUserViewHolder(@NonNull View itemView, UserListActionListener listener) {
        super(itemView);
        context = itemView.getContext();
        prefs = new UserManagerPreferences(context);
        this.listener = listener;
        userName = itemView.findViewById(R.id.username);
        profile = itemView.findViewById(R.id.profile_image_view);
        rolesSpinner = itemView.findViewById(R.id.rolesSpinner);

        deleteButton = itemView.findViewById(R.id.deleteUserButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteButtonClick();
            }
        });

        roles = RoleFactory.getRolesList();
        rolesSpinner.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, roles));
    }

    public void setUser(User user) {
        rolesSpinner.setOnItemSelectedListener(null);

        this.user = user;
        userName.setText(user.getName());
        PicassoLoader.load(context, user.getPicture(), profile);
        initializeRolesSpinner();
        checkUserRole();
    }

    private void deleteButtonClick() {
        listener.onUserDeleted(user);
    }

    private void roleChanged(String selectedRole) {
        listener.onUserRoleChanged(user, selectedRole);
    }

    private void initializeRolesSpinner() {
        String userRole = RoleTranslator.translateToSpanish(user.getRole());
        rolesSpinner.setSelection(((ArrayAdapter) rolesSpinner.getAdapter()).getPosition(userRole), false);

        rolesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedRole = RoleTranslator.translateToEnglish(roles.get(position));
                roleChanged(selectedRole);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });
    }

    private void checkUserRole() {
        Role role = RoleFactory.getRole(prefs.getOrganizationRole());
        boolean isCurrentUser = user.getEmail().equals(FirebaseAuthService.getCurrentUser().getEmail());

        deleteButton.setVisibility(role.hasUsersPermissions() && !isCurrentUser ? View.VISIBLE : View.GONE);
        rolesSpinner.setEnabled(role.hasOrganizationPermissions() && !isCurrentUser);
    }
}
