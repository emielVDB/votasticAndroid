package com.vandenbussche.emiel.projectsbp.auth;

/**
 * Created by Stijn on 15/10/2016.
 */
public class Contract {
    public static final String BASEURL = "http://172.30.252.65:3000";

    public static final String ACCOUNT_TYPE = "com.vandenbussche.emiel.projectsbp.account";

    public static final String ACCESS_TOKEN_URL = BASEURL+"/api/user/register";
    public static final String REFRESH_ACCESS_TOKEN_URL = BASEURL+"/api/user/refreshAccessToken";


    public static final String ACCESS_TOKEN_BODY = "birthDay=%d&gender=%d";
    public static final String REFRESH_ACCESS_TOKEN_BODY = "refreshToken=%s";
}
