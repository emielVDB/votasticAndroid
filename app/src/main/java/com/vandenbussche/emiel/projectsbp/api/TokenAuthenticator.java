package com.vandenbussche.emiel.projectsbp.api;

import android.content.Context;

import com.vandenbussche.emiel.projectsbp.auth.AuthHelper;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by emielPC on 16/11/16.
 */
public class TokenAuthenticator  implements Authenticator {
    Context context;

    public TokenAuthenticator(Context context) {
        this.context = context;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        try {
            String newAccessToken = AuthHelper.refreshAccessToken(context);

            // Add new header to rejected request and retry it
            return response.request().newBuilder()
                    .header("authorization", newAccessToken)
                    .build();
        }catch (Exception ex){
            return response.request();
        }


    }
}
