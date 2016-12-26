package com.vandenbussche.emiel.projectsbp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.vandenbussche.emiel.projectsbp.auth.AuthHelper;
import com.vandenbussche.emiel.projectsbp.auth.Contract;

import java.io.IOException;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.Request;

/**
 * Created by emielPC on 9/12/16.
 */

public class VotasticApplication extends Application{
    private static Socket connection;

    public static Socket getConnection() {
        if( connection == null) {
            //problemen

        }
        return connection;
    }
    public static void setConnection(Socket connection) {
        VotasticApplication.connection = connection;
    }

    public static void initConnection(Context context){
        if(connection != null) return;
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(Contract.ACCOUNT_TYPE);

        if (accounts.length != 0) {
            String token = AuthHelper.getCurrentAccessToken(context, accounts[0]);
            new VotasticApplication.ConnectSocketTask().execute(token);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

       initConnection(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        if(connection == null) return;

        connection.close();
        connection = null;

    }

    public static class ConnectSocketTask extends AsyncTask<String, Void, Socket> {

        @Override
        protected Socket doInBackground(String... params) {
            if(VotasticApplication.getConnection() != null) return VotasticApplication.getConnection();

            try {
                connection = IO.socket(Contract.SOCKETURL);
                connection.connect();
                connection.on("authenticate", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        String text = (String)args[0];
                        System.out.println("socketAntwoord!: "+ text);
                    }
                });
                connection.emit("authenticate", params[0]);

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return connection;
        }

        @Override
        protected void onPostExecute(Socket result) {

        }
    }
}
