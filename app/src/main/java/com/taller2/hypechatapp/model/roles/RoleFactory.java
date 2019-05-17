package com.taller2.hypechatapp.model.roles;

public class RoleFactory {

    public static final String ROLE_CREATOR = "creator";
    public static final String ROLE_MODERATOR = "moderator";
    public static final String ROLE_MEMBER = "member";

    public static Role getRole(String role) {
        switch (role) {
            case ROLE_CREATOR:
                return new CreatorRole();
            case ROLE_MODERATOR:
                return new ModeratorRole();
            case ROLE_MEMBER:
                return new MemberRole();
        }

        throw new IllegalArgumentException();
    }

}
