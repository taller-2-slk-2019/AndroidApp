package com.taller2.hypechatapp.model.roles;

import java.util.HashMap;
import java.util.Map;

public class RoleTranslator {

    private static final HashMap<String, String> TRANSLATIONS = new HashMap<String, String>() {{
        put(RoleFactory.ROLE_CREATOR, "Creador");
        put(RoleFactory.ROLE_MODERATOR, "Moderador");
        put(RoleFactory.ROLE_MEMBER, "Miembro");
    }};

    public static String translateToSpanish(String role) {
        return TRANSLATIONS.get(role);
    }

    public static String translateToEnglish(String role) {
        for (Map.Entry<String, String> entry : TRANSLATIONS.entrySet()) {
            if (role.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
