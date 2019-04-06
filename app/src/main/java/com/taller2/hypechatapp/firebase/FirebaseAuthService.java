package com.taller2.hypechatapp.firebase;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthService {

    public static FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static boolean isUserLoggedIn(){
        return FirebaseAuthService.getCurrentUser() != null;
    }

    public static String getCurrentUserToken(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static void logOut(){
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
    }
}
