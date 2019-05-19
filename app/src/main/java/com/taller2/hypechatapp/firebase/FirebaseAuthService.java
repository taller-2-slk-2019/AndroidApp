package com.taller2.hypechatapp.firebase;

import android.content.Context;
import android.util.Log;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.services.FirebaseApiService;

import androidx.annotation.NonNull;

public class FirebaseAuthService {

    public static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static boolean isCurrentUser(User user) {
        return getCurrentUser().getEmail().equals(user.getEmail());
    }

    public static boolean isUserLoggedIn() {
        return FirebaseAuthService.getCurrentUser() != null;
    }

    public static String getCurrentUserToken() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static void logOut(final Context context) {
        if (isUserLoggedIn()) {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                return;
                            }

                            // Get new Instance ID token
                            String token = task.getResult().getToken();

                            new FirebaseApiService().deleteFCMtoken(token, getFCMTokenClient(context, "deleted"));
                        }
                    });
        }

        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
    }

    public static void logIn(final Context context) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        new FirebaseApiService().updateFCMtoken(token, getFCMTokenClient(context, "updated"));
                    }
                });
    }

    private static Client<Void> getFCMTokenClient(final Context context, final String action) {
        return new Client<Void>() {
            @Override
            public void onResponseSuccess(Void responseBody) {
                //do nothing
                Log.i("FCM token", "token " + action);
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                //do nothing
            }

            @Override
            public Context getContext() {
                return context;
            }
        };
    }
}
