package com.akexorcist.sleepingforless.network.sfl;

import com.akexorcist.sleepingforless.network.sfl.model.InsertTokenRequest;
import com.akexorcist.sleepingforless.network.sfl.model.InsertTokenResponse;
import com.akexorcist.sleepingforless.network.sfl.model.RemoveTokenRequest;
import com.akexorcist.sleepingforless.network.sfl.model.RemoveTokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public interface SleepingForLessService {
    @POST(SleepingForLessUrl.ADD_SUBSCRIBER)
    Call<InsertTokenResponse> addGcmToken(@Body InsertTokenRequest request);

    @POST(SleepingForLessUrl.REMOVE_OLD_SUBSCRIBER)
    Call<RemoveTokenResponse> removeGcmToken(@Body RemoveTokenRequest request);
}
