package com.vandenbussche.emiel.projectsbp.gui.activities;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.auth.AuthHelper;
import com.vandenbussche.emiel.projectsbp.auth.Contract;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private AccountManager mAccountManager;
    private AccountAuthenticatorResponse mAccountAuthenticatorResponse;

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Register");
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.btnRegister).setOnClickListener(this);
        editText = (EditText)findViewById(R.id.txtDate);
        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideKeyboard();
                new DatePickerDialog(LoginActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mAccountManager = AccountManager.get(this);
        mAccountAuthenticatorResponse = this.getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse.onRequestContinued();
        }
    }

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        editText.setText(sdf.format(myCalendar.getTime()));
        hideKeyboard();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        GetTokenTask getAccessToken = new GetTokenTask(this);
        String gender = ((Spinner)findViewById(R.id.spnrGender)).getSelectedItem().toString();
        int iGender = 0;
        if(gender.equals("Male")){
            iGender = 1;
        }else if(gender.equals("Female")){
            iGender = 2;
        }else{
            return;
        }

        if(editText.getText().toString().equals("")){
            return;
        }

        getAccessToken.execute(myCalendar.getTimeInMillis() + "", iGender + "");
    }

    class GetTokenTask extends AsyncTask<String, Void, GetTokenTask.Data> {

        class Data {
            String id;
            String accessToken;
            long atExpirationDate;
            String refreshToken;
            String error;
        }


        private ProgressDialog pDialog;
        private Context mContext;

        private static final String TAG = "GetTokenTask";
        private static final boolean DEBUG = true;

        public GetTokenTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage("Contacting Votastic...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected Data doInBackground(String... strings) {
            return getToken(Long.parseLong(strings[0]), Integer.parseInt(strings[1]));
        }

        //zie stap 2
        public Data getToken(Long birthDay, int gender) {
            Data data = new Data();
            try {
                if (DEBUG) Log.d(TAG, "+++ AccessToken Start ");

                String sJson = AuthHelper.getAccessToken(birthDay, gender);
                if (DEBUG) Log.d(TAG, "+++ AccessToken received: " + data.accessToken);

                JSONObject json = new JSONObject(sJson);
                data.id = json.getString("_id");
                data.accessToken = json.getString("accessToken");
                data.refreshToken = json.getString("refreshToken");
                data.atExpirationDate = json.getLong("atExpirationDate");


            } catch (UnsupportedEncodingException e) {
                Log.e("UnsupportedEncodingEx", "Description: " + e.getMessage());
                data.error = "UnsupportedEncodingEx";
            } catch (IOException e) {
                Log.e("IOException", "Description: " + e.getMessage());
                data.error = "IOException";
                e.printStackTrace();
            } catch (Exception e) {
                Log.e("Exception", "Error converting result " + e.getMessage());
                e.printStackTrace();
                data.error = "Exception";
            }
            return data;
        }

        @Override
        protected void onPostExecute(Data data) {
            pDialog.dismiss();
            //if (DEBUG) Log.d(TAG, "+++ onPostExecute: " + json);
            //data teruggeven
            if(data.error == null) {
                addAccount(data.id, data.accessToken, data.refreshToken);
            }
        }

    }

    private void addAccount(String userNameString, String accessToken, String refreshToken) {
        Account[] accountsByType = mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE);
        Account account;
        if (accountsByType.length == 0) {
            // nog geen account aanwezig
            account = new Account(userNameString, Contract.ACCOUNT_TYPE);
            mAccountManager.addAccountExplicitly(account, null, null);
        } else if (!userNameString.equals(accountsByType[0].name)) {
            // er bestaat reeds een account met andere naam
            mAccountManager.removeAccount(accountsByType[0], null, null);
            account = new Account("test", Contract.ACCOUNT_TYPE);
            mAccountManager.addAccountExplicitly(account, null, null);
        } else {
            // account met de zelfde username terug gevonden
            account = accountsByType[0];
        }

        if (accessToken != null)
            mAccountManager.setAuthToken(account, "access_token", accessToken);

        if (refreshToken != null)
            mAccountManager.setAuthToken(account, "refresh_token", refreshToken);



        Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, userNameString);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Contract.ACCOUNT_TYPE);

        if (mAccountAuthenticatorResponse != null) {
            Bundle bundle = intent.getExtras();
            bundle.putString(AccountManager.KEY_ACCOUNT_NAME, userNameString);
            bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, Contract.ACCOUNT_TYPE);
            mAccountAuthenticatorResponse.onResult(bundle);
        }

        setResult(RESULT_OK, intent);
        finish();
    }
}
