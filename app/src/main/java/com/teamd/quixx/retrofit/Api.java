package com.teamd.quixx.retrofit;

import com.teamd.quixx.domain.History;
import com.teamd.quixx.domain.Organization;
import com.teamd.quixx.domain.SimpleApiResponse;
import com.teamd.quixx.domain.Delivery;
import com.teamd.quixx.domain.DeliveryMan;
import com.teamd.quixx.domain.DeliveryStatus;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @GET("deliveryMan/auth/{phoneNumber}")
    Call<SimpleApiResponse> getVerificationCode(@Path("phoneNumber") String phoneNumber);
    @GET("deliveryMan/verify/authCode/{code}/{phoneNumber}")
    Call<SimpleApiResponse> verifyAuthCode(@Path("code") String code, @Path("phoneNumber") String phoneNumber);


    @GET("deliveryMan/isApproved/{phoneNumber}")
    Call<SimpleApiResponse> deliveryManIsApproved(@Path("phoneNumber") String phoneNumber);

    @GET("deliveryMan/getId/{phoneNumber}")
    Call<SimpleApiResponse> getUserId(@Path("phoneNumber") String phoneNumber);

    @GET("deliveryMan/getDeliveryManById/{deliveryManId}")
    Call<DeliveryMan> getUserDetailsById(@Path("deliveryManId") String deliveryManId);
    @GET("deliveryMan/getOrgInfo/{deliveryManId}")
    Call<Organization> getOrganizationInfo(@Path("deliveryManId") String deliveryManId);

    @GET("delivery/deliveries/{deliveryManId}")
    Call<Delivery> getAllDeliveryByDeliveryManId(@Path("deliveryManId") String deliveryManId);
    @GET("delivery/history/{deliveryManId}")
    Call<History> getAllHistoryByDeliveryManId(@Path("deliveryManId") String deliveryManId);

    @PUT("delivery/status/{deliveryId}/{status}")
    Call<DeliveryStatus> updateDeliveryStatus(@Path("deliveryId") String deliveryId, @Path("status") String status);

    @GET("delivery/statusForDeliveryMan/{deliveryId}")
    Call<DeliveryStatus> getStatusFroDeliveryMan(@Path("deliveryId") String deliveryId);


    @PUT("deliveryMan/updateDeliveryManLatLong/{deliveryManId}?")
    Call<DeliveryStatus> updateDeliveryManLocation( @Path("deliveryManId") String deliveryManId, @Query("latitude") String latitude, @Query("longitude") String longitude);

}
