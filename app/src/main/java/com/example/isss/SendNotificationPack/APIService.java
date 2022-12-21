package com.example.isss.SendNotificationPack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAKV4YmY0:APA91bHdxfcm1BHtIW3sbvMin5S-l_wb-t14jYeoD6Ff491SBSkZ_OvPRc1o4DybNFxo6uw6csCovEqkCKUO2YNDceHF-HrgPy9nOCiRmcTU45X1agP1-jL1vtatdBYrSSaBkNDBO7U1" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}

