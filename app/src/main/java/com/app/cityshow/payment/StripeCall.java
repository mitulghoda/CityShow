package com.app.cityshow.payment;


import com.app.cityshow.network.ApiService;
import com.app.cityshow.network.RetroClient;
import com.app.cityshow.model.AbstractCallback;

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


    public void paymentIntent(HashMap<String, Object> param, AbstractCallback<ResponseBody> callback) {
        apiService.paymentIntent(param).enqueue(callback);
    }
    public void subscribeUser(HashMap<String, Object> param, AbstractCallback<ResponseBody> callback) {
        apiService.subscribeUserStripe(param).enqueue(callback);
    }

    public void confirmIntent(HashMap<String, Object> param, AbstractCallback<ResponseBody> callback) {
        apiService.confirmIntent(param).enqueue(callback);
    }

    public void captureIntent(HashMap<String, Object> param, AbstractCallback<ResponseBody> callback) {
        apiService.captureIntent(param).enqueue(callback);
    }


  /*  public void purchasePackage(String intent_id, String lock_id, String coupon_code_id, Package aPackage, String type, Boolean isCouponApplied, AbstractResponseListener<ObjectBaseModel<Package>> callback) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("intent_id", intent_id);
        apiService.purchaseRental(param).enqueue(callback);
    }
*/

}
