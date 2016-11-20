package com.vandenbussche.emiel.projectsbp.database.provider;

import android.net.Uri;

/**
 * Created by Stijn on 10/10/2016.
 */
public class Contract {

    public static final String AUTHORITY = "com.vandenbussche.emiel.Votastic";

    //CONTENT-URIS
    public static final Uri POLLS_URI = Uri.parse("content://" + AUTHORITY + "/polls");
    public static final Uri POLLS_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/polls/");
    //MIME-TYPES
    public static final String POLLS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.votastic.poll";
    public static final String POLLS_ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.votastic.poll";

    public static final int POLL_ID_PATH_POSITION = 1;
}
