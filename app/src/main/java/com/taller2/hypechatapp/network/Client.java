package com.taller2.hypechatapp.network;

import android.content.Context;

public interface Client<T> {

    void onResponseSuccess(T responseBody);

    void onResponseError(String errorMessage);

    Context getContext();
}
