package com.app.cityshow.payment;


import com.app.cityshow.model.AbstractCallback;
import com.app.cityshow.network.ApiService;
import com.app.cityshow.network.RetroClient;

import java.util.HashMap;

import okhttp3.ResponseBody;

public class StripeCall {
    private static final StripeCall instance = new StripeCall();
    private final ApiService apiService;

    private StripeCall() {
        apiService = RetroClient.INSTANCE.getApiService();
    }

    public static StripeCall getInstance() {
        return instance;
    }

    public void createKey(AbstractCallback<ResponseBody> callback) {
        apiService.createKey().enqueue(callback);
    }

    public void subscribeUser(HashMap<String, Object> param, AbstractCallback<ResponseBody> callback) {
        apiService.subscribeUserStripe(param).enqueue(callback);
    }

}
