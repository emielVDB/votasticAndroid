package com.vandenbussche.emiel.projectsbp.auth;

/**
 * Created by Stijn on 15/10/2016.
 */
public class Contract {
    public static final String SERVER_HOST = "obscure-fjord-28372.herokuapp.com";
//    public static final String SERVER_HOST = "192.168.9.136";
    public static final String BASEURL = "http://"+SERVER_HOST+":80";
    public static final String SOCKETURL = "http://"+SERVER_HOST+":80";
//    public static final String BASEURL = "http://"+SERVER_HOST+":3000";
//    public static final String SOCKETURL = "http://"+SERVER_HOST+":3000";

    public static final String BASEFILESURL = "https://s3.eu-west-2.amazonaws.com/votastic";

    public static final String ACCOUNT_TYPE = "com.vandenbussche.emiel.projectsbp.account";

    public static final String ACCESS_TOKEN_URL = BASEURL+"/api/user/register";
    public static final String REFRESH_ACCESS_TOKEN_URL = BASEURL+"/api/user/refreshAccessToken";


    public static final String ACCESS_TOKEN_BODY = "birthDay=%d&gender=%d";
    public static final String REFRESH_ACCESS_TOKEN_BODY = "refreshToken=%s";
}
