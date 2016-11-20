package com.vandenbussche.emiel.projectsbp.auth;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Stijn on 18/10/2016.
 */
public class AuthHelper {

    private static AccountManager mAccountManager;
    private static AccountAuthenticatorResponse mAccountAuthenticatorResponse;

    public static String getUsername(Context context){
        mAccountManager = AccountManager.get(context);

        Account[] accounts =  mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE);

        if(accounts.length>0){
            return accounts[0].name;
        }
        else return null;
    }

    public static Account getAccount(Context context){
        mAccountManager = AccountManager.get(context);

        Account[] accounts =  mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE);

        if(accounts.length>0){
            return accounts[0];
        }
        else return null;
    }

    public static Boolean  isUserLoggedIn(Context context){
        mAccountManager = AccountManager.get(context);
        Account[] accounts =  mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE);
        if(accounts.length>0){
            return true;
        }
        else return false;

    }

    public static void logUserOff(Context context) {
        mAccountManager = AccountManager.get(context);
        Account[] accounts = mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE);
        for (int index = 0; index < accounts.length; index++) {
            mAccountManager.removeAccount(accounts[index], null, null);
        }

    }

    public static String refreshAccessToken(Context context) throws Exception{

        String urlParameters = String.format(Contract.REFRESH_ACCESS_TOKEN_BODY, getRefreshToken(context));
        URL url = new URL(Contract.REFRESH_ACCESS_TOKEN_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
        connection.setUseCaches(false);

        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        wr.write(urlParameters);
        wr.flush();
        wr.close();

        BufferedReader rdr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = rdr.readLine()) != null)
            sb.append(line);

        rdr.close();
        connection.disconnect();

//        Map<String, String> params = getParams(sb.toString());
//        String accessToken = params.get("access_token");
        return sb.toString();
    }

    public static String getAccessToken(long birthDay, int gender) throws  Exception{
        String urlParameters = String.format(Contract.ACCESS_TOKEN_BODY, birthDay, gender);
        URL url = new URL(Contract.ACCESS_TOKEN_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
        connection.setUseCaches(false);

        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        wr.write(urlParameters);
        wr.flush();
        wr.close();

        BufferedReader rdr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = rdr.readLine()) != null)
            sb.append(line);

        rdr.close();
        connection.disconnect();

//        Map<String, String> params = getParams(sb.toString());
//        String accessToken = params.get("access_token");


        return sb.toString();
    }

    //nieuw week 7
    public static String getCurrentAccessToken(Context context, Account account) {
        AccountManager mgt = AccountManager.get(context);
        String accessToken = mgt.peekAuthToken(account, "access_token");
        return accessToken;
    }

    public static String getRefreshToken(Context context) {
        Account account = mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE)[0];

        AccountManager mgt = AccountManager.get(context);
        String accessToken = mgt.peekAuthToken(account, "refresh_token");
        return accessToken;
    }

    public static void setRefreshToken(Context context, String newAccessToken) {
        Account account = mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE)[0];

        AccountManager mgt = AccountManager.get(context);
        mgt.setAuthToken(account, "access_token", newAccessToken);
    }
}
