package com.vandenbussche.emiel.projectsbp.api;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import com.vandenbussche.emiel.projectsbp.auth.AuthHelper;
import com.vandenbussche.emiel.projectsbp.auth.Contract;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class SyncInterceptor implements Interceptor {
    private Context context;

    public SyncInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(Contract.ACCOUNT_TYPE);
        Request original = chain.request();
        Request request = original;

        if (accounts.length != 0) {
            String token = AuthHelper.getCurrentAccessToken(context, accounts[0]);
            if (token != null) {
                request = original.newBuilder()
                        .header("authorization", "bearer " + token)
                        .method(original.method(), original.body())
                        .build();
            }
        }

        Response response = chain.proceed(request);
        return response;
    }
}
