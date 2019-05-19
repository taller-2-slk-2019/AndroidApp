package com.taller2.hypechatapp.ui.model;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.UserListActionListener;
import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.model.roles.Role;
import com.taller2.hypechatapp.model.roles.RoleFactory;
import com.taller2.hypechatapp.model.roles.RoleTranslator;

import java.util.List;

import androidx.annotation.NonNull;

public class OrganizationListUserViewHolder extends ListUserViewHolder {

    private Spinner rolesSpinner;
    private List<String> roles;

    public OrganizationListUserViewHolder(@NonNull View itemView, UserListActionListener listener) {
        super(itemView, listener);
        rolesSpinner = itemView.findViewById(R.id.rolesSpinner);
        rolesSpinner.setVisibility(View.VISIBLE);

        roles = RoleFactory.getRolesList();
        rolesSpinner.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, roles));
    }

    @Override
    protected void bindUser(User user) {
        initializeRolesSpinner();
        checkUserRole();
    }

    private void roleChanged(String selectedRole) {
        listener.onUserRoleChanged(user, selectedRole);
    }

    private void initializeRolesSpinner() {
        rolesSpinner.setOnItemSelectedListener(null);
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
        boolean isCurrentUser = FirebaseAuthService.isCurrentUser(user);

        deleteButton.setVisibility(role.hasUsersPermissions() && !isCurrentUser ? View.VISIBLE : View.GONE);
        rolesSpinner.setEnabled(role.hasOrganizationPermissions() && !isCurrentUser);
    }
}
