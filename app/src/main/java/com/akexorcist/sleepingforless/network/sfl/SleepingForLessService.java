package com.akexorcist.sleepingforless.network.sfl;

import com.akexorcist.sleepingforless.network.sfl.model.GcmTokenRequest;
import com.akexorcist.sleepingforless.network.sfl.model.GcmTokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public interface SleepingForLessService {
    @POST(SleepingForLessUrl.ADD_SUBSCRIBER)
    Call<GcmTokenResponse> addGcmToken(@Body GcmTokenRequest request);
}
